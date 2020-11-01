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

import com.andronauts.quizard.api.responseModels.faculty.FacultyUpdateResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.dataModels.Faculty;
import com.andronauts.quizard.databinding.ActivityRegisterFacultyBinding;
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

public class RegisterFacultyActivity extends AppCompatActivity {
    ActivityRegisterFacultyBinding binding;

    int PICK_PHOTO =100, PICK_CARD=101;
    SharedPrefs prefs;
    Faculty faculty= new Faculty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterFacultyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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


        new PermissionCtrl(this).askStoragePermission();

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
                        .start(RegisterFacultyActivity.this);
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
                Toast.makeText(RegisterFacultyActivity.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegisterFacultyActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate(){
        if(faculty.getFacultyId().equals("")){
            Toast.makeText(this, "Please enter faculty ID", Toast.LENGTH_SHORT).show();
            return false;
        }else if(faculty.getName().equals("")){
            Toast.makeText(this, "Please enter faculty name", Toast.LENGTH_SHORT).show();
            return false;
        }else if(faculty.getDepartment().equals("")){
            Toast.makeText(this, "Please enter a department", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void handleSubmit(){
        faculty.setFacultyId(binding.facultyId.getText().toString());
        faculty.setDepartment(binding.department.getText().toString());
        faculty.setName(binding.name.getText().toString());

        if(!validate()){
            return;
        }

        RetrofitClient.getClient().facultyUpdate(prefs.getToken(),faculty).enqueue(new Callback<FacultyUpdateResponse>() {
            @Override
            public void onResponse(Call<FacultyUpdateResponse> call, Response<FacultyUpdateResponse> response) {
                Toast.makeText(RegisterFacultyActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                goToStudentHome();
            }

            @Override
            public void onFailure(Call<FacultyUpdateResponse> call, Throwable t) {

            }
        });

    }

    private void goToStudentHome(){
        prefs.saveNewUser(false);
        Intent i = new Intent(this, HomeFacultyActivity.class);
        startActivity(i);
        finish();
    }
}