package com.andronauts.quizzard.faculty.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andronauts.quizzard.api.responseModels.quiz.QuizResponse;
import com.andronauts.quizzard.api.responseModels.subject.SubjectGetResponse;
import com.andronauts.quizzard.api.retrofit.RetrofitClient;
import com.andronauts.quizzard.dataModels.Quiz;
import com.andronauts.quizzard.dataModels.Subject;
import com.andronauts.quizzard.databinding.RecyclerUpcomingQuizFacultyBinding;
import com.andronauts.quizzard.faculty.activities.HostQuizFacultyActivity;
import com.andronauts.quizzard.utils.CalendarManager;
import com.andronauts.quizzard.utils.DateFormatter;
import com.andronauts.quizzard.utils.SharedPrefs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingQuizFacultyRecycler extends RecyclerView.Adapter<UpcomingQuizFacultyRecycler.ViewHolder> {

    private List<Quiz> data = new ArrayList<>();
    private Context context;
    private SharedPrefs prefs;
    private RecyclerUpcomingQuizFacultyBinding binding;

    public UpcomingQuizFacultyRecycler(List<Quiz> data, Context context) {
        this.data = data;
        this.context = context;

        prefs =new SharedPrefs(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerUpcomingQuizFacultyBinding.inflate(LayoutInflater.from(context),parent,false);
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

        holder.cancelQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleCancelQuiz(quiz,position);
            }
        });

        holder.addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CalendarManager(context).addQuizEvent(data.get(position));
            }
        });

        holder.editQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, HostQuizFacultyActivity.class);
                i.putExtra("quizId",quiz.getId());
                i.putExtra("updateQuiz",true);
                context.startActivity(i);
            }
        });
    }

    private void handleCancelQuiz(Quiz quiz,int position){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);

        builder.setTitle("Are you sure you want to cancel this quiz");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                RetrofitClient.getClient().deleteQuiz(prefs.getToken(),quiz.getId()).enqueue(new Callback<QuizResponse>() {
                    @Override
                    public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context, "Quiz Deleted Successfully", Toast.LENGTH_SHORT).show();
                            data.remove(position);
                            UpcomingQuizFacultyRecycler.this.notifyItemRemoved(position);
                        }
                    }

                    @Override
                    public void onFailure(Call<QuizResponse> call, Throwable t) {

                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,description,date,duration,subjectCode,subject;
        MaterialButton cancelQuiz;
        ImageView addEvent,editQuiz;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = binding.title;
            description = binding.description;
            date = binding.date;
            duration = binding.duration;
            subject = binding.subject;
            subjectCode = binding.subjectCode;
            cancelQuiz = binding.cancelQuiz;
            addEvent = binding.addEvent;
            editQuiz = binding.editQuiz;
        }
    }
}
