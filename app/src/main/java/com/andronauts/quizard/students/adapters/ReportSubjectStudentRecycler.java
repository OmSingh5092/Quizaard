package com.andronauts.quizard.students.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andronauts.quizard.R;
import com.andronauts.quizard.dataModels.Result;
import com.andronauts.quizard.dataModels.Subject;
import com.andronauts.quizard.databinding.RecyclerReportSubjectBinding;
import com.andronauts.quizard.utils.DateFormatter;
import com.andronauts.quizard.utils.SharedPrefs;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ReportSubjectStudentRecycler extends RecyclerView.Adapter<ReportSubjectStudentRecycler.ViewHolder> {
    private RecyclerReportSubjectBinding binding;
    private Context context;
    private List<String> subjectIds;
    private Map<String,List<Result>> subjectResult;
    private Map<String, Subject> studentSubject;
    private SharedPrefs prefs;
    private ViewTouchHandler handler;

    public interface ViewTouchHandler{
        void onTouch(int position);
    }

    private String[] colors;

    public ReportSubjectStudentRecycler(Context context, List<String> subjectIds, Map<String, List<Result>> subjectResult, Map<String, Subject> studentSubject, ViewTouchHandler handler) {
        this.context = context;
        this.subjectIds = subjectIds;
        this.subjectResult = subjectResult;
        this.studentSubject = studentSubject;
        this.handler = handler;

        prefs = new SharedPrefs(context);
        colors = context.getResources().getStringArray(R.array.color_array);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerReportSubjectBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String subjectId = subjectIds.get(position);
        Subject subject = studentSubject.get(subjectId);
        List<Result> results = subjectResult.get(subjectId);

        holder.name.setText(subject.getName());
        holder.color.setBackgroundColor(Color.parseColor(colors[position]));

        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(results.size() == 1){
                    Toast.makeText(context, "Given ony one test", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(holder.chart.getVisibility() == View.VISIBLE){
                    holder.chart.setVisibility(View.GONE);
                    holder.viewMore.setText("View More");
                }else{
                    holder.chart.setVisibility(View.VISIBLE);
                    holder.viewMore.setText("View Less");
                }
            }
        });

        //Setting line charts
        setLineChart(subject,results,holder);
    }

    private void setLineChart(Subject subject, List<Result> results, ViewHolder holder){
        int position = holder.getAdapterPosition();
        ValueLineSeries series = new ValueLineSeries();
        series.setColor(Color.parseColor(colors[position]));

        for(int i =0 ;i<results.size(); i++){
            Result result = results.get(i);
            series.addPoint(new ValueLinePoint(new DateFormatter(result.getSubmitTime()).getDate(), (result.getScore()*100)/result.getTotal()));
        }

        holder.chart.addSeries(series);
        holder.chart.startAnimation();
    }

    @Override
    public int getItemCount() {
        return subjectIds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ValueLineChart chart;
        TextView name,viewMore;
        ImageView color;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chart = binding.lineChart;
            name = binding.name;
            viewMore = binding.viewMore;
            color = binding.color;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handler.onTouch(getAdapterPosition());
                }
            });
        }
    }
}
