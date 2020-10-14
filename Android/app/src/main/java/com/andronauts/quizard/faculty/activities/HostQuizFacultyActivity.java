package com.andronauts.quizard.faculty.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import it.sephiroth.android.library.numberpicker.NumberPicker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.controllers.QuizCtrl;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.databinding.ActivityHostQuizFacultyBinding;
import com.andronauts.quizard.faculty.adapters.MakeQuestionRecycler;
import com.andronauts.quizard.utils.DateFormatter;
import com.andronauts.quizard.utils.SharedPrefs;
import com.andronauts.quizard.utils.firebase.StorageCtrl;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;

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
    Quiz quiz = new Quiz();
    List<Quiz.Question> questions = new ArrayList<>();

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
        prefs = new SharedPrefs(this);

        setQuestionRecyclerView();
        setQuestionSelector();
        setDateTimePickers();
        setQuizDurationPicker();

        binding.host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHost();
            }
        });

        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpload();
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

                binding.date.setText(new DateFormatter(calendar.getTimeInMillis()).getDate());
            }
        },present.get(Calendar.YEAR),present.get(Calendar.MONTH),present.get(Calendar.DAY_OF_MONTH));

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(Calendar.HOUR_OF_DAY,i);
                calendar.set(Calendar.MINUTE,i1);

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

    private void onHost(){
        quiz.setQuestion(questions);
        quiz.setTitle(binding.title.getText().toString());
        quiz.setDescription(binding.description.getText().toString());
        quiz.setFaculty(prefs.getToken());
        quiz.setSubject(subjectId);
        quiz.setStartTime(String.valueOf(calendar.getTimeInMillis()));
        quiz.setEndTime(String.valueOf(calendar.getTimeInMillis()+(1000*duration*60)));


        new QuizCtrl(this).createQuiz(quiz, new QuizCtrl.CreateQuizHandler() {
            @Override
            public void onSuccess(Quiz quiz) {
                Toast.makeText(HostQuizFacultyActivity.this, "Quiz Hosted Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Throwable t) {

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
        new StorageCtrl(this).uploadFile("quiz/" + System.currentTimeMillis() + ".pdf", uri, new StorageCtrl.handleUpload() {
            @Override
            public void onSuccess() {
                quiz.setPaper("quiz/" + System.currentTimeMillis() + ".pdf");
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
}