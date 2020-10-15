package com.andronauts.quizard.general.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andronauts.quizard.R;
import com.andronauts.quizard.api.responseModels.signIn.GoogleSignInResponse;
import com.andronauts.quizard.api.retrofit.RetrofitClient;
import com.andronauts.quizard.databinding.ActivityLoginBinding;
import com.andronauts.quizard.faculty.activities.HomeFacultyActivity;
import com.andronauts.quizard.faculty.activities.RegisterFacultyActivity;
import com.andronauts.quizard.students.activities.HomeStudentActivity;
import com.andronauts.quizard.students.activities.RegisterStudentActivity;
import com.andronauts.quizard.utils.PermissionCtrl;
import com.andronauts.quizard.utils.SharedPrefs;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    GoogleSignInClient client;
    FirebaseAuth mAuth;
    SharedPrefs sharedPrefs;

    Boolean isStudent = true;
    int RC_SIGN_IN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefs = new SharedPrefs(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        client= GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        binding.faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStudent = false;
                signIn();
            }
        });

        binding.student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStudent = true;
                signIn();
            }
        });

        new PermissionCtrl(this).askStoragePermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            userSignIn(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Google Authentication Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void userSignIn(GoogleSignInAccount account){
        //Add code to submit user info in the database
        Map<String,String> map = new HashMap<>();
        map.put("idToken",account.getIdToken());
        if(isStudent){
            RetrofitClient.getClient().studentGoogleSignIn(map).enqueue(new Callback<GoogleSignInResponse>() {
                @Override
                public void onResponse(Call<GoogleSignInResponse> call, Response<GoogleSignInResponse> response) {

                    sharedPrefs.saveName(account.getDisplayName());
                    sharedPrefs.saveEmail(account.getEmail());
                    sharedPrefs.saveNewUser(response.body().isNewUser());
                    sharedPrefs.saveToken(response.body().getAuthToken());
                    sharedPrefs.saveUserType(isStudent);
                    firebaseAuthWithGoogle(account.getIdToken());

                    Log.i("Token",response.body().getAuthToken());
                }

                @Override
                public void onFailure(Call<GoogleSignInResponse> call, Throwable t) {

                }
            });
        }else{
            RetrofitClient.getClient().facultyGoogleSignIn(map).enqueue(new Callback<GoogleSignInResponse>() {
                @Override
                public void onResponse(Call<GoogleSignInResponse> call, Response<GoogleSignInResponse> response) {

                    sharedPrefs.saveName(account.getDisplayName());
                    sharedPrefs.saveEmail(account.getEmail());
                    sharedPrefs.saveNewUser(response.body().isNewUser());
                    sharedPrefs.saveToken(response.body().getAuthToken());
                    sharedPrefs.saveUserType(!isStudent);
                    firebaseAuthWithGoogle(account.getIdToken());

                    Log.i("Token",response.body().getAuthToken());
                }

                @Override
                public void onFailure(Call<GoogleSignInResponse> call, Throwable t) {

                }
            });
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            if(sharedPrefs.getNewUser()){
                                goToOnBoarding();
                            }else{
                                goToHome();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void goToOnBoarding(){
        if(isStudent){
            Intent i = new Intent(this, RegisterStudentActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }else{
            Intent i = new Intent(this, RegisterFacultyActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

    }

    private  void goToHome(){
        if(isStudent){
            Intent i = new Intent(this, HomeStudentActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }else{
            Intent i = new Intent(this, HomeFacultyActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

    }


}