package com.example.next2me;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.User;
import com.example.next2me.utils.ChatConstants;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        getChatList();

        Button button = getView().findViewById(R.id.butt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChatActivity.start(getContext(), "djx5yqtt3cyrcbnxclphww5b22r2");
            }
        });
    }



    private void initViews() {

        if (CometChat.getLoggedInUser() == null) {
            CometChat.login(FirebaseAuth.getInstance().getCurrentUser().getUid(), ChatConstants.API_KEY, new CometChat.CallbackListener<User>() {

                @Override
                public void onSuccess(User user) {
                }

                @Override
                public void onError(CometChatException e) {
                }
            });
        }
    }

    private void getChatList() {
        ConversationsRequest conversationRequest = new ConversationsRequest
                .ConversationsRequestBuilder()
                .build();

        Log.d("maps", "fuori dalla lista");
        conversationRequest.fetchNext(new CometChat.CallbackListener<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                Log.d("chat", "entrato nella lista");
                updateUI(conversations);

            }

            @Override
            public void onError(CometChatException e) {
                // Hanlde failure
            }
        });
    }

    private void updateUI(List<Conversation> conversations) {
        RecyclerView chatList = getView().findViewById(R.id.chatsListRV);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));
        ChatAdapter chatAdapter = new ChatAdapter(conversations, getContext());
        chatList.setAdapter(chatAdapter);
        Log.d("chat", String.valueOf("null = " + chatList.getAdapter() == null));

    }


}