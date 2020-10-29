package com.andronauts.quizard.api.responseModels.result;

import com.andronauts.quizard.dataModels.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultListGetResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("results")
    @Expose
    private List<Result> results;

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
}
