package com.example.next2me.service;

import android.util.Log;

import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.utils.UserHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;

public class myFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        UserHelper.getInstance().setNotificationsTokenId(token);
    }
}
