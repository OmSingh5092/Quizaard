package com.andronauts.quizard.api.controllers;

import android.content.Context;

import com.andronauts.quizard.api.responseModels.subject.SubjectGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.utils.SharedPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectCtrl {

    Context context;
    SharedPrefs prefs;

    public SubjectCtrl(Context context) {
        this.context = context;
        prefs = new SharedPrefs(context);
    }

    public interface GetSubjectHandler{
        void onSuccess(Subject subject);
        void onFailure(Throwable t);
    }

    public void getSubject (String id,GetSubjectHandler handler){
        Call<SubjectGetResponse> call = RetrofitClient.getClient().getSubject(id);

        call.enqueue(new Callback<SubjectGetResponse>() {
            @Override
            public void onResponse(Call<SubjectGetResponse> call, Response<SubjectGetResponse> response) {
                if(response.isSuccessful()){
                    handler.onSuccess(response.body().getSubject());
                }
            }

            @Override
            public void onFailure(Call<SubjectGetResponse> call, Throwable t) {
                handler.onFailure(t);
            }
        });
    }
}
