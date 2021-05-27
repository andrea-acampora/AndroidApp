package com.example.next2me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText emailTIET,passwordTIET;
    private Button registerBtn;
    private ProgressDialog progressDialog;
    private TextView haveAccountTV;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        emailTIET = findViewById(R.id.email_register);
        passwordTIET = findViewById(R.id.password_register);
        haveAccountTV = findViewById(R.id.have_account_tv);
        registerBtn = findViewById(R.id.button_register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");

        registerBtn.setOnClickListener(v -> {
            String email = emailTIET.getText().toString().trim();
            String password = passwordTIET.getText().toString().trim();
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailTIET.setError("Invalid Email");
                emailTIET.setFocusable(true);
            }else if(password.length()<6){
                passwordTIET.setError("Password length at least 6 characters");
                passwordTIET.setFocusable(true);
            }else{
                registerUser(email,password);
            }
        });
        haveAccountTV.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this,LoginActivity.class)));
    }

    private void registerUser(String email, String password) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user!=null){
                        startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                    }
                } else{
                    progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this,"L'email inserita è gia presente",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this,"Authentication failed.",Toast.LENGTH_SHORT).show();
                Toast.makeText(SignUpActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}