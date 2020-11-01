package com.andronauts.quizzard.api.responseModels.subject;

import com.andronauts.quizzard.dataModels.Subject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubjectGetResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("subject")
    @Expose
    private Subject subject;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
