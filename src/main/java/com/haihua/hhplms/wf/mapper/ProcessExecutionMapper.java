package com.haihua.hhplms.wf.mapper;

import com.haihua.hhplms.wf.entity.ProcessExecution;

import java.util.List;
import java.util.Map;

public interface ProcessExecutionMapper {
    int updateByParams(Map<String, Object> params);
    int create(ProcessExecution processExecution);
    int countByParams(Map<String, Object> params);
    List<ProcessExecution> findByParams(Map<String, Object> params);
}
