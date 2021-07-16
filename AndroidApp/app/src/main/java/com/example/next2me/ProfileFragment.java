package com.example.next2me;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.next2me.data.User;
import com.example.next2me.utils.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    private Button editButton;
    private Button logoutButton;
    private ImageView profilePic;
    private TextView nameAgeTV;
    private TextView sexTV;
    private TextView descriptionTV;
    private ImageButton gpsButton;
    private boolean isPosSharingEnabled = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
        this.editButton = view.findViewById(R.id.buttonEditProfile);
        this.logoutButton = view.findViewById(R.id.buttonLogout);
        this.editButton.setOnClickListener(v -> startActivity(new Intent(getActivity(),RegistrationActivity.class)));
        this.logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(),MainActivity.class));
        });

        this.profilePic = view.findViewById(R.id.profileImage);
        StorageReference imageRef = DatabaseHelper.getInstance().getStorageRef().child("ProfilePictures/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
        GlideApp.with(view)
                .load(imageRef)
                .circleCrop()
                .into(this.profilePic);

        nameAgeTV = view.findViewById(R.id.nameAge);
        sexTV = view.findViewById(R.id.gender);
        descriptionTV = view.findViewById(R.id.userDescription);
        gpsButton = view.findViewById(R.id.gpsButton);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPosSharingEnabled){
                    Snackbar.make(getView(), R.string.attivata_condivisione_della_posizione, Snackbar.LENGTH_SHORT)
                            .show();
                    isPosSharingEnabled = true;
                    gpsButton.getBackground().setTint(Color.BLUE);
                }else{
                    Snackbar.make(getView(), R.string.disattivata_condivisione_della_posizione, Snackbar.LENGTH_SHORT)
                            .show();
                    isPosSharingEnabled=false;
                    gpsButton.getBackground().setTint(Color.DKGRAY);

                }
            }
        });


        DatabaseHelper.getInstance().getDb().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("INFORMATIONS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            User user = snapshot.getValue(User.class);
            nameAgeTV.setText(user.getName());
            //ageTV.setText(String.valueOf(Utilities.getAge(user.getBirthdate())));
            sexTV.setText(user.getGender().equals("F") ? "Femmina" : "Maschio");
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