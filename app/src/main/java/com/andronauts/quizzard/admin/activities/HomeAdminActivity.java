package com.andronauts.quizzard.admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.andronauts.quizzard.R;
import com.andronauts.quizzard.admin.fragments.FacultyAdminFragment;
import com.andronauts.quizzard.admin.fragments.StudentAdminFragment;
import com.andronauts.quizzard.databinding.ActivityHomeAdminBinding;
import com.andronauts.quizzard.general.activities.LoginActivity;
import com.andronauts.quizzard.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class HomeAdminActivity extends AppCompatActivity {
    private ActivityHomeAdminBinding binding;

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new StudentAdminFragment();
                case 1: return new FacultyAdminFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setSideNav();
        setUpViewPager();
    }

    private void setSideNav(){
        binding.sideNav.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.logout){
                    new SessionManager(HomeAdminActivity.this).logOutUser(new SessionManager.LogoutHandler() {
                        @Override
                        public void onSuccess() {
                            Intent i = new Intent(HomeAdminActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                        @Override
                        public void onFailure() {

                        }
                    });
                }else if(item.getItemId() == R.id.subjects){
                    Intent i = new Intent(HomeAdminActivity.this, SubjectAdminActivity.class);
                    i.putExtra("isStudent",false);
                    startActivity(i);
                }else if (item.getItemId() == R.id.profile){

                }else if(item.getItemId() == R.id.students){
                    Intent i = new Intent(HomeAdminActivity.this, StudentAdminActivity.class);
                    startActivity(i);
                }else if(item.getItemId() == R.id.faculties){
                    Intent i =new Intent(HomeAdminActivity.this, FacultyAdminActivity.class);
                    startActivity(i);
                }

                return false;
            }
        });
    }

    private void setUpViewPager(){
        HomeAdminActivity.ViewPagerAdapter adapter = new HomeAdminActivity.ViewPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.tabLayout.getTabAt(0).setText("Students");
        binding.tabLayout.getTabAt(1).setText("Faculties");
    }

    @Override
    public boolean onSupportNavigateUp() {
        binding.drawer.openDrawer(Gravity.LEFT);
        return super.onSupportNavigateUp();
    }
}