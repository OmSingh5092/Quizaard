package com.andronauts.quizard.students.activities;

import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.quiz.QuizGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.databinding.ActivityQuizStudentBinding;
import com.andronauts.quizard.utils.DateFormatter;
import com.andronauts.quizard.utils.SharedPrefs;
import com.andronauts.quizard.utils.firebase.StorageCtrl;
import com.github.barteksc.pdfviewpager.adapter.PDFPagerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class QuizStudentActivity extends AppCompatActivity {
    ActivityQuizStudentBinding binding;
    private String quizId;
    private Quiz quiz;
    private SharedPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        prefs = new SharedPrefs(this);
        quizId = getIntent().getStringExtra("quizId");
        loadData();

    }

    private void loadData(){
        RetrofitClient.getClient().getQuiz(prefs.getToken(),quizId).enqueue(new Callback<QuizGetResponse>() {
            @Override
            public void onResponse(Call<QuizGetResponse> call, Response<QuizGetResponse> response) {
                if(response.isSuccessful()){
                    quiz = response.body().getQuiz();
                    setData();
                }
            }

            @Override
            public void onFailure(Call<QuizGetResponse> call, Throwable t) {

            }
        });
    }

    private void setData(){
        binding.title.setText(quiz.getTitle());
        binding.description.setText(quiz.getDescription());
        setTimer();
        setPdf();
        setQuestionView();
    }

    private void setTimer(){
        long minutes = new DateFormatter(quiz.getEndTime()).getTimeStamp()-System.currentTimeMillis();
        minutes/=60000;

        binding.circularCountdown.create(0, (int) minutes, CircularCountdown.TYPE_MINUTE)
                .listener(new CircularListener() {
                    @Override
                    public void onTick(int i) {

                    }

                    @Override
                    public void onFinish(boolean b, int i) {
                        Toast.makeText(QuizStudentActivity.this, "Timer finished", Toast.LENGTH_SHORT).show();
                    }
                });

        binding.circularCountdown.start();
    }

    private void setPdf(){


    }

    private void setQuestionView(){

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}