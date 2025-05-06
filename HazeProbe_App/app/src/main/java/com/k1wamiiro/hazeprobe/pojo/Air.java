package com.k1wamiiro.hazeprobe.pojo;

public class Air {
    private String aqi;
    private String pm25;
    private String pm10;

    // 默认构造函数
    public Air() {
        this.aqi = "";
        this.pm25 = "";
        this.pm10 = "";
    }

    public Air(String aqi, String pm25, String pm10) {
        this.aqi = aqi;
        this.pm25 = pm25;
        this.pm10 = pm10;
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
