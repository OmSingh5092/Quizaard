package com.andronauts.quizard.students.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.andronauts.quizard.R;
import com.andronauts.quizard.databinding.ActivityQuizStudentBinding;

public class QuizStudentActivity extends AppCompatActivity {
    ActivityQuizStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}