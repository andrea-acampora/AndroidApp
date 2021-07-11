package com.example.next2me;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.viewmodel.CardItem;
import com.example.next2me.viewmodel.ListViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class DetailsFragment extends Fragment {

    private TextView name;
    private TextView descrizione;
    private TextView genere;
    private ImageView profilePic;
    private ImageButton matchRequest;
    private ListViewModel listViewModel;
    private TextView eta;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.name = view.findViewById(R.id.nome);
        this.profilePic = view.findViewById(R.id.profile);
        this.matchRequest = view.findViewById(R.id.matchRequest);
        this.descrizione = view.findViewById(R.id.descrizione);
        this.genere = view.findViewById(R.id.genere);
        this.eta = view.findViewById(R.id.eta);


        Activity activity = getActivity();
        if (activity != null) {
            listViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(ListViewModel.class);
            listViewModel.getSelected().observe(getViewLifecycleOwner(), new Observer<CardItem>() {
                @Override
                public void onChanged(CardItem cardItem) {
                    name.setText(cardItem.getName());

                    DatabaseReference reference = DatabaseHelper.getInstance().getDb().getReference("Users").child(cardItem.getId()).child("INFORMATIONS");
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String descrizione = snapshot.child("description").getValue().toString();
                            String genere = snapshot.child("gender").getValue().toString();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    setImage(cardItem.getId());
                }
            });
        }

        this.matchRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.getInstance().getDb().getReference("Users")
                        .child(listViewModel.getSelected().getValue().getId())
                        .child("MATCHES").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("pending");
            }
        });
    }

    private void setImage(String profilePicId) {
        StorageReference imageRef = DatabaseHelper.getInstance().getStorageRef().child("ProfilePictures/" + profilePicId + ".jpg");
        GlideApp.with(getView())
                .load(imageRef)
                .into(this.profilePic);
    }
}