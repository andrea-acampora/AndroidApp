package com.example.next2me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //query a firebase per vedere se c'è l email nel db, se non c'è faccio partire activity per la registrazione, altrimenti lo mando alla home
            startActivity(new Intent(this, RegistrationActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}