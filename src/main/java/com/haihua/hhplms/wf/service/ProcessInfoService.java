package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.wf.entity.ProcessInfo;
import com.haihua.hhplms.wf.vo.ProcessInfoVO;

import java.util.List;

public interface ProcessInfoService {
    List<ProcessInfoVO> getAvailableProcesses();
    ProcessInfoVO loadDetail(String code);
    ProcessInfo findByCode(String code);
    ProcessInfo findBySid(Long sid);
    List<ProcessInfo> findBySids(List<Long> searchingSids);
}
