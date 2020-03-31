package com.dev.staffapp.Retrofit;

import com.dev.staffapp.Model.FCMResponse;
import com.dev.staffapp.Model.FCMSendData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAUMA0Ncs:APA91bHVSOBNWAcu8Ab2230TspBPFUtMs8RSfXh70FfzdscESAgeD8hnoB_CL5E09zvLxLjv9eCl393uf0TFws6_u5didQJoK6U-_APhjUa30GY-3Scnqs8NoqgXl1a5dVqsscsRdZRg"
    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);

}
