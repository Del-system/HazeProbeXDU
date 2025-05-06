package com.k1wamiiro.hazeprobe.pojo;

public class InfoData {
    private String deviceId;
    private String province;
    private String city;
    private String district;
    private double latitude;
    private double longitude;
    private String temp;
    private String weatherInfo;
    private String aqi;
    private String pm25;
    private String pm10;

    //构造函数和getters/setters
    public InfoData(String deviceId, String province, String city, String district, double latitude, double longitude,
                    String temp, String weatherInfo, String aqi, String pm25, String pm10) {
        this.deviceId = deviceId;
        this.province = province;
        this.city = city;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temp = temp;
        this.weatherInfo = weatherInfo;
        this.aqi = aqi;
        this.pm25 = pm25;
        this.pm10 = pm10;
    }
    public InfoData() {
        // 默认构造函数
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
    public String getAqi() {
        return aqi;
    }
    public void setAqi(String aqi) {
        this.aqi = aqi;
    }
    public String getPm25() {
        return pm25;
    }
    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
    public String getPm10() {
        return pm10;
    }
    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }
}
