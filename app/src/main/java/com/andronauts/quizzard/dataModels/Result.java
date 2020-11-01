package com.andronauts.quizzard.dataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("score")
    @Expose
    private int score;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("submit_time")
    @Expose
    private String submitTime;
    @SerializedName("responses")
    @Expose
    private int[] responses;
    @SerializedName("student")
    @Expose
    private String student;
    @SerializedName("quiz")
    @Expose
    private String quiz;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("end_time")
    @Expose
    private String endTime;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getQuiz() {
        return quiz;
    }

    public void setQuiz(String quiz) {
        this.quiz = quiz;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public int[] getResponses() {
        return responses;
    }

    public void setResponses(int[] responses) {
        this.responses = responses;
    }
}
