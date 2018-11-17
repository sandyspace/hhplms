package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.wf.entity.Step;
import com.haihua.hhplms.wf.vo.StepVO;

import java.util.List;

public interface StepService {
    List<StepVO> getStepsOfProcess(Long processSid);
    List<Step> findBySids(List<Long> searchingSids);
}
