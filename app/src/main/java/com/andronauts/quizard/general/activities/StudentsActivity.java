package com.andronauts.quizard.general.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.student.StudentGetListResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Student;
import com.andronauts.quizard.databinding.ActivityStudentsBinding;
import com.andronauts.quizard.general.adapters.StudentProfileRecycler;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.List;

public class StudentsActivity extends AppCompatActivity {
    private ActivityStudentsBinding binding;
    private SharedPrefs prefs;
    private StudentProfileRecycler adapter;
    private List<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = new SharedPrefs(this);
        setSupportActionBar(binding.toolbar);

        loadData();

    }

    private void loadData(){
        RetrofitClient.getClient().studentGetAll(prefs.getToken()).enqueue(new Callback<StudentGetListResponse>() {
            @Override
            public void onResponse(Call<StudentGetListResponse> call, Response<StudentGetListResponse> response) {
                if(response.isSuccessful()){
                    students = response.body().getStudents();
                    setUpRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<StudentGetListResponse> call, Throwable t) {

            }
        });
    }

    private void setUpRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentProfileRecycler(this,students);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}