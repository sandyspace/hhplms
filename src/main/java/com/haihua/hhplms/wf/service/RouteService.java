package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.wf.entity.Route;

import java.util.List;

public interface RouteService {
    Route findFirstRouteFragment(Long processSid);
    Route findRouteFragment(Long processSid, Long fromStepSid);
    List<Route> findByProcessSid(Long processSid);
}
