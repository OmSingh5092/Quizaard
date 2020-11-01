package com.andronauts.quizzard.admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andronauts.quizzard.api.responseModels.subject.SubjectResponse;
import com.andronauts.quizzard.api.retrofit.RetrofitClient;
import com.andronauts.quizzard.dataModels.Subject;
import com.andronauts.quizzard.databinding.RecyclerSubjectAdminBinding;
import com.andronauts.quizzard.utils.SharedPrefs;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubjectAdminRecycler extends RecyclerView.Adapter<SubjectAdminRecycler.ViewHolder> {
    private RecyclerSubjectAdminBinding binding;
    private Context context;
    private List<Subject> data;
    private SharedPrefs prefs;

    public SubjectAdminRecycler(Context context, List<Subject> data) {
        this.context = context;
        this.data = data;

        prefs = new SharedPrefs(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerSubjectAdminBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject subject = data.get(position);
        holder.name.setText(subject.getName());
        holder.code.setText(subject.getSubjectCode());

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitClient.getClient().deleteSubject(prefs.getToken(),subject.getId()).enqueue(new Callback<SubjectResponse>() {
                    @Override
                    public void onResponse(Call<SubjectResponse> call, Response<SubjectResponse> response) {
                        Toast.makeText(context, "Subject Removed Successfully!", Toast.LENGTH_SHORT).show();
                        data.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<SubjectResponse> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,code;
        ImageView remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = binding.name;
            code = binding.code;
            remove = binding.remove;
        }
    }
}
