package com.example.next2me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.next2me.utils.DatabaseHelper;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Context context;
    private List<String> names;
    private List<String> profilePic;

    public CardAdapter(Context context, List<String> names, List<String> profilePic) {
        this.context = context;
        this.names = names;
        this. profilePic = profilePic;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_layout, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.textView.setText(names.get(position));
        //holder.imageView.setImageResource(profilePic.get(position));
        holder.setImage(profilePic.get(position));

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("prova", "prova");
                }
            });
        }

        public void setImage(String profilePicId){
            StorageReference imageRef = DatabaseHelper.getInstance().getStorageRef().child("ProfilePictures/" + profilePicId + ".jpg");
            GlideApp.with(itemView)
                    .load(imageRef)
                    .into(this.imageView);
        }
    }
}