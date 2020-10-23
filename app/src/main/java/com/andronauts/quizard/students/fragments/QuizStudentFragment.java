package com.andronauts.quizard.students.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andronauts.quizard.api.responseModels.quiz.QuizListGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.api.WebSocketClient;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.databinding.FragmentQuizStudentBinding;
import com.andronauts.quizard.students.adapters.UpcomingQuizStudentRecycler;
import com.andronauts.quizard.utils.SharedPrefs;
import com.github.nkzawa.emitter.Emitter;

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
    private FragmentQuizStudentBinding binding;
    private Context context;
    private Activity parent;
    private SharedPrefs prefs;
    private UpcomingQuizStudentRecycler adapter;
    private List<Quiz> quizzes = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        this.parent = (Activity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizStudentBinding.inflate(inflater,container,false);
        prefs = new SharedPrefs(context);
        loadData();

        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        setWebSocketListeners();
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

    private void setWebSocketListeners(){
        WebSocketClient.getMSocket().on("quizStart", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                parent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String quizId = (String) args[0];
                        setQuizLive(quizId);
                        adapter.notifyDataSetChanged();
                        Log.i("MESSAGE","Quiz Started!");
                    }
                });
            }
        });

        WebSocketClient.getMSocket().on("quizStop", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                parent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String quizId = (String) args[0];
                        setQuizNotLive(quizId);
                        adapter.notifyDataSetChanged();
                        Log.i("MESSAGE","Quiz Stopped!");
                    }
                });


            }
        });
    }

    private void setQuizLive(String quizId){
        for(Quiz quiz:quizzes){
            if(quiz.getId() == quizId){
                quiz.setLive(true);
                return;
            }

        }
    }

    private void setQuizNotLive(String quizId){
        for(Quiz quiz:quizzes){
            if(quiz.getId() == quizId){
                quiz.setLive(false);
                return;
            }
        }
    }

    private void setUpRecyclerView(){
        adapter = new UpcomingQuizStudentRecycler(quizzes,context);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setAdapter(adapter);
    }
}
