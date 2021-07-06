package com.example.next2me;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.next2me.data.MatchRequest;

import java.util.ArrayList;
import java.util.List;

public class MatchRequestAdapter extends RecyclerView.Adapter<MatchRequestViewHolder> {

    private List<MatchRequest> matchRequestList;
    private Activity activity;

    public MatchRequestAdapter(Activity activity, List<MatchRequest> matchRequestList){
        this.activity = activity;
        this.matchRequestList = new ArrayList<>(matchRequestList);
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
        holder.profilePic.setImageBitmap(matchRequest.getProfilePic());

    }

    @Override
    public int getItemCount() {
        return matchRequestList.size();
    }
}
