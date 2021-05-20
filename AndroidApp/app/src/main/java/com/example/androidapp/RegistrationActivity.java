package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.androidapp.data.User;
import com.example.androidapp.utils.Utilities;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class RegistrationActivity extends AppCompatActivity {


    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        user = new User();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            user.setEmail(account.getEmail());
        }
            Utilities.insertFragment(this, new NameFragment(), "FRAGMENT_TAG");
    }

    public User getUser(){
        return user;
    }
}