package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.androidapp.Data.User;

public class RegistrationActivity extends AppCompatActivity {


    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        user = new User();

        Utilities.insertFragment(this, new NameFragment(), "FRAGMENT_TAG");
    }

    public User getUser(){
        return user;
    }
}