package com.andronauts.quizard.students.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.andronauts.quizard.R;
import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.databinding.ActivityHomeStudentBinding;
import com.andronauts.quizard.faculty.activities.HomeFacultyActivity;
import com.andronauts.quizard.general.activities.LoginActivity;
import com.andronauts.quizard.general.activities.SubjectActivity;
import com.andronauts.quizard.students.fragments.QuizStudentFragment;
import com.andronauts.quizard.students.fragments.ReportStudentFragment;
import com.andronauts.quizard.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class HomeStudentActivity extends AppCompatActivity {
    ActivityHomeStudentBinding binding;

    class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new ReportStudentFragment();
                case 1: return new QuizStudentFragment();
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
        binding = ActivityHomeStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setSideNav();
        setUpViewPager();

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
                }else if(item.getItemId() == R.id.subject){
                    Intent i = new Intent(HomeStudentActivity.this, SubjectActivity.class);
                    i.putExtra("isStudent",true);
                    startActivity(i);
                }else if(item.getItemId()== R.id.profile){
                    Intent i = new Intent(HomeStudentActivity.this,ProfileStudentActivity.class);
                    startActivity(i);
                }

                return false;
            }
        });
    }

    private void setUpViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        binding.tabLayout.getTabAt(0).setText("Report");
        binding.tabLayout.getTabAt(1).setText("Quizzes");
    }
}