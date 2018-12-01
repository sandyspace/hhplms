package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.Account;
import com.haihua.hhplms.ana.entity.CompanyInfo;
import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.mapper.CompanyInfoMapper;
import com.haihua.hhplms.ana.vo.*;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.utils.EnumUtil;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.wf.service.ProcessExecutionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("companyInfoService")
public class CompanyInfoServiceImpl implements CompanyInfoService {
    @Autowired
    private CompanyInfoMapper companyInfoMapper;

    @Autowired
    @Qualifier("accountService")
    private AccountService accountService;

    @Autowired
    @Qualifier("processExecutionService")
    private ProcessExecutionService processExecutionService;

    public PageWrapper<List<CompanyInfoVO>> loadCompanyInfosByPage(String code,
                                                                      String type,
                                                                      String contactNameLike,
                                                                      String contactMobileLike,
                                                                      String status,
                                                                      Integer pageNo,
                                                                      Integer pageSize) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("你不是" + Role.Category.EMPLOYEE.getName() + "，请立刻停止非法操作");
        }
        Map<String, Object> params = new HashMap<>();
        if (!StringUtils.isBlank(code)) {
            params.put("code", code);
        }
        if (!StringUtils.isBlank(type)) {
            params.put("type", type);
        }
        if (!StringUtils.isBlank(contactNameLike)) {
            params.put("contactNameLike", contactNameLike);
        }
        if (!StringUtils.isBlank(contactMobileLike)) {
            params.put("contactMobileLike", contactMobileLike);
        }
        if (!StringUtils.isBlank(status)) {
            params.put("status", status);
        }
        return loadCompanyInfosByPage(params, pageNo, pageSize);
    }

    public PageWrapper<List<CompanyInfoVO>> loadCompanyInfosByPage(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        int count = countByParams(params);
        PageWrapper<List<CompanyInfoVO>> pageOfCompanyInfos = new PageWrapper<>(pageNo, pageSize, count);
        if (count > 0) {
            pageNo = Objects.isNull(pageNo) ? GlobalConstant.DEFAULT_PAGE_NO : pageNo;
            pageSize = Objects.isNull(pageSize) ? GlobalConstant.DEFAULT_PAGE_SIZE : pageSize;
            params.put("offset", (pageNo - 1) * pageSize);
            params.put("limit", pageSize);
            List<CompanyInfo> matchedCompanyInfoList = findByParams(params);
            if (Objects.nonNull(matchedCompanyInfoList) && !matchedCompanyInfoList.isEmpty()) {
                pageOfCompanyInfos.setResult(matchedCompanyInfoList.stream()
                        .map(matchedCompanyInfo -> new CompanyInfoVO(matchedCompanyInfo))
                        .collect(Collectors.toList()));
            }
        }
        return pageOfCompanyInfos;
    }

    public List<CompanyInfoVO> getAvailableCompanyInfos() {
        Map<String, Object> params = new HashMap<>();
        if (!WebUtils.isEmployee()) {
            params.put("status", CompanyInfo.Status.APPROVED.getCode());
        }
        List<CompanyInfo> availableCompanyInfos = findByParams(params);
        if (Objects.nonNull(availableCompanyInfos) && !availableCompanyInfos.isEmpty()) {
            return availableCompanyInfos.stream()
                    .map(availableCompanyInfo -> new CompanyInfoVO(availableCompanyInfo.getSid(), availableCompanyInfo.getName()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public CompanyInfoVO getCompanyInfo(String loginName) {
        if (!WebUtils.isEmployee()) {
            if (!WebUtils.getLoginName().equals(loginName)) {
                throw new ServiceException("无法获取其他企业的信息");
            }
        }
        Account account = accountService.findByLoginName(loginName);
        if (Objects.isNull(account)) {
            throw new ServiceException("登录名为[" + loginName + "]的账号不存在");
        }
        return loadDetail(account.getCompanyInfoSid());
    }

    public CompanyInfoVO getCompanyInfoOfAccount() {
        if (WebUtils.isEmployee()) {
            throw new ServiceException("雇员没有企业信息");
        }
        Long companyInfoSid = WebUtils.getCompanyId();
        if (Objects.isNull(companyInfoSid) || companyInfoSid < 0) {
            return null;
        }
        return loadDetail(companyInfoSid);
    }

    public CompanyInfoVO loadDetail(Long sid) {
        if (!WebUtils.isEmployee()) {
            if (!WebUtils.getCompanyId().equals(sid)) {
                throw new ServiceException("无法获取其他企业的信息");
            }
        }
        CompanyInfo companyInfo = findBySid(sid);
        if (Objects.isNull(companyInfo)) {
            throw new ServiceException("ID为[" + sid + "]的企业信息不存在");
        }
        return new CompanyInfoVO(companyInfo);
    }

    public void updateCompanyInfo(Long sid, CompanyInfoUpdateVO companyInfoUpdateVO) {
        if (WebUtils.isCompany()) {
            throw new ServiceException(Account.Type.COMPANY.getName() + "请联系系统管理员更新企业信息");
        }
        CompanyInfo editingCompanyInfo = findBySid(sid);
        if (Objects.isNull(editingCompanyInfo)) {
            throw new ServiceException("ID为[" + sid + "]的企业信息不存在");
        }
        if (WebUtils.isMember()) {
            if (!WebUtils.getLoginName().equals(editingCompanyInfo.getCreatedBy())) {
                throw new ServiceException("只有企业主才能更新企业信息");
            }
        }

        CompanyInfo example = new CompanyInfo();
        example.setSid(sid);
        example.setCode(companyInfoUpdateVO.getCode());
        example.setName(companyInfoUpdateVO.getName());
        example.setType(EnumUtil.codeOf(CompanyInfo.Type.class, companyInfoUpdateVO.getType()));
        example.setContactName(companyInfoUpdateVO.getContactName());
        example.setContactPhone(companyInfoUpdateVO.getContactPhone());
        example.setUpdatedBy(WebUtils.getLoginName());
        example.setUpdatedTime(new Date(System.currentTimeMillis()));
        example.setVersionNum(editingCompanyInfo.getVersionNum());

        int updatedRows = updateCompanyInfo(example);
        if (updatedRows == 0) {
            throw new ServiceException("此企业信息正在被其他人修改，请稍后再试");
        }
    }

    public int updateCompanyInfo(CompanyInfo companyInfo) {
        int updatedRows;
        try {
            updatedRows = companyInfoMapper.updateByExample(companyInfo);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    public void updateCompanyInfoStatus(Long sid, String status) {
        UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest();
        updateStatusRequest.setStatus(status);

        updateCompanyInfoStatus(sid, updateStatusRequest);
    }

    public void updateCompanyInfoStatus(Long sid, UpdateStatusRequest updateStatusRequest) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("你不是" + Role.Category.EMPLOYEE.getName() + "，请立刻停止非法操作");
        }

        CompanyInfo companyInfo = findBySid(sid);
        if (Objects.isNull(companyInfo)) {
            throw new ServiceException("ID为[" + sid + "]的企业信息不存在");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("status", updateStatusRequest.getStatus());
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", companyInfo.getVersionNum());
        params.put("sid", companyInfo.getSid());

        int updatedRows = updateCompanyInfo(params);
        if (updatedRows == 0) {
            throw new ServiceException("此企业信息正在被其他人修改，请稍后再试");
        }
    }

    public int updateCompanyInfo(Map<String, Object> params) {
        int updatedRows;
        try {
            updatedRows = companyInfoMapper.updateByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    @Transactional
    public void joinCompany(JoinCompanyRequest joinCompanyRequest) {
        if (WebUtils.isEmployee()) {
            throw new ServiceException(Role.Category.EMPLOYEE.getName() + "不能够加入企业");
        }
        CompanyInfo companyInfo = findBySid(joinCompanyRequest.getCompanyId());
        if (Objects.isNull(companyInfo)) {
            throw new ServiceException("要加入的企业不存在");
        }
        accountService.associateWithCompanyInfo(joinCompanyRequest);
        processExecutionService.initProcess("P-JRQY", companyInfo.getSid());
    }

    @Transactional
    public CompanyInfo uploadCompanyInfo(UploadCompanyInfoRequest uploadCompanyInfoRequest) {
        if (!WebUtils.isMember()) {
            throw new ServiceException("只有" + Account.Type.MEMBER + "才有权限上传企业信息");
        }

        CompanyInfo companyInfo = new CompanyInfo();
        if (StringUtils.isBlank(uploadCompanyInfoRequest.getCode())) {
            companyInfo.setCode(UUID.randomUUID().toString());
        } else {
            companyInfo.setCode(uploadCompanyInfoRequest.getCode());
        }
        companyInfo.setName(uploadCompanyInfoRequest.getName());
        companyInfo.setType(EnumUtil.codeOf(CompanyInfo.Type.class, uploadCompanyInfoRequest.getType()));
        companyInfo.setAddress(uploadCompanyInfoRequest.getAddress());
        companyInfo.setContactName(uploadCompanyInfoRequest.getContactName());
        companyInfo.setContactPhone(uploadCompanyInfoRequest.getContactPhone());
        companyInfo.setStatus(CompanyInfo.Status.VERIFYING);
        companyInfo.setCreatedBy(WebUtils.getLoginName());
        companyInfo.setCreatedTime(new Date(System.currentTimeMillis()));
        companyInfo.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);
        createCompanyInfo(companyInfo);

        accountService.associateWithCompanyInfo(new JoinCompanyRequest(companyInfo.getSid(),
                uploadCompanyInfoRequest.getCompanyOwnerName(), uploadCompanyInfoRequest.getCompanyOwnerSex()));
        processExecutionService.initProcess("P-QYXXSQ", null);
        return companyInfo;
    }

    public CompanyInfo createCompanyInfo(CompanyInfoCreationVO companyInfoCreationVO) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("只有" + Role.Category.EMPLOYEE.getName() + "才有权限创建企业信息");
        }

        CompanyInfo companyInfo = new CompanyInfo();
        if (StringUtils.isBlank(companyInfoCreationVO.getCode())) {
            companyInfo.setCode(UUID.randomUUID().toString());
        } else {
            companyInfo.setCode(companyInfoCreationVO.getCode());
        }
        companyInfo.setName(companyInfoCreationVO.getName());
        companyInfo.setType(EnumUtil.codeOf(CompanyInfo.Type.class, companyInfoCreationVO.getType()));
        companyInfo.setAddress(companyInfoCreationVO.getAddress());
        companyInfo.setContactName(companyInfoCreationVO.getContactName());
        companyInfo.setContactPhone(companyInfoCreationVO.getContactPhone());
        companyInfo.setStatus(EnumUtil.codeOf(CompanyInfo.Status.class, companyInfoCreationVO.getStatus()));
        companyInfo.setCreatedBy(WebUtils.getLoginName());
        companyInfo.setCreatedTime(new Date(System.currentTimeMillis()));
        companyInfo.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);

        createCompanyInfo(companyInfo);
        return companyInfo;
    }

    public int createCompanyInfo(CompanyInfo companyInfo) {
        int insertedRows;
        try {
            insertedRows = companyInfoMapper.create(companyInfo);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public void deleteBySid(Long sid) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("你不是" + Role.Category.EMPLOYEE.getName() + "，请立刻停止非法操作");
        }

        CompanyInfo companyInfo = findBySid(sid);
        if (Objects.isNull(companyInfo)) {
            throw new ServiceException("ID为[" + sid + "]的企业信息不存在");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("sid", sid);
        int deletedRows = deleteByParams(params);
        if (deletedRows == 0) {
            throw new ServiceException("ID为[" + sid + "]的企业信息已经被其他人删除");
        }
    }

    public int deleteByParams(Map<String, Object> params) {
        int deletedRows;
        try {
            deletedRows = companyInfoMapper.deleteByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return deletedRows;
    }

    public CompanyInfo findBySid(Long sid) {
        return findSingle("sid", sid);
    }

    private CompanyInfo findSingle(String paramName, Object paramValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(paramName, paramValue);
        List<CompanyInfo> matchedCompanyInfoList = findByParams(params);
        if (Objects.nonNull(matchedCompanyInfoList) && !matchedCompanyInfoList.isEmpty()) {
            return matchedCompanyInfoList.get(0);
        }
        return null;
    }

    public int countByParams(Map<String, Object> params) {
        int count;
        try {
            count = companyInfoMapper.countByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return count;
    }

    public List<CompanyInfo> findByParams(Map<String, Object> params) {
        List<CompanyInfo> matchedCompanyInfoList;
        try {
            matchedCompanyInfoList = companyInfoMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedCompanyInfoList;
    }
}
