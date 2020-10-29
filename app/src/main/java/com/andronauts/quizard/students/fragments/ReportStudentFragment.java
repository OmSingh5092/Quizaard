package com.andronauts.quizard.students.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.result.ResultListGetResponse;
import com.andronauts.quizard.api.responseModels.subject.SubjectsGetByStudentResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Result;
import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.databinding.FragmentReportStudentBinding;
import com.andronauts.quizard.students.adapters.ReportSubjectStudentRecycler;
import com.andronauts.quizard.utils.DateFormatter;
import com.andronauts.quizard.utils.SharedPrefs;
import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.*;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import kotlin.ResultKt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportStudentFragment extends Fragment {
    private FragmentReportStudentBinding binding;
    private Context context;
    private SharedPrefs prefs;

    private List<Result> results;
    private Map<String,List<Result>> subjectResult;
    private Map<String, Subject> studentSubject;
    private List<String> subjectIds;
    private String[] colors;

    private boolean chartsCreated;

    private ReportSubjectStudentRecycler adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportStudentBinding.inflate(inflater,container,false);
        context = getContext();
        prefs = new SharedPrefs(context);
        colors = context.getResources().getStringArray(R.array.color_array);
        chartsCreated = false;

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        loadData();

        return binding.getRoot();
    }

    private void loadData(){
        RetrofitClient.getClient().getResultsByStudent(prefs.getToken()).enqueue(new Callback<ResultListGetResponse>() {
            @Override
            public void onResponse(Call<ResultListGetResponse> call, Response<ResultListGetResponse> response) {
                if(response.isSuccessful()){
                    results = response.body().getResults();
                    if(results.size() == 0){
                        Toast.makeText(context, "No Report!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    loadSubjects();
                }

            }

            @Override
            public void onFailure(Call<ResultListGetResponse> call, Throwable t) {

            }
        });
    }

    private void loadSubjects(){
        RetrofitClient.getClient().getSubjectsByStudent(prefs.getToken()).enqueue(new Callback<SubjectsGetByStudentResponse>() {
            @Override
            public void onResponse(Call<SubjectsGetByStudentResponse> call, Response<SubjectsGetByStudentResponse> response) {
                if(response.isSuccessful()){
                    studentSubject = response.body().getStudentSubject();
                    createSubjectResult();
                    createPieChart();
                    setUpRecyclerView();

                    binding.swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<SubjectsGetByStudentResponse> call, Throwable t) {

            }
        });
    }

    private void createSubjectResult(){
        subjectResult = new HashMap<>();
        subjectIds = new ArrayList<>();
        for(Result result: results){
            if(subjectResult.get(result.getSubject()) == null){
                subjectResult.put(result.getSubject() ,new ArrayList<>());
                subjectIds.add(result.getSubject());
            }
            subjectResult.get(result.getSubject()).add(result);
        }
    }
    private void createPieChart(){
        binding.piechart.clearChart();
        int total = 0;
        for(int i =0;i<subjectIds.size(); i++){
            String subjectId = subjectIds.get(i);
            int marks = getSubjectAverage(subjectResult.get(subjectId));
            total+=marks;
            binding.piechart.addPieSlice(new PieModel(studentSubject.get(subjectId).getName(),marks , Color.parseColor(colors[i])));
        }
        binding.piechart.addPieSlice(new PieModel("Wrong Answers",100-total, Color.parseColor("#000000")));
        binding.piechart.startAnimation();
    }


    private void setUpRecyclerView(){
        adapter = new ReportSubjectStudentRecycler(context, subjectIds, subjectResult, studentSubject, new ReportSubjectStudentRecycler.ViewTouchHandler() {
            @Override
            public void onTouch(int position) {
                binding.piechart.setCurrentItem(position);
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setAdapter(adapter);
    }



    private int getSubjectAverage(List<Result> results){
        int totalMarks = 0;
        int total = 0;

        for(Result result: results){
            totalMarks+=result.getScore();
            total +=result.getTotal();
        }

        return (totalMarks*100/total);
    }
}
