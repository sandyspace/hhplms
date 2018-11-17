package com.haihua.hhplms.wf.mapper;

import com.haihua.hhplms.wf.entity.ProcessInfo;

import java.util.List;
import java.util.Map;

public interface ProcessInfoMapper {
    List<ProcessInfo> findByParams(Map<String, Object> params);
}
