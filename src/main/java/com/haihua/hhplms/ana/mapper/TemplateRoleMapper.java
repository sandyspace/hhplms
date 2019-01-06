package com.haihua.hhplms.ana.mapper;

import com.haihua.hhplms.ana.entity.TemplateRole;

import java.util.List;
import java.util.Map;

public interface TemplateRoleMapper {
    int create(TemplateRole templateRole);
    int updateByExample(TemplateRole templateRole);
    int deleteByParams(Map<String, Object> params);
    int countByParams(Map<String, Object> params);
    List<TemplateRole> findByParams(Map<String, Object> params);
}
