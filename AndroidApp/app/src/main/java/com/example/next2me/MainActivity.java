package com.example.next2me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.next2me.data.User;
import com.example.next2me.utils.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userTable = DatabaseHelper.getInstance().getDb().getReference("Users");
            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            userTable.child(uid).child("INFORMATIONS").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean isUserRegistered = dataSnapshot.exists();
                    if(!isUserRegistered){
                        startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                    }else{
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("db","Error while reading data");
                }
            });

        } else {
            setContentView(R.layout.activity_main);

            Button loginButton = findViewById(R.id.login_button);
            Button registerButton = findViewById(R.id.register_button);

            loginButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
            registerButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignUpActivity.class)));
        }
    }
}