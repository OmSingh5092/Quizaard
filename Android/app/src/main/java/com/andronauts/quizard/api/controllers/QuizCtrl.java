package com.andronauts.quizard.api.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.andronauts.quizard.api.responseModels.quiz.QuizCreateResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.utils.SharedPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizCtrl {

    Context context;
    SharedPrefs prefs;

    public QuizCtrl(Context context) {
        this.context = context;
        prefs = new SharedPrefs(context);
    }

    public interface CreateQuizHandler {
        void onSuccess(Quiz quiz);
        void onFailure(Throwable t);
    }

    public void createQuiz(Quiz quiz, CreateQuizHandler handler){
        Call<QuizCreateResponse> call = RetrofitClient.getClient().createQuiz(prefs.getToken(),quiz);
        call.enqueue(new Callback<QuizCreateResponse>() {
            @Override
            public void onResponse(Call<QuizCreateResponse> call, Response<QuizCreateResponse> response) {

                if(response.isSuccessful()){
                    handler.onSuccess(response.body().getQuiz());
                    Log.i("Response",response.body().getQuiz().toString());
                }else{
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuizCreateResponse> call, Throwable t) {
                Log.e("Error",t.getMessage());
                handler.onFailure(t);
            }
        });
    }
}
