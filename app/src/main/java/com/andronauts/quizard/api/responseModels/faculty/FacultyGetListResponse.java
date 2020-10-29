package com.andronauts.quizard.api.responseModels.faculty;

import com.andronauts.quizard.dataModels.Faculty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FacultyGetListResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("faculties")
    @Expose
    private List<Faculty> faculties;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }
}
