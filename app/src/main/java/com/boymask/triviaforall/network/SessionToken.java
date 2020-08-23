package com.boymask.triviaforall.network;

import com.google.gson.annotations.SerializedName;

public class SessionToken {

    @SerializedName("token")
    private String token;

    public SessionToken(String token) {
        this.token = token;

    }

    public String getToken() {
        return token;
    }
}
