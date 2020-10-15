package com.andronauts.quizard.api.responseModels.subject;

import com.andronauts.quizard.dataModels.Subject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubjectGetAllResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("subjects")
    @Expose
    private List<Subject> subjects;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
