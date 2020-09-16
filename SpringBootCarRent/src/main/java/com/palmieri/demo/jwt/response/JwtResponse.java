package com.palmieri.demo.jwt.response;

import com.palmieri.demo.entities.User;

public class JwtResponse {

    private String token;



    private String type = "Bearer";
    private User user;

    public JwtResponse(String token, String type, User user) {
        this.token = token;
        this.type=type;
        this.user = user;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
