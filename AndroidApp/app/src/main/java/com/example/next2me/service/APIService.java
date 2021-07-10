package com.example.next2me.service;




import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA0gXTowU:APA91bE9tS47rqya60zyBFcfHJmbQKlZbiz-vQDxZkF9eMJuaN1EnbZsbY3buJHMYnjpuBeEJt-Q5m4b2xArNdgU1Zl0eJWWbJ1x-G1288iXybvPjMEg-00uLMCr1n4xlCdKdnAW8r-w"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}