package com.andronauts.quizard.api.responseModels.result;

import com.andronauts.quizard.dataModels.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
