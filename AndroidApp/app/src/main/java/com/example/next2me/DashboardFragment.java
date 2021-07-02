package com.example.next2me;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.next2me.utils.DatabaseHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<String> names;
    private List<String> profilePic;
    private CardAdapter adapter;

    private final int LOCATION_PERMISSION_CODE = 1;
    LocationManager locationManager;
    private LatLng currentUserPos = new LatLng(44.0575500,12.5652800);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("frag", "created fragment");
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        names = new ArrayList<>();
        profilePic = new ArrayList<>();
        adapter = new CardAdapter(getActivity(), names, profilePic);

        DatabaseReference userTable = DatabaseHelper.getInstance().getDb().getReference("Users");
        userTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()){
                    try {
                        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
                        } else {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, location -> {
                                currentUserPos = new LatLng(location.getLatitude(), location.getLongitude());
                            });
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    LatLng otherUserLoc = new LatLng((double)user.child("POS").child("lat").getValue(),(double)user.child("POS").child("long").getValue());
                    double distance = SphericalUtil.computeDistanceBetween(currentUserPos, otherUserLoc);
                    if (distance < 5000){
                        names.add(user.child("INFORMATIONS").child("name").getValue().toString());
                        profilePic.add(user.getKey());
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db","Error while reading data");
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);
        return view;

    }

}