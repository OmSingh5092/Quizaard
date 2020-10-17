package com.andronauts.quizard.faculty.activities;

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
import com.andronauts.quizard.api.responseModels.faculty.FacultyGetResponse;
import com.andronauts.quizard.api.responseModels.faculty.FacultyUpdateResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.databinding.ActivityProfileFacultyBinding;
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

public class ProfileFacultyActivity extends AppCompatActivity {
    ActivityProfileFacultyBinding binding;

    int PICK_PHOTO =100, PICK_CARD=101;
    SharedPrefs prefs;
    Faculty faculty= new Faculty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileFacultyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        prefs = new SharedPrefs(this);

        loadData();

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


        new PermissionCtrl(this).askStoragePermission();
    }

    private void fillData(){
        binding.name.setText(faculty.getName());
        binding.facultyId.setText(faculty.getFacultyId());
        binding.department.setText(faculty.getDepartment());

        try {
            File file = File.createTempFile("photo","jpeg");
            new StorageCtrl(this).downloadFile(faculty.getPhotoPath(), Uri.fromFile(file), new StorageCtrl.handleDownload() {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadData(){
        Snackbar snackbar = Snackbar.make(binding.getRoot(),"Loading Data",Snackbar.LENGTH_INDEFINITE);
        snackbar.show();

        RetrofitClient.getClient().facultyGetProfile(prefs.getToken()).enqueue(new Callback<FacultyGetResponse>() {
            @Override
            public void onResponse(Call<FacultyGetResponse> call, Response<FacultyGetResponse> response) {
                if(response.isSuccessful()){
                    faculty = response.body().getFaculty();
                    fillData();
                    snackbar.dismiss();
                }

            }

            @Override
            public void onFailure(Call<FacultyGetResponse> call, Throwable t) {
                snackbar.dismiss();
                Toast.makeText(ProfileFacultyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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
                        .start(ProfileFacultyActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                Toast.makeText(ProfileFacultyActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                faculty.setPhotoPath(prefs.getEmail() + "/profile/photo.jpg");
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
                Toast.makeText(ProfileFacultyActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSubmit(){
        faculty.setFacultyId(binding.facultyId.getText().toString());
        faculty.setDepartment(binding.department.getText().toString());
        faculty.setName(binding.name.getText().toString());

        RetrofitClient.getClient().facultyUpdate(prefs.getToken(),faculty).enqueue(new Callback<FacultyUpdateResponse>() {
            @Override
            public void onResponse(Call<FacultyUpdateResponse> call, Response<FacultyUpdateResponse> response) {
                Toast.makeText(ProfileFacultyActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<FacultyUpdateResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}