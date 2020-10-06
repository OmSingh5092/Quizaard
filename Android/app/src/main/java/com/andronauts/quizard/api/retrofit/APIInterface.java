package com.andronauts.quizard.api.retrofit;

import com.andronauts.quizard.api.responseModels.signIn.GoogleSignInResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {
    //Google SignIn
    @POST("signin/student")
    Call<GoogleSignInResponse> studentGoogleSignIn(@Body Map<String, String> body);
    @POST("signin/faculty")
    Call<GoogleSignInResponse> facultyGoogleSignIn(@Body Map<String, String> body);
}
