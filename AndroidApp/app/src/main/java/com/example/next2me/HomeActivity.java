package com.example.next2me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.next2me.data.User;
import com.example.next2me.utils.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getUserInfo();
        updateUI();
    }

    private void getUserInfo(){
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseDatabase.getInstance().getReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db","Error while reading data");
            }
        });
    }

    private void updateUI() {
        DatabaseHelper dbh = DatabaseHelper.getInstance();
        ((ImageView)findViewById(R.id.profilePic)).setImageBitmap(dbh.getPhotoFromStorage(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()));
        ((TextView)findViewById(R.id.name)).setText(user.getName());
        ((TextView)findViewById(R.id.surname)).setText(user.getSurname());
        ((TextView)findViewById(R.id.birthdate)).setText(user.getBirthdate());
        ((TextView)findViewById(R.id.gender)).setText(user.getGender());
        ((TextView)findViewById(R.id.preferences)).setText(user.getPreferences());
        ((TextView)findViewById(R.id.mail)).setText(user.getEmail());
        ((TextView)findViewById(R.id.description)).setText(user.getDescription());
    }

}