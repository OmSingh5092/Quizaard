package com.andronauts.quizard.students.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.databinding.RecyclerQuizStudentQuestionBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionsStudentRecycler extends RecyclerView.Adapter<QuestionsStudentRecycler.ViewHolder> {

    RecyclerQuizStudentQuestionBinding binding;
    Context context;
    List<Quiz.Question> data;
    int[] response;

    public QuestionsStudentRecycler(Context context, List<Quiz.Question> data, int[] response) {
        this.context = context;
        this.data = data;
        this.response = response;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerQuizStudentQuestionBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quiz.Question question = data.get(position);
        holder.serial.setText(String.valueOf(position+1));
        holder.question.setText(question.getQuestion());
        List<String> options = question.getOptions();
        for(String option: options){
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(option);
            holder.optionRadioGroup.addView(radioButton);
        }

        holder.optionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int index = holder.optionRadioGroup.indexOfChild(radioGroup.findViewById(i));
                response[position] =index;

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView serial,question;
        RadioGroup optionRadioGroup;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serial = binding.serial;
            question = binding.question;
            optionRadioGroup = binding.optionRadioGroup;
        }
    }
}
