package com.example.next2me;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.example.next2me.models.DefaultDialog;
import com.example.next2me.models.MessageWrapper;
import com.example.next2me.models.UserChatWrapper;
import com.example.next2me.utils.DatabaseHelper;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.List;


public class ChatListFragment extends Fragment {

    DialogsListAdapter dialogsListAdapter;

    public ChatListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        addAdapter();
        getChatList();
        addListener();
    }

    private void addAdapter() {
        dialogsListAdapter = new DialogsListAdapter((imageView, url, payload) -> {
            Picasso.get().load(url).into(imageView);
        });

        DialogsList dialogsList = getView().findViewById(R.id.dialogsList);
        dialogsList.setAdapter(dialogsListAdapter);

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
        Log.d("chat", "conv num = " + conversations.size());
        for(Conversation conversation : conversations){
            Gson g = new Gson();
            User receiver = (User) conversation.getConversationWith();
            List<IUser> users = new ArrayList<>();
            users.add(new UserChatWrapper(CometChat.getLoggedInUser()));
            users.add(new UserChatWrapper(receiver));
            Log.d("chat", "user app entity = " + receiver.getName());
            dialogsListAdapter.addItem(new DefaultDialog(conversation.getConversationId(), receiver.getAvatar(), receiver.getName(), users, new MessageWrapper((TextMessage)conversation.getLastMessage()), conversation.getUnreadMessageCount(), receiver.getUid()));
        }
    }

    private void addListener() {
        dialogsListAdapter.setOnDialogClickListener((DialogsListAdapter.OnDialogClickListener<IDialog>) dialog -> {

            SingleChatActivity.start(getContext(), ((DefaultDialog)dialog).getReceiverId());
        });
    }
}