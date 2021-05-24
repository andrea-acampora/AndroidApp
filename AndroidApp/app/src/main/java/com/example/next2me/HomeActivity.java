package com.example.next2me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.next2me.data.User;
import com.example.next2me.utils.DatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        DatabaseHelper dbh = new DatabaseHelper();

        users = dbh.getDb().getReference("Users");

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.e("db", "name = "+ user.getName());
                Log.e("db", "name =" + user.getSurname());            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("db","Error while reading data");
            }
        });
    }
}