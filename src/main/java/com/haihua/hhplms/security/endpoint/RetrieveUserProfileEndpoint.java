package com.haihua.hhplms.security.endpoint;

import com.haihua.hhplms.security.model.UserProfile;
import com.haihua.hhplms.common.utils.WebUtils;
import com.haihua.hhplms.security.model.UserProfile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetrieveUserProfileEndpoint {

    @RequestMapping(value="/api/auth/user/profile", method= RequestMethod.GET, produces={ MediaType.APPLICATION_JSON_UTF8_VALUE })
    public UserProfile retrieveUserProfile() {
        return WebUtils.getUserContext().getUserProfile();
    }
}
