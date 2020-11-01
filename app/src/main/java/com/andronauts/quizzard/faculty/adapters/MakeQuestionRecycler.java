package com.andronauts.quizzard.faculty.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andronauts.quizzard.dataModels.Quiz;
import com.andronauts.quizzard.databinding.RecyclerMakeQuestionsFacultyBinding;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.sephiroth.android.library.numberpicker.NumberPicker;

public class MakeQuestionRecycler extends RecyclerView.Adapter<MakeQuestionRecycler.ViewHolder> {

    List<Quiz.Question> data = new ArrayList<>();
    Context context;
    RecyclerMakeQuestionsFacultyBinding binding;

    public MakeQuestionRecycler(List<Quiz.Question> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerMakeQuestionsFacultyBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quiz.Question question = data.get(position);

        //Setting values
        holder.serial.setText(String.valueOf(position+1));
        holder.question.setText(question.getQuestion());
        holder.total.setProgress(question.getTotal());
        holder.correct.setProgress(question.getCorrect());
        holder.positive.setProgress(question.getPositive());
        holder.negative.setProgress(question.getNegative());


        holder.question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                data.get(position).setQuestion(editable.toString());
            }
        });

        List<String> options;
        if(question.getOptions() == null){
            options = new ArrayList<>();
            question.setOptions(options);
        }else{
            options = question.getOptions();
        }

        binding.options.setLayoutManager(new LinearLayoutManager(context));
        MakeQuestionOptionRecycler adapter = new MakeQuestionOptionRecycler(context,options);
        binding.options.setAdapter(adapter);


        holder.total.setNumberPickerChangeListener(new NumberPicker.OnNumberPickerChangeListener() {
            @Override
            public void onProgressChanged(@NotNull NumberPicker numberPicker, int i, boolean b) {
                data.get(position).setTotal(i);

                while (options.size()!=i){
                    if(options.size()<i){
                        options.add("");
                    }else{
                        options.remove(options.size()-1);
                    }
                }

                adapter.notifyDataSetChanged();

                //Setting upper limit of correct option
                holder.correct.setMaxValue(data.get(position).getTotal());
            }

            @Override
            public void onStartTrackingTouch(@NotNull NumberPicker numberPicker) {

            }

            @Override
            public void onStopTrackingTouch(@NotNull NumberPicker numberPicker) {

            }
        });



        holder.correct.setNumberPickerChangeListener(new NumberPicker.OnNumberPickerChangeListener() {
            @Override
            public void onProgressChanged(@NotNull NumberPicker numberPicker, int i, boolean b) {
                data.get(position).setCorrect(i);
            }

            @Override
            public void onStartTrackingTouch(@NotNull NumberPicker numberPicker) {

            }

            @Override
            public void onStopTrackingTouch(@NotNull NumberPicker numberPicker) {

            }
        });

        holder.positive.setNumberPickerChangeListener(new NumberPicker.OnNumberPickerChangeListener() {
            @Override
            public void onProgressChanged(@NotNull NumberPicker numberPicker, int i, boolean b) {
                data.get(position).setPositive(i);
            }

            @Override
            public void onStartTrackingTouch(@NotNull NumberPicker numberPicker) {

            }

            @Override
            public void onStopTrackingTouch(@NotNull NumberPicker numberPicker) {

            }
        });

        holder.negative.setNumberPickerChangeListener(new NumberPicker.OnNumberPickerChangeListener() {
            @Override
            public void onProgressChanged(@NotNull NumberPicker numberPicker, int i, boolean b) {
                data.get(position).setNegative(i);
            }

            @Override
            public void onStartTrackingTouch(@NotNull NumberPicker numberPicker) {

            }

            @Override
            public void onStopTrackingTouch(@NotNull NumberPicker numberPicker) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView serial;
        NumberPicker total, correct, positive, negative;
        TextInputEditText question;
        RecyclerView options;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            serial = binding.serial;
            question = binding.question;
            total = binding.total;
            correct = binding.correct;
            positive = binding.positive;
            negative = binding.negative;
            options = binding.options;
        }
    }
}
