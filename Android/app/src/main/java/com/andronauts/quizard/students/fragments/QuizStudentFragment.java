package com.andronauts.quizard.students.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andronauts.quizard.api.responseModels.quiz.QuizListGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.databinding.FragmentQuizStudentBinding;
import com.andronauts.quizard.students.adapters.UpcomingQuizStudentRecycler;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizStudentFragment extends Fragment {
    FragmentQuizStudentBinding binding;
    Context context;
    SharedPrefs prefs;
    UpcomingQuizStudentRecycler adapter;
    List<Quiz> quizzes = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizStudentBinding.inflate(inflater,container,false);
        context = getContext();
        prefs = new SharedPrefs(context);
        loadData();

        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        return binding.getRoot();
    }

    private void loadData(){
        RetrofitClient.getClient().getQuizByStudent(prefs.getToken()).enqueue(new Callback<QuizListGetResponse>() {
            @Override
            public void onResponse(Call<QuizListGetResponse> call, Response<QuizListGetResponse> response) {
                binding.refresh.setRefreshing(false);
                quizzes = response.body().getQuizzes();
                setUpRecyclerView();
            }

            @Override
            public void onFailure(Call<QuizListGetResponse> call, Throwable t) {

            }
        });
    }

    private void setUpRecyclerView(){
        adapter = new UpcomingQuizStudentRecycler(quizzes,context);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setAdapter(adapter);
    }
}
