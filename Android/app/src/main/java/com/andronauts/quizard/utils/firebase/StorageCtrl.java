package com.andronauts.quizard.utils.firebase;

import android.content.Context;
import android.net.Uri;

import com.andronauts.quizard.utils.SharedPrefs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;

public class StorageCtrl {
    Context context;
    SharedPrefs prefs;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    public StorageCtrl(Context context) {
        this.context = context;
        prefs = new SharedPrefs(context);
    }

    public interface handleUpload{
        void onSuccess();
        void onFailure();
    }

    public interface handleDownload{
        void onSuccess();
        void onFailure();
    }

    public void uploadFile(String path, Uri uri, handleUpload handler){
        storage.getReference(path).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                handler.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onFailure();
            }
        });
    }

    public void downloadFile(String path, Uri uri, handleDownload handler){

    }
}
