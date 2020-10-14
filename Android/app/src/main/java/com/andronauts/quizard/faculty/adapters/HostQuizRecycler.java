package com.andronauts.quizard.faculty.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andronauts.quizard.api.controllers.SubjectCtrl;
import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.databinding.RecyclerHostQuizFacultyBinding;
import com.andronauts.quizard.faculty.activities.HostQuizFacultyActivity;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HostQuizRecycler extends RecyclerView.Adapter<HostQuizRecycler.ViewHolder> {

    List<String> data = new ArrayList<>();
    Context context;
    RecyclerHostQuizFacultyBinding binding;

    public HostQuizRecycler(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerHostQuizFacultyBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        new SubjectCtrl(context).getSubject(data.get(position), new SubjectCtrl.GetSubjectHandler() {
            @Override
            public void onSuccess(Subject subject) {
                holder.name.setText(subject.getName());
                holder.code.setText(subject.getSubjectCode());

                holder.host.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(context, HostQuizFacultyActivity.class);
                        i.putExtra("subjectId",data.get(position));
                        context.startActivity(i);
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, "Subject load failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        MaterialButton host;
        TextView name,code;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            host = binding.host;
            name = binding.name;
            code = binding.code;
        }
    }


}
