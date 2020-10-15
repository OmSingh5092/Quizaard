package com.andronauts.quizard.students.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andronauts.quizard.databinding.FragmentReportStudentBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReportStudentFragment extends Fragment {
    FragmentReportStudentBinding binding;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportStudentBinding.inflate(inflater,container,false);
        context = getContext();



        return binding.getRoot();
    }
}
