package com.example.next2me;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.AppEntity;
import com.cometchat.pro.models.Conversation;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Conversation> conversations;
    private Context context;

    public ChatAdapter(List<Conversation> conversations, Context context) {
        this.conversations = conversations;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(conversations.get(position));
    }
    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public List<Conversation> getConversations(){
        return this.conversations;
    }



    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView chatName;
        FrameLayout containerLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatName = itemView.findViewById(R.id.chatNameTV);
            containerLayout = itemView.findViewById(R.id.containerLayout);
        }

        public void bind(Conversation conversation){
            chatName.setText(conversation.getConversationId());
            String convId = conversation.getConversationId();
            String[] parts = convId.split("_");
            String receiverId;
            if(parts[2].equals(CometChat.getLoggedInUser().getUid())){
                receiverId = parts[0];
            }else{
                receiverId = parts[2];
            }
            Log.d("chat", "conv with = " + receiverId);
            containerLayout.setOnClickListener(itemView -> SingleChatActivity.start(context, receiverId));
        }
    }
}
