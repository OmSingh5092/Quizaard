package com.andronauts.quizard.api.responseModels.subject;

import com.andronauts.quizard.dataModels.Subject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class SubjectsGetByStudentResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("subjects")
    @Expose
    private Map<String,Subject> studentSubject;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, Subject> getStudentSubject() {
        return studentSubject;
    }

    public void setStudentSubject(Map<String, Subject> studentSubject) {
        this.studentSubject = studentSubject;
    }
}
