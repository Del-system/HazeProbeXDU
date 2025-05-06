package com.k1wamiiro.hazeprobe.service;

import android.util.Log;

import com.k1wamiiro.hazeprobe.ApiClient;
import com.k1wamiiro.hazeprobe.pojo.InfoData;
import com.k1wamiiro.hazeprobe.pojo.InfoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoSF {
    private static InfoApiService apiService = ApiClient.getClient().create(InfoApiService.class);

    public interface InfoCallback {
        void onSuccess(InfoData infoData);
        void onFailure(String errorMessage);
    }

    public interface SaveInfoCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public static void fetchInfo(String deviceId, InfoCallback callback) {
        Call<InfoData> call = apiService.getInfo(deviceId);
        call.enqueue(new Callback<InfoData>() {
            @Override
            public void onResponse(Call<InfoData> call, Response<InfoData> response) {
                if (response.isSuccessful()) {
                    InfoData infoData = response.body();
                    if (infoData != null) {
                        callback.onSuccess(infoData);
                    } else {
                        Log.e("InfoSF", "Response body is null");
                        callback.onFailure("Response body is null");
                    }
                } else {
                    Log.e("InfoSF", "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<InfoData> call, Throwable t) {
                Log.e("InfoSF", "Network error: " + t.getMessage());
            }
        });
    }

    public static void saveInfo(InfoData data, SaveInfoCallback callback){
        Call<InfoResponse<Void>> call = apiService.postInfo(data);
        call.enqueue(new Callback<InfoResponse<Void>>() {
            @Override
            public void onResponse(Call<InfoResponse<Void>> call, Response<InfoResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InfoResponse<Void> infoResponse = response.body();
                    if (infoResponse.getCode() == 1) {
                        callback.onSuccess();
                    } else {
                        Log.e("InfoSF", "Server error: " + infoResponse.getMsg());
                    }
                } else {
                    Log.e("InfoSF", "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<InfoResponse<Void>> call, Throwable t) {
                Log.e("InfoSF", "Network error: " + t.getMessage());
            }
        });
    }
}
