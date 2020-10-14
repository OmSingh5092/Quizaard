package com.andronauts.quizard.faculty.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.controllers.FacultyCtrl;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.databinding.FragmentHostFacultyBinding;
import com.andronauts.quizard.faculty.adapters.HostQuizRecycler;
import com.andronauts.quizard.utils.SharedPrefs;


public class HostFacultyFragment extends Fragment {
    FragmentHostFacultyBinding binding;
    Context context;
    Faculty faculty = new Faculty();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHostFacultyBinding.inflate(getLayoutInflater(),container,false);
        context = getContext();

        loadData();



        return binding.getRoot();
    }

    private void loadData(){
        new FacultyCtrl(context).getProfile(new FacultyCtrl.GetProfileHandler() {
            @Override
            public void onSuccess(Faculty faculty) {
                HostFacultyFragment.this.faculty = faculty;
                setUpRecyclerView();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setUpRecyclerView(){
        HostQuizRecycler adapter = new HostQuizRecycler(faculty.getSubjects(),getContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setAdapter(adapter);
    }
}