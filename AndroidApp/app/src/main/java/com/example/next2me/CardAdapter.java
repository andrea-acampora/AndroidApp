package com.example.next2me;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.viewmodel.CardItem;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private Context context;
    private List<CardItem> cardItems;
    private OnItemListener listener;

    public CardAdapter(Context context, List<CardItem> cardItems, OnItemListener lister) {
        this.context = context;
        this.cardItems = cardItems;
        this.listener = lister;
    }

    public CardItem getItem(int position){
        return this.cardItems.get(position);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_layout, parent, false);
        return new CardViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        String name = cardItems.get(position).getName();
        String nameUpp = name.substring(0, 1).toUpperCase() + name.substring(1);
        holder.username.setText(nameUpp + ", " + getAge(cardItems.get(position).getBirthday()));
        //holder.imageView.setImageResource(profilePic.get(position));
        holder.setImage(cardItems.get(position).getId());
    }

    public void setData(List<CardItem> list) {
        this.cardItems = new ArrayList<>(list);
        notifyDataSetChanged();
    }




    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView userPic;
        TextView username;
        private DashboardFragment dashboardFragment = new DashboardFragment();
        private OnItemListener itemListener;


        public CardViewHolder(@NonNull View itemView, OnItemListener listener) {
            super(itemView);
            userPic = itemView.findViewById(R.id.userPic);
            username = itemView.findViewById(R.id.username);

            itemListener = listener;

            itemView.setOnClickListener(this);
        }

        public void setImage(String profilePicId){
            StorageReference imageRef = DatabaseHelper.getInstance().getStorageRef().child("ProfilePictures/" + profilePicId + ".jpg");
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
            GlideApp.with(itemView)
                    .load(imageRef)
                    .apply(requestOptions)
                    .into(this.userPic);
        }

        @Override
        public void onClick(View v) {
            itemListener.onItemClick(getAdapterPosition());
        }
    }

    private String getAge(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}