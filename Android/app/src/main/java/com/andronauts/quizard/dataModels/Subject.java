package com.andronauts.quizard.dataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subject {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("subject_code")
    @Expose
    private String subjectCode;
    @SerializedName("department")
    @Expose
    private String department;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
