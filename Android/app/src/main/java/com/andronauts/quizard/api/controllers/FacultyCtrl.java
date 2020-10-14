package com.andronauts.quizard.api.controllers;

import android.content.Context;

import com.andronauts.quizard.api.responseModels.faculty.FacultyGetResponse;
import com.andronauts.quizard.api.responseModels.faculty.FacultyUpdateResponse;
import com.andronauts.quizard.api.responseModels.student.StudentUpdateResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.utils.SharedPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultyCtrl {

    private Context context;
    private SharedPrefs prefs;

    public FacultyCtrl(Context context) {
        this.context = context;
        prefs = new SharedPrefs(context);
    }

    public interface updateHandler{
        void onSuccess();
        void onFailure(Throwable t);
    }

    public interface GetProfileHandler{
        void onSuccess(Faculty faculty);
        void onFailure(Throwable t);
    }

    public void updateProfile(Faculty faculty, updateHandler handler){
        Call<FacultyUpdateResponse> call = RetrofitClient.getClient().facultyUpdate(prefs.getToken(),faculty);

        call.enqueue(new Callback<FacultyUpdateResponse>() {
            @Override
            public void onResponse(Call<FacultyUpdateResponse> call, Response<FacultyUpdateResponse> response) {
                handler.onSuccess();
            }

            @Override
            public void onFailure(Call<FacultyUpdateResponse> call, Throwable t) {
                handler.onFailure(t);
            }
        });
    }

    public void getProfile (GetProfileHandler handler){
        Call<FacultyGetResponse> call = RetrofitClient.getClient().facultyGetProfile(prefs.getToken());
        call.enqueue(new Callback<FacultyGetResponse>() {
            @Override
            public void onResponse(Call<FacultyGetResponse> call, Response<FacultyGetResponse> response) {
                if(response.isSuccessful()){
                    handler.onSuccess(response.body().getFaculty());
                }
            }

            @Override
            public void onFailure(Call<FacultyGetResponse> call, Throwable t) {
                handler.onFailure(t);
            }
        });
    }
}
