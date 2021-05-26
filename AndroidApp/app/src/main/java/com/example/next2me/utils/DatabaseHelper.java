package com.example.next2me.utils;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.next2me.data.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class DatabaseHelper {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageRef = storage.getReferenceFromUrl("gs://androidapp-1620979629021.appspot.com");


    private static DatabaseHelper instance = null;
    private DatabaseHelper(){}

    public static DatabaseHelper getInstance(){
        if(instance == null){
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public void addPhotoToStorage(Bitmap imageBitmap, String userId){
       StorageReference imageRef = storageRef.child("ProfilePictures/" + userId + ".jpg");
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
       byte[] data = baos.toByteArray();
       UploadTask uploadTask = imageRef.putBytes(data);
       uploadTask.addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Log.e("image", e.getMessage());
           }
       }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               Log.d("image", "image upload success");
           }
       });
    }


    public void addUserToDB(User user){
        DatabaseReference userTable = db.getReference("Users");
        userTable.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("INFORMATIONS").setValue(user);
    }

    public void SendUserPositionToDB(LatLng userPos){
        DatabaseReference userTable = db.getReference("Users");
        userTable.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("POS").setValue(userPos);
    }



    public FirebaseDatabase getDb() {
        return db;
    }

    public StorageReference getStorageRef() {
        return storageRef;
    }
}
