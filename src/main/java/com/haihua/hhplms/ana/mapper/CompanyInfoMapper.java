package com.haihua.hhplms.ana.mapper;

import com.haihua.hhplms.ana.entity.CompanyInfo;

import java.util.List;
import java.util.Map;

public interface CompanyInfoMapper {
    int create(CompanyInfo companyInfo);
    int updateByParams(Map<String, Object> params);
    int updateByExample(CompanyInfo companyInfo);
    int deleteByParams(Map<String, Object> params);
    List<CompanyInfo> findByParams(Map<String, Object> params);
    int countByParams(Map<String, Object> params);
}
