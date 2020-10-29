package com.andronauts.quizard.admin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andronauts.quizard.admin.adapters.VerifyFacultyAdminRecycler;
import com.andronauts.quizard.api.responseModels.faculty.FacultyGetListResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.databinding.FragmentVerifyFacultyAdminBinding;
import com.andronauts.quizard.utils.SharedPrefs;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyFacultyAdminFragment extends Fragment {
    private FragmentVerifyFacultyAdminBinding binding;
    private Context context;
    private SharedPrefs prefs;
    private VerifyFacultyAdminRecycler adapter;

    private List<Faculty> faculties;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVerifyFacultyAdminBinding.inflate(getLayoutInflater(),container,false);
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
        RetrofitClient.getClient().adminGetFaculties(prefs.getToken(),false).enqueue(new Callback<FacultyGetListResponse>() {
            @Override
            public void onResponse(Call<FacultyGetListResponse> call, Response<FacultyGetListResponse> response) {
                if(response.isSuccessful()){
                    faculties = response.body().getFaculties();
                    setUpRecyclerView();
                }
                binding.swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<FacultyGetListResponse> call, Throwable t) {

            }
        });
    }

    private void setUpRecyclerView(){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new VerifyFacultyAdminRecycler(context,faculties);
        binding.recyclerView.setAdapter(adapter);
    }
}
