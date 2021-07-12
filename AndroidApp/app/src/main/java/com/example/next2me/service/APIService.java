package com.example.next2me.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA0gXTowU:APA91bFlZUZmjPJXmn26Ugpzl-lyaQ3f0b3DexOolartQSgAStlKSo6Tlwuu3I06lHZPXaZP6GqjJ1Fia9SxbXb4QTUXT59IZdtyjyQpoqvbdgjqhcjvSHaafr0XPi7nr_5imwibVw6X"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}