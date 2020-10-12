package com.andronauts.quizard.dataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Student {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("registration_number")
    @Expose
    private String registrationNumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("photo_path")
    @Expose
    private String photoPath;
    @SerializedName("admit_card_path")
    @Expose
    private String admitCardPath;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("year")
    private String year;
    @SerializedName("is_registered")
    @Expose
    private boolean isRegistered;
    @SerializedName("subjects")
    @Expose
    private List<String> subjects;
    @SerializedName("results")
    @Expose
    private List<String> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getAdmitCardPath() {
        return admitCardPath;
    }

    public void setAdmitCardPath(String admitCardPath) {
        this.admitCardPath = admitCardPath;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }
}
