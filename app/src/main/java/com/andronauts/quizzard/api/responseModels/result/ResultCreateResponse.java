package com.andronauts.quizzard.api.responseModels.result;

import com.andronauts.quizzard.dataModels.Result;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultCreateResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("result")
    @Expose
    private Result result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
