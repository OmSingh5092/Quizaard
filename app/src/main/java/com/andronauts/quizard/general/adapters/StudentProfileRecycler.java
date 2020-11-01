package com.andronauts.quizard.general.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andronauts.quizard.dataModels.Student;
import com.andronauts.quizard.databinding.RecyclerProfileStudentBinding;
import com.andronauts.quizard.general.activities.ChatActivity;
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

public class StudentProfileRecycler extends RecyclerView.Adapter<StudentProfileRecycler.ViewHolder>{
    private RecyclerProfileStudentBinding binding;
    private Context context;
    private List<Student> data;
    private SharedPrefs prefs;
    private boolean isStudent;

    public StudentProfileRecycler(Context context, List<Student> students,boolean isStudent) {
        this.context = context;
        this.data = students;
        this.isStudent = isStudent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RecyclerProfileStudentBinding.inflate(LayoutInflater.from(context),parent,false);
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

        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",student.getEmail(), null));
                context.startActivity(Intent.createChooser(emailIntent, "Send email"));
            }
        });

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("receiverId",student.getId());
                i.putExtra("isSenderStudent",isStudent);
                i.putExtra("isReceiverStudent",true);
                i.putExtra("name",student.getName());
                context.startActivity(i);
            }
        });

        if(student.isRegistered()){
            holder.verified.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,registrationNumber,department,year;
        ImageView email,message,profileImage,verified;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = binding.name;
            registrationNumber = binding.registrationNumber;
            department = binding.department;
            email = binding.email;
            message = binding.chat;
            year = binding.year;
            profileImage = binding.profileImage;
            verified = binding.verified;
        }
    }
}
