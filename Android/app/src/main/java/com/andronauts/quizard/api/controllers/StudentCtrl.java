package com.andronauts.quizard.api.controllers;

import android.content.Context;

import com.andronauts.quizard.dataModels.Student;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.HashMap;
import java.util.Map;

public class StudentCtrl {

    private Context context;
    private SharedPrefs prefs;

    public StudentCtrl(Context context) {
        this.context = context;
        prefs = new SharedPrefs(context);
    }

    public interface updateHandler{
        void onSuccess();
        void onFailure(Throwable t);
    }

    public interface getProfileHandler{
        void onSuccess();
        void onFailure(Throwable t);
    }

    public void updateProfile(Student student, updateHandler handler){

    }
}
