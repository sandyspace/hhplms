package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.wf.entity.Route;
import com.haihua.hhplms.wf.vo.RouteVO;

import java.util.List;

public interface RouteService {
    RouteVO getRouteFragment(Long processSid, Long fromStepSid);
    Route findFirstRouteFragment(Long processSid);
    Route findRouteFragment(Long processSid, Long fromStepSid);
    List<Route> findByProcessSid(Long processSid);
}
