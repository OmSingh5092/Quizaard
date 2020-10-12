package com.andronauts.quizard.api.controllers;

import android.content.Context;

import com.andronauts.quizard.api.responseModels.student.StudentUpdateResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Student;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Call<StudentUpdateResponse> call = RetrofitClient.getClient().studentUpdate(prefs.getToken(),student);
        call.enqueue(new Callback<StudentUpdateResponse>() {
            @Override
            public void onResponse(Call<StudentUpdateResponse> call, Response<StudentUpdateResponse> response) {
                handler.onSuccess();
            }

            @Override
            public void onFailure(Call<StudentUpdateResponse> call, Throwable t) {

            }
        });
    }
}
