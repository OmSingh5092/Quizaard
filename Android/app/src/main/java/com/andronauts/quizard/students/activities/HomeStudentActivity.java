package com.andronauts.quizard.students.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;

import com.andronauts.quizard.R;
import com.andronauts.quizard.databinding.ActivityHomeStudentBinding;

public class HomeStudentActivity extends AppCompatActivity {
    ActivityHomeStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

    }

    @Override
    public boolean onSupportNavigateUp() {
        binding.drawer.openDrawer(Gravity.LEFT);
        return super.onSupportNavigateUp();
    }
}