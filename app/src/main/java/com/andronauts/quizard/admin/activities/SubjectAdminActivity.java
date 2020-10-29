package com.andronauts.quizard.admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.andronauts.quizard.admin.adapters.SubjectAdminRecycler;
import com.andronauts.quizard.api.responseModels.subject.SubjectResponse;
import com.andronauts.quizard.api.responseModels.subject.SubjectGetAllResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.databinding.ActivitySubjectAdminBinding;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.List;

public class SubjectAdminActivity extends AppCompatActivity {
    private ActivitySubjectAdminBinding binding;
    private SharedPrefs prefs;
    private List<Subject> subjects;
    private SubjectAdminRecycler adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubjectAdminBinding.inflate(getLayoutInflater());
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

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAdd();
            }
        });

    }

    private void loadData(){
        RetrofitClient.getClient().getAllSubjects(prefs.getToken()).enqueue(new Callback<SubjectGetAllResponse>() {
            @Override
            public void onResponse(Call<SubjectGetAllResponse> call, Response<SubjectGetAllResponse> response) {
                if(response.isSuccessful()){
                    subjects = response.body().getSubjects();
                    setUpRecyclerView();
                }

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<SubjectGetAllResponse> call, Throwable t) {

            }
        });
    }

    private void setUpRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubjectAdminRecycler(this,subjects);
        binding.recyclerView.setAdapter(adapter);
    }

    private void onAdd(){
        if(!validate()){
            Toast.makeText(this, "Please enter all the fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        Subject subject = new Subject();
        subject.setName(binding.subjectName.getText().toString());
        subject.setSubjectCode(binding.subjectCode.getText().toString());

        RetrofitClient.getClient().addSubject(prefs.getToken(),subject).enqueue(new Callback<SubjectResponse>() {
            @Override
            public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(SubjectAdminActivity.this, "Subject Added Successfully!", Toast.LENGTH_SHORT).show();
                    subjects.add(subject);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<SubjectResponse> call, Throwable t) {

            }
        });
    }

    private boolean validate(){
        if(binding.subjectName.getText().toString() == ""){
            return false;
        }else if(binding.subjectCode.getText().toString() == ""){
            return false;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}