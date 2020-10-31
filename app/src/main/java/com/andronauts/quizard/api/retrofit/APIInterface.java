package com.andronauts.quizard.api.retrofit;

import com.andronauts.quizard.api.responseModels.admin.AdminGetResponse;
import com.andronauts.quizard.api.responseModels.chat.ChatGetListResponse;
import com.andronauts.quizard.api.responseModels.faculty.FacultyGetListResponse;
import com.andronauts.quizard.api.responseModels.faculty.FacultyGetResponse;
import com.andronauts.quizard.api.responseModels.faculty.FacultyUpdateResponse;
import com.andronauts.quizard.api.responseModels.quiz.QuizCreateResponse;
import com.andronauts.quizard.api.responseModels.quiz.QuizResponse;
import com.andronauts.quizard.api.responseModels.quiz.QuizListGetResponse;
import com.andronauts.quizard.api.responseModels.result.ResultCreateResponse;
import com.andronauts.quizard.api.responseModels.result.ResultGetBySubjectResponse;
import com.andronauts.quizard.api.responseModels.result.ResultGetResponse;
import com.andronauts.quizard.api.responseModels.result.ResultListGetResponse;
import com.andronauts.quizard.api.responseModels.signIn.GoogleSignInResponse;
import com.andronauts.quizard.api.responseModels.student.StudentGetListResponse;
import com.andronauts.quizard.api.responseModels.student.StudentGetResponse;
import com.andronauts.quizard.api.responseModels.student.StudentSubjectAddResponse;
import com.andronauts.quizard.api.responseModels.student.StudentSubjectDeleteResponse;
import com.andronauts.quizard.api.responseModels.student.StudentUpdateResponse;
import com.andronauts.quizard.api.responseModels.subject.SubjectResponse;
import com.andronauts.quizard.api.responseModels.subject.SubjectGetAllResponse;
import com.andronauts.quizard.api.responseModels.subject.SubjectGetResponse;
import com.andronauts.quizard.api.responseModels.subject.SubjectsGetByStudentResponse;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.dataModels.Result;
import com.andronauts.quizard.dataModels.Student;
import com.andronauts.quizard.dataModels.Subject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIInterface {
    //Google SignIn
    @POST("signin/student")
    Call<GoogleSignInResponse> studentGoogleSignIn(@Body Map<String, String> body);
    @POST("signin/faculty")
    Call<GoogleSignInResponse> facultyGoogleSignIn(@Body Map<String, String> body);
    @POST("signin/admin")
    Call<GoogleSignInResponse> adminGoogleSignIn(@Body Map<String,String> body);

    @GET("admin/get/students")
    Call<StudentGetListResponse> adminGetStudents (@Header("token") String token, @Header("registered") boolean registered);
    @GET("admin/get/faculties")
    Call<FacultyGetListResponse> adminGetFaculties (@Header("token")String token,@Header("registered") boolean registered);
    @GET("admin/get/student")
    Call<StudentGetResponse> adminGetStudent(@Header("token")String token,@Header("student") String student);
    @GET("admin/get/faculty")
    Call<FacultyGetResponse> adminGetFaculty(@Header("token")String token,@Header("faculty")String faculty);
    @POST("admin/register/student")
    Call<StudentUpdateResponse> adminRegisterStudent (@Header("token")String token,@Header("student")String student,@Header("register") boolean register);
    @POST("admin/register/faculty")
    Call<FacultyUpdateResponse> adminRegisterFaculty (@Header("token")String token,@Header("faculty") String faculty,@Header("register") boolean register);
    @POST("admin/delete/student")
    Call<StudentUpdateResponse> adminDeleteStudent(@Header("token")String token,@Header("student") String student);
    @POST("admin/delete/faculty")
    Call<FacultyUpdateResponse> adminDeleteFaculty(@Header("token")String token,@Header("faculty") String faculty);
    @POST("admin/add/student/subject")
    Call<StudentUpdateResponse> adminAddStudentSubject(@Header("token")String token,@Header("student") String student,@Header("subject") String subject);
    @POST("admin/add/faculty/subject")
    Call<FacultyUpdateResponse> adminAddFacultySubject(@Header("token")String token,@Header("faculty") String faculty,@Header("subject") String subject);
    @POST("admin/remove/student/subject")
    Call<StudentUpdateResponse> adminRemoveStudentSubject(@Header("token")String token,@Header("student") String student,@Header("subject") String subject);
    @POST("admin/remove/faculty/subject")
    Call<FacultyUpdateResponse> adminRemoveFacultySubject(@Header("token")String token,@Header("faculty") String faculty,@Header("subject")String subject);

    @POST("student/update")
    Call<StudentUpdateResponse> studentUpdate(@Header("token") String token ,@Body Student student);
    @GET("student/get")
    Call<StudentGetResponse> studentGetProfile(@Header("token") String token);
    @GET("student/get/all")
    Call<StudentGetListResponse> studentGetAll (@Header("token")String token);


    @POST("faculty/update")
    Call<FacultyUpdateResponse> facultyUpdate(@Header("token") String token, @Body Faculty faculty);
    @GET("faculty/get")
    Call<FacultyGetResponse> facultyGetProfile(@Header("token") String token);
    @GET("faculty/get/all")
    Call<FacultyGetListResponse> facultyGetAll (@Header("token")String token);

    @POST("subject/create")
    Call<SubjectResponse> addSubject(@Header("token") String token, @Body Subject subject);
    @GET("subject/get")
    Call<SubjectGetResponse> getSubject(@Header("id") String id);
    @GET("subject/get/all")
    Call<SubjectGetAllResponse> getAllSubjects(@Header("token") String token);
    @GET("subject/get/student")
    Call<SubjectsGetByStudentResponse> getSubjectsByStudent(@Header("token")String token);
    @DELETE("subject/delete")
    Call<SubjectResponse> deleteSubject(@Header("token")String token,@Header("subject") String subjectId);

    @POST("quiz/create")
    Call<QuizCreateResponse> createQuiz(@Header("token") String token, @Body Quiz quiz);
    @GET("quiz/get/faculty")
    Call<QuizListGetResponse> getQuizByFaculty(@Header("token")String token,@Header("completed") boolean completed);
    @GET("quiz/get/subject")
    Call<QuizListGetResponse> getQuizBySubject(@Header("id") String id);
    @GET("quiz/get/student")
    Call<QuizListGetResponse> getQuizByStudent(@Header("token") String token,@Header("completed") boolean completed);
    @GET("quiz/get")
    Call<QuizResponse> getQuiz(@Header("token")String token, @Header("id") String id);
    @DELETE("quiz/delete")
    Call<QuizResponse> deleteQuiz(@Header("token")String token,@Header("id")String quizId);
    @POST("quiz/update")
    Call<QuizResponse> updateQuiz(@Header("token")String token,@Body Quiz quiz);

    @POST("result/create")
    Call<ResultCreateResponse> createResult(@Header("token") String token, @Body Result result);
    @GET("result/get/subject")
    Call<ResultGetBySubjectResponse> getResultBySubject(@Header("token") String token, @Header("subject") String subject);
    @GET("result/get/student/quiz")
    Call<ResultGetResponse> getResultByQuizAndStudent(@Header("token")String token, @Header("quiz") String quizId);
    @POST("result/update")
    Call<ResultGetResponse> updateResult(@Header("token")String token, @Body Result result);
    @GET("result/get/student")
    Call<ResultListGetResponse> getResultsByStudent (@Header("token")String token);

    @GET("chat/get/all")
    Call<ChatGetListResponse> getAllChats(@Header("token")String token,@Header("sender")String sender);



}
