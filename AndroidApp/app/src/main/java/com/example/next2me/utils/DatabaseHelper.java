package com.example.next2me.utils;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.next2me.data.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class DatabaseHelper {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReferenceFromUrl("gs://androidapp-1620979629021.appspot.com");


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
        userTable.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
    }
}
