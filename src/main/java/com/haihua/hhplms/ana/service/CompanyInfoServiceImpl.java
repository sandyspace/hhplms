package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.CompanyInfo;
import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.ana.mapper.CompanyInfoMapper;
import com.haihua.hhplms.ana.vo.CompanyInfoCreationVO;
import com.haihua.hhplms.ana.vo.CompanyInfoUpdateVO;
import com.haihua.hhplms.ana.vo.CompanyInfoVO;
import com.haihua.hhplms.ana.vo.UpdateStatusRequest;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.utils.EnumUtil;
import com.haihua.hhplms.common.utils.WebUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("companyInfoService")
public class CompanyInfoServiceImpl implements CompanyInfoService {
    @Autowired
    private CompanyInfoMapper companyInfoMapper;

    public PageWrapper<List<CompanyInfoVO>> loadCompanyInfoListByPage(String code,
                                                                      String type,
                                                                      String contactNameLike,
                                                                      String contactMobileLike,
                                                                      String status,
                                                                      Integer pageNo,
                                                                      Integer pageSize) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            throw new ServiceException("You have insufficient right to view the information of companies");
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
        return loadCompanyInfoListByPage(params, pageNo, pageSize);
    }

    public PageWrapper<List<CompanyInfoVO>> loadCompanyInfoListByPage(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        int count = countByParams(params);
        PageWrapper<List<CompanyInfoVO>> pageOfCompanyInfos = new PageWrapper<>(pageNo, pageSize, count);
        if (count > 0) {
            pageNo = Objects.isNull(pageNo) ? GlobalConstant.DEFAULT_PAGE_NO : pageNo;
            pageSize = Objects.isNull(pageSize) ? GlobalConstant.DEFAULT_PAGE_SIZE : pageSize;
            params.put("offset", (pageNo - 1) * pageSize);
            params.put("limit", pageNo * pageSize);
            List<CompanyInfo> matchedCompanyInfoList = findByParams(params);
            if (Objects.nonNull(matchedCompanyInfoList) && !matchedCompanyInfoList.isEmpty()) {
                pageOfCompanyInfos.setResult(matchedCompanyInfoList.stream()
                        .map(matchedCompanyInfo -> new CompanyInfoVO(matchedCompanyInfo))
                        .collect(Collectors.toList()));
            }
        }
        return pageOfCompanyInfos;
    }

    public void updateCompanyInfo(Long sid, CompanyInfoUpdateVO companyInfoUpdateVO) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            if (!WebUtils.getCompanyId().equals(sid)) {
                throw new ServiceException("You have insufficient right to edit company info");
            }
        }
        CompanyInfo editingCompanyInfo = findBySid(sid);
        if (Objects.isNull(editingCompanyInfo)) {
            throw new ServiceException("Company Info with id:[" + sid + "] does not exist");
        }
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setSid(sid);
        companyInfo.setCode(companyInfoUpdateVO.getCode());
        companyInfo.setName(companyInfoUpdateVO.getName());
        companyInfo.setType(EnumUtil.codeOf(CompanyInfo.CompanyType.class, companyInfoUpdateVO.getType()));
        companyInfo.setContactName(companyInfoUpdateVO.getContactName());
        companyInfo.setContactMobile(companyInfoUpdateVO.getContactMobile());
        companyInfo.setUpdatedBy(WebUtils.getLoginName());
        companyInfo.setUpdatedTime(new Date(System.currentTimeMillis()));
        companyInfo.setVersionNum(editingCompanyInfo.getVersionNum());

        int updatedRows = updateCompanyInfo(companyInfo);
        if (updatedRows == 0) {
            throw new ServiceException("Company info is being edited by others, please try again later");
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

    public void updateCompanyInfoStatus(Long sid, UpdateStatusRequest updateStatusRequest) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            if (!WebUtils.getCompanyId().equals(sid)) {
                throw new ServiceException("You have insufficient right to update status of company info");
            }
        }

        CompanyInfo companyInfo = findBySid(sid);
        if (Objects.isNull(companyInfo)) {
            throw new ServiceException("Company info does not exist, failed to change status");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("status", updateStatusRequest.getStatus());
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", companyInfo.getVersionNum());
        params.put("sid", companyInfo.getSid());

        int updatedRows = updateCompanyInfo(params);
        if (updatedRows == 0) {
            throw new ServiceException("Company info is being edited by others, please try again later");
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

    public CompanyInfo createCompanyInfo(CompanyInfoCreationVO companyInfoCreationVO) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            companyInfoCreationVO.setStatus(CompanyInfo.Status.VERIFYING.getCode());
        }
        CompanyInfo companyInfo = new CompanyInfo();
        if (StringUtils.isBlank(companyInfoCreationVO.getCode())) {
            companyInfo.setCode(UUID.randomUUID().toString());
        } else {
            companyInfo.setCode(companyInfoCreationVO.getCode());
        }
        companyInfo.setName(companyInfoCreationVO.getName());
        companyInfo.setType(EnumUtil.codeOf(CompanyInfo.CompanyType.class, companyInfoCreationVO.getType()));
        companyInfo.setAddress(companyInfoCreationVO.getAddress());
        companyInfo.setContactName(companyInfoCreationVO.getContactName());
        companyInfo.setContactMobile(companyInfoCreationVO.getContactMobile());
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
        Map<String, Object> params = new HashMap<>();
        params.put("sid", sid);
        int deletedRows = deleteByParams(params);
        if (deletedRows == 0) {
            throw new ServiceException("Company Info with id:[" + sid + "] has been deleted by others");
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
