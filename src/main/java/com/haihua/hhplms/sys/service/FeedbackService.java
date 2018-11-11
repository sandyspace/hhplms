package com.haihua.hhplms.sys.service;

import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.sys.entity.Feedback;
import com.haihua.hhplms.sys.vo.FeedbackCreationVO;
import com.haihua.hhplms.sys.vo.FeedbackVO;

import java.util.List;

public interface FeedbackService {
    PageWrapper<List<FeedbackVO>> loadFeedbacksByPage(Long createdTimeFrom,
                                                      Long createdTimeTo,
                                                      String contactLike,
                                                      String contactMobileLike,
                                                      Integer pageNo,
                                                      Integer pageSize);
    void deleteBySid(Long sid);
    Feedback createFeedback(FeedbackCreationVO feedbackCreationVO);
}
