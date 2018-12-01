package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.CompanyInfo;
import com.haihua.hhplms.ana.vo.*;
import com.haihua.hhplms.common.model.PageWrapper;

import java.util.List;

public interface CompanyInfoService {
    PageWrapper<List<CompanyInfoVO>> loadCompanyInfosByPage(String code,
                                                                String type,
                                                                String contactNameLike,
                                                                String contactMobileLike,
                                                                String status,
                                                                Integer pageNo,
                                                                Integer pageSize);
    List<CompanyInfoVO> getAvailableCompanyInfos();
    CompanyInfoVO loadDetail(Long sid);
    CompanyInfoVO getCompanyInfo(String loginName);
    CompanyInfoVO getCompanyInfoOfAccount();
    void joinCompany(JoinCompanyRequest joinCompanyRequest);
    CompanyInfo uploadCompanyInfo(UploadCompanyInfoRequest uploadCompanyInfoRequest);
    CompanyInfo createCompanyInfo(CompanyInfoCreationVO companyInfoCreationVO);
    void updateCompanyInfo(Long sid, CompanyInfoUpdateVO companyInfoUpdateVO);
    void updateCompanyInfoStatus(Long sid, String status);
    void updateCompanyInfoStatus(Long sid, UpdateStatusRequest updateStatusRequest);
    CompanyInfo findBySid(Long sid);
    void deleteBySid(Long sid);
}
