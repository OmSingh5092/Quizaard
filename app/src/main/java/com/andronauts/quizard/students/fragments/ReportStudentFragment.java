package com.andronauts.quizard.students.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andronauts.quizard.api.responseModels.result.ResultListGetResponse;
import com.andronauts.quizard.api.responseModels.subject.SubjectsGetByStudentResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Result;
import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.databinding.FragmentReportStudentBinding;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentReportStudentBinding.inflate(inflater,container,false);
        context = getContext();
        prefs = new SharedPrefs(context);

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
                        binding.noReport.setVisibility(View.VISIBLE);
                        binding.report.setVisibility(View.GONE);
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
                    createLineChart();
                    createPieChart();

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
        //Necessary to instantiate the chart view
        APIlib.getInstance().setActiveAnyChartView(binding.pieChart);
        //Making pie chart
        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();

        int total= 0;
        int avg;
        for(String subjectId :subjectIds){
            List<Result> results = subjectResult.get(subjectId);
            avg = getSubjectAverage(results);
            total+=100-avg;
            data.add(new ValueDataEntry(studentSubject.get(subjectId).getName(),avg));
        }

        data.add(new ValueDataEntry("Wrong",total/subjectIds.size()));

        pie.data(data);
        pie.title("Overall Subject Performance");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Retail channels")
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);

        binding.pieChart.setChart(pie);
    }

    private void createLineChart(){
        //Necessary to instantiate the chart view
        APIlib.getInstance().setActiveAnyChartView(binding.lineChart);
        //Making Data
        Map<String,Map<String,Result>>data = new HashMap<>();
        List<String> days=  new ArrayList<>();

        for(Result result: results){
            if(data.get(result.getSubmitTime()) == null){
                data.put(result.getSubmitTime(),new HashMap<>());
                days.add(result.getSubmitTime());
            }

            data.get(result.getSubmitTime()).put(result.getSubject(),result);
        }

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Time trend of marks");

        cartesian.yAxis(0).title("Time");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();

        for(String day: days){
            seriesData.add(new CustomDataEntry(day,data.get(day)));
        }

        Set set = Set.instantiate();
        set.data(seriesData);
        for(String subjectId: subjectIds){
            Mapping seriesMapping = set.mapAs("{ x: 'x', value: 'value' }");

            Line series1 = cartesian.line(seriesMapping);
            series1.name(studentSubject.get(subjectId).getName());
            series1.hovered().markers().enabled(true);
            series1.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            series1.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);

        }

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);


        binding.lineChart.setChart(cartesian);
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String day,Map<String,Result> data) {
            super(new DateFormatter(day).getDate(),data.get(subjectIds.get(0))==null ?0:data.get(subjectIds.get(0)).getScore()/data.get(subjectIds.get(0)).getTotal());

            for(int i =1 ;i<subjectIds.size(); i++){
                String subjectId = subjectIds.get(i);
                if(data.get(subjectId) != null){ ;
                    setValue(subjectId,getPercentage(data.get(subjectId)));
                }else{
                    setValue(subjectId,0);
                }
            }
        }
        private int getPercentage(Result result){
            return result.getScore()/result.getTotal();
        }

    }

    private void setUpRecyclerView(){

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
