package com.haihua.hhplms.wf.web.controller;

import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.wf.service.StepService;
import com.haihua.hhplms.wf.vo.StepVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StepController {
    @Autowired
    @Qualifier("stepService")
    private StepService stepService;

    @GetMapping(path = "/api/wf/processes/{id}/steps", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<List<StepVO>> getStepsOfProcess(@PathVariable("id") Long processSid) {
        return ResultBean.Success.of(stepService.getStepsOfProcess(processSid), "");
    }
}
