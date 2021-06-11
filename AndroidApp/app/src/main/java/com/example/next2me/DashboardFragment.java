package com.example.next2me;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.next2me.utils.DatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<String> names;
    private List<Integer> profilePic;
    private CardAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        names = new ArrayList<>();
        profilePic = new ArrayList<>();
        adapter = new CardAdapter(getActivity(), names, profilePic);

        profilePic.add(R.drawable.female);
        profilePic.add(R.drawable.male);
        names.add("giacomo");
        names.add("andrea");
        profilePic.add(R.drawable.female);
        profilePic.add(R.drawable.male);
        names.add("marco");
        names.add("antonio");
        profilePic.add(R.drawable.female);
        profilePic.add(R.drawable.male);
        names.add("francesco");
        names.add("massimo");
        profilePic.add(R.drawable.female);
        profilePic.add(R.drawable.male);
        names.add("giacomo");
        names.add("andrea");
        profilePic.add(R.drawable.female);
        profilePic.add(R.drawable.male);
        names.add("marco");
        names.add("antonio");
        profilePic.add(R.drawable.female);
        profilePic.add(R.drawable.male);
        names.add("francesco");
        names.add("massimo");


        /*DatabaseReference userTable = DatabaseHelper.getInstance().getDb().getReference("Users");
        userTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot user : dataSnapshot.getChildren()){
                    Log.d("users", user.child("name").getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("db","Error while reading data");
            }
        });*/

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}