package com.andronauts.quizard.students.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import douglasspgyn.com.github.circularcountdown.CircularCountdown;
import douglasspgyn.com.github.circularcountdown.listener.CircularListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.andronauts.quizard.api.responseModels.quiz.QuizResponse;
import com.andronauts.quizard.api.responseModels.result.ResultCreateResponse;
import com.andronauts.quizard.api.responseModels.result.ResultGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.dataModels.Result;
import com.andronauts.quizard.databinding.ActivityQuizStudentBinding;
import com.andronauts.quizard.students.adapters.QuestionsStudentRecycler;
import com.andronauts.quizard.utils.DateFormatter;
import com.andronauts.quizard.utils.FileManager;
import com.andronauts.quizard.utils.PermissionCtrl;
import com.andronauts.quizard.utils.SharedPrefs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

public class QuizStudentActivity extends AppCompatActivity {
    private ActivityQuizStudentBinding binding;
    private String quizId;
    private Quiz quiz;
    private SharedPrefs prefs;
    private QuestionsStudentRecycler adapter;
    private int[] responses;
    private Result result;

    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        prefs = new SharedPrefs(this);
        quizId = getIntent().getStringExtra("quizId");
        result = new Result();
        fileManager = new FileManager(this);
        loadData();


        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEndTest();
            }
        });

        binding.downloadPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadPdf();
            }
        });

        new PermissionCtrl(this).askStoragePermission();

    }

    private void loadData(){
        RetrofitClient.getClient().getQuiz(prefs.getToken(),quizId).enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if(response.isSuccessful()){
                    quiz = response.body().getQuiz();
                    responses = new int[quiz.getQuestion().size()];

                    /*//Checking if quiz is ended or not
                    if(new DateFormatter(quiz.getEndTime()).getTimeStamp()<System.currentTimeMillis()){
                        Toast.makeText(QuizStudentActivity.this, "Quiz is already ended", Toast.LENGTH_SHORT).show();
                        finish();
                    }  */
                    //Checking if there is any question paper or not
                    if(quiz.getPaper() != null){
                        binding.downloadPaper.setVisibility(View.VISIBLE);
                    }
                    loadResult();
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {

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
                    updateResponses(result.getResponses());
                    result.setResponses(responses);
                    setData();
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
        result.setEndTime(quiz.getEndTime());
        result.setSubject(quiz.getSubject());
        result.setSubmitTime(String.valueOf(System.currentTimeMillis()));

        RetrofitClient.getClient().createResult(prefs.getToken(),result).enqueue(new Callback<ResultCreateResponse>() {
            @Override
            public void onResponse(Call<ResultCreateResponse> call, Response<ResultCreateResponse> response) {
                if(response.isSuccessful()){
                    result = response.body().getResult();
                    result.setResponses(responses);
                    setData();
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
    }

    private void setTimer(){
        long minutes = new DateFormatter(quiz.getEndTime()).getTimeStamp()-System.currentTimeMillis();
        minutes/=1000;

        binding.circularCountdown.create(0, (int) minutes, CircularCountdown.TYPE_SECOND)
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
        File pdfFile = fileManager.getQuestionPaperPdfFile();

        FirebaseStorage.getInstance().getReference(quiz.getPaper()).getFile(pdfFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                fileManager.openPdfFile(pdfFile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

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