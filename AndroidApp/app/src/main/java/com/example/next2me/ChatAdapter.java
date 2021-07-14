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
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.example.next2me.models.MessageWrapper;

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
        FrameLayout frameLayout;
        //TextView unreadBubble;
        TextView lastMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            chatName = itemView.findViewById(R.id.dialogName);
            frameLayout = itemView.findViewById(R.id.dialogRootLayout);
          //  unreadBubble = itemView.findViewById(R.id.dialogUnreadBubble);
            lastMessage = itemView.findViewById(R.id.dialogLastMessage);
        }

        public void bind(Conversation conversation){

            User receiver = (User) conversation.getConversationWith();
            String receiverId = receiver.getUid();
            Log.d("chat", "receiverId = " + receiverId);
            chatName.setText(receiver.getName());
         //   unreadBubble.setText(conversation.getUnreadMessageCount());
            lastMessage.setText(new MessageWrapper((TextMessage)conversation.getLastMessage()).getText());
            frameLayout.setOnClickListener(itemView -> SingleChatActivity.start(context, receiverId));
        }
    }
}
