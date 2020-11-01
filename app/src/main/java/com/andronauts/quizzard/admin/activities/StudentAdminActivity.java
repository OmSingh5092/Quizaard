package com.andronauts.quizzard.admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.andronauts.quizzard.R;
import com.andronauts.quizzard.admin.adapters.StudentAdminRecycler;
import com.andronauts.quizzard.api.responseModels.student.StudentGetListResponse;
import com.andronauts.quizzard.api.retrofit.RetrofitClient;
import com.andronauts.quizzard.dataModels.Student;
import com.andronauts.quizzard.databinding.ActivityStudentsBinding;
import com.andronauts.quizzard.utils.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

public class StudentAdminActivity extends AppCompatActivity {
    //Using activity_students
    private ActivityStudentsBinding binding;
    private SharedPrefs prefs;
    private StudentAdminRecycler adapter;
    private List<Student> students;
    private List<Student> sortedStudents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Changing toolbar color
        binding.toolbar.setBackgroundColor(getResources().getColor(R.color.colorAdmin));
        setSupportActionBar(binding.toolbar);
        prefs = new SharedPrefs(this);

        loadData();

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sortList(editable.toString());
            }
        });

    }

    private void loadData(){
        RetrofitClient.getClient().adminGetStudents(prefs.getToken(),true).enqueue(new Callback<StudentGetListResponse>() {
            @Override
            public void onResponse(Call<StudentGetListResponse> call, Response<StudentGetListResponse> response) {
                if(response.isSuccessful()){
                    students = response.body().getStudents();
                    sortedStudents = new ArrayList<>(students);
                    setUpRecyclerView();
                }

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<StudentGetListResponse> call, Throwable t) {

            }
        });
    }

    private void sortList(String key){
        sortedStudents.clear();
        for(Student student:students){
            if(student.getName().toLowerCase().contains(key) ||
                    student.getRegistrationNumber().toLowerCase().contains(key)){
                sortedStudents.add(student);
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void setUpRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdminRecycler(this,sortedStudents,true);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}