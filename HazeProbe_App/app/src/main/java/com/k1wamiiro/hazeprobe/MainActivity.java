package com.k1wamiiro.hazeprobe;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.k1wamiiro.hazeprobe.pojo.Air;
import com.k1wamiiro.hazeprobe.pojo.InfoData;
import com.k1wamiiro.hazeprobe.pojo.Weather;
import com.k1wamiiro.hazeprobe.service.InfoSF;
import com.qweather.sdk.Callback;
import com.qweather.sdk.JWTGenerator;
import com.qweather.sdk.QWeather;
import com.qweather.sdk.parameter.geo.GeoCityLookupParameter;
import com.qweather.sdk.response.error.ErrorResponse;
import com.qweather.sdk.response.geo.GeoCityLookupResponse;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        LocationHelper.OnLocationResultListener{

    private Button btnGetLocation;
    private TextView tvLocation, tvWeather, tvTemp, tvAqi, tvPm25, tvPm10;
    private LineChart lineChart;
    private LocationHelper locationHelper;
    private static final int PERMISSION_REQUEST_LOCATION = 1;
    private JWTGenerator jwt;
    private static final String API_HOST = "p378kxjxdm.re.qweatherapi.com";
    private ExecutorService executorService;
    private String deviceId;
    private InfoData preData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        // 初始化线程池
        executorService = Executors.newFixedThreadPool(4);
        // 初始化定位工具类
        locationHelper = new LocationHelper(this, this);
        checkLocationPermission();

        // 初始化天气API
        QWeather.getInstance(MainActivity.this, API_HOST).setLogEnable(true);

        // 设置Token生成器
        jwt = new JWTGenerator("MC4CAQAwBQYDK2VwBCIEID+G4srVqfgmXNldCkBudeBWfBAEh6M9M2TUPToakNVF", "4NKPCDB22T", "CAGXV24NHU");
        QWeather.instance.setTokenGenerator(jwt);

        // 初始化折线图
        lineChart.setDrawGridBackground(false); //不绘制背景网格
        lineChart.setPinchZoom(false); //禁止缩放
        lineChart.setDrawMarkers(true); //绘制标记
        lineChart.setTouchEnabled(false); //禁止触摸
        lineChart.getDescription().setEnabled(false);
        //配置X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(6);
        //配置左侧Y轴
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setEnabled(false);
        //禁用右侧Y轴
        lineChart.getAxisRight().setEnabled(false);

        //获取设备ID
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //从服务器获取数据
        preData = new InfoData();
        preData.setDeviceId(deviceId);
        InfoSF.fetchInfo(deviceId, new InfoSF.InfoCallback() {
            @Override
            public void onSuccess(InfoData infoData) {
                preData = infoData;
                runOnUiThread(() -> {
                    tvLocation.setText(infoData.getProvince() + " " + infoData.getCity() + " " + infoData.getDistrict());
                    tvTemp.setText(infoData.getTemp());
                    tvWeather.setText(infoData.getWeatherInfo());
                    tvAqi.setText("污染指数：" + infoData.getAqi());
                    tvPm25.setText("PM2.5：" + infoData.getPm25());
                    tvPm10.setText("PM10：" + infoData.getPm10());
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("TAG", "onFailure: " + errorMessage);
            }
        });
    }

    private void initViews() {
        btnGetLocation = findViewById(R.id.btn_01);
        tvLocation = findViewById(R.id.tv_location);
        tvTemp = findViewById(R.id.tv_temp);
        tvWeather = findViewById(R.id.tv_weather);
        tvAqi = findViewById(R.id.tv_aqi);
        tvPm10 = findViewById(R.id.tv_pm10);
        tvPm25 = findViewById(R.id.tv_pm25);
        lineChart = findViewById(R.id.chart);
        btnGetLocation.setOnClickListener(this);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限已授予", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "需要定位权限才能获取位置", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_01) {
            locationHelper.startLocation(); // 触发定位
        }
    }

    // 定位成功回调
    @Override
    public void onSuccess(AMapLocation location) {
        runOnUiThread(() -> {
            tvLocation.setText(location.getProvince() + " " + location.getCity() + " " + location.getDistrict());
            // 保存位置信息
            preData.setProvince(location.getProvince());
            preData.setCity(location.getCity());
            preData.setDistrict(location.getDistrict());
            preData.setLatitude(location.getLatitude());
            preData.setLongitude(location.getLongitude());
            // 获取城市ID
            String cityId = location.getAdCode();
            executorService.execute(() -> fetchAirQualityData(location.getLatitude(), location.getLongitude()));
            // 调用天气API获取城市ID;
            GeoCityLookupParameter parameter = new GeoCityLookupParameter(cityId);
            // 调用天气API获取天气数据
            QWeather.instance.geoCityLookup(parameter, new Callback<GeoCityLookupResponse>() {
                @Override
                public void onSuccess(GeoCityLookupResponse geoCityLookupResponse) {
                    String locationID = geoCityLookupResponse.getLocation().get(0).getId();
                    executorService.execute(() -> fetchWeatherData(locationID));
                    executorService.execute(() -> fetchLineChartData(locationID));
                    // 保存数据到服务器
                    InfoSF.saveInfo(preData, new InfoSF.SaveInfoCallback() {
                        @Override
                        public void onSuccess() {
                            Log.i("TAG", "onSuccess: 数据保存成功");
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.i("TAG", "onFailure: 数据保存失败: " + errorMessage);
                        }
                    });
                }

                @Override
                public void onFailure(ErrorResponse errorResponse) {
                    Log.i("TAG", "onFailure: " + errorResponse.toString());
                }

                @Override
                public void onException(Throwable e) {
                    e.printStackTrace();
                }
            });
        });
    }

    // 定位失败回调
    @Override
    public void onFailure(String errorMsg) {
        runOnUiThread(() -> Toast.makeText(this, "定位失败: " + errorMsg, Toast.LENGTH_LONG).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationHelper != null) {
            locationHelper.stopLocation(); // 避免内存泄漏
        }
    }

    private void fetchWeatherData(String locationId) {
        // 获取天气数据
        WeatherHelper.getWeatherData(locationId, new WeatherHelper.WeatherCallback() {
            @Override
            public void onWeatherDataReceived(Weather weatherData) {
                runOnUiThread(() -> {
                    tvTemp.setText(weatherData.getTemp());
                    tvWeather.setText(weatherData.getWeatherInfo());
                    // 更新InfoData对象
                    preData.setTemp(weatherData.getTemp());
                    preData.setWeatherInfo(weatherData.getWeatherInfo());
                });
            }
        });
    }

    private void fetchAirQualityData(double latitude, double longitude) {
        // 获取空气质量数据
        AirHelper.getAirQuality(latitude, longitude, new AirHelper.AirQualityCallback() {
            @Override
            public void onDataReceived(Air airData) {
                runOnUiThread(() ->{
                    tvAqi.setText("污染指数：" + airData.getAqi());
                    tvPm25.setText("PM2.5：" + airData.getPm25());
                    tvPm10.setText("PM10：" + airData.getPm10());
                    // 更新InfoData对象
                    preData.setAqi(airData.getAqi());
                    preData.setPm25(airData.getPm25());
                    preData.setPm10(airData.getPm10());
                });
            }
        });
    }

    private void fetchLineChartData(String locationId) {
        // 获取折线图数据
        DrawLineChart.setData(locationId, new DrawLineChart.DrawLineChartCallback() {
            @Override
            public void onDataReceived(List<String> xLabels, LineData data) {
                runOnUiThread(() -> {
                    lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
                    lineChart.setData(data);
                    lineChart.invalidate(); // 刷新图表
                });
            }
        });
    }
}