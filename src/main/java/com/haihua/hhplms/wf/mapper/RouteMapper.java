package com.haihua.hhplms.wf.mapper;

import com.haihua.hhplms.wf.entity.Route;

import java.util.List;
import java.util.Map;

public interface RouteMapper {
    List<Route> findByParams(Map<String, Object> params);
}
