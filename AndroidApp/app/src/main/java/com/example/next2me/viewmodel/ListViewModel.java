package com.example.next2me.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.next2me.utils.DatabaseHelper;
import com.facebook.internal.BoltsMeasurementEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class ListViewModel extends AndroidViewModel {

    private final MutableLiveData<List<CardItem>> cardItems = new MutableLiveData<>();
    private final MutableLiveData<CardItem> itemSelected = new MutableLiveData<>();

    public ListViewModel(@NonNull Application application) {
        super(application);
    }

    private List<CardItem> getUsers(LatLng currentUserPos){
        List<CardItem> users = new ArrayList<>();

        DatabaseReference userTable = DatabaseHelper.getInstance().getDb().getReference("Users");
        userTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentUserPreferences = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("INFORMATIONS").child("preferences").getValue().toString();
                String currentUserGender = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("INFORMATIONS").child("gender").getValue().toString();
                for (DataSnapshot user : dataSnapshot.getChildren()){
                    if(user.hasChild("INFORMATIONS")){
                        if(!user.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            String userPreferences = user.child("INFORMATIONS").child("preferences").getValue().toString();
                            String userGender = user.child("INFORMATIONS").child("gender").getValue().toString();
                            boolean possibleMatch = (currentUserPreferences.contains( userGender) && userPreferences.contains(currentUserGender));
                            if(possibleMatch){
                                if(user.hasChild("POS")){
                                    LatLng otherUserLoc = new LatLng((double)user.child("POS").child("lat").getValue(),(double)user.child("POS").child("long").getValue());
                                    double distance = SphericalUtil.computeDistanceBetween(currentUserPos, otherUserLoc);
                                    if (distance < 5000){
                                        users.add(new CardItem(user.child("INFORMATIONS").child("name").getValue().toString(), user.getKey(), user.child("INFORMATIONS").child("birthdate").getValue().toString(), distance/1000));
                                    }
                                }
                            }
                        }
                    }
                }
                addUsersToList(users);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db","Error while reading data");
            }
        });
        return users;
    }

    public void select(CardItem cardItem) {
        itemSelected.setValue(cardItem);
    }

    public LiveData<CardItem> getSelected() {
        return itemSelected;
    }

    public MutableLiveData<List<CardItem>> getItems(LatLng currentPosition){
        cardItems.setValue(getUsers(currentPosition));
        return cardItems;
    }

    private void addUsersToList(List<CardItem> user){
        cardItems.setValue(user);
    }
}
