package com.andronauts.quizard.api.responseModels.faculty;

import com.andronauts.quizard.dataModels.Faculty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacultyUpdateResponse {

    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("update")
    @Expose
    private Faculty update;
    @SerializedName("msg")
    @Expose
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Faculty getUpdate() {
        return update;
    }

    public void setUpdate(Faculty update) {
        this.update = update;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
