package com.k1wamiiro.hazeprobe;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class LocationHelper {
    private final AMapLocationClient mLocationClient;
    private final OnLocationResultListener mListener;

    public interface OnLocationResultListener {
        void onSuccess(AMapLocation location);  // 定位成功回调
        void onFailure(String errorMsg);      // 定位失败回调
    }

    public LocationHelper(Context context, OnLocationResultListener listener) {
        this.mListener = listener;
        try {
            AMapLocationClient.updatePrivacyShow(context, true, true);
            AMapLocationClient.updatePrivacyAgree(context, true);
            mLocationClient = new AMapLocationClient(context);
            initLocationOptions();
        } catch (Exception e) {
            throw new RuntimeException("初始化定位失败", e);
        }
    }

    private void initLocationOptions() {
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocationLatest(true);
        option.setNeedAddress(true);
        option.setMockEnable(false);
        option.setLocationCacheEnable(false);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(mLocationListener);
    }

    // 开始定位
    public void startLocation() {
        if (mLocationClient != null) {
            mLocationClient.startLocation();
        }
    }

    // 停止定位（避免内存泄漏）
    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
    }

    private final AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (location != null) {
                if (location.getErrorCode() == 0) {
                    mListener.onSuccess(location);  // 返回完整定位信息
                } else {
                    String errorMsg = "ErrorCode:" + location.getErrorCode() + ", " + location.getErrorInfo();
                    mListener.onFailure(errorMsg);
                }
            }
        }
    };
}
