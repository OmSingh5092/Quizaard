package com.andronauts.quizard.faculty.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andronauts.quizard.databinding.RecyclerMakeQuestionsOptionsBinding;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MakeQuestionOptionRecycler extends RecyclerView.Adapter<MakeQuestionOptionRecycler.ViewHolder> {
    RecyclerMakeQuestionsOptionsBinding binding;
    Context context;
    List<String> data;

    public MakeQuestionOptionRecycler(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerMakeQuestionsOptionsBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.serial.setText(String.valueOf(position+1));
        holder.option.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                data.set(position,editable.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextInputEditText option;
        TextView serial;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            option = binding.option;
            serial = binding.serial;
        }
    }
}
