package com.haihua.hhplms.wf.mapper;

import com.haihua.hhplms.wf.entity.Step;

import java.util.List;
import java.util.Map;

public interface StepMapper {
    List<Step> findByParams(Map<String, Object> params);
}
