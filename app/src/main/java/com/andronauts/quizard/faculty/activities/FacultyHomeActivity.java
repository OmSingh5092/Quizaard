package com.andronauts.quizard.faculty.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.andronauts.quizard.R;
import com.andronauts.quizard.databinding.ActivityFacultyHomeBinding;

public class FacultyHomeActivity extends AppCompatActivity {
    ActivityFacultyHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFacultyHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}