package com.andronauts.quizzard.admin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andronauts.quizzard.admin.adapters.StudentAdminRecycler;
import com.andronauts.quizzard.api.responseModels.student.StudentGetListResponse;
import com.andronauts.quizzard.api.retrofit.RetrofitClient;
import com.andronauts.quizzard.dataModels.Student;
import com.andronauts.quizzard.databinding.FragmentVerifyStudentAdminBinding;
import com.andronauts.quizzard.utils.SharedPrefs;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentAdminFragment extends Fragment {
    private FragmentVerifyStudentAdminBinding binding;
    private Context context;
    private SharedPrefs prefs;
    private StudentAdminRecycler adapter;

    private List<Student> students;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVerifyStudentAdminBinding.inflate(getLayoutInflater(),container,false);
        context = getContext();
        prefs = new SharedPrefs(context);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        loadData();

        return binding.getRoot();
    }

    private void loadData(){
        RetrofitClient.getClient().adminGetStudents(prefs.getToken(),false).enqueue(new Callback<StudentGetListResponse>() {
            @Override
            public void onResponse(Call<StudentGetListResponse> call, Response<StudentGetListResponse> response) {
                if(response.isSuccessful()){
                    students = response.body().getStudents();
                    setUpRecyclerView();
                }

                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<StudentGetListResponse> call, Throwable t) {

            }
        });
    }

    private void setUpRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new StudentAdminRecycler(context,students,false);
        binding.recyclerView.setAdapter(adapter);
    }
}
