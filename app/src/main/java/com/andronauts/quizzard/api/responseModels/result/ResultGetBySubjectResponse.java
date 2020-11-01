package com.andronauts.quizzard.api.responseModels.result;

import com.andronauts.quizzard.dataModels.Result;
import com.andronauts.quizzard.dataModels.Student;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ResultGetBySubjectResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("results")
    @Expose
    private List<Result> results;
    @SerializedName("average")
    @Expose
    private float average;
    @SerializedName("attendance")
    @Expose
    private float attendance;
    @SerializedName("students")
    @Expose
    private Map<String,Student> students;

    public Map<String, Student> getStudents() {
        return students;
    }

    public void setStudents(Map<String, Student> students) {
        this.students = students;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public float getAttendance() {
        return attendance;
    }

    public void setAttendance(float attendance) {
        this.attendance = attendance;
    }
}
