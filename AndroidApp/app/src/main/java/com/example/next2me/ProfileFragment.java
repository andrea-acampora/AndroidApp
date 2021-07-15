package com.example.next2me;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.next2me.data.User;
import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.utils.Utilities;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    private Button editButton;
    private Button logoutButton;
    private ImageView profilePic;
    private TextView nameTV;
    private TextView ageTV;
    private TextView sexTV;
    private TextView descriptionTV;
    private ShapeableImageView sharingPosImageView;
    private boolean isPosSharingEnabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.editButton = view.findViewById(R.id.fragmet_profile_button_edit);
        this.logoutButton = view.findViewById(R.id.fragment_profile_button_logout);
        this.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),RegistrationActivity.class));
            }
        });
        this.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });

        this.profilePic = view.findViewById(R.id.profile_pic_fragment_profile);
        StorageReference imageRef = DatabaseHelper.getInstance().getStorageRef().child("ProfilePictures/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
        GlideApp.with(view)
                .load(imageRef)
                .into(this.profilePic);

        nameTV = view.findViewById(R.id.fragmet_profile_name_user);
        ageTV = view.findViewById(R.id.fragmet_profile_age_user);
        sexTV = view.findViewById(R.id.fragmet_profile_sex_user);
        descriptionTV = view.findViewById(R.id.fragmet_profile_description_user);
        sharingPosImageView = view.findViewById(R.id.sharePos);
        sharingPosImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPosSharingEnabled==false){
                    isPosSharingEnabled = true;
                }else{
                    isPosSharingEnabled=false;
                }
            }
        });


        DatabaseHelper.getInstance().getDb().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("INFORMATIONS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            User user = snapshot.getValue(User.class);
            nameTV.setText(user.getName());
            ageTV.setText(String.valueOf(Utilities.getAge(user.getBirthdate())));
            sexTV.setText(user.getGender());
            descriptionTV.setText(user.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public boolean isPosSharingEnabled() {
        return isPosSharingEnabled;
    }
}