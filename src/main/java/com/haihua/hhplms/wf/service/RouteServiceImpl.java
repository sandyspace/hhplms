package com.haihua.hhplms.wf.service;

import com.haihua.hhplms.ana.entity.Account;
import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.wf.entity.Route;
import com.haihua.hhplms.wf.mapper.RouteMapper;
import com.haihua.hhplms.wf.vo.RouteVO;
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

    public RouteVO getRouteFragment(Long processSid, Long fromStepSid) {
        if (WebUtils.isMember()) {
            throw new ServiceException(Account.Type.MEMBER.getName() + "没有权限查看，请立刻停止非法操作");
        }
        Route routeFragment = findRouteFragment(processSid, fromStepSid);
        return new RouteVO(routeFragment);
    }

    public Route findFirstRouteFragment(Long processSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("processSid", processSid);
        params.put("startFlag", GlobalConstant.FLAG_YES_VALUE);
        return findSingle(params);
    }

    public Route findRouteFragment(Long processSid, Long fromStepSid) {
        Map<String, Object> params = new HashMap<>();
        params.put("processSid", processSid);
        params.put("fromStepSid", fromStepSid);
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
        params.put("processSid", processSid);
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
