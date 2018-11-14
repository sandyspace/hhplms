package com.haihua.hhplms.pm.mapper;

import com.haihua.hhplms.pm.entity.PreferentialMsg;

import java.util.List;
import java.util.Map;

public interface PreferentialMsgMapper {
    int countByParams(Map<String, Object> params);
    List<PreferentialMsg> findByParams(Map<String, Object> params);
    int create(PreferentialMsg preferentialMsg);
    int deleteByParams(Map<String, Object> params);
    int updateByExample(PreferentialMsg preferentialMsg);
    int updateByParams(Map<String, Object> params);
}
