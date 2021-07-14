package com.example.next2me.models;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.List;

public class DefaultDialog implements IDialog {
    private String id;
    private String dialogPhoto;
    private String dialogName;
    private List<IUser> users;
    private IMessage lastMessage;
    private int unreadCount;
    private String receiverId;

    public DefaultDialog(String id, String dialogPhoto, String dialogName, List<IUser> users, IMessage lastMessage, int unreadCount, String receiverId) {
        this.id = id;
        this.dialogPhoto = dialogPhoto;
        this.dialogName = dialogName;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
        this.receiverId = receiverId;
    }

    public String getReceiverId() { return receiverId; }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public List<IUser> getUsers() { return users; }

    @Override
    public IMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(IMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }
}