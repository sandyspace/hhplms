package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.ana.entity.Account;
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
                                                                             Long currentStepSid,
                                                                             String stepStatus,
                                                                             String activeFlag,
                                                                             Long checkedTimeFrom,
                                                                             Long checkedTimeTo,
                                                                             Long initTimeFrom,
                                                                             Long initTimeTo,
                                                                             Integer pageNo,
                                                                             Integer pageSize) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限查看，请立刻停止非法操作");
        }
        Map<String, Object> params = new HashMap<>();
        if (WebUtils.isCompany()) {
            params.put("processOwner", Role.Category.ACCOUNT.getCode());
            params.put("ownerSid", WebUtils.getCompanyId());
        } else {
            params.put("processOwner", Role.Category.EMPLOYEE.getCode());
        }
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

    public boolean existExecutingProcess(String processCode, Long ownSid, String initBy) {
        ProcessInfo processInfo = processInfoService.findByCode(processCode);
        if (Objects.nonNull(processInfo)) {
            Map<String, Object> params = new HashMap<>();
            params.put("processSid", processInfo.getSid());
            params.put("processOwner", processInfo.getOwner());
            if (processInfo.getOwner() == Role.Category.ACCOUNT) {
                params.put("ownerSid", ownSid);
            }
            params.put("processStatus", ProcessExecution.ProcessStatus.PROCESSING.getCode());
            params.put("activeFlag", GlobalConstant.FLAG_YES_VALUE);
            params.put("initBy", initBy);
            List<ProcessExecution> executingProcesses = getByParams(params);
            return Objects.nonNull(executingProcesses) && !executingProcesses.isEmpty();
        }
        return false;
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

    public void terminateProcessExecution(Long processExecutionSid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限终止该待办事项，请立刻停止非法操作");
        }

        ProcessExecution processExecution = findBySid(processExecutionSid);
        if (Objects.isNull(processExecution)) {
            throw new ServiceException("该待办事项不存在");
        }

        if (processExecution.getProcessOwner() != EnumUtil.codeOf(Role.Category.class, WebUtils.getUserType())) {
            throw new ServiceException("你没有权限权限终止该待办事项");
        }

        terminateProcess(processExecution);
    }

    private void terminateProcess(ProcessExecution processExecution) {
        Map<String, Object> params = new HashMap<>();
        params.put("sid", processExecution.getSid());
        params.put("processStatus", ProcessExecution.ProcessStatus.REJECTED.getCode());
        params.put("stepStatus", ProcessExecution.StepStatus.ERROR.getCode());
        params.put("activeFlag", GlobalConstant.FLAG_NO_VALUE);
        params.put("checkedBy", WebUtils.getLoginName());
        params.put("checkedTime", new Date(System.currentTimeMillis()));
        params.put("updatedBy", WebUtils.getLoginName());
        params.put("updatedTime", new Date(System.currentTimeMillis()));
        params.put("versionNum", processExecution.getVersionNum());

        int updatedRows = updateProcessExecution(params);
        if (updatedRows == 0) {
            throw new ServiceException("此待办事项正在被其他人审核，请稍后在试");
        }
    }

    @Transactional
    public void checkProcessExecution(Long processExecutionSid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限审核，请立刻停止非法操作");
        }

        ProcessExecution processExecution = findBySid(processExecutionSid);
        if (Objects.isNull(processExecution)) {
            throw new ServiceException("该待办事项不存在");
        }

        if (processExecution.getProcessOwner() != EnumUtil.codeOf(Role.Category.class, WebUtils.getUserType())) {
            throw new ServiceException("你没有权限审核该待办事项");
        }

        if (!GlobalConstant.FLAG_YES_VALUE.equals(processExecution.getActiveFlag())) {
            throw new ServiceException("该待办事项已经被审核");
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

    public ProcessExecution initProcess(String processCode, Long ownerSid) {
        ProcessInfo process = processInfoService.findByCode(processCode);
        if (Objects.isNull(process)) {
            throw new ServiceException("编码为[" + processCode + "]的流程不存在，启动失败");
        }
        Route firstRouteFragment = routeService.findFirstRouteFragment(process.getSid());
        ProcessExecution firstStepOfProcess = new ProcessExecution();
        firstStepOfProcess.setInitBy(WebUtils.getLoginName());
        firstStepOfProcess.setInitTime(new Date(System.currentTimeMillis()));
        firstStepOfProcess.setCreatedBy(firstStepOfProcess.getInitBy());
        firstStepOfProcess.setCreatedTime(firstStepOfProcess.getInitTime());
        firstStepOfProcess.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);

        firstStepOfProcess.setProcessSid(process.getSid());
        firstStepOfProcess.setProcessOwner(process.getOwner());
        firstStepOfProcess.setOwnerSid(ownerSid);

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
            throw new ServiceException("此待办事项正在被其他人审核，请稍后在试");
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
            throw new ServiceException("此待办事项正在被其他人审核，请稍后在试");
        }
    }

    private void processNextStepOfProcess(ProcessExecution currentStepOfProcess, Route currentRouteFragment) {
        ProcessExecution nextStepOfProcess = new ProcessExecution();
        nextStepOfProcess.setProcessSid(currentStepOfProcess.getProcessSid());
        nextStepOfProcess.setProcessOwner(currentStepOfProcess.getProcessOwner());
        nextStepOfProcess.setOwnerSid(currentStepOfProcess.getOwnerSid());

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
