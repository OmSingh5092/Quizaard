package com.andronauts.quizzard.utils;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;

public class SessionManager {
    Context context;
    SharedPrefs prefs;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    GoogleSignInClient mGoogleSignInClient;

    public SessionManager(Context context) {
        this.context = context;
        prefs = new SharedPrefs(context);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public interface LogoutHandler{
        void onSuccess();
        void onFailure();
    }

    public void logOutUser( LogoutHandler handler){
        //Removing FCM Topics

        //Removing User topic
        //messaging.unsubscribeFromTopic(auth.getUid());

        //Removing Box Topics

        /*for(String boxId: GlobalVar.userData.userInfo.boxes){
              messaging.unsubscribeFromTopic(boxId);
        }  */

        auth.signOut();
        mGoogleSignInClient.revokeAccess().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                prefs.clearData();
                auth.signOut();
                handler.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onFailure();
            }
        });
    }
}
