package com.example.next2me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.next2me.data.User;
import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.utils.Utilities;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        updateUI();
    }

    public void updateUI(){
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference userTable = DatabaseHelper.getInstance().getDb().getReference("Users");
        userTable.child(uid).child("INFORMATIONS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                StorageReference imageRef = DatabaseHelper.getInstance().getStorageRef().child("ProfilePictures/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
                final long ONE_MEGABYTE = 1024 * 1024;
                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        ((ImageView)findViewById(R.id.profilePicture)).setImageBitmap(Utilities.byteToBitmap(bytes));

                    }
                });

                ((TextView)findViewById(R.id.name)).setText(user.getName());
                ((TextView)findViewById(R.id.surname)).setText(user.getSurname());
                ((TextView)findViewById(R.id.birthdate)).setText(user.getBirthdate());
                ((TextView)findViewById(R.id.gender)).setText(user.getGender());
                ((TextView)findViewById(R.id.preferences)).setText(user.getPreferences());
                ((TextView)findViewById(R.id.mail)).setText(user.getEmail());
                ((TextView)findViewById(R.id.description)).setText(user.getDescription());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db","Error while reading data");
            }
        });
    }
}