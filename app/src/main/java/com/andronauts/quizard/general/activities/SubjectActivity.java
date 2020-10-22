package com.andronauts.quizard.general.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.faculty.FacultyGetResponse;
import com.andronauts.quizard.api.responseModels.student.StudentGetResponse;
import com.andronauts.quizard.api.responseModels.student.StudentSubjectAddResponse;
import com.andronauts.quizard.api.responseModels.student.StudentSubjectDeleteResponse;
import com.andronauts.quizard.api.responseModels.subject.SubjectGetAllResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.dataModels.Student;
import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.databinding.ActivitySubjectBinding;
import com.andronauts.quizard.general.adapters.SubjectRecycler;
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

    SubjectRecycler registeredSubjectAdapter, notRegisteredSubjectAdapter;

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
                    RetrofitClient.getClient().studentSubjectRemove(prefs.getToken(),registeredSubjects.get(position).getId()).enqueue(new Callback<StudentSubjectDeleteResponse>() {
                        @Override
                        public void onResponse(Call<StudentSubjectDeleteResponse> call, Response<StudentSubjectDeleteResponse> response) {
                            notRegisteredSubjects.add(registeredSubjects.get(position));
                            registeredSubjectAdapter.notifyDataSetChanged();
                            registeredSubjects.remove(position);
                            registeredSubjectAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure(Call<StudentSubjectDeleteResponse> call, Throwable t) {

                        }
                    });
                }else{
                    RetrofitClient.getClient().facultySubjectRemove(prefs.getToken(),registeredSubjects.get(position).getId()).enqueue(new Callback<StudentSubjectDeleteResponse>() {
                        @Override
                        public void onResponse(Call<StudentSubjectDeleteResponse> call, Response<StudentSubjectDeleteResponse> response) {
                            notRegisteredSubjects.add(registeredSubjects.get(position));
                            registeredSubjectAdapter.notifyDataSetChanged();
                            registeredSubjects.remove(position);
                            registeredSubjectAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure(Call<StudentSubjectDeleteResponse> call, Throwable t) {

                        }
                    });
                }

            }
        });

        notRegisteredSubjectAdapter = new SubjectRecycler(notRegisteredSubjects, this, false, new SubjectRecycler.ActionHandler() {
            @Override
            public void onAction(int position) {
                if(isStudent){
                    RetrofitClient.getClient().studentSubjectAdd(prefs.getToken(),notRegisteredSubjects.get(position).getId()).enqueue(new Callback<StudentSubjectAddResponse>() {
                        @Override
                        public void onResponse(Call<StudentSubjectAddResponse> call, Response<StudentSubjectAddResponse> response) {
                            registeredSubjects.add(notRegisteredSubjects.get(position));
                            registeredSubjectAdapter.notifyDataSetChanged();
                            notRegisteredSubjects.remove(position);
                            notRegisteredSubjectAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure(Call<StudentSubjectAddResponse> call, Throwable t) {

                        }
                    });
                }else{
                    RetrofitClient.getClient().facultySubjectAdd(prefs.getToken(),notRegisteredSubjects.get(position).getId()).enqueue(new Callback<StudentSubjectAddResponse>() {
                        @Override
                        public void onResponse(Call<StudentSubjectAddResponse> call, Response<StudentSubjectAddResponse> response) {
                            registeredSubjects.add(notRegisteredSubjects.get(position));
                            registeredSubjectAdapter.notifyDataSetChanged();
                            notRegisteredSubjects.remove(position);
                            notRegisteredSubjectAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFailure(Call<StudentSubjectAddResponse> call, Throwable t) {

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