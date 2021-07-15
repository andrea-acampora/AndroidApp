package com.example.next2me;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.next2me.data.MatchRequest;
import com.example.next2me.data.User;
import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.utils.Utilities;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.USB_SERVICE;

public class MatchFragment extends Fragment {

    private MatchRequestAdapter adapter;
    private RecyclerView recyclerView;


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
        this.showMatchesRequests();

    }

    private void setRecyclerView(final Activity activity) {
        recyclerView = getView().findViewById(R.id.match_request_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new MatchRequestAdapter(activity);
        recyclerView.setAdapter(adapter);
    }

    private void addToMatchesList(List<MatchRequest> matchRequestList){
        adapter.addData(matchRequestList);
    }

    private void showMatchesRequests(){
        DatabaseReference matchesTable = DatabaseHelper.getInstance().getDb().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MATCHES");
        matchesTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<MatchRequest> matches_list = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    if(data.getValue().toString().equals("pending")){
                        Log.d("notif-match","here");
                        MatchRequest request = new MatchRequest();
                        request.setUid(data.getKey());
                        DatabaseHelper.getInstance().getDb().getReference("Users").child(data.getKey()).child("INFORMATIONS").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                request.setName(snapshot.child("name").getValue().toString()+",");
                                request.setAge(String.valueOf(Utilities.getAge(snapshot.child("birthdate").getValue().toString())));
                                matches_list.add(request);
                                addToMatchesList(matches_list);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db","Error while reading data");
            }
        });
    }

}