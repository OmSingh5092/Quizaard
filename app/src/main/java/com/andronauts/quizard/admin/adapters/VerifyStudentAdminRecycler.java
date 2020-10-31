package com.andronauts.quizard.admin.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andronauts.quizard.api.responseModels.student.StudentUpdateResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Student;
import com.andronauts.quizard.databinding.RecyclerVerifyStudentBinding;
import com.andronauts.quizard.utils.FileManager;
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

public class VerifyStudentAdminRecycler extends RecyclerView.Adapter<VerifyStudentAdminRecycler.ViewHolder> {
    private RecyclerVerifyStudentBinding binding;
    private Context context;
    private List<Student> data;
    private SharedPrefs prefs;
    private FileManager fileManager;
    private boolean registered;

    public VerifyStudentAdminRecycler(Context context, List<Student> data,boolean registered) {
        this.context = context;
        this.data = data;
        this.registered = registered;

        fileManager = new FileManager(context);
        prefs = new SharedPrefs(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerVerifyStudentBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = data.get(position);
        holder.name.setText(student.getName());
        holder.registrationNumber.setText(student.getRegistrationNumber());
        holder.year.setText(String.valueOf(student.getYear()));
        holder.department.setText(student.getDepartment());

        //Adding profile photo
        if(student.getPhotoPath()!=null){
            try {
                File file = File.createTempFile("profile","jpeg");
                FirebaseStorage.getInstance().getReference(student.getPhotoPath()).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
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

        holder.admitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(student.getAdmitCardPath()!=null){
                    File file = fileManager.getStudentReportPdfFile();

                    FirebaseStorage.getInstance().getReference(student.getAdmitCardPath()).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            fileManager.openImageFile(file);
                        }
                    });
                }else{
                    Toast.makeText(context, "No Admit Card Uploaded", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Changing the button text if the user is registered
        if(registered){
            holder.register.setText("Unregister");
        }

        holder.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Changing registration state of the user
                RetrofitClient.getClient().adminRegisterStudent(prefs.getToken(),student.getId(),!registered).enqueue(new Callback<StudentUpdateResponse>() {
                    @Override
                    public void onResponse(Call<StudentUpdateResponse> call, Response<StudentUpdateResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            data.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<StudentUpdateResponse> call, Throwable t) {

                    }
                });

            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitClient.getClient().adminDeleteStudent(prefs.getToken(),student.getId()).enqueue(new Callback<StudentUpdateResponse>() {
                    @Override
                    public void onResponse(Call<StudentUpdateResponse> call, Response<StudentUpdateResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(context, "User Removed Successfully!", Toast.LENGTH_SHORT).show();
                            data.remove(position);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<StudentUpdateResponse> call, Throwable t) {

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
        TextView name,registrationNumber,department,year;
        ImageView profileImage,admitCard,remove;
        MaterialButton register;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = binding.name;
            registrationNumber = binding.registrationNumber;
            department = binding.department;
            admitCard = binding.admitCard;
            year = binding.year;
            profileImage = binding.profileImage;
            register = binding.register;
            remove = binding.remove;
        }
    }
}
