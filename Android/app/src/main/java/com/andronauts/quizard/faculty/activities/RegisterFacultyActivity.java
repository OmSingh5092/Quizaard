package com.andronauts.quizard.faculty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.andronauts.quizard.R;
import com.andronauts.quizard.databinding.ActivityRegisterFacultyBinding;

public class RegisterFacultyActivity extends AppCompatActivity {
    ActivityRegisterFacultyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterFacultyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}