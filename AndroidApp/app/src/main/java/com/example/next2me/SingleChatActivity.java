package com.example.next2me;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.example.next2me.models.MessageWrapper;
import com.example.next2me.utils.ChatConstants;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.jivesoftware.smack.chat.Chat;

import java.util.ArrayList;
import java.util.List;

public class SingleChatActivity extends AppCompatActivity {

    private String receiverId;
    private MessagesListAdapter<IMessage>   adapter;

    public static void start(Context context, String receiverId){
        Intent starter = new Intent(context, SingleChatActivity.class);
        starter.putExtra(ChatConstants.CONV_ID, receiverId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
        Intent intent = getIntent();
        if (intent != null){
            receiverId = intent.getStringExtra(ChatConstants.CONV_ID);
        }

        Log.d("chat" , "rec id = " + receiverId);
        initViews();
        addListener(); 
        fetchPreviousMessages();
    }

    private void fetchPreviousMessages() {

        /*int latestId = CometChat.getLastDeliveredMessageId();
        MessagesRequest messagesRequest = new MessagesRequest.MessagesRequestBuilder().build();

        messagesRequest.fetchNext(new CometChat.CallbackListener<List<BaseMessage>>() {
            @Override
            public void onSuccess(List<BaseMessage> messages) {
                List<IMessage> list = new ArrayList<>();
                for (BaseMessage message: messages) {
                    Log.d("chat", "mess = " + message.toString());
                    if (message instanceof TextMessage) {
                        list.add(new MessageWrapper((TextMessage) message));
                    } else if (message instanceof MediaMessage) {

                    }
                }
                adapter.addToEnd(list, true);
            }
            @Override
            public void onError(CometChatException e) {
            }
        });


        MessagesRequest messagesRequest2 = new MessagesRequest.MessagesRequestBuilder().setUID(CometChat.getLoggedInUser().getUid()).setUnread(true).build();

        messagesRequest2.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
            @Override
            public void onSuccess(List <BaseMessage> messages) {
                List<IMessage> list2 = new ArrayList<>();
                for (BaseMessage message: messages) {
                    Log.d("chat", "mess = " + message.toString());
                    if (message instanceof TextMessage) {
                        list2.add(new MessageWrapper((TextMessage) message));
                    } else if (message instanceof MediaMessage) {

                    }
                }
                adapter.addToEnd(list2, true);
            }
            @Override
            public void onError(CometChatException e) {
                Log.d("chat", "Message fetching failed with exception: " + e.getMessage());
            }
        });*/

        MessagesRequest messagesRequest = new MessagesRequest.MessagesRequestBuilder().setUID(receiverId).build();
        messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
            @Override
            public void onSuccess(List<BaseMessage> messages) {
                List<IMessage> list = new ArrayList<>();
                for (BaseMessage message : messages) {
                    Log.d("chat", "mess = " + message.toString());
                    if (message instanceof TextMessage) {
                        list.add(new MessageWrapper((TextMessage) message));
                    }
                }
            }
            @Override
            public void onError(CometChatException e){ }
            });

    }

    private void addListener() {
        String listenerID = "listener  1";

        CometChat.addMessageListener(listenerID, new CometChat.MessageListener() {
            @Override
            public void onTextMessageReceived(TextMessage textMessage) {
                addMessage(textMessage);
            }
            @Override
            public void onMediaMessageReceived(MediaMessage mediaMessage) {
            }
            @Override
            public void onCustomMessageReceived(CustomMessage customMessage) {
            }
        });
    }

    private void initViews() {
        MessageInput inputView = findViewById(R.id.input);
        MessagesList messagesList = findViewById(R.id.messagesList);

        /*CometChat.getConversation(receiverId, CometChatConstants.CONVERSATION_TYPE_USER, new CometChat.CallbackListener<Conversation>() {
            @Override
            public void onSuccess(Conversation conversation) {
            }

            @Override
            public void onError(CometChatException e) {

            }
        });*/

        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                //validate and send message
                sendMessage(input.toString()); 
                return true;
            }
        });
        String senderId = CometChat.getLoggedInUser().getUid();
        ImageLoader imageLoader = (imageView, url, payload) -> Picasso.get().load(url).into(imageView);

        adapter = new MessagesListAdapter<>(senderId, imageLoader);
        messagesList.setAdapter(adapter);

    }

    private void sendMessage(String message) {
        String receiverID = receiverId;
        String messageText = message;
        String receiverType = CometChatConstants.RECEIVER_TYPE_USER;

        TextMessage textMessage = new TextMessage(receiverID, messageText, receiverType);

        CometChat.sendMessage(textMessage, new CometChat.CallbackListener <TextMessage> () {
            @Override
            public void onSuccess(TextMessage textMessage) {
                addMessage(textMessage); 
            }
            @Override
            public void onError(CometChatException e) {
            }
        });
    }

    private void addMessage(TextMessage textMessage) {
        adapter.addToStart(new MessageWrapper(textMessage), true);
        Log.d("chat", "item count = " + adapter.getItemCount());
    }
}