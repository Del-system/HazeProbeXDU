package com.k1wamiiro.hazeprobe.service;

import com.k1wamiiro.hazeprobe.pojo.InfoData;
import com.k1wamiiro.hazeprobe.pojo.InfoResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InfoApiService {
    @GET("/api/info/{deviceId}")
    Call<InfoData> getInfo(@Path("deviceId") String deviceId);

    @POST("/api/info")
    Call<InfoResponse<Void>> postInfo(@Body InfoData infoData);
}
