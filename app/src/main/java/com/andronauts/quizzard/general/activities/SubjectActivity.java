package com.andronauts.quizzard.general.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;

import com.andronauts.quizzard.R;
import com.andronauts.quizzard.api.responseModels.faculty.FacultyGetResponse;
import com.andronauts.quizzard.api.responseModels.student.StudentGetResponse;
import com.andronauts.quizzard.api.retrofit.RetrofitClient;
import com.andronauts.quizzard.dataModels.Faculty;
import com.andronauts.quizzard.dataModels.Student;
import com.andronauts.quizzard.databinding.ActivitySubjectBinding;
import com.andronauts.quizzard.general.adapters.SubjectRecycler;
import com.andronauts.quizzard.utils.SharedPrefs;

import java.util.List;

public class SubjectActivity extends AppCompatActivity {
    private ActivitySubjectBinding binding;
    private SharedPrefs prefs;
    private Student student;
    private Faculty faculty;
    private boolean isStudent;
    private SubjectRecycler adapter;
    private List<String> subjectIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = new SharedPrefs(this);
        isStudent = getIntent().getBooleanExtra("isStudent",false);
        if(!isStudent){
            binding.toolbar.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        }
        setSupportActionBar(binding.toolbar);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        loadData();
    }

    private void loadData(){
        if(isStudent){
            RetrofitClient.getClient().studentGetProfile(prefs.getToken()).enqueue(new Callback<StudentGetResponse>() {
                @Override
                public void onResponse(Call<StudentGetResponse> call, Response<StudentGetResponse> response) {
                    if(response.isSuccessful()){
                        student = response.body().getStudent();
                        subjectIds = student.getSubjects();
                        setUpRecyclerView();
                    }

                    binding.swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<StudentGetResponse> call, Throwable t) {

                }
            });
        }else{
            RetrofitClient.getClient().facultyGetProfile(prefs.getToken()).enqueue(new Callback<FacultyGetResponse>() {
                @Override
                public void onResponse(Call<FacultyGetResponse> call, Response<FacultyGetResponse> response) {
                    if(response.isSuccessful()){
                        faculty = response.body().getFaculty();
                        subjectIds = faculty.getSubjects();
                        setUpRecyclerView();
                    }
                }

                @Override
                public void onFailure(Call<FacultyGetResponse> call, Throwable t) {

                }
            });
        }
    }

    private void setUpRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubjectRecycler(this,subjectIds);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}