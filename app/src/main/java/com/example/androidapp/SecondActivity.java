package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Users;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Amplify.DataStore.query(
                Users.class,
                items -> {
                    while (items.hasNext()) {
                        Users item = items.next();
                        Log.i("Amplify", "Id " + item.getNome());
                    }
                },
                failure -> Log.e("Amplify", "Could not query DataStore", failure)
        );
    }
}