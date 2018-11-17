package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.wf.entity.Route;
import com.haihua.hhplms.wf.mapper.RouteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("routeService")
public class RouteServiceImpl implements RouteService {
    @Autowired
    private RouteMapper routeMapper;

    public Route findFirstRouteFragment(Long processSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("process_sid", processSid);
        params.put("startFlag", GlobalConstant.FLAG_YES_VALUE);
        return findSingle(params);
    }

    public Route findRouteFragment(Long processSid, Long fromStepSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("process_sid", processSid);
        params.put("from_step_sid", fromStepSid);
        return findSingle(params);
    }

    public Route findSingle(Map<String, Object> params) {
        List<Route> matchedRouteFragments = findByParams(params);
        if (Objects.isNull(matchedRouteFragments) || matchedRouteFragments.isEmpty()) {
            return null;
        }
        return matchedRouteFragments.get(0);
    }

    public List<Route> findByProcessSid(Long processSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("process_sid", processSid);
        return findByParams(params);
    }

    public List<Route> findByParams(Map<String, Object> params) {
        List<Route> matchedRouteFragments;
        try {
            matchedRouteFragments = routeMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedRouteFragments;
    }
}
