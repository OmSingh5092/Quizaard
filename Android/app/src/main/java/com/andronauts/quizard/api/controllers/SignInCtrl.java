package com.andronauts.quizard.api.controllers;

import android.content.Context;
import android.widget.Toast;

import com.andronauts.quizard.api.responseModels.signIn.GoogleSignInResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInCtrl {
    Context context;
    SharedPrefs prefs;

    public SignInCtrl(Context context) {
        this.context = context;
        prefs = new SharedPrefs(context);
    }

    public interface signInHandler{
        void onSuccess(String token, boolean isNewUser);
        void onFailure(Throwable e);
    }

    public void signIn(String token,boolean isStudent, signInHandler handler){
        Map<String,String> body = new HashMap<>();
        body.put("idToken",token);
        Call<GoogleSignInResponse> call;
        if(isStudent){
            call = RetrofitClient.getClient().studentGoogleSignIn(body);
        }else{
            call = RetrofitClient.getClient().facultyGoogleSignIn(body);
        }
        call.enqueue(new Callback<GoogleSignInResponse>() {
            @Override
            public void onResponse(Call<GoogleSignInResponse> call, Response<GoogleSignInResponse> response) {
                if(response.isSuccessful()){
                    handler.onSuccess(response.body().getAuthToken(),response.body().isNewUser());
                }else{
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoogleSignInResponse> call, Throwable t) {
                handler.onFailure(t);
            }
        });
    }
}
