package com.example.next2me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.next2me.data.User;
import com.example.next2me.utils.UserHelper;
import com.example.next2me.utils.Utilities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        progressBar = findViewById(R.id.my_progressBar);
        progressBar.setProgress(20);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            UserHelper.getInstance().setEmail(firebaseUser.getEmail());
        }
            Utilities.insertFragment(this, new NameFragment(), "FRAGMENT_TAG");
    }

    public void increaseProgressBar(int amount){
        this.progressBar.setProgress(this.progressBar.getProgress()+amount);
    }

}