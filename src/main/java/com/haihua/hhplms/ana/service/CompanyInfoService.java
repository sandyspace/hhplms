package com.haihua.hhplms.ana.service;

import com.haihua.hhplms.ana.entity.CompanyInfo;
import com.haihua.hhplms.ana.vo.CompanyInfoCreationVO;
import com.haihua.hhplms.ana.vo.CompanyInfoUpdateVO;
import com.haihua.hhplms.ana.vo.CompanyInfoVO;
import com.haihua.hhplms.ana.vo.UpdateStatusRequest;
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
    CompanyInfo createCompanyInfo(CompanyInfoCreationVO companyInfoCreationVO);
    void updateCompanyInfo(Long sid, CompanyInfoUpdateVO companyInfoUpdateVO);
    void updateCompanyInfoStatus(Long sid, UpdateStatusRequest updateStatusRequest);
    void deleteBySid(Long sid);
}
