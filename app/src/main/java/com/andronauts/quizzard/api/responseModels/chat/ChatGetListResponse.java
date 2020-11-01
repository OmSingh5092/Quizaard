package com.andronauts.quizzard.api.responseModels.chat;

import com.andronauts.quizzard.dataModels.Chat;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatGetListResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("chats")
    @Expose
    private List<Chat> chats;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }
}
