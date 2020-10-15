package com.andronauts.quizard.general.activities;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.faculty.FacultyGetResponse;
import com.andronauts.quizard.api.responseModels.student.StudentGetResponse;
import com.andronauts.quizard.api.responseModels.subject.SubjectGetAllResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.dataModels.Student;
import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.databinding.ActivitySubjectBinding;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {
    ActivitySubjectBinding binding;
    SharedPrefs prefs;
    boolean isStudent;

    Faculty faculty;
    Student student;
    List<Subject> subjects = new ArrayList<>();
    List<Subject> registeredSubjects = new ArrayList<>();
    List<Subject> notRegisteredSubjects = new ArrayList<>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = new SharedPrefs(this);
        isStudent = getIntent().getBooleanExtra("isStudent",false);
        if(isStudent){
            binding.toolbar.setBackgroundColor(R.color.colorPrimaryDark);
        }
        setSupportActionBar(binding.toolbar);

        loadData();

    }

    private void loadData(){
        if(isStudent){
            RetrofitClient.getClient().studentGetProfile(prefs.getToken()).enqueue(new Callback<StudentGetResponse>() {
                @Override
                public void onResponse(Call<StudentGetResponse> call, Response<StudentGetResponse> response) {
                    student = response.body().getStudent();
                    loadSubjects();
                }

                @Override
                public void onFailure(Call<StudentGetResponse> call, Throwable t) {

                }
            });
        }else{
            RetrofitClient.getClient().facultyGetProfile(prefs.getToken()).enqueue(new Callback<FacultyGetResponse>() {
                @Override
                public void onResponse(Call<FacultyGetResponse> call, Response<FacultyGetResponse> response) {
                    faculty = response.body().getFaculty();
                    loadSubjects();
                }

                @Override
                public void onFailure(Call<FacultyGetResponse> call, Throwable t) {

                }
            });
        }
    }


    private void loadSubjects(){
        RetrofitClient.getClient().getAllSubjects(prefs.getToken()).enqueue(new Callback<SubjectGetAllResponse>() {
            @Override
            public void onResponse(Call<SubjectGetAllResponse> call, Response<SubjectGetAllResponse> response) {
                subjects = response.body().getSubjects();
                List<String> subjectIds;
                if(isStudent){
                    subjectIds = student.getSubjects();
                }else{
                    subjectIds = faculty.getSubjects();
                }

                for(Subject subject: subjects){
                    if(subjectIds.contains(subject.getId())){
                        registeredSubjects.add(subject);
                    }else{
                        notRegisteredSubjects.add(subject);
                    }
                }

            }

            @Override
            public void onFailure(Call<SubjectGetAllResponse> call, Throwable t) {

            }
        });
    }

    private void setUpRecyclerViews(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}