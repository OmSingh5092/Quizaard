package com.andronauts.quizard.api.responseModels.quiz;

import com.andronauts.quizard.dataModels.Quiz;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuizListGetResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("quizzes")
    @Expose
    private List<Quiz> quizzes;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}
