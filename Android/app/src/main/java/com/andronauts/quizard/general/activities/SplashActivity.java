package com.andronauts.quizard.general.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.andronauts.quizard.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if(currentUser == null){
                            Log.i("SPLASH SCREEN INTENT", "Not logged in");
                            Intent login = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(login);
                            finish();
                        }else{
                            Log.i("SPLASH SCREEN INTENT", "Logged in");
                            Intent main = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(main);
                            finish();
                        }
                    }
                },
                3000
        );
    }
}