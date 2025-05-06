package com.k1wamiiro.hazeprobe;

import android.util.Log;

import com.k1wamiiro.hazeprobe.pojo.Weather;
import com.qweather.sdk.Callback;
import com.qweather.sdk.QWeather;
import com.qweather.sdk.basic.Lang;
import com.qweather.sdk.basic.Unit;
import com.qweather.sdk.parameter.weather.WeatherParameter;
import com.qweather.sdk.response.error.ErrorResponse;
import com.qweather.sdk.response.weather.WeatherNowResponse;

public class WeatherHelper {
    private static final String TAG = "WeatherHelper";

    public interface WeatherCallback {
        void onWeatherDataReceived(Weather weatherData);
    }

    public static void getWeatherData(String locationId, WeatherCallback callback) {
        WeatherParameter parameter = new WeatherParameter(locationId)
                .lang(Lang.ZH_HANS)// 设置语言
                .unit(Unit.METRIC); // 设置单位
        QWeather.instance.weatherNow(parameter, new Callback<WeatherNowResponse>() {
            @Override
            public void onSuccess(WeatherNowResponse response) {
                // 处理成功的响应
                Weather weatherData = new Weather();
                weatherData.setTemp(response.getNow().getTemp() + "°C");
                weatherData.setWeatherInfo(response.getNow().getText());
                callback.onWeatherDataReceived(weatherData);
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
