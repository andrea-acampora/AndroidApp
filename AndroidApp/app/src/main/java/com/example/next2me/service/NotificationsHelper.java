package com.example.next2me.service;

import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsHelper {

    private static NotificationsHelper instance = null;
    private NotificationsHelper(){}

    private APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

    public static NotificationsHelper getInstance(){
        if(instance==null){
            instance = new NotificationsHelper();
        }
        return instance;
    }

    public void sendNotifications(String userToken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, userToken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Log.d("notification","error");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

}
