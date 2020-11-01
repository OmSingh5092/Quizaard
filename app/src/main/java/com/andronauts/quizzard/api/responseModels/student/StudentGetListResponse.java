package com.andronauts.quizzard.api.responseModels.student;

import com.andronauts.quizzard.dataModels.Student;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StudentGetListResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("students")
    @Expose
    private List<Student> students;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
