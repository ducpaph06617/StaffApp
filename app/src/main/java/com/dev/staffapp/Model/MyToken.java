package com.dev.staffapp.Model;

import com.dev.staffapp.Common.Common;

public class MyToken {

    private  String userPhone;
    private String token;
    private Common.TOKEN_TYPE tokenType;

    public MyToken() {
    }

    public MyToken(String userPhone, String token, Common.TOKEN_TYPE tokenType) {
        this.userPhone = userPhone;
        this.token = token;
        this.tokenType = tokenType;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Common.TOKEN_TYPE getTokenType() {
        return tokenType;
    }

    public void setTokenType(Common.TOKEN_TYPE tokenType) {
        this.tokenType = tokenType;
    }
}
