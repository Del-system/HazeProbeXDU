package com.k1wamiiro.hazeprobe;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.qweather.sdk.Callback;
import com.qweather.sdk.QWeather;
import com.qweather.sdk.basic.Lang;
import com.qweather.sdk.basic.Unit;
import com.qweather.sdk.parameter.weather.WeatherParameter;
import com.qweather.sdk.response.error.ErrorResponse;
import com.qweather.sdk.response.weather.WeatherHourly;
import com.qweather.sdk.response.weather.WeatherHourlyResponse;

import java.util.ArrayList;
import java.util.List;

public class DrawLineChart {
    public static LineChart lineChart;
    private static final String TAG = "DrawLineChart";

    public interface DrawLineChartCallback {
        void onDataReceived(List<String> xLabels, LineData data);
    }

    public static void setData(String locationId, DrawLineChartCallback callback) {
        //获取24小时的天气数据
        WeatherParameter parameter = new WeatherParameter(locationId).lang(Lang.ZH_HANS).unit(Unit.METRIC);
        QWeather.instance.weather24h(parameter, new Callback<WeatherHourlyResponse>() {
            @Override
            public void onSuccess(WeatherHourlyResponse response) {
                if (response.getCode().equals("200")) {
                    //记录各项数据
                    List<String> xLabels = new ArrayList<>();
                    List<Float> temperatureData = new ArrayList<>();
                    List<Float> humidityData = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        xLabels.add(response.getHourly().get(i).getFxTime().substring(11, 16));
                        temperatureData.add(Float.parseFloat(response.getHourly().get(i).getTemp()));
                        humidityData.add(Float.parseFloat(response.getHourly().get(i).getHumidity()));
                    }
                    //回调
                    callback.onDataReceived(xLabels, setLineChartData(temperatureData, humidityData));
                    Log.d(TAG, "X轴标签: " + xLabels + ", 温度数据: " + temperatureData + ", 湿度数据: " + humidityData);
                }
            }

            @Override
            public void onFailure(ErrorResponse errorResponse) {
                //处理请求错误
                Log.e(TAG, "Request Error: " + errorResponse.getError());
            }

            @Override
            public void onException(Throwable e) {
                //处理异常
                e.printStackTrace();
            }
        });
    }

    public static LineData setLineChartData(List<Float> temperatureData, List<Float> humidityData) {
        //将温度和湿度数据转换为Entry对象
        List<Entry> entriesTemp = new ArrayList<>();
        List<Entry> entriesHumidity = new ArrayList<>();

        for(int i = 0; i < temperatureData.size(); i++) {
            entriesTemp.add(new Entry(i , temperatureData.get(i)));
            entriesHumidity.add(new Entry(i, humidityData.get(i)));
        }
        //设置数据集
        LineDataSet dataSetTemp = new LineDataSet(entriesTemp, "温度");
        dataSetTemp.setColor(Color.RED);
        dataSetTemp.setLineWidth(2f);
        dataSetTemp.setCircleColor(Color.RED);
        dataSetTemp.setCircleRadius(4f);

        LineDataSet dataSetHumidity = new LineDataSet(entriesHumidity, "湿度");
        dataSetHumidity.setColor(Color.BLUE);
        dataSetHumidity.setLineWidth(2f);
        dataSetHumidity.setCircleColor(Color.BLUE);
        dataSetHumidity.setCircleRadius(4f);
        //将数据集添加到图表
        LineData lineData = new LineData(dataSetTemp, dataSetHumidity);
        return lineData;
    }


}
