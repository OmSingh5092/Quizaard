package com.andronauts.quizard.faculty.activities;

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

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.faculty.FacultyGetResponse;
import com.andronauts.quizard.api.responseModels.student.StudentGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.databinding.ActivityHomeFacultyBinding;
import com.andronauts.quizard.faculty.fragments.HostFacultyFragment;
import com.andronauts.quizard.faculty.fragments.ResultFacultyFragment;
import com.andronauts.quizard.general.activities.FacultiesActivity;
import com.andronauts.quizard.general.activities.LoginActivity;
import com.andronauts.quizard.general.activities.StudentsActivity;
import com.andronauts.quizard.admin.activities.UserSubjectAdminActivity;
import com.andronauts.quizard.general.activities.SubjectActivity;
import com.andronauts.quizard.students.activities.HomeStudentActivity;
import com.andronauts.quizard.students.activities.ProfileStudentActivity;
import com.andronauts.quizard.utils.SessionManager;
import com.andronauts.quizard.utils.SharedPrefs;
import com.github.nkzawa.socketio.client.On;
import com.google.android.material.navigation.NavigationView;

public class HomeFacultyActivity extends AppCompatActivity {
    private ActivityHomeFacultyBinding binding;
    private SharedPrefs prefs;
    private Faculty faculty;

    private class ViewPagerAdapter extends FragmentPagerAdapter{

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new HostFacultyFragment();
                case 1: return new ResultFacultyFragment();
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
        binding = ActivityHomeFacultyBinding.inflate(getLayoutInflater());
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
        RetrofitClient.getClient().facultyGetProfile(prefs.getToken()).enqueue(new Callback<FacultyGetResponse>() {
            @Override
            public void onResponse(Call<FacultyGetResponse> call, Response<FacultyGetResponse> response) {
                if(response.isSuccessful()){
                    faculty = response.body().getFaculty();
                    updateUI();
                }

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<FacultyGetResponse> call, Throwable t) {

            }
        });
    }

    private void updateUI(){
        if(faculty == null){
            //Profile deleted
            Toast.makeText(this, "Profile has been deleted!", Toast.LENGTH_SHORT).show();
            //logging out
            logOut();
            return;
        }
        if(faculty.isRegistered()){
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
                        Intent i = new Intent(HomeFacultyActivity.this, ProfileStudentActivity.class);
                        startActivity(i);
                    }else if(item.getItemId() == R.id.logout){
                        logOut();
                    }else{
                        Toast.makeText(HomeFacultyActivity.this, "User not registered!", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        }
    }

    private void logOut(){
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

    private void setSideNav(){
        binding.sideNav.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.logout){

                }else if(item.getItemId() == R.id.subject){
                    Intent i = new Intent(HomeFacultyActivity.this, UserSubjectAdminActivity.class);
                    i.putExtra("isStudent",false);
                    startActivity(i);
                }else if (item.getItemId() == R.id.profile){
                    Intent i = new Intent(HomeFacultyActivity.this,ProfileFacultyActivity.class);
                    startActivity(i);
                }else if(item.getItemId() == R.id.hosted_quizzes){
                    Intent i = new Intent(HomeFacultyActivity.this, SubjectActivity.class);
                    startActivity(i);
                }else if(item.getItemId() == R.id.students){
                    Intent i = new Intent(HomeFacultyActivity.this, StudentsActivity.class);
                    i.putExtra("isStudent",false);
                    startActivity(i);
                }else if(item.getItemId() == R.id.faculties){
                    Intent i =new Intent(HomeFacultyActivity.this, FacultiesActivity.class);
                    i.putExtra("isStudent",false);
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

        binding.tabLayout.getTabAt(0).setText("Host Quiz");
        binding.tabLayout.getTabAt(1).setText("Results");
    }

    @Override
    public boolean onSupportNavigateUp() {
        binding.drawer.openDrawer(Gravity.LEFT);
        return super.onSupportNavigateUp();
    }
}