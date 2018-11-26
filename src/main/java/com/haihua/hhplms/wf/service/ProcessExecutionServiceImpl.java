package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.utils.EnumUtil;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.wf.entity.Route;
import com.haihua.hhplms.wf.entity.ProcessExecution;
import com.haihua.hhplms.wf.entity.ProcessInfo;
import com.haihua.hhplms.wf.entity.Step;
import com.haihua.hhplms.wf.mapper.ProcessExecutionMapper;
import com.haihua.hhplms.wf.vo.ProcessExecutionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("processExecutionService")
public class ProcessExecutionServiceImpl implements ProcessExecutionService {
    @Autowired
    private ProcessExecutionMapper processExecutionMapper;

    @Autowired
    @Qualifier("stepService")
    private StepService stepService;

    @Autowired
    @Qualifier("processInfoService")
    private ProcessInfoService processInfoService;

    @Autowired
    @Qualifier("routeService")
    private RouteService routeService;

    @Autowired
    @Qualifier("defaultBizProcessHandler")
    private BizProcessHandler bizProcessHandler;

    public PageWrapper<List<ProcessExecutionVO>> loadProcessExecutionsByPage(Long processSid,
                                                                             String processStatus,
                                                                             String currentStepSid,
                                                                             String stepStatus,
                                                                             String activeFlag,
                                                                             Long checkedTimeFrom,
                                                                             Long checkedTimeTo,
                                                                             Long initTimeFrom,
                                                                             Long initTimeTo,
                                                                             Integer pageNo,
                                                                             Integer pageSize) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            throw new ServiceException("You have insufficient right to view process executions");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("assignedToRoles", WebUtils.getGrantedRoles());
        params.put("assignedToIndividual", WebUtils.getLoginName());
        if (Objects.nonNull(processSid)) {
            params.put("processSid", processSid);
        }
        if (!StringUtils.isBlank(processStatus)) {
            params.put("processStatus", processStatus);
        }
        if (Objects.nonNull(currentStepSid)) {
            params.put("currentStepSid", currentStepSid);
        }
        if (!StringUtils.isBlank(stepStatus)) {
            params.put("stepStatus", stepStatus);
        }
        if (!StringUtils.isBlank(activeFlag)) {
            params.put("activeFlag", activeFlag);
        }
        if (Objects.nonNull(checkedTimeFrom)) {
            params.put("checkedTimeFrom", new Date(checkedTimeFrom));
        }
        if (Objects.nonNull(checkedTimeTo)) {
            params.put("checkedTimeTo", new Date(checkedTimeTo));
        }
        if (Objects.nonNull(initTimeFrom)) {
            params.put("initTimeFrom", new Date(initTimeFrom));
        }
        if (Objects.nonNull(initTimeTo)) {
            params.put("initTimeTo", new Date(initTimeTo));
        }
        return loadProcessExecutionsByPage(params, pageNo, pageSize);
    }

    public PageWrapper<List<ProcessExecutionVO>> loadProcessExecutionsByPage(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        final int count = countByParams(params);
        final PageWrapper<List<ProcessExecutionVO>> pageOfProcessExecutions = new PageWrapper<>(pageNo, pageSize, count);
        if (count > 0) {
            pageNo = Objects.isNull(pageNo) ? GlobalConstant.DEFAULT_PAGE_NO : pageNo;
            pageSize = Objects.isNull(pageSize) ? GlobalConstant.DEFAULT_PAGE_SIZE : pageSize;
            params.put("offset", (pageNo - 1) * pageSize);
            params.put("limit", pageSize);
            final List<ProcessExecution> matchedProcessExecutions = findByParams(params);
            if (Objects.nonNull(matchedProcessExecutions) && !matchedProcessExecutions.isEmpty()) {
                final Set<Long> processSidSet = new HashSet<>();
                final Set<Long> stepSidSet = new HashSet<>();
                matchedProcessExecutions.forEach(matchedProcessExecution -> {
                    processSidSet.add(matchedProcessExecution.getProcessSid());
                    stepSidSet.add(matchedProcessExecution.getCurrentStepSid());
                });

                final List<ProcessInfo> matchedProcessInfos = processInfoService.findBySids(Arrays.asList(processSidSet.toArray(new Long [] {})));
                final Map<Long, ProcessInfo> processInfoMap = new HashMap<>();
                matchedProcessInfos.forEach(matchedProcessInfo -> processInfoMap.put(matchedProcessInfo.getSid(), matchedProcessInfo));

                List<Step> matchedSteps = stepService.findBySids(Arrays.asList(stepSidSet.toArray(new Long [] {})));
                final Map<Long, Step> stepMap = new HashMap<>();
                matchedSteps.forEach(matchedStep -> stepMap.put(matchedStep.getSid(), matchedStep));

                pageOfProcessExecutions.setResult(matchedProcessExecutions.stream()
                        .map(matchedProcessExecution -> new ProcessExecutionVO(matchedProcessExecution,
                                processInfoMap.get(matchedProcessExecution.getProcessSid()),
                                stepMap.get(matchedProcessExecution.getCurrentStepSid())))
                        .collect(Collectors.toList()));
            }
        }
        return pageOfProcessExecutions;
    }

    public List<ProcessExecutionVO> getProcessExecutionsLaunchedByAccount(Long processSid) {
        String processInstanceId = null;
        final ProcessExecution currentStepOfExecutingProcess = getCurrentStepOfExecutingProcess(processSid);
        if (Objects.nonNull(currentStepOfExecutingProcess)) {
            processInstanceId = currentStepOfExecutingProcess.getProcessInstanceId();
        } else {
            final ProcessExecution lastStepOfLatestCompletedProcess = getLastStepOfLatestCompletedProcess(processSid);
            if (Objects.nonNull(lastStepOfLatestCompletedProcess)) {
                processInstanceId = lastStepOfLatestCompletedProcess.getProcessInstanceId();
            }
        }
        if (Objects.isNull(processInstanceId)) {
            return null;
        }
        return getProcessExecutionsByProcessInstanceId(processInstanceId)
                .stream().map(processExecution -> new ProcessExecutionVO(processExecution))
                .collect(Collectors.toList());
    }

    private List<ProcessExecution> getProcessExecutionsByProcessInstanceId(String processInstanceId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("processInstanceId", processInstanceId);
        return getByParams(params);
    }

    private ProcessExecution getCurrentStepOfExecutingProcess(Long processSid) {
        final Map<String, Object> params = new HashMap<>();
        params.put("processSid", processSid);
        params.put("processStatus", ProcessExecution.ProcessStatus.PROCESSING.getCode());
        params.put("activeFlag", GlobalConstant.FLAG_YES_VALUE);
        params.put("initBy", WebUtils.getLoginName());
        return findSingle(params);
    }

    private ProcessExecution getLastStepOfLatestCompletedProcess(Long processSid) {
        final Map<String, Object> params = new HashMap<>();
        params.put("processSid", processSid);
        params.put("processStatus", ProcessExecution.ProcessStatus.COMPLETED.getCode());
        params.put("activeFlag", GlobalConstant.FLAG_NO_VALUE);
        params.put("initBy", WebUtils.getLoginName());
        return findSingle(params);
    }

    @Transactional
    public void checkProcessExecution(Long processExecutionSid) {
        String userType = WebUtils.getUserType();
        if (Role.Category.ACCOUNT.getCode().equals(userType)) {
            throw new ServiceException("You have insufficient right to check pending item");
        }

        ProcessExecution processExecution = findBySid(processExecutionSid);
        if (Objects.isNull(processExecution)) {
            throw new ServiceException("Current pending item does not exist, failed to check");
        }
        if (!GlobalConstant.FLAG_YES_VALUE.equals(processExecution.getActiveFlag())) {
            throw new ServiceException("Current pending item has been checked by others");
        }

        final Long processSid = processExecution.getProcessSid();
        final Long currentStepSid = processExecution.getCurrentStepSid();
        Route currentRouteFragment = routeService.findRouteFragment(processSid, currentStepSid);

        if (Objects.nonNull(currentRouteFragment.getAttachedBiz())) {
            bizProcessHandler.handleBizProcess(EnumUtil.codeOf(BizCode.class, currentRouteFragment.getAttachedBiz()), processExecution);
        }

        if (lastStep(currentRouteFragment)) {
            completeProcess(processExecution);
        } else {
            finishCurrentStepOfProcess(processExecution);
            processNextStepOfProcess(processExecution, currentRouteFragment);
        }
    }

    public ProcessExecution initProcess(String processCode) {
        ProcessInfo process = processInfoService.findByCode(processCode);
        if (Objects.isNull(process)) {
            throw new ServiceException("Process with code: [" + processCode + "] does not exit, failed to init");
        }
        Route firstRouteFragment = routeService.findFirstRouteFragment(process.getSid());
        ProcessExecution firstStepOfProcess = new ProcessExecution();
        firstStepOfProcess.setInitBy(WebUtils.getLoginName());
        firstStepOfProcess.setInitTime(new Date(System.currentTimeMillis()));
        firstStepOfProcess.setCreatedBy(firstStepOfProcess.getInitBy());
        firstStepOfProcess.setCreatedTime(firstStepOfProcess.getInitTime());
        firstStepOfProcess.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);

        firstStepOfProcess.setProcessSid(process.getSid());
        firstStepOfProcess.setProcessInstanceId(UUID.nameUUIDFromBytes((processCode + "." + firstStepOfProcess.getInitBy() + "." + System.nanoTime()).getBytes()).toString());
        firstStepOfProcess.setProcessStatus(ProcessExecution.ProcessStatus.PROCESSING);
        firstStepOfProcess.setCurrentStepSid(firstRouteFragment.getFromStepSid());
        firstStepOfProcess.setAssignedType(firstRouteFragment.getAssignedType());
        firstStepOfProcess.setAssignedTo(firstRouteFragment.getAssignedTo());
        firstStepOfProcess.setStepStatus(ProcessExecution.StepStatus.PROCESS);
        firstStepOfProcess.setActiveFlag(GlobalConstant.FLAG_YES_VALUE);

        createProcessExecution(firstStepOfProcess);
        return firstStepOfProcess;
    }

    private void completeProcess(ProcessExecution processExecution) {
        Map<String, Object> params = new HashMap<>();
        params.put("sid", processExecution.getSid());
        params.put("processStatus", ProcessExecution.ProcessStatus.COMPLETED.getCode());
        params.put("stepStatus", ProcessExecution.StepStatus.FINISH.getCode());
        params.put("activeFlag", GlobalConstant.FLAG_NO_VALUE);
        params.put("checkedBy", WebUtils.getLoginName());
        params.put("checkedTime", new Date(System.currentTimeMillis()));
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", processExecution.getVersionNum());

        int updatedRows = updateProcessExecution(params);
        if (updatedRows == 0) {
            throw new ServiceException("Pending item is being checked by others, please try again later");
        }
    }

    private void finishCurrentStepOfProcess(ProcessExecution processExecution) {
        Map<String, Object> params = new HashMap<>();
        params.put("sid", processExecution.getSid());
        params.put("stepStatus", ProcessExecution.StepStatus.FINISH.getCode());
        params.put("activeFlag", GlobalConstant.FLAG_NO_VALUE);
        params.put("checkedBy", WebUtils.getLoginName());
        params.put("checkedTime", new Date(System.currentTimeMillis()));
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", processExecution.getVersionNum());

        int updatedRows = updateProcessExecution(params);
        if (updatedRows == 0) {
            throw new ServiceException("Pending item is being checked by others, please try again later");
        }
    }

    private void processNextStepOfProcess(ProcessExecution currentStepOfProcess, Route currentRouteFragment) {
        ProcessExecution nextStepOfProcess = new ProcessExecution();
        nextStepOfProcess.setProcessSid(currentStepOfProcess.getProcessSid());
        nextStepOfProcess.setProcessInstanceId(currentStepOfProcess.getProcessInstanceId());
        nextStepOfProcess.setProcessStatus(currentStepOfProcess.getProcessStatus());
        nextStepOfProcess.setCurrentStepSid(currentRouteFragment.getToStepSid());

        Route nextRouteFragment = routeService.findRouteFragment(currentStepOfProcess.getProcessSid(), currentRouteFragment.getToStepSid());
        nextStepOfProcess.setAssignedType(nextRouteFragment.getAssignedType());
        nextStepOfProcess.setAssignedTo(nextRouteFragment.getAssignedTo());

        nextStepOfProcess.setStepStatus(ProcessExecution.StepStatus.PROCESS);
        nextStepOfProcess.setActiveFlag(GlobalConstant.FLAG_YES_VALUE);

        nextStepOfProcess.setInitBy(currentStepOfProcess.getInitBy());
        nextStepOfProcess.setInitTime(currentStepOfProcess.getInitTime());
        nextStepOfProcess.setCreatedBy(WebUtils.getLoginName());
        nextStepOfProcess.setCreatedTime(new Date(System.currentTimeMillis()));
        nextStepOfProcess.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);

        createProcessExecution(nextStepOfProcess);
    }

    private boolean lastStep(Route route) {
        return Objects.isNull(route.getToStepSid());
    }

    public int createProcessExecution(ProcessExecution processExecution) {
        int insertedRows;
        try {
            insertedRows = processExecutionMapper.create(processExecution);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public int updateProcessExecution(Map<String, Object> params) {
        int updatedRows;
        try {
            updatedRows = processExecutionMapper.updateByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return updatedRows;
    }

    public ProcessExecution findBySid(Long sid) {
        return findSingle("sid", sid);
    }

    private ProcessExecution findSingle(Map<String, Object> params) {
        List<ProcessExecution> matchedProcessExecutions = getByParams(params);
        if (Objects.nonNull(matchedProcessExecutions) && !matchedProcessExecutions.isEmpty()) {
            return matchedProcessExecutions.get(0);
        }
        return null;
    }

    private ProcessExecution findSingle(String paramName, Object paramValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(paramName, paramValue);
        return findSingle(params);
    }

    public List<ProcessExecution> getByParams(Map<String, Object> params) {
        List<ProcessExecution> processExecutions;
        try {
            processExecutions = processExecutionMapper.getByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return processExecutions;
    }

    public int countByParams(Map<String, Object> params) {
        int count;
        try {
            count = processExecutionMapper.countByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return count;
    }

    public List<ProcessExecution> findByParams(Map<String, Object> params) {
        List<ProcessExecution> matchedProcessExecutions;
        try {
            matchedProcessExecutions = processExecutionMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedProcessExecutions;
    }
}
