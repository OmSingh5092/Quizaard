package com.andronauts.quizzard.students.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andronauts.quizzard.api.responseModels.result.ResultGetResponse;
import com.andronauts.quizzard.api.responseModels.subject.SubjectGetResponse;
import com.andronauts.quizzard.api.retrofit.RetrofitClient;
import com.andronauts.quizzard.dataModels.Quiz;
import com.andronauts.quizzard.dataModels.Result;
import com.andronauts.quizzard.dataModels.Subject;
import com.andronauts.quizzard.databinding.RecyclerPreviousQuizzesStudentBinding;
import com.andronauts.quizzard.utils.DateFormatter;
import com.andronauts.quizzard.utils.FileManager;
import com.andronauts.quizzard.utils.SharedPrefs;
import com.andronauts.quizzard.utils.pdfGenerators.QuestionsStudentGenerator;
import com.andronauts.quizzard.utils.pdfGenerators.ReportStudentGenerator;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreviousQuizzesStudentRecycler extends RecyclerView.Adapter<PreviousQuizzesStudentRecycler.ViewHolder> {

    private RecyclerPreviousQuizzesStudentBinding binding;
    private Context context;
    private List<Quiz> data;
    private SharedPrefs prefs;
    private FileManager fileManager;

    public PreviousQuizzesStudentRecycler(Context context, List<Quiz> quizzes) {
        this.context = context;
        this.data= quizzes;

        prefs = new SharedPrefs(context);
        fileManager = new FileManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerPreviousQuizzesStudentBinding.inflate(LayoutInflater.from(context),parent,false);
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

        RetrofitClient.getClient().getResultByQuizAndStudent(prefs.getToken(),quiz.getId()).enqueue(new Callback<ResultGetResponse>() {
            @Override
            public void onResponse(Call<ResultGetResponse> call, Response<ResultGetResponse> response) {
                if(response.isSuccessful()){
                    Result result = response.body().getResult();

                    if(result == null){
                        holder.attempted.setVisibility(View.GONE);
                        holder.notAttempted.setVisibility(View.VISIBLE);
                        return;
                    }

                    setResult(holder,position,result);

                }
            }

            @Override
            public void onFailure(Call<ResultGetResponse> call, Throwable t) {

            }
        });

        holder.questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new QuestionsStudentGenerator(context).createPdf(quiz);
            }
        });
    }

    private void setResult(ViewHolder holder, int position, Result result){
        Quiz quiz = data.get(position);

        if(quiz.getPaper() == null){
            holder.paper.setVisibility(View.GONE);
        }

        holder.score.setText(String.valueOf(result.getScore()));
        holder.total.setText(String.valueOf(getTotal(quiz)));

        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReportStudentGenerator(context).createReport(quiz,result);
            }
        });



        holder.paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = fileManager.getQuestionPaperPdfFile();
                FirebaseStorage.getInstance().getReference(quiz.getPaper()).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        fileManager.openPdfFile(file);
                    }
                });
            }
        });
    }

    private int getTotal(Quiz quiz){
        int total  =0;
        for(Quiz.Question question: quiz.getQuestion()){
            total+= question.getPositive();
        }

        return total;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,description,date,duration,subjectCode,subject,report,paper,questions,score,total;
        MaterialButton virtual;
        LinearLayout attempted;
        TextView notAttempted;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = binding.title;
            description = binding.description;
            date = binding.date;
            duration = binding.duration;
            subject = binding.subject;
            subjectCode = binding.subjectCode;
            virtual = binding.virtual;
            report = binding.report;
            paper = binding.paper;
            questions = binding.questions;
            score = binding.score;
            total = binding.totalScore;
            attempted = binding.attempted;
            notAttempted = binding.notAttempted;
        }
    }
}
