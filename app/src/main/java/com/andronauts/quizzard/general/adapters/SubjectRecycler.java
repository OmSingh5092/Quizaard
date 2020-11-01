package com.andronauts.quizzard.general.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andronauts.quizzard.api.responseModels.subject.SubjectGetResponse;
import com.andronauts.quizzard.api.retrofit.RetrofitClient;
import com.andronauts.quizzard.dataModels.Subject;
import com.andronauts.quizzard.databinding.RecyclerSubjectBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectRecycler extends RecyclerView.Adapter<SubjectRecycler.ViewHolder> {
    private RecyclerSubjectBinding binding;
    private Context context;
    private List<String> data;

    public SubjectRecycler(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding= RecyclerSubjectBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String subjectId = data.get(position);
        RetrofitClient.getClient().getSubject(subjectId).enqueue(new Callback<SubjectGetResponse>() {
            @Override
            public void onResponse(Call<SubjectGetResponse> call, Response<SubjectGetResponse> response) {
                if(response.isSuccessful()){
                    Subject subject = response.body().getSubject();

                    holder.name.setText(subject.getName());
                    holder.code.setText(subject.getSubjectCode());
                }
            }

            @Override
            public void onFailure(Call<SubjectGetResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,code;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = binding.name;
            code = binding.code;
        }
    }
}
