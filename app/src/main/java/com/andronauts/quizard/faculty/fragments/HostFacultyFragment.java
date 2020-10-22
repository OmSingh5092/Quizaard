package com.andronauts.quizard.faculty.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andronauts.quizard.api.responseModels.faculty.FacultyGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.databinding.FragmentHostFacultyBinding;
import com.andronauts.quizard.faculty.adapters.HostQuizRecycler;
import com.andronauts.quizard.utils.SharedPrefs;


public class HostFacultyFragment extends Fragment {
    FragmentHostFacultyBinding binding;
    Context context;
    Faculty faculty = new Faculty();
    SharedPrefs prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHostFacultyBinding.inflate(getLayoutInflater(),container,false);
        context = getContext();
        prefs = new SharedPrefs(context);
        loadData();


        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        return binding.getRoot();
    }

    private void loadData(){
        RetrofitClient.getClient().facultyGetProfile(prefs.getToken()).enqueue(new Callback<FacultyGetResponse>() {
            @Override
            public void onResponse(Call<FacultyGetResponse> call, Response<FacultyGetResponse> response) {
                HostFacultyFragment.this.faculty = response.body().getFaculty();
                setUpRecyclerView();
                binding.refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<FacultyGetResponse> call, Throwable t) {

            }
        });
    }
    private void setUpRecyclerView(){
        HostQuizRecycler adapter = new HostQuizRecycler(faculty.getSubjects(),getContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setAdapter(adapter);
    }

}