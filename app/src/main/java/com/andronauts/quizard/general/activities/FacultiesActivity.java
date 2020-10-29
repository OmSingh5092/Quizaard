package com.andronauts.quizard.general.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.faculty.FacultyGetListResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.databinding.ActivityFacultiesBinding;
import com.andronauts.quizard.general.adapters.FacultyProfileRecycler;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.List;

public class FacultiesActivity extends AppCompatActivity {
    private ActivityFacultiesBinding binding;
    private SharedPrefs prefs;
    private FacultyProfileRecycler adapter;
    private List<Faculty> faculties;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFacultiesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        prefs = new SharedPrefs(this);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        loadData();
    }

    private void loadData(){
        RetrofitClient.getClient().facultyGetAll(prefs.getToken()).enqueue(new Callback<FacultyGetListResponse>() {
            @Override
            public void onResponse(Call<FacultyGetListResponse> call, Response<FacultyGetListResponse> response) {
                if(response.isSuccessful()){
                    faculties = response.body().getFaculties();
                    setUpRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<FacultyGetListResponse> call, Throwable t) {

            }
        });
    }

    private void setUpRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FacultyProfileRecycler(this,faculties);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}