package com.k1wamiiro.hazeprobe.pojo;

public class Weather {
    private String temp;
    private String weatherInfo;

    public Weather(String temp, String weatherInfo) {
        this.temp = temp;
        this.weatherInfo = weatherInfo;
    }
    // 默认构造函数
    public Weather() {
        this.temp = "";
        this.weatherInfo = "";
    }

    public String getTemp() {
        return temp;
    }
    public void setTemp(String temp) {
        this.temp = temp;
    }
    public String getWeatherInfo() {
        return weatherInfo;
    }
    public void setWeatherInfo(String weatherInfo) {
        this.weatherInfo = weatherInfo;
    }
}
