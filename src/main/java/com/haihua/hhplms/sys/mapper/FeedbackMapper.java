package com.haihua.hhplms.sys.mapper;

import com.haihua.hhplms.sys.entity.Feedback;

import java.util.List;
import java.util.Map;

public interface FeedbackMapper {
    int create(Feedback feedback);
    int countByParams(Map<String, Object> params);
    List<Feedback> findByParams(Map<String, Object> params);
    int deleteByParams(Map<String, Object> params);
}
