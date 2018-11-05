package com.haihua.hhplms.security.model;


public class UserContext {
    private UserProfile userProfile;
    private String username;

    public UserContext(String username, UserProfile userProfile) {
        this.username = username;
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public String getUsername() {
        return username;
    }
}
