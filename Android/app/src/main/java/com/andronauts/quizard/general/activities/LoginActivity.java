package com.andronauts.quizard.general.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.andronauts.quizard.R;
import com.andronauts.quizard.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}