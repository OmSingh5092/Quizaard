package com.andronauts.quizard.api.responseModels.quiz;

import com.andronauts.quizard.dataModels.Quiz;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizResponse {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("quiz")
    @Expose
    private Quiz quiz;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}
