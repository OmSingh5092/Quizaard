package com.andronauts.quizard.students.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.andronauts.quizard.dataModels.Subject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReportSubjectStudentRecycler extends RecyclerView.Adapter<ReportSubjectStudentRecycler.ViewHolder> {

    public class ReportSubjectRecyclerDataModel{
        Subject subject;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
