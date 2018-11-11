package com.haihua.hhplms.sys.web.controller;

import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.model.ResultBean;
import com.haihua.hhplms.sys.entity.Feedback;
import com.haihua.hhplms.sys.service.FeedbackService;
import com.haihua.hhplms.sys.vo.FeedbackCreationVO;
import com.haihua.hhplms.sys.vo.FeedbackVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FeedbackController {
    @Autowired
    @Qualifier("feedbackService")
    private FeedbackService feedbackService;

    @GetMapping(path = "/api/sys/feedbacks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<PageWrapper<List<FeedbackVO>>> loadFeedbacks(@RequestParam(name = "createdTimeFrom", required = false) Long createdTimeFrom,
                                                                              @RequestParam(name = "createdTimeTo", required = false) Long createdTimeTo,
                                                                              @RequestParam(name = "contact", required = false) String contactLike,
                                                                              @RequestParam(name = "contactMobile", required = false) String contactMobileLike,
                                                                              @RequestParam(name = "pageNo") Integer pageNo,
                                                                              @RequestParam(name = "pageSize") Integer pageSize) {
        PageWrapper<List<FeedbackVO>> feedbacks = feedbackService.loadFeedbacksByPage(createdTimeFrom, createdTimeTo, contactLike, contactMobileLike, pageNo, pageSize);
        return ResultBean.Success.of(feedbacks, "");
    }

    @DeleteMapping(path = "/api/sys/feedbacks/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> deleteFeedback(@PathVariable("id") Long sid) {
        feedbackService.deleteBySid(sid);
        return ResultBean.Success.of(sid, "");
    }

    @PostMapping(path = "/api/sys/feedbacks", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultBean.Success<Long> createFeedback(@RequestBody FeedbackCreationVO feedbackCreationVO) {
        Feedback feedback = feedbackService.createFeedback(feedbackCreationVO);
        return ResultBean.Success.of(feedback.getSid(), "");
    }
}
