package com.haihua.hhplms.common.utils;

import com.haihua.hhplms.security.model.UserContext;
import com.haihua.hhplms.security.model.UserContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    private static final String CONTENT_TYPE = "Content-type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }

    public static boolean isAjax(SavedRequest request) {
        return request.getHeaderValues(X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
    }

    public static boolean isContentTypeJson(SavedRequest request) {
        return request.getHeaderValues(CONTENT_TYPE).contains(CONTENT_TYPE_JSON);
    }

    public static UserContext getUserContext() {
        return (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Long getUserId() {
        UserContext userContext = getUserContext();
        return userContext.getUserProfile().getBasicInfo().getId();
    }

    public static String getLoginName() {
        UserContext userContext = getUserContext();
        return userContext.getUserProfile().getBasicInfo().getLoginName();
    }

    public static String getUserType() {
        UserContext userContext = getUserContext();
        return userContext.getUserProfile().getBasicInfo().getType();
    }

    public static Long getCompanyId() {
        UserContext userContext = getUserContext();
        return userContext.getUserProfile().getBasicInfo().getCompanyId();
    }
}
