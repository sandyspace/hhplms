package com.haihua.hhplms.wf.web.controller;

import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.wf.service.RouteService;
import com.haihua.hhplms.wf.vo.RouteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RouteController {

    @Autowired
    @Qualifier("routeService")
    private RouteService routeService;

    @GetMapping(path = "/api/wf/routes/{processId}/{fromStepId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<RouteVO> getRouteFragment(@PathVariable("processId") Long processSid, @PathVariable("fromStepId") Long fromStepSid) {
        return ResultBean.Success.of(routeService.getRouteFragment(processSid, fromStepSid), "");
    }
}
