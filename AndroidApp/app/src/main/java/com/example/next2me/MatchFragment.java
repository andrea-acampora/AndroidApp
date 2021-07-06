package com.example.next2me;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.next2me.data.MatchRequest;
import com.example.next2me.utils.DatabaseHelper;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class MatchFragment extends Fragment {

    private MatchRequestAdapter adapter;
    private RecyclerView recyclerView;
    private List<MatchRequest> matches_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null){
            setRecyclerView(activity);
        }

        DatabaseReference matchesTable = DatabaseHelper.getInstance().getDb().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MATCHES").child("PENDING");
        matchesTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    MatchRequest match = data.getValue(MatchRequest.class);
                    matches_list.add(match);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db","Error while reading data");
            }
        });
    }

    private void setRecyclerView(final Activity activity) {
        recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        matches_list = new ArrayList<>();
        adapter = new MatchRequestAdapter(activity,matches_list);
        recyclerView.setAdapter(adapter);
    }
}