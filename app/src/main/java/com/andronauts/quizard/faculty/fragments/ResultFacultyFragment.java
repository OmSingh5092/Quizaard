package com.andronauts.quizard.faculty.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andronauts.quizard.api.responseModels.quiz.QuizListGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.databinding.FragmentResultFacultyBinding;
import com.andronauts.quizard.faculty.adapters.QuizResultRecycler;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultFacultyFragment extends Fragment {
    private FragmentResultFacultyBinding binding;
    private Context context;
    private SharedPrefs prefs;
    private List<Quiz> quizzes;
    private QuizResultRecycler adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResultFacultyBinding.inflate(getLayoutInflater(),container,false);
        context = getContext();
        prefs = new SharedPrefs(context);
        loadData();

        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        return binding.getRoot();
    }

    private void loadData(){
        RetrofitClient.getClient().getQuizByFaculty(prefs.getToken(),true).enqueue(new Callback<QuizListGetResponse>() {
            @Override
            public void onResponse(Call<QuizListGetResponse> call, Response<QuizListGetResponse> response) {
                if(response.isSuccessful()){
                    quizzes = response.body().getQuizzes();
                    setUpRecyclerView();
                    binding.refreshLayout.setRefreshing(false);
                }else{
                    binding.refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<QuizListGetResponse> call, Throwable t) {
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }

    private void setUpRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new QuizResultRecycler(quizzes,context);
        binding.recyclerView.setAdapter(adapter);
    }
}
