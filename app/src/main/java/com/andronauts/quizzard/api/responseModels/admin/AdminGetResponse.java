package com.andronauts.quizzard.api.responseModels.admin;

import com.andronauts.quizzard.dataModels.Admin;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdminGetResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("admin")
    @Expose
    private Admin admin;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}
