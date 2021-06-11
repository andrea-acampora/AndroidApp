package com.example.next2me;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.utils.UserHelper;
import com.example.next2me.utils.Utilities;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import static android.app.Activity.RESULT_OK;

@SuppressWarnings("ALL")
public class PhotoFragment extends Fragment {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GET_SINGLE_FILE = 2;
    ImageView profilePic;
    Button takePhoto;
    Button galleryPhoto;
    Button btnContinua;
    Bitmap bm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profilePic = view.findViewById(R.id.profilePic);
        final RegistrationActivity activity = (RegistrationActivity) getActivity();
        if(activity != null){
            activity.increaseProgressBar(20);
            takePhoto = view.findViewById(R.id.camera);
            galleryPhoto = view.findViewById(R.id.galleria);
            btnContinua = view.findViewById(R.id.buttonContinua);
            btnContinua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profilePic.setDrawingCacheEnabled(true);
                    profilePic.buildDrawingCache();
                    Bitmap bitmap = ((BitmapDrawable) profilePic.getDrawable()).getBitmap();
                    UserHelper.getInstance().setProfilePic(bitmap);
                    DatabaseHelper dbh = DatabaseHelper.getInstance();
                    dbh.addPhotoToStorage(UserHelper.getInstance().getProfilePic(), FirebaseAuth.getInstance().getCurrentUser().getUid());
                    dbh.addUserToDB(UserHelper.getInstance().getAppUser());
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                }
            });

            takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePictureIntent();
                }
            });

            galleryPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePictureFromGallery();
                }
            });


        }
    }

    private void takePictureFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select picture"), REQUEST_GET_SINGLE_FILE);
    }

    private void takePictureIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }catch (ActivityNotFoundException e){
            Log.e("camera", "error");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Do other work with full size photo saved in locationForPhotos
            Bundle extras = data.getExtras();
            bm = (Bitmap) extras.get("data");
            profilePic.setImageBitmap(bm);
        }

        if(data != null && requestCode == REQUEST_GET_SINGLE_FILE){
            try {
                // Uri targetUri = data.getData();
                // bm = BitmapFactory.decodeStream(getContext().getContentResolver())
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profilePic.setImageBitmap(bm);
    }
}