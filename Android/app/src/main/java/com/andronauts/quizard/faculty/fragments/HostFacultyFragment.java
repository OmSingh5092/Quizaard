package com.andronauts.quizard.faculty.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andronauts.quizard.R;
import com.andronauts.quizard.databinding.FragmentHostFacultyBinding;


public class HostFacultyFragment extends Fragment {
    FragmentHostFacultyBinding binding;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHostFacultyBinding.inflate(getLayoutInflater(),container,false);
        context = getContext();




        return binding.getRoot();
    }
}