package com.andronauts.quizard.faculty.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.quiz.QuizListGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.databinding.ActivityHostQuizFacultyBinding;
import com.andronauts.quizard.databinding.ActivityHostedQuizFacultyBinding;
import com.andronauts.quizard.databinding.RecyclerUpcomingQuizFacultyBinding;
import com.andronauts.quizard.faculty.adapters.UpcomingQuizFacultyRecycler;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.List;

public class HostedQuizFacultyActivity extends AppCompatActivity {
    private ActivityHostedQuizFacultyBinding binding;
    private SharedPrefs prefs;
    private List<Quiz> quizzes;
    private UpcomingQuizFacultyRecycler adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostedQuizFacultyBinding.inflate(getLayoutInflater());
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
        RetrofitClient.getClient().getQuizByFaculty(prefs.getToken(),false).enqueue(new Callback<QuizListGetResponse>() {
            @Override
            public void onResponse(Call<QuizListGetResponse> call, Response<QuizListGetResponse> response) {
                if(response.isSuccessful()){
                    quizzes = response.body().getQuizzes();
                    setUpRecyclerView();
                    binding.swipeRefreshLayout.setRefreshing(false);
                }else{
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<QuizListGetResponse> call, Throwable t) {
                Log.e("Error",t.getMessage());
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setUpRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UpcomingQuizFacultyRecycler(quizzes,this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}