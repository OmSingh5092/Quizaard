package com.andronauts.quizard.faculty.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andronauts.quizard.api.responseModels.result.ResultGetBySubjectResponse;
import com.andronauts.quizard.api.responseModels.subject.SubjectGetResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Quiz;
import com.andronauts.quizard.dataModels.Result;
import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.databinding.RecyclerQuizResultFacultyBinding;
import com.andronauts.quizard.utils.DateFormatter;
import com.andronauts.quizard.utils.PDFGenerator;
import com.andronauts.quizard.utils.SharedPrefs;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizResultRecycler extends RecyclerView.Adapter<QuizResultRecycler.ViewHolder> {

    private RecyclerQuizResultFacultyBinding binding;
    private List<Quiz> data;
    private Context context;
    private SharedPrefs prefs;

    public QuizResultRecycler(List<Quiz> data, Context context) {
        this.data = data;
        this.context = context;

        prefs = new SharedPrefs(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding  = RecyclerQuizResultFacultyBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quiz quiz = data.get(position);
        binding.title.setText(quiz.getTitle());
        binding.description.setText(quiz.getDescription());
        binding.date.setText(new DateFormatter(quiz.getStartTime()).getDateAndTime());
        binding.duration.setText(String.valueOf((Long.valueOf(quiz.getEndTime())-Long.valueOf(quiz.getStartTime()))/1000));

        RetrofitClient.getClient().getSubject(quiz.getSubject()).enqueue(new Callback<SubjectGetResponse>() {
            @Override
            public void onResponse(Call<SubjectGetResponse> call, Response<SubjectGetResponse> response) {
                Subject subject = response.body().getSubject();
                binding.subject.setText(subject.getName());
                binding.subjectCode.setText(subject.getSubjectCode());
            }

            @Override
            public void onFailure(Call<SubjectGetResponse> call, Throwable t) {

            }
        });


        RetrofitClient.getClient().getResultBySubject(prefs.getToken(),quiz.getSubject()).enqueue(new Callback<ResultGetBySubjectResponse>() {
            @Override
            public void onResponse(Call<ResultGetBySubjectResponse> call, Response<ResultGetBySubjectResponse> response) {
                if(response.isSuccessful()){
                    holder.attendance.setText(String.valueOf(response.body().getAttendance()));
                    holder.average.setText(String.valueOf(response.body().getAverage()));

                    binding.quizReport.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new PDFGenerator(context).createReportPdf(quiz,response.body().getResults(),response.body().getStudents());
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResultGetBySubjectResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,description,date,duration,subjectCode,subject,attendance, average;
        MaterialButton showResults;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = binding.title;
            description = binding.description;
            date = binding.date;
            duration = binding.duration;
            subject = binding.subject;
            subjectCode = binding.subjectCode;
            attendance = binding.attendance;
            average = binding.averageMarks;
            showResults = binding.quizReport;
        }
    }
}
