package com.andronauts.quizard.students.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.andronauts.quizard.R;
import com.andronauts.quizard.databinding.ActivityStudentHomeBinding;

public class StudentHomeActivity extends AppCompatActivity {
    ActivityStudentHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityStudentHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
     
    }
}
