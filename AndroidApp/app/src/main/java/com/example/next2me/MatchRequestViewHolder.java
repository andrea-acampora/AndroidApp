package com.example.next2me;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MatchRequestViewHolder extends RecyclerView.ViewHolder {

    ImageView profilePic;
    TextView name;
    TextView age;
    ImageButton accept;
    ImageButton decline;


    public MatchRequestViewHolder(@NonNull View itemView) {
        super(itemView);
        this.profilePic = itemView.findViewById(R.id.match_request_profile_pic);
        this.name = itemView.findViewById(R.id.match_request_name);
        this.age = itemView.findViewById(R.id.match_request_age);
        this.accept = itemView.findViewById(R.id.match_request_accept);
        this.decline = itemView.findViewById(R.id.match_request_decline);
    }
}
