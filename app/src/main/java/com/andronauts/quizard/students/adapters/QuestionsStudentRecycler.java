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

    public interface RadioChangeHandler{
        void onChange();
    }

    RecyclerQuizStudentQuestionBinding binding;
    Context context;
    List<Quiz.Question> data;
    int[] response;
    RadioChangeHandler handler;


    public QuestionsStudentRecycler(Context context, List<Quiz.Question> data, int[] response,RadioChangeHandler handler) {
        this.context = context;
        this.data = data;
        this.response = response;
        this.handler = handler;
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
        holder.positive.setText(String.valueOf(question.getPositive()));
        holder.negative.setText(String.valueOf(question.getNegative()));
        List<String> options = question.getOptions();

        //Adding radio button to radio group only if there are no children
        if(holder.optionRadioGroup.getChildCount() ==0){
            for(String option: options){
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(option);
                holder.optionRadioGroup.addView(radioButton);
            }
        }

        if(response[position]!=0){
            holder.optionRadioGroup.check(holder.optionRadioGroup.getChildAt(response[position]-1).getId());
        }else{
            holder.optionRadioGroup.clearCheck();
        }

        holder.optionRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int index = holder.optionRadioGroup.indexOfChild(radioGroup.findViewById(i));
                response[position] =index+1;
                handler.onChange();

                holder.removeResponse.setVisibility(View.VISIBLE);
            }
        });

        //Setting the visibility of remove response button
        if(response[position] == 0){
            holder.removeResponse.setVisibility(View.GONE);
        }

        holder.removeResponse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                response[position]=0;
                handler.onChange();
                holder.optionRadioGroup.clearCheck();
                holder.removeResponse.setVisibility(View.GONE);

            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView serial,question,removeResponse,positive,negative;
        RadioGroup optionRadioGroup;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serial = binding.serial;
            question = binding.question;
            optionRadioGroup = binding.optionRadioGroup;
            removeResponse = binding.removeResponse;
            positive = binding.positive;
            negative = binding.negative;
        }
    }
}
