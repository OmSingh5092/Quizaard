package com.andronauts.quizzard.general.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.andronauts.quizzard.admin.activities.HomeAdminActivity;
import com.andronauts.quizzard.databinding.ActivitySplashScreenBinding;
import com.andronauts.quizzard.faculty.activities.HomeFacultyActivity;
import com.andronauts.quizzard.students.activities.HomeStudentActivity;
import com.andronauts.quizzard.utils.SharedPrefs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    SharedPrefs prefs;
    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = new SharedPrefs(this);
        userType = prefs.getUserType();

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if(currentUser != null){
                            if(userType== 0){
                                Log.i("SPLASH SCREEN INTENT", "Not logged in");
                                Intent login = new Intent(SplashActivity.this, HomeStudentActivity.class);
                                startActivity(login);
                                finish();
                            }else if(userType == 1){
                                Log.i("SPLASH SCREEN INTENT", "Not logged in");
                                Intent login = new Intent(SplashActivity.this, HomeFacultyActivity.class);
                                startActivity(login);
                                finish();
                            }else{
                                Log.i("SPLASH SCREEN INTENT", "Not logged in");
                                Intent login = new Intent(SplashActivity.this, HomeAdminActivity.class);
                                startActivity(login);
                                finish();
                            }
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