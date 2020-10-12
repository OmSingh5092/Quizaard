package com.andronauts.quizard.faculty.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.andronauts.quizard.R;
import com.andronauts.quizard.databinding.ActivityHomeFacultyBinding;
import com.andronauts.quizard.general.activities.LoginActivity;
import com.andronauts.quizard.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class HomeFacultyActivity extends AppCompatActivity {
    ActivityHomeFacultyBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeFacultyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setSideNav();

    }

    private void setSideNav(){
        binding.sideNav.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.logout){
                    new SessionManager(HomeFacultyActivity.this).logOutUser(new SessionManager.LogoutHandler() {
                        @Override
                        public void onSuccess() {
                            Intent i = new Intent(HomeFacultyActivity.this, LoginActivity.class);
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

    @Override
    public boolean onSupportNavigateUp() {
        binding.drawer.openDrawer(Gravity.LEFT);
        return super.onSupportNavigateUp();
    }
}