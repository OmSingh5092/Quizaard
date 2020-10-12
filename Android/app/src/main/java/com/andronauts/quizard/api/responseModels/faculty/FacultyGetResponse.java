package com.andronauts.quizard.api.responseModels.faculty;

import com.andronauts.quizard.dataModels.Faculty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacultyGetResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("profile")
    @Expose
    private Faculty faculty;
    @SerializedName("msg")
    @Expose
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
