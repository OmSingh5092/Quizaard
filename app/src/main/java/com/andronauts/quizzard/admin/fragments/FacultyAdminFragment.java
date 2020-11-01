package com.andronauts.quizzard.admin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andronauts.quizzard.admin.adapters.FacultyAdminRecycler;
import com.andronauts.quizzard.api.responseModels.faculty.FacultyGetListResponse;
import com.andronauts.quizzard.api.retrofit.RetrofitClient;
import com.andronauts.quizzard.dataModels.Faculty;
import com.andronauts.quizzard.databinding.FragmentVerifyFacultyAdminBinding;
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

public class FacultyAdminFragment extends Fragment {
    private FragmentVerifyFacultyAdminBinding binding;
    private Context context;
    private SharedPrefs prefs;
    private FacultyAdminRecycler adapter;

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
        adapter = new FacultyAdminRecycler(context,faculties,false);
        binding.recyclerView.setAdapter(adapter);
    }
}
