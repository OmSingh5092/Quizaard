package com.andronauts.quizard.general.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.databinding.RecyclerSubjectBinding;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubjectRecycler extends RecyclerView.Adapter<SubjectRecycler.ViewHolder> {

    public interface ActionHandler{
        void onAction(int position);
    }

    List<Subject> data = new ArrayList<>();
    Context context;
    ActionHandler handler;
    boolean isRegistered;

    RecyclerSubjectBinding binding;

    public SubjectRecycler(List<Subject> data, Context context, boolean isRegistered,ActionHandler handler) {
        this.data = data;
        this.context = context;
        this.handler = handler;
        this.isRegistered = isRegistered;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerSubjectBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject subject = data.get(position);
        holder.name.setText(subject.getName());
        holder.code.setText(subject.getSubjectCode());
        if(isRegistered){
            holder.register.setVisibility(View.GONE);
            holder.remove.setVisibility(View.VISIBLE);
        }else{
            holder.register.setVisibility(View.VISIBLE);
            holder.remove.setVisibility(View.GONE);
        }

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.onAction(position);
            }
        });
        holder.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.onAction(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,code;
        ImageView remove;
        MaterialButton register;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = binding.name;
            code = binding.code;
            remove = binding.remove;
            register = binding.register;
        }
    }
}
