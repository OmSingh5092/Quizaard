package com.andronauts.quizzard.students.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andronauts.quizzard.R;
import com.andronauts.quizzard.api.responseModels.student.StudentGetResponse;
import com.andronauts.quizzard.api.retrofit.RetrofitClient;
import com.andronauts.quizzard.dataModels.Student;
import com.andronauts.quizzard.databinding.ActivityHomeStudentBinding;
import com.andronauts.quizzard.general.activities.FacultiesActivity;
import com.andronauts.quizzard.general.activities.LoginActivity;
import com.andronauts.quizzard.general.activities.StudentsActivity;
import com.andronauts.quizzard.general.activities.SubjectActivity;
import com.andronauts.quizzard.students.fragments.QuizStudentFragment;
import com.andronauts.quizzard.students.fragments.ReportStudentFragment;
import com.andronauts.quizzard.utils.SessionManager;
import com.andronauts.quizzard.utils.SharedPrefs;
import com.google.android.material.navigation.NavigationView;

public class HomeStudentActivity extends AppCompatActivity {
    private ActivityHomeStudentBinding binding;
    private SharedPrefs prefs;
    private Student student;

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
        prefs = new SharedPrefs(this);

        loadData();

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    private void loadData(){
        RetrofitClient.getClient().studentGetProfile(prefs.getToken()).enqueue(new Callback<StudentGetResponse>() {
            @Override
            public void onResponse(Call<StudentGetResponse> call, Response<StudentGetResponse> response) {
                if(response.isSuccessful()){
                    student = response.body().getStudent();
                    updateUI();
                }

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<StudentGetResponse> call, Throwable t) {

            }
        });
    }

    private void updateUI(){
        if(student == null){
            //Profile deleted
            Toast.makeText(this, "Profile has been deleted!", Toast.LENGTH_SHORT).show();
            //logging out
            logOut();
            return;
        }
        if(student.isRegistered()){
            binding.viewPager.setVisibility(View.VISIBLE);
            binding.tabLayout.setVisibility(View.VISIBLE);
            binding.swipeRefreshLayout.setVisibility(View.GONE);
            setSideNav();
            setUpViewPager();
        }else{
            binding.viewPager.setVisibility(View.GONE);
            binding.tabLayout.setVisibility(View.GONE);
            binding.swipeRefreshLayout.setVisibility(View.VISIBLE);

            binding.sideNav.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if(item.getItemId() == R.id.profile){
                        Intent i = new Intent(HomeStudentActivity.this,ProfileStudentActivity.class);
                        startActivity(i);
                    }else if(item.getItemId() == R.id.logout){
                        logOut();
                    }else{
                        Toast.makeText(HomeStudentActivity.this, "User not registered!", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        }
    }

    private void logOut(){
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
                    logOut();
                }else if(item.getItemId() == R.id.subject){
                    Intent i = new Intent(HomeStudentActivity.this, SubjectActivity.class);
                    i.putExtra("isStudent",true);
                    startActivity(i);
                }else if(item.getItemId()== R.id.profile){
                    Intent i = new Intent(HomeStudentActivity.this,ProfileStudentActivity.class);
                    startActivity(i);
                }else if(item.getItemId() == R.id.previous_quizzes){
                    Intent i =new Intent(HomeStudentActivity.this, PreviousQuizzesStudentActivity.class);
                    startActivity(i);
                }else if(item.getItemId() == R.id.students){
                    Intent i =new Intent(HomeStudentActivity.this, StudentsActivity.class);
                    i.putExtra("isStudent",true);
                    startActivity(i);
                }else if(item.getItemId() == R.id.faculties){
                    Intent i = new Intent(HomeStudentActivity.this, FacultiesActivity.class);
                    i.putExtra("isStudent",true);
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