package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.wf.entity.Route;
import com.haihua.hhplms.wf.entity.Step;
import com.haihua.hhplms.wf.mapper.StepMapper;
import com.haihua.hhplms.wf.vo.StepVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("stepService")
public class StepServiceImpl implements StepService {
    @Autowired
    private StepMapper stepMapper;

    @Autowired
    @Qualifier("routeService")
    private RouteService routeService;

    public List<StepVO> getStepsOfProcess(Long processSid) {
        List<Step> stepsOfProcess = findStepsOfGivenProcess(processSid);
        if (Objects.isNull(stepsOfProcess) || stepsOfProcess.isEmpty()) {
            return null;
        }
        return stepsOfProcess.stream()
                .map(stepOfProcess -> new StepVO(stepOfProcess))
                .collect(Collectors.toList());
    }

    public List<Step> findStepsOfGivenProcess(Long processSid) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            throw new ServiceException("You have insufficient right to view steps of process");
        }
        List<Route> definitionsOfProcess = routeService.findByProcessSid(processSid);
        if (Objects.isNull(definitionsOfProcess) || definitionsOfProcess.isEmpty()) {
            return null;
        }
        return findBySids(definitionsOfProcess.stream()
                .map(definitionOfProcess -> definitionOfProcess.getFromStepSid())
                .collect(Collectors.toList()));
    }

    public Step findBySid(Long sid) {
        return findSingle("sid", sid);
    }

    private Step findSingle(String paramName, Object paramValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(paramName, paramValue);
        List<Step> matchedSteps = findByParams(params);
        if (Objects.nonNull(matchedSteps) && !matchedSteps.isEmpty()) {
            return matchedSteps.get(0);
        }
        return null;
    }

    public List<Step> findBySids(List<Long> searchingSids) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchingSids", searchingSids);
        return findByParams(params);
    }

    public List<Step> findByParams(Map<String, Object> params) {
        List<Step> matchedSteps;
        try {
            matchedSteps = stepMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedSteps;
    }
}
