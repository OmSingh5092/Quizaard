package com.andronauts.quizard.students.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andronauts.quizard.api.responseModels.subject.SubjectGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.databinding.RecyclerUpcomingQuizStudentBinding;
import com.andronauts.quizard.students.activities.QuizStudentActivity;
import com.andronauts.quizard.utils.CalendarManager;
import com.andronauts.quizard.utils.DateFormatter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingQuizStudentRecycler extends RecyclerView.Adapter<UpcomingQuizStudentRecycler.ViewHolder> {

    List<Quiz> data = new ArrayList<>();
    Context context;
    RecyclerUpcomingQuizStudentBinding binding;

    public UpcomingQuizStudentRecycler(List<Quiz> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerUpcomingQuizStudentBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quiz quiz = data.get(position);
        holder.title.setText(quiz.getTitle());
        holder.description.setText(quiz.getDescription());
        holder.date.setText(new DateFormatter(quiz.getStartTime()).getDateAndTime());
        holder.duration.setText(String.valueOf((Long.valueOf(quiz.getEndTime())-Long.valueOf(quiz.getStartTime()))/60000));

        RetrofitClient.getClient().getSubject(quiz.getSubject()).enqueue(new Callback<SubjectGetResponse>() {
            @Override
            public void onResponse(Call<SubjectGetResponse> call, Response<SubjectGetResponse> response) {
                Subject subject = response.body().getSubject();
                holder.subject.setText(subject.getName());
                holder.subjectCode.setText(subject.getSubjectCode());
            }

            @Override
            public void onFailure(Call<SubjectGetResponse> call, Throwable t) {

            }
        });

        if(quiz.isLive()){
            holder.startQuiz.setVisibility(View.VISIBLE);
        }

        holder.startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, QuizStudentActivity.class);
                i.putExtra("quizId",quiz.getId());
                context.startActivity(i);
            }
        });

        holder.addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CalendarManager(context).addQuizEvent(data.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,description,date,duration,subjectCode,subject;
        MaterialButton startQuiz;
        ImageView addEvent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = binding.title;
            description = binding.description;
            date = binding.date;
            duration = binding.duration;
            subject = binding.subject;
            subjectCode = binding.subjectCode;
            startQuiz = binding.startQuiz;
            addEvent = binding.addEvent;
        }
    }
}
