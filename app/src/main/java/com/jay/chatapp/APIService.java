package com.jay.chatapp;

import com.jay.chatapp.Notifications.MyResponse;
import com.jay.chatapp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAKN6tKVc:APA91bHOVtjxsxSgPQvy8c88jNXuEqz4RdOrL0b1LAyMZ5SD7yFHBaP7EXiRnCghLa8uMUKMDFisx79D1BsLCk2GfjbQv7mvVWnOisqALpOARQYRsZ14zQRsH_To8l6mxOeP8GH5nJib"
            }
    )


    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
