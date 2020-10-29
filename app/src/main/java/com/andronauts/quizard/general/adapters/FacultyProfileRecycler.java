package com.andronauts.quizard.general.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.databinding.RecyclerProfileFacultyBinding;
import com.andronauts.quizard.utils.SharedPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FacultyProfileRecycler extends RecyclerView.Adapter<FacultyProfileRecycler.ViewHolder> {

    private RecyclerProfileFacultyBinding binding;
    private Context context;
    private SharedPrefs prefs;
    private List<Faculty> data;

    public FacultyProfileRecycler(Context context, List<Faculty> data) {
        this.context = context;
        this.data = data;

        prefs = new SharedPrefs(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerProfileFacultyBinding.inflate(LayoutInflater.from(context),parent,false);
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

        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,facultyId,department;
        ImageView email,message,profileImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = binding.name;
            facultyId = binding.facultyId;
            department = binding.department;
            email = binding.email;
            message = binding.chat;
            profileImage = binding.profileImage;
        }
    }
}