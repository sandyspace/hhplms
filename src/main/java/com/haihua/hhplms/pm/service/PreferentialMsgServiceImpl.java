package com.haihua.hhplms.pm.service;

import com.haihua.hhplms.ana.entity.Account;
import com.haihua.hhplms.ana.entity.CompanyInfo;
import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.service.CompanyInfoService;
import com.haihua.hhplms.ana.vo.UpdateStatusRequest;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.utils.EnumUtil;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.pm.entity.PreferentialMsg;
import com.haihua.hhplms.pm.mapper.PreferentialMsgMapper;
import com.haihua.hhplms.pm.vo.PreferentialMsgCreationVO;
import com.haihua.hhplms.pm.vo.PreferentialMsgUpdateVO;
import com.haihua.hhplms.pm.vo.PreferentialMsgVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("preferentialMsgService")
public class PreferentialMsgServiceImpl implements PreferentialMsgService {
    @Autowired
    private PreferentialMsgMapper preferentialMsgMapper;

    @Autowired
    @Qualifier("companyInfoService")
    private CompanyInfoService companyInfoService;

    public List<PreferentialMsgVO> loadLatestPreferentialMsgs() {
        final Map<String, Object> params = new HashMap<>();
        params.put("status", PreferentialMsg.Status.RELEASE);
        params.put("offset", 0);
        params.put("limit", 100);

        final List<PreferentialMsg> latestPreferentialMsgs = findByParams(params);
        if (Objects.nonNull(latestPreferentialMsgs) && !latestPreferentialMsgs.isEmpty()) {
            final List<CompanyInfo> allCompanyInfos = companyInfoService.findByParams(null);
            final Map<Long, CompanyInfo> companyInfoMap = new HashMap<>();
            if (Objects.nonNull(allCompanyInfos) && !allCompanyInfos.isEmpty()) {
                allCompanyInfos.forEach(companyInfo -> companyInfoMap.put(companyInfo.getSid(), companyInfo));
            }

            return latestPreferentialMsgs.stream()
                    .map(latestPreferentialMsg -> new PreferentialMsgVO(latestPreferentialMsg, companyInfoMap.get(latestPreferentialMsg.getCompanyInfoSid())))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public PreferentialMsgVO loadDetail(Long sid) {
        PreferentialMsg matchedPreferentialMsg = findBySid(sid);
        if (Objects.isNull(matchedPreferentialMsg)) {
            throw new ServiceException("Notification with id: [" + sid + "] does not exist");
        }
        return new PreferentialMsgVO(matchedPreferentialMsg);
    }

    public PageWrapper<List<PreferentialMsgVO>> loadPreferentialMsgsByPage(String title,
                                                                           String status,
                                                                           Long companyInfoSid,
                                                                           Integer pageNo,
                                                                           Integer pageSize) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限查看");
        }
        Map<String, Object> params = new HashMap<>();
        if (WebUtils.isCompany()) {
            params.put("companyInfoSid", WebUtils.getCompanyId());
        } else {
            if (Objects.nonNull(companyInfoSid)) {
                params.put("companyInfoSid",companyInfoSid);
            }
        }
        if (!StringUtils.isBlank(title)) {
            params.put("title", title);
        }
        if(!StringUtils.isBlank(status)) {
            params.put("status",status);
        }
        return loadPreferentialMsgsByPage(params, pageNo, pageSize);
    }

    public PageWrapper<List<PreferentialMsgVO>> loadPreferentialMsgsByPage(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        int count = countByParams(params);
        PageWrapper<List<PreferentialMsgVO>> pageOfPreferentialMsgs = new PageWrapper<>(pageNo, pageSize, count);
        if (count > 0) {
            pageNo = Objects.isNull(pageNo) ? GlobalConstant.DEFAULT_PAGE_NO : pageNo;
            pageSize = Objects.isNull(pageSize) ? GlobalConstant.DEFAULT_PAGE_SIZE : pageSize;
            params.put("offset", (pageNo - 1) * pageSize);
            params.put("limit", pageSize);
            List<PreferentialMsg> matchedPreferentialMsgs = findByParams(params);
            if (Objects.nonNull(matchedPreferentialMsgs) && !matchedPreferentialMsgs.isEmpty()) {
                pageOfPreferentialMsgs.setResult(matchedPreferentialMsgs.stream()
                        .map(matchedPreferentialMsg -> new PreferentialMsgVO(matchedPreferentialMsg))
                        .collect(Collectors.toList()));
            }
        }
        return pageOfPreferentialMsgs;
    }

    public PreferentialMsg findBySid(Long sid) {
        return findSingle("sid", sid);
    }

    private PreferentialMsg findSingle(String paramName, Object paramValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(paramName, paramValue);
        List<PreferentialMsg> matchedPreferentialMsgs = findByParams(params);
        if (Objects.nonNull(matchedPreferentialMsgs) && !matchedPreferentialMsgs.isEmpty()) {
            return matchedPreferentialMsgs.get(0);
        }
        return null;
    }

