package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.wf.entity.Route;
import com.haihua.hhplms.wf.entity.ProcessInfo;
import com.haihua.hhplms.wf.entity.Step;
import com.haihua.hhplms.wf.mapper.ProcessInfoMapper;
import com.haihua.hhplms.wf.vo.ProcessExecutionVO;
import com.haihua.hhplms.wf.vo.ProcessInfoVO;
import com.haihua.hhplms.wf.vo.StepVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("processInfoService")
public class ProcessInfoServiceImpl implements ProcessInfoService {
    @Autowired
    private ProcessInfoMapper processInfoMaper;

    @Autowired
    @Qualifier("stepService")
    private StepService stepService;

    @Autowired
    @Qualifier("routeService")
    private RouteService routeService;

    @Autowired
    @Qualifier("processExecutionService")
    private ProcessExecutionService processExecutionService;

    public List<ProcessInfoVO> getAvailableProcesses() {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            throw new ServiceException("You have insufficient right to view process infos");
        }
        List<ProcessInfo> availableProcessInfos = findByParams(null);
        if (Objects.isNull(availableProcessInfos) || availableProcessInfos.isEmpty()) {
            return null;
        }
        return availableProcessInfos.stream()
                .map(availableProcessInfo -> new ProcessInfoVO(availableProcessInfo))
                .collect(Collectors.toList());
    }

    public ProcessInfoVO loadDetail(String code) {
        final ProcessInfo processInfo = findByCode(code);
        if (Objects.isNull(processInfo)) {
            return null;
        }
        final ProcessInfoVO processInfoVO = new ProcessInfoVO(processInfo);

        final List<Route> routeFragments = routeService.findByProcessSid(processInfo.getSid());
        if (Objects.nonNull(routeFragments) && !routeFragments.isEmpty()) {
            final Map<Long, Route> routeFragmentMap = new HashMap<>();
            routeFragments.forEach(routeFragment -> routeFragmentMap.put(routeFragment.getFromStepSid(), routeFragment));

            final List<Step> steps = stepService.findBySids(routeFragments.stream()
                    .map(routeFragment -> routeFragment.getFromStepSid())
                    .collect(Collectors.toList()));
            final Map<Long, Step> stepMap = new HashMap<>();
            steps.forEach(step -> stepMap.put(step.getSid(), step));

            Route startOfRoute = null;
            for(Route routeFragment : routeFragments) {
                if (GlobalConstant.FLAG_YES_VALUE.equals(routeFragment.getStartFlag())) {
                    startOfRoute = routeFragment;
                    break;
                }
            }
            List<StepVO> processSteps = new ArrayList<>(steps.size());
            while(true) {
                processSteps.add(new StepVO(stepMap.get(startOfRoute.getFromStepSid()), startOfRoute));
                if (Objects.isNull(startOfRoute.getToStepSid())) {
                    break;
                }
                startOfRoute = routeFragmentMap.get(startOfRoute.getToStepSid());
            }
            processInfoVO.setSteps(processSteps);
        }

        final List<ProcessExecutionVO> processExecutionsLaunchedByAccount = processExecutionService.getProcessExecutionsLaunchedByAccount(processInfo.getSid());
        processInfoVO.setProcessExecutions(processExecutionsLaunchedByAccount);

        return processInfoVO;
    }

    public ProcessInfo findByCode(String code) {
        return findSingle("code", code);
    }

    public ProcessInfo findBySid(Long sid) {
        return findSingle("sid", sid);
    }

    public List<ProcessInfo> findBySids(List<Long> searchingSids) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchingSids", searchingSids);
        return findByParams(params);
    }

    private ProcessInfo findSingle(String paramName, Object paramValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(paramName, paramValue);
        List<ProcessInfo> matchedProcessInfoList = findByParams(params);
        if (Objects.nonNull(matchedProcessInfoList) && !matchedProcessInfoList.isEmpty()) {
            return matchedProcessInfoList.get(0);
        }
        return null;
    }

    public List<ProcessInfo> findByParams(Map<String, Object> params) {
        List<ProcessInfo> processInfoList;
        try {
            processInfoList = processInfoMaper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return processInfoList;
    }
}
