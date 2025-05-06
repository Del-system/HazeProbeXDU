package com.k1wamiiro.hazeprobe;

import android.util.Log;

import com.qweather.sdk.Callback;
import com.qweather.sdk.QWeather;
import com.qweather.sdk.basic.Lang;
import com.qweather.sdk.parameter.air.AirV1Parameter;
import com.qweather.sdk.response.air.v1.AirV1CurrentResponse;
import com.qweather.sdk.response.error.ErrorResponse;
import com.k1wamiiro.hazeprobe.pojo.Air;

import java.util.*;

public class AirHelper {
    private static final String TAG = "airHelper";

    public interface AirQualityCallback {
        void onDataReceived(Air airData);
    }

    public static void getAirQuality(double latitude, double longitude, AirQualityCallback callback) {
        // 创建一个空列表来存储空气质量数据
        Air airData = new Air();
        // 设置请求参数
        AirV1Parameter parameter = new AirV1Parameter(latitude, longitude)
                .setLang(Lang.ZH_HANS); // 设置语言
        // 获取空气质量数据
        QWeather.instance.airCurrent(parameter, new Callback<AirV1CurrentResponse>() {
            @Override
            public void onSuccess(AirV1CurrentResponse response) {
                // 处理成功的响应
                airData.setAqi("" + response.getIndexes().get(0).getAqi() + " " + response.getIndexes().get(0).getCategory());
                airData.setPm25("" + response.getPollutants().get(0).getConcentration().getValue());
                airData.setPm10("" + response.getPollutants().get(1).getConcentration().getValue());
                Log.d(TAG, "Air Quality Data: " + airData);
                callback.onDataReceived(airData);
            }

            @Override
            public void onFailure(ErrorResponse errorResponse) {
                // 处理请求错误
                Log.e(TAG, "Request Error: " + errorResponse.toString());
            }

            @Override
            public void onException(Throwable e) {
                // 处理异常
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        });
    }
}
