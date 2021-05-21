package com.example.next2me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.next2me.data.User;
import com.example.next2me.utils.Utilities;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {


    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        user = new User();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            user.setEmail(firebaseUser.getEmail());
        }
            Utilities.insertFragment(this, new NameFragment(), "FRAGMENT_TAG");
    }

    public User getUser() {
        return user;
    }
}