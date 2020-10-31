package com.andronauts.quizard.admin.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andronauts.quizard.admin.activities.StudentAdminActivity;
import com.andronauts.quizard.admin.activities.SubjectAdminActivity;
import com.andronauts.quizard.admin.activities.UserSubjectAdminActivity;
import com.andronauts.quizard.api.responseModels.faculty.FacultyUpdateResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.databinding.RecyclerFacultyAdminBinding;
import com.andronauts.quizard.utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacultyAdminRecycler extends RecyclerView.Adapter<FacultyAdminRecycler.ViewHolder>{
    private RecyclerFacultyAdminBinding binding;
    private Context context;
    private List<Faculty> data;
    private SharedPrefs prefs;
    private boolean registered;
    public FacultyAdminRecycler(Context context, List<Faculty> data, boolean registered) {
        this.context = context;
        this.data = data;
        this.registered = registered;

        prefs = new SharedPrefs(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerFacultyAdminBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Faculty faculty = data.get(position);
        holder.name.setText(faculty.getName());
        holder.facultyId.setText(faculty.getFacultyId());
        holder.department.setText(faculty.getDepartment());

        if(faculty.getPhotoPath()!=null){
            try {
                File file = File.createTempFile("profile","jpeg");
                FirebaseStorage.getInstance().getReference(faculty.getPhotoPath()).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        try {
                            FileInputStream stream = new FileInputStream(file);
                            Bitmap bitmap = BitmapFactory.decodeStream(stream);
                            holder.profileImage.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //Changing the layout
        if(registered){
            holder.register.setText("Unregister");
            holder.subjects.setVisibility(View.VISIBLE);
        }

        holder.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Changing registration state of the user
                RetrofitClient.getClient().adminRegisterFaculty(prefs.getToken(),faculty.getId(),!registered).enqueue(new Callback<FacultyUpdateResponse>() {
                    @Override
                    public void onResponse(Call<FacultyUpdateResponse> call, Response<FacultyUpdateResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context, "Successful!", Toast.LENGTH_SHORT).show();
                            data.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<FacultyUpdateResponse> call, Throwable t) {

                    }
                });

            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitClient.getClient().adminDeleteFaculty(prefs.getToken(),faculty.getId()).enqueue(new Callback<FacultyUpdateResponse>() {
                    @Override
                    public void onResponse(Call<FacultyUpdateResponse> call, Response<FacultyUpdateResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context, "User Removed Successfully!", Toast.LENGTH_SHORT).show();
                            data.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<FacultyUpdateResponse> call, Throwable t) {

                    }
                });
            }
        });

        holder.subjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, UserSubjectAdminActivity.class);
                i.putExtra("userId",faculty.getId());
                i.putExtra("isStudent",false);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,facultyId,department;
        ImageView profileImage,remove;
        MaterialButton register,subjects;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = binding.name;
            facultyId = binding.facultyId;
            department = binding.department;
            profileImage = binding.profileImage;
            register = binding.register;
            remove = binding.remove;
            subjects = binding.subjects;
        }
    }
}
