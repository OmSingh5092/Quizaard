package com.andronauts.quizard.api.responseModels.student;

import com.andronauts.quizard.dataModels.Student;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentUpdateResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("update")
    @Expose
    private Student student;
    @SerializedName("msg")
    @Expose
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