    public int countByParams(Map<String, Object> params) {
        int count;
        try {
            count = preferentialMsgMapper.countByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return count;
    }

    public List<PreferentialMsg> findByParams(Map<String, Object> params) {
        List<PreferentialMsg> matchedPreferentialMsgs;
        try {
            matchedPreferentialMsgs = preferentialMsgMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedPreferentialMsgs;
    }

    public PreferentialMsg createdPreferentialMsg(PreferentialMsgCreationVO preferentialMsgCreationVO) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限创建");
        }
        if (WebUtils.isCompany()) {
            preferentialMsgCreationVO.setCompanyId(WebUtils.getCompanyId());
        }
        PreferentialMsg preferentialMsg = new PreferentialMsg();
        preferentialMsg.setTitle(preferentialMsgCreationVO.getTitle());
        preferentialMsg.setContent(preferentialMsgCreationVO.getContent());
        preferentialMsg.setImgUrl(preferentialMsgCreationVO.getImgUrl());
        preferentialMsg.setStatus(EnumUtil.codeOf(PreferentialMsg.Status.class, preferentialMsgCreationVO.getStatus()));
        preferentialMsg.setCompanyInfoSid(preferentialMsgCreationVO.getCompanyId());

        preferentialMsg.setCreatedBy(WebUtils.getLoginName());
        preferentialMsg.setCreatedTime(new Date(System.currentTimeMillis()));
        preferentialMsg.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);

        createdPreferentialMsg(preferentialMsg);
        return preferentialMsg;
    }

    public int createdPreferentialMsg(PreferentialMsg preferentialMsg) {
        int insertedRows;
        try {
            insertedRows = preferentialMsgMapper.create(preferentialMsg);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public void updatePreferentialMsg(Long preferentialMsgSid, PreferentialMsgUpdateVO preferentialMsgUpdateVO) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限更新");
        }

        PreferentialMsg editingPreferentialMsg = findBySid(preferentialMsgSid);
        if (Objects.isNull(editingPreferentialMsg)) {
            throw new ServiceException("ID为[" + preferentialMsgSid + "]的优惠信息不存在");
        }
        if (WebUtils.isCompany()) {
            if (!WebUtils.getCompanyId().equals(editingPreferentialMsg.getCompanyInfoSid())) {
                throw new ServiceException(Account.Type.COMPANY.getName() + "没有权限修改其他" + Account.Type.COMPANY.getName() + "的优惠信息");
            }
            preferentialMsgUpdateVO.setCompanyId(null);
        }

        PreferentialMsg preferentialMsg = new PreferentialMsg();
        preferentialMsg.setSid(preferentialMsgSid);
        preferentialMsg.setTitle(preferentialMsgUpdateVO.getTitle());
        preferentialMsg.setContent(preferentialMsgUpdateVO.getContent());
        preferentialMsg.setImgUrl(preferentialMsgUpdateVO.getImgUrl());
        preferentialMsg.setCompanyInfoSid(preferentialMsgUpdateVO.getCompanyId());

        preferentialMsg.setUpdatedBy(WebUtils.getLoginName());
        preferentialMsg.setUpdatedTime(new Date(System.currentTimeMillis()));
        preferentialMsg.setVersionNum(editingPreferentialMsg.getVersionNum());

        int updatedRows = updatePreferentialMsg(preferentialMsg);
        if (updatedRows == 0) {
            throw new ServiceException("此优惠信息正在被其他人修改，请稍后再试");
        }
    }

    public int updatePreferentialMsg(PreferentialMsg preferentialMsg) {
        int updatedRows;
        try {
            updatedRows = preferentialMsgMapper.updateByExample(preferentialMsg);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    public void updatePreferentialMsgStatus(Long preferentialMsgSid, UpdateStatusRequest updateStatusRequest) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限更新优惠信息状态");
        }

        PreferentialMsg preferentialMsg = findBySid(preferentialMsgSid);
        if (Objects.isNull(preferentialMsg)) {
            throw new ServiceException("ID为[" + preferentialMsgSid + "]的优惠信息不存在");
        }
        if (WebUtils.isCompany()) {
            if (!WebUtils.getCompanyId().equals(preferentialMsg.getCompanyInfoSid())) {
                throw new ServiceException(Account.Type.COMPANY.getName() + "没有权限更新其他" + Account.Type.COMPANY.getName() + "的优惠信息的状态");
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("status", updateStatusRequest.getStatus());
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", preferentialMsg.getVersionNum());
        params.put("sid", preferentialMsg.getSid());

        int updatedRows = updatePreferentialMsg(params);
        if (updatedRows == 0) {
            throw new ServiceException("此优惠信息正在被其他人修改，请稍后再试");
        }
    }

    public int updatePreferentialMsg(Map<String, Object> params) {
        int updatedRows;
        try {
            updatedRows = preferentialMsgMapper.updateByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    public void deleteBySid(Long sid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限删除优惠信息");
        }

        PreferentialMsg preferentialMsg = findBySid(sid);
        if (Objects.isNull(preferentialMsg)) {
            throw new ServiceException("ID为[" + sid + "]的优惠信息不存在");
        }
        if (WebUtils.isCompany()) {
            if (!WebUtils.getCompanyId().equals(preferentialMsg.getCompanyInfoSid())) {
                throw new ServiceException(Account.Type.COMPANY.getName() + "没有权限删除其他" + Account.Type.COMPANY.getName() + "的优惠信息");
            }
        }

        Map<String, Object> params = new HashMap<>();
        params.put("sid", sid);
        int deletedRows = deleteByParams(params);
        if (deletedRows == 0) {
            throw new ServiceException("ID为[" + sid + "]的优惠信息已经被其他人删除");
        }
    }

    public int deleteByParams(Map<String, Object> params) {
        int deletedRows;
        try {
            deletedRows = preferentialMsgMapper.deleteByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return deletedRows;
    }
}
