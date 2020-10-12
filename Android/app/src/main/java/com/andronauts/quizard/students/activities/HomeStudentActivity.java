package com.andronauts.quizard.students.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.andronauts.quizard.R;
import com.andronauts.quizard.databinding.ActivityHomeStudentBinding;
import com.andronauts.quizard.faculty.activities.HomeFacultyActivity;
import com.andronauts.quizard.general.activities.LoginActivity;
import com.andronauts.quizard.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class HomeStudentActivity extends AppCompatActivity {
    ActivityHomeStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setSideNav();
    }

    @Override
    public boolean onSupportNavigateUp() {
        binding.drawer.openDrawer(Gravity.LEFT);
        return super.onSupportNavigateUp();
    }

    private void setSideNav(){
        binding.sideNav.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.logout){
                    new SessionManager(HomeStudentActivity.this).logOutUser(new SessionManager.LogoutHandler() {
                        @Override
                        public void onSuccess() {
                            Intent i = new Intent(HomeStudentActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }

                return false;
            }
        });
    }
}