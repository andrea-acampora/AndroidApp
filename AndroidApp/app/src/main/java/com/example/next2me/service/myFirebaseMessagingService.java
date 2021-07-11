package com.example.next2me.service;

import android.util.Log;

import com.example.next2me.utils.DatabaseHelper;
import com.example.next2me.utils.UserHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;

public class myFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        //PROBLEMA------AGGIORNARE IL TOKEN NEL DB MA ANCORA NON HO LO UID DI FIREBASE
        //SE LO SALVO QUI DOPO NON LO RIAGGIORNO NEL DB QUANDO ELIMINO I DATI
        UserHelper.getInstance().setNotificationsTokenId(token);
    }
}
