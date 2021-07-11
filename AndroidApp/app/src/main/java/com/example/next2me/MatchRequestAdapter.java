package com.example.next2me;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.next2me.data.MatchRequest;
import com.example.next2me.service.Data;
import com.example.next2me.service.NotificationsHelper;
import com.example.next2me.utils.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchRequestAdapter extends RecyclerView.Adapter<MatchRequestViewHolder> {

    private List<MatchRequest> matchRequestList;
    private Activity activity;

    public MatchRequestAdapter(Activity activity){
        this.activity = activity;
        this.matchRequestList = new ArrayList<>();
    }

    public void addData(List<MatchRequest> requestList){
        this.matchRequestList = requestList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MatchRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.matchrequest_layout, parent, false);
        return new MatchRequestViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchRequestViewHolder holder, int position) {
        MatchRequest matchRequest = matchRequestList.get(position);
        holder.name.setText(matchRequest.getName());
        holder.age.setText(matchRequest.getAge());
        holder.setImage(matchRequest.getUid());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.getInstance().getDb().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("MATCHES").child(matchRequest.getUid()).setValue("accepted");

                DatabaseHelper.getInstance().getDb().getReference("Users")
                        .child(matchRequest.getUid())
                        .child("MATCHES").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("accepted");


                DatabaseHelper.getInstance().getDb().getReference("Users")
                                                                .child(matchRequest.getUid())
                                                                .child("NOTIFICATIONS").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String tokenId = snapshot.child("token-id").getValue().toString();
                        if(tokenId.length() > 0 ){
                            NotificationsHelper.getInstance().sendNotifications(tokenId,"Richiesta accettata!","La tua richiesta di amicizia è stata accettata!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }

        });


        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper.getInstance().getDb().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("MATCHES").child(matchRequest.getUid()).removeValue();
            }
        });
    }



    @Override
    public int getItemCount() {
        return matchRequestList.size();
    }
}
