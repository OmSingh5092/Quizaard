package com.andronauts.quizard.api.responseModels.signIn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoogleSignInResponse {
    @SerializedName("success")
    @Expose
    boolean success;
    @SerializedName("jwt")
    @Expose
    String authToken;
    @SerializedName("newUser")
    @Expose
    boolean newUser;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }
}

