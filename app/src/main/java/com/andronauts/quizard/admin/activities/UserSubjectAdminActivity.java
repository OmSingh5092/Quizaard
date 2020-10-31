package com.andronauts.quizard.admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.faculty.FacultyGetResponse;
import com.andronauts.quizard.api.responseModels.faculty.FacultyUpdateResponse;
import com.andronauts.quizard.api.responseModels.student.StudentGetResponse;
import com.andronauts.quizard.api.responseModels.student.StudentSubjectAddResponse;
import com.andronauts.quizard.api.responseModels.student.StudentSubjectDeleteResponse;
import com.andronauts.quizard.api.responseModels.student.StudentUpdateResponse;
import com.andronauts.quizard.api.responseModels.subject.SubjectGetAllResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.dataModels.Student;
import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.databinding.ActivitySubjectUserAdminBinding;
import com.andronauts.quizard.admin.adapters.SubjectRecycler;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

public class UserSubjectAdminActivity extends AppCompatActivity {
    private ActivitySubjectUserAdminBinding binding;
    private SharedPrefs prefs;
    private boolean isStudent;
    private String userId;

    private Faculty faculty;
    private Student student;
    private List<Subject> subjects = new ArrayList<>();
    private List<Subject> registeredSubjects = new ArrayList<>();
    private List<Subject> notRegisteredSubjects = new ArrayList<>();

    private SubjectRecycler registeredSubjectAdapter, notRegisteredSubjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubjectUserAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = new SharedPrefs(this);
        isStudent = getIntent().getBooleanExtra("isStudent",false);
        userId = getIntent().getStringExtra("userId");
        setSupportActionBar(binding.toolbar);

        loadData();

    }

    private void loadData(){
        if(isStudent){
            RetrofitClient.getClient().adminGetStudent(prefs.getToken(),userId).enqueue(new Callback<StudentGetResponse>() {
                @Override
                public void onResponse(Call<StudentGetResponse> call, Response<StudentGetResponse> response) {
                    if(response.isSuccessful()){
                        student = response.body().getStudent();
                        loadSubjects();
                    }
                }

                @Override
                public void onFailure(Call<StudentGetResponse> call, Throwable t) {

                }
            });
        }else{
            RetrofitClient.getClient().adminGetFaculty(prefs.getToken(),userId).enqueue(new Callback<FacultyGetResponse>() {
                @Override
                public void onResponse(Call<FacultyGetResponse> call, Response<FacultyGetResponse> response) {
                    if(response.isSuccessful()){
                        faculty = response.body().getFaculty();
                        loadSubjects();
                    }
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
                setUpRecyclerViews();
            }

            @Override
            public void onFailure(Call<SubjectGetAllResponse> call, Throwable t) {

            }
        });
    }

    private void setUpRecyclerViews(){
        binding.registeredRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));

        registeredSubjectAdapter = new SubjectRecycler(registeredSubjects, this, true, new SubjectRecycler.ActionHandler() {
            @Override
            public void onAction(int position) {
                if(isStudent){
                    RetrofitClient.getClient().adminRemoveStudentSubject(prefs.getToken(),userId,registeredSubjects.get(position).getId()).enqueue(new Callback<StudentUpdateResponse>() {
                        @Override
                        public void onResponse(Call<StudentUpdateResponse> call, Response<StudentUpdateResponse> response) {
                            notRegisteredSubjects.add(registeredSubjects.get(position));
                            registeredSubjectAdapter.notifyDataSetChanged();
                            registeredSubjects.remove(position);
                            registeredSubjectAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<StudentUpdateResponse> call, Throwable t) {

                        }
                    });
                }else{
                    RetrofitClient.getClient().adminRemoveFacultySubject(prefs.getToken(),userId,registeredSubjects.get(position).getId()).enqueue(new Callback<FacultyUpdateResponse>() {
                        @Override
                        public void onResponse(Call<FacultyUpdateResponse> call, Response<FacultyUpdateResponse> response) {
                            notRegisteredSubjects.add(registeredSubjects.get(position));
                            registeredSubjectAdapter.notifyDataSetChanged();
                            registeredSubjects.remove(position);
                            registeredSubjectAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<FacultyUpdateResponse> call, Throwable t) {

                        }
                    });
                }

            }
        });

        notRegisteredSubjectAdapter = new SubjectRecycler(notRegisteredSubjects, this, false, new SubjectRecycler.ActionHandler() {
            @Override
            public void onAction(int position) {
                if(isStudent){
                    RetrofitClient.getClient().adminAddStudentSubject(prefs.getToken(),userId,notRegisteredSubjects.get(position).getId()).enqueue(new Callback<StudentUpdateResponse>() {
                        @Override
                        public void onResponse(Call<StudentUpdateResponse> call, Response<StudentUpdateResponse> response) {
                            registeredSubjects.add(notRegisteredSubjects.get(position));
                            registeredSubjectAdapter.notifyDataSetChanged();
                            notRegisteredSubjects.remove(position);
                            notRegisteredSubjectAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<StudentUpdateResponse> call, Throwable t) {

                        }
                    });
                }else{
                    RetrofitClient.getClient().adminAddFacultySubject(prefs.getToken(),userId,notRegisteredSubjects.get(position).getId()).enqueue(new Callback<FacultyUpdateResponse>() {
                        @Override
                        public void onResponse(Call<FacultyUpdateResponse> call, Response<FacultyUpdateResponse> response) {
                            registeredSubjects.add(notRegisteredSubjects.get(position));
                            registeredSubjectAdapter.notifyDataSetChanged();
                            notRegisteredSubjects.remove(position);
                            notRegisteredSubjectAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<FacultyUpdateResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });

        binding.registeredRecycler.setAdapter(registeredSubjectAdapter);
        binding.recycler.setAdapter(notRegisteredSubjectAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}