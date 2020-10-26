package com.andronauts.quizard.students.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.widget.Toast;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.quiz.QuizListGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.databinding.ActivityPreviousQuizzesStudentBinding;
import com.andronauts.quizard.students.adapters.PreviousQuizzesStudentRecycler;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.List;

public class PreviousQuizzesStudentActivity extends AppCompatActivity {
    private ActivityPreviousQuizzesStudentBinding binding;
    private SharedPrefs prefs;
    private PreviousQuizzesStudentRecycler adapter;
    private List<Quiz> quizzes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviousQuizzesStudentBinding.inflate(getLayoutInflater());
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
        RetrofitClient.getClient().getQuizByStudent(prefs.getToken(),true).enqueue(new Callback<QuizListGetResponse>() {
            @Override
            public void onResponse(Call<QuizListGetResponse> call, Response<QuizListGetResponse> response) {
                if(response.isSuccessful()){
                    quizzes = response.body().getQuizzes();
                    setUpRecyclerView();
                }
                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<QuizListGetResponse> call, Throwable t) {
                Toast.makeText(PreviousQuizzesStudentActivity.this, "Some Error Occurred", Toast.LENGTH_SHORT).show();
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setUpRecyclerView(){
        adapter = new PreviousQuizzesStudentRecycler(this,quizzes);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}