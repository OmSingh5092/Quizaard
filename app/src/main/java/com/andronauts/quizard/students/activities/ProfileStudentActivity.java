package com.andronauts.quizard.students.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.student.StudentGetResponse;
import com.andronauts.quizard.api.responseModels.student.StudentUpdateResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Student;
import com.andronauts.quizard.databinding.ActivityProfileStudentBinding;
import com.andronauts.quizard.utils.PermissionCtrl;
import com.andronauts.quizard.utils.SharedPrefs;
import com.andronauts.quizard.utils.firebase.StorageCtrl;
import com.google.android.material.snackbar.Snackbar;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ProfileStudentActivity extends AppCompatActivity {
    ActivityProfileStudentBinding binding;

    int PICK_PHOTO =100, PICK_CARD=101;
    SharedPrefs prefs;
    Student student = new Student();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        prefs = new SharedPrefs(this);

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSubmit();
            }
        });

        binding.uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pictureIntent.setType("image/*");
                pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(pictureIntent,"Select Picture"), PICK_PHOTO);
            }
        });

        binding.uploadAdmitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                pictureIntent.setType("image/*");
                pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(pictureIntent,"Select Picture"), PICK_CARD);
            }
        });
        loadData();

        new PermissionCtrl(this).askStoragePermission();

    }

    private void loadData(){
        Snackbar snackbar = Snackbar.make(binding.getRoot(),"Fetching Profile...",Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        RetrofitClient.getClient().studentGetProfile(prefs.getToken()).enqueue(new Callback<StudentGetResponse>() {
            @Override
            public void onResponse(Call<StudentGetResponse> call, Response<StudentGetResponse> response) {
                if(response.isSuccessful()){
                    student = response.body().getStudent();
                    setData();
                    snackbar.dismiss();
                }
            }

            @Override
            public void onFailure(Call<StudentGetResponse> call, Throwable t) {
                snackbar.dismiss();
            }
        });
    }

    private void setData(){
        binding.name.setText(student.getName());
        binding.registrationNumber.setText(student.getRegistrationNumber());
        binding.department.setText(student.getDepartment());
        binding.year.setText(String.valueOf(student.getYear()));

        if(student.getPhotoPath() == null){
            return;
        }
        try {
            File file = File.createTempFile("photo","jpeg");
            new StorageCtrl(this).downloadFile(student.getPhotoPath(), Uri.fromFile(file), new StorageCtrl.handleDownload() {
                @Override
                public void onSuccess() {
                    InputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(file.getPath());
                        binding.photo.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure() {

                }
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getImageFile() throws IOException {
        String currentPhotoPath = "";
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DCIM
                ), "Camera"
        );
        File file = File.createTempFile(
                imageFileName, ".jpg"
        );
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_PHOTO){
            Uri sourceUri = data.getData(); // 1
            File file = null; // 2
            try {
                file = getImageFile();
                Uri destinationUri = Uri.fromFile(file);
                UCrop.of(sourceUri, destinationUri)
                        .start(ProfileStudentActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == PICK_CARD){
            handleCardUpload(data.getData());
        }
        else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            handlePhotoUpload(resultUri);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    private void handlePhotoUpload(Uri uri){
        Snackbar snackbar = Snackbar.make(binding.getRoot(),"Uploading...",Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        new StorageCtrl(this).uploadFile(prefs.getEmail() + "/profile/photo.jpg", uri, new StorageCtrl.handleUpload() {
            @Override
            public void onSuccess() {
                snackbar.dismiss();
                Toast.makeText(ProfileStudentActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                student.setPhotoPath(prefs.getEmail() + "/profile/photo.jpg");
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(new File(uri.getPath()));
                    binding.photo.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                snackbar.dismiss();
                Toast.makeText(ProfileStudentActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleCardUpload(Uri uri){
        Snackbar snackbar = Snackbar.make(binding.getRoot(),"Uploading...",Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        new StorageCtrl(this).uploadFile(prefs.getEmail() + "/profile/card.jpg", uri, new StorageCtrl.handleUpload() {
            @Override
            public void onSuccess() {
                snackbar.dismiss();
                Toast.makeText(ProfileStudentActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                student.setAdmitCardPath(prefs.getEmail() + "/profile/card.jpg");
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(new File(uri.getPath()));
                    binding.admitCard.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                snackbar.dismiss();
                Toast.makeText(ProfileStudentActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSubmit(){
        student.setRegistrationNumber(binding.registrationNumber.getText().toString());
        student.setDepartment(binding.department.getText().toString());
        student.setYear(Integer.valueOf(binding.year.getText().toString()));

        RetrofitClient.getClient().studentUpdate(prefs.getToken(),student).enqueue(new Callback<StudentUpdateResponse>() {
            @Override
            public void onResponse(Call<StudentUpdateResponse> call, Response<StudentUpdateResponse> response) {
                Toast.makeText(ProfileStudentActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<StudentUpdateResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}