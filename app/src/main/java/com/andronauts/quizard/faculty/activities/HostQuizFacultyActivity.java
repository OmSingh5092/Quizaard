package com.andronauts.quizard.faculty.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import it.sephiroth.android.library.numberpicker.NumberPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andronauts.quizard.api.responseModels.quiz.QuizCreateResponse;
import com.andronauts.quizard.api.responseModels.quiz.QuizResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.databinding.ActivityHostQuizFacultyBinding;
import com.andronauts.quizard.faculty.adapters.MakeQuestionRecycler;
import com.andronauts.quizard.utils.DateFormatter;
import com.andronauts.quizard.utils.SharedPrefs;
import com.andronauts.quizard.utils.firebase.StorageCtrl;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HostQuizFacultyActivity extends AppCompatActivity {
    ActivityHostQuizFacultyBinding binding;
    String subjectId;
    SharedPrefs prefs;

    MakeQuestionRecycler adapter;

    Quiz quiz;
    String quizId = "default";
    List<Quiz.Question> questions;

    private boolean updateQuiz ;

    Calendar calendar = Calendar.getInstance();
    int duration = 0;
    int GET_PDF = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostQuizFacultyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        subjectId = getIntent().getStringExtra("subjectId");
        quizId = getIntent().getStringExtra("quizId");
        updateQuiz = getIntent().getBooleanExtra("updateQuiz",false);

        prefs = new SharedPrefs(this);

        loadData();

        binding.host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateQuiz){
                    onUpdate();
                }else{
                    onHost();
                }
            }
        });

        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpload();
            }
        });

        setTextListeners();
    }

    private void setData(){
        binding.title.setText(quiz.getTitle());
        binding.description.setText(quiz.getDescription());
        binding.date.setText(new DateFormatter(quiz.getStartTime()).getDate());
        binding.time.setText(new DateFormatter(quiz.getStartTime()).getTime());
        binding.duration.setProgress((int) new DateFormatter(quiz.getStartTime(),quiz.getEndTime()).getDurationMinutes());
        binding.questionSelector.setProgress(quiz.getQuestion().size());

        questions = quiz.getQuestion();

        setQuestionRecyclerView();
        setQuestionSelector();
        setDateTimePickers();
        setQuizDurationPicker();

    }

    private void loadData(){

        if(!updateQuiz){
            quiz = new Quiz();
            questions=  new ArrayList<>();
            quiz.setQuestion(questions);
            quiz.setSubject(subjectId);

            setQuestionRecyclerView();
            setQuestionSelector();
            setDateTimePickers();
            setQuizDurationPicker();
            return;
        }

        RetrofitClient.getClient().getQuiz(prefs.getToken(),quizId).enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if(response.isSuccessful()){
                    quiz = response.body().getQuiz();
                    setData();
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {

            }
        });
    }

    private void setQuestionRecyclerView(){
        adapter = new MakeQuestionRecycler(questions,this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void setQuestionSelector(){
        binding.questionSelector.setNumberPickerChangeListener(new NumberPicker.OnNumberPickerChangeListener() {
            @Override
            public void onProgressChanged(@NotNull NumberPicker numberPicker, int i, boolean b) {
                while (questions.size()!=i){
                    if(questions.size()>i){
                        questions.remove(questions.size()-1);
                    }else{
                        questions.add(new Quiz.Question());
                    }
                }
                setQuestionRecyclerView();
            }

            @Override
            public void onStartTrackingTouch(@NotNull NumberPicker numberPicker) {
                Toast.makeText(HostQuizFacultyActivity.this, "start tracking", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(@NotNull NumberPicker numberPicker) {
                Toast.makeText(HostQuizFacultyActivity.this, "stop tracking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setQuizDurationPicker(){
        binding.duration.setNumberPickerChangeListener(new NumberPicker.OnNumberPickerChangeListener() {
            @Override
            public void onProgressChanged(@NotNull NumberPicker numberPicker, int i, boolean b) {
                duration = i;
                quiz.setEndTime(String.valueOf(calendar.getTimeInMillis()+(1000*duration*60)));
            }

            @Override
            public void onStartTrackingTouch(@NotNull NumberPicker numberPicker) {

            }

            @Override
            public void onStopTrackingTouch(@NotNull NumberPicker numberPicker) {

            }
        });
    }

    private void setDateTimePickers(){
        Calendar present = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH,i1);
                calendar.set(Calendar.DAY_OF_MONTH,i2);

                quiz.setStartTime(String.valueOf(calendar.getTimeInMillis()));
                quiz.setEndTime(String.valueOf(calendar.getTimeInMillis()+(1000*duration*60)));
                binding.date.setText(new DateFormatter(calendar.getTimeInMillis()).getDate());
            }
        },present.get(Calendar.YEAR),present.get(Calendar.MONTH),present.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY,i);
                calendar.set(Calendar.MINUTE,i1);

                quiz.setStartTime(String.valueOf(calendar.getTimeInMillis()));
                quiz.setEndTime(String.valueOf(calendar.getTimeInMillis()+(1000*duration*60)));
                binding.time.setText(new DateFormatter(calendar.getTimeInMillis()).getTime());
            }
        },present.get(Calendar.HOUR_OF_DAY),present.get(Calendar.MINUTE),false);


        binding.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    private boolean validate(){
        if(quiz.getTitle()==null|| quiz.getTitle().equals("")){
            Toast.makeText(this, "Please Enter a title", Toast.LENGTH_SHORT).show();
            return false;
        }else if(quiz.getDescription() == null||quiz.getDescription().equals("")){
            Toast.makeText(this, "Please enter the description", Toast.LENGTH_SHORT).show();
            return false;
        }else if(quiz.getQuestion() == null ||quiz.getQuestion().size() == 0){
            Toast.makeText(this, "Please add a question", Toast.LENGTH_SHORT).show();
            return false;
        }else if(quiz.getStartTime() == null|| quiz.getStartTime().equals("")){
            Toast.makeText(this, "Please enter time", Toast.LENGTH_SHORT).show();
            return false;
        }else if(quiz.getEndTime() == null || quiz.getEndTime().equals("")){
            Toast.makeText(this, "Please enter test duration", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void onUpdate(){
        RetrofitClient.getClient().updateQuiz(prefs.getToken(),quiz).enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(HostQuizFacultyActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {

            }
        });
    }

    private void setTextListeners(){
        binding.title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                quiz.setTitle(editable.toString());
            }
        });

        binding.description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                quiz.setDescription(editable.toString());
            }
        });
    }

    private void onHost(){
        if(!validate()){
            return;
        }

        RetrofitClient.getClient().createQuiz(prefs.getToken(),quiz).enqueue(new Callback<QuizCreateResponse>() {
            @Override
            public void onResponse(Call<QuizCreateResponse> call, Response<QuizCreateResponse> response) {
                Toast.makeText(HostQuizFacultyActivity.this, "Quiz Hosted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<QuizCreateResponse> call, Throwable t) {

            }
        });
    }

    private void onUpload(){
        Intent pdfIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pdfIntent.setType("application/pdf");
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(pdfIntent,"Select PDF"),GET_PDF);

    }

    private void uploadPdf(Uri uri){
        Snackbar snackbar = Snackbar.make(binding.getRoot(),"Uploading...",Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        String path = "quiz/" + System.currentTimeMillis() + ".pdf";
        new StorageCtrl(this).uploadFile(path, uri, new StorageCtrl.handleUpload() {
            @Override
            public void onSuccess() {
                quiz.setPaper(path);
                snackbar.dismiss();
                binding.done.setVisibility(View.VISIBLE);
                Toast.makeText(HostQuizFacultyActivity.this, "Question paper uploaded successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GET_PDF){
            uploadPdf(data.getData());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onPostResume() {
        loadData();
        super.onPostResume();
    }
}