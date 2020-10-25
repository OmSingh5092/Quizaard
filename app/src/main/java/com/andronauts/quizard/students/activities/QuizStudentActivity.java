package com.andronauts.quizard.students.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import douglasspgyn.com.github.circularcountdown.CircularCountdown;
import douglasspgyn.com.github.circularcountdown.listener.CircularListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.quiz.QuizGetResponse;
import com.andronauts.quizard.api.responseModels.result.ResultCreateResponse;
import com.andronauts.quizard.api.responseModels.result.ResultGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.dataModels.Result;
import com.andronauts.quizard.databinding.ActivityQuizStudentBinding;
import com.andronauts.quizard.students.adapters.QuestionsStudentRecycler;
import com.andronauts.quizard.utils.DateFormatter;
import com.andronauts.quizard.utils.SharedPrefs;
import com.andronauts.quizard.utils.firebase.StorageCtrl;
import com.github.barteksc.pdfviewpager.adapter.PDFPagerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class QuizStudentActivity extends AppCompatActivity {
    ActivityQuizStudentBinding binding;
    private String quizId;
    private Quiz quiz;
    private SharedPrefs prefs;
    private QuestionsStudentRecycler adapter;
    private int[] responses;
    private Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        prefs = new SharedPrefs(this);
        quizId = getIntent().getStringExtra("quizId");
        result = new Result();
        loadData();


        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEndTest();
            }
        });

    }

    private void loadData(){
        RetrofitClient.getClient().getQuiz(prefs.getToken(),quizId).enqueue(new Callback<QuizGetResponse>() {
            @Override
            public void onResponse(Call<QuizGetResponse> call, Response<QuizGetResponse> response) {
                if(response.isSuccessful()){
                    quiz = response.body().getQuiz();
                    responses = new int[quiz.getQuestion().size()];
                    loadResult();
                    setData();
                }
            }

            @Override
            public void onFailure(Call<QuizGetResponse> call, Throwable t) {

            }
        });
    }

    private void loadResult(){
        RetrofitClient.getClient().getResultByQuizAndStudent(prefs.getToken(),quizId).enqueue(new Callback<ResultGetResponse>() {
            @Override
            public void onResponse(Call<ResultGetResponse> call, Response<ResultGetResponse> response) {
                if(response.isSuccessful()){
                    result = response.body().getResult();
                    if(result == null){
                        makeResult();
                        return;
                    }
                    result.setResponses(responses);
                    updateResponses(result.getResponses());
                }
            }

            @Override
            public void onFailure(Call<ResultGetResponse> call, Throwable t) {

            }
        });
    }

    private void makeResult(){
        result = new Result();
        result.setResponses(responses);
        result.setQuiz(quizId);
        result.setTotal(responses.length);
        result.setSubject(quiz.getSubject());
        result.setSubmitTime(String.valueOf(System.currentTimeMillis()));

        RetrofitClient.getClient().createResult(prefs.getToken(),result).enqueue(new Callback<ResultCreateResponse>() {
            @Override
            public void onResponse(Call<ResultCreateResponse> call, Response<ResultCreateResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(QuizStudentActivity.this, "Quiz Submitted successfully", Toast.LENGTH_SHORT).show();
                    result = response.body().getResult();
                    result.setResponses(responses);
                }
            }

            @Override
            public void onFailure(Call<ResultCreateResponse> call, Throwable t) {

            }
        });
    }

    private void setData(){
        binding.title.setText(quiz.getTitle());
        binding.description.setText(quiz.getDescription());
        setTimer();
        setQuestionRecycler();
        setDownloadPdf();
    }

    private void updateResponses(int[] newResponses){
        for(int i =0 ;i<responses.length ; i++){
            responses[i] = newResponses[i];
        }
        adapter.notifyDataSetChanged();
    }

    private void setTimer(){
        long minutes = new DateFormatter(quiz.getEndTime()).getTimeStamp()-System.currentTimeMillis();
        minutes/=60000;

        binding.circularCountdown.create(0, (int) minutes, CircularCountdown.TYPE_MINUTE)
                .listener(new CircularListener() {
                    @Override
                    public void onTick(int i) {
                        if(i == 5){
                            Toast.makeText(QuizStudentActivity.this, "Hurry! Only 5 min is left", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFinish(boolean b, int i) {
                        Toast.makeText(QuizStudentActivity.this, "Timer finished", Toast.LENGTH_SHORT).show();
                        binding.circularCountdown.stop();
                        onEndTest();
                    }
                });

        binding.circularCountdown.start();
    }

    private void setDownloadPdf(){
        if(quiz.getPaper() != ""){
            return;
        }
        binding.downloadPaper.setVisibility(View.VISIBLE);
        binding.downloadPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPdf();
            }
        });
    }

    private void downloadPdf(){

    }

    private void setQuestionRecycler(){
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuestionsStudentRecycler(this, quiz.getQuestion(), responses, new QuestionsStudentRecycler.RadioChangeHandler() {
            @Override
            public void onChange() {
                RetrofitClient.getClient().updateResult(prefs.getToken(),result).enqueue(new Callback<ResultGetResponse>() {
                    @Override
                    public void onResponse(Call<ResultGetResponse> call, Response<ResultGetResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<ResultGetResponse> call, Throwable t) {

                    }
                });
            }
        });
        binding.recycler.setAdapter(adapter);
    }

    private void onEndTest(){
        finish();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}