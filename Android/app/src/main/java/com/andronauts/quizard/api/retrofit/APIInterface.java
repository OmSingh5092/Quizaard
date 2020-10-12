package com.andronauts.quizard.api.retrofit;

import com.andronauts.quizard.api.responseModels.faculty.FacultyGetResponse;
import com.andronauts.quizard.api.responseModels.faculty.FacultyUpdateResponse;
import com.andronauts.quizard.api.responseModels.signIn.GoogleSignInResponse;
import com.andronauts.quizard.api.responseModels.student.StudentGetResponse;
import com.andronauts.quizard.api.responseModels.student.StudentUpdateResponse;

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

    @POST("student/update")
    Call<StudentUpdateResponse> studentUpdate(@Body Map<String, String> body);
    @POST("student/get")
    Call<StudentGetResponse> studentGetProfile(@Body Map<String, String> body);

    @POST("faculty/update")
    Call<FacultyUpdateResponse> facultyUpdate(@Body Map<String, String> body);
    @POST("faculty/update")
    Call<FacultyGetResponse> facultyGetProfile(@Body Map<String, String> body);
}
