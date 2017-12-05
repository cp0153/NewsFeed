package me.cpearce.newsfeed;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText txtEmailAddress;
    private EditText txtPassword;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        txtEmailAddress = (EditText) findViewById(R.id.txtEmailRegistration);
        txtPassword = (EditText) findViewById(R.id.txtPasswordRegistration);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnRegisterUser_Click(View v){

        final ProgressDialog progressDialog = ProgressDialog.show(RegistrationActivity.this, "Please wait...", "Processing...", true);
        (firebaseAuth.createUserWithEmailAndPassword(txtEmailAddress.getText().toString(), txtPassword.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else{
                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

