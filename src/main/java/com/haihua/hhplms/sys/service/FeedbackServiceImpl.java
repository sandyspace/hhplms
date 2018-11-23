package com.haihua.hhplms.sys.service;

import com.haihua.hhplms.ana.entity.Account;
import com.haihua.hhplms.ana.entity.Role;
import com.haihua.hhplms.common.constant.GlobalConstant;
import com.haihua.hhplms.common.exception.ServiceException;
import com.haihua.hhplms.common.model.PageWrapper;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.sys.entity.Feedback;
import com.haihua.hhplms.sys.mapper.FeedbackMapper;
import com.haihua.hhplms.sys.vo.FeedbackCreationVO;
import com.haihua.hhplms.sys.vo.FeedbackVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("feedbackService")
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackMapper feedbackMapper;

    public PageWrapper<List<FeedbackVO>> loadFeedbacksByPage(Long createdTimeFrom,
                                                             Long createdTimeTo,
                                                             String contactLike,
                                                             String contactMobileLike,
                                                             Integer pageNo,
                                                             Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        if (Objects.nonNull(createdTimeFrom)) {
            params.put("createdTimeFrom", new Date(createdTimeFrom));
        }
        if (Objects.nonNull(createdTimeTo)) {
            params.put("createdTimeTo", new Date(createdTimeTo));
        }
        if (!StringUtils.isBlank(contactLike)) {
            params.put("contactLike", contactLike);
        }
        if (!StringUtils.isBlank(contactMobileLike)) {
            params.put("contactMobileLike", contactMobileLike);
        }
        return loadFeedbacksByPage(params, pageNo, pageSize);
    }

    public PageWrapper<List<FeedbackVO>> loadFeedbacksByPage(Map<String, Object> params, Integer pageNo, Integer pageSize) {
        int count = countByParams(params);
        PageWrapper<List<FeedbackVO>> pageOfFeedbacks = new PageWrapper<>(pageNo, pageSize, count);
        if (count > 0) {
            pageNo = Objects.isNull(pageNo) ? GlobalConstant.DEFAULT_PAGE_NO : pageNo;
            pageSize = Objects.isNull(pageSize) ? GlobalConstant.DEFAULT_PAGE_SIZE : pageSize;
            params.put("offset", (pageNo - 1) * pageSize);
            params.put("limit", pageNo * pageSize);
            List<Feedback> matchedFeedbacks = findByParams(params);
            if (Objects.nonNull(matchedFeedbacks) && !matchedFeedbacks.isEmpty()) {
                pageOfFeedbacks.setResult(matchedFeedbacks.stream()
                        .map(pageOfFeedback -> new FeedbackVO(pageOfFeedback))
                        .collect(Collectors.toList()));
            }
        }
        return pageOfFeedbacks;
    }

    public Feedback createFeedback(FeedbackCreationVO feedbackCreationVO) {
        Feedback feedback = new Feedback();
        feedback.setTitle(feedbackCreationVO.getTitle());
        feedback.setContent(feedbackCreationVO.getContent());
        feedback.setContact(feedbackCreationVO.getContact());
        feedback.setContactMobile(feedbackCreationVO.getContactMobile());
        feedback.setCreatedBy("default");
        feedback.setCreatedTime(new Date(System.currentTimeMillis()));
        feedback.setVersionNum(GlobalConstant.INIT_VERSION_NUM_VALUE);

        createFeedback(feedback);
        return feedback;
    }

    public void deleteBySid(Long sid) {
        if (!WebUtils.isEmployee()) {
            throw new ServiceException("你不是" + Role.Category.EMPLOYEE.getName() + "，请立刻停止非法操作");
        }

        Feedback feedback = findBySid(sid);
        if (Objects.isNull(feedback)) {
            throw new ServiceException("ID为[" + sid + "]的访客留言不存在");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("sid", sid);
        int deletedRows = deleteByParams(params);
        if (deletedRows == 0) {
            throw new ServiceException("ID为[" + sid + "]的访客留言已经被其他人删除");
        }
    }

    public int deleteByParams(Map<String, Object> params) {
        int deletedRows;
        try {
            deletedRows = feedbackMapper.deleteByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return deletedRows;
    }

    public int createFeedback(Feedback feedback) {
        int insertedRows;
        try {
            insertedRows = feedbackMapper.create(feedback);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return insertedRows;
    }

    public Feedback findBySid(Long sid) {
        return findSingle("sid", sid);
    }

    private Feedback findSingle(String paramName, Object paramValue) {
        Map<String, Object> params = new HashMap<>();
        params.put(paramName, paramValue);
        List<Feedback> matchedFeedbacks = findByParams(params);
        if (Objects.nonNull(matchedFeedbacks) && !matchedFeedbacks.isEmpty()) {
            return matchedFeedbacks.get(0);
        }
        return null;
    }

    public int countByParams(Map<String, Object> params) {
        int count;
        try {
            count = feedbackMapper.countByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return count;
    }

    public List<Feedback> findByParams(Map<String, Object> params) {
        List<Feedback> matchedFeedbacks;
        try {
            matchedFeedbacks = feedbackMapper.findByParams(params);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        return matchedFeedbacks;
    }
}
