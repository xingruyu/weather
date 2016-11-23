package com.xingruyu.weather;

import android.content.Intent;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.xingruyu.weather.bean.LocationInfo;
import com.xingruyu.weather.config.Constants;

import static com.baidu.location.BDLocation.TypeCriteriaException;
import static com.baidu.location.BDLocation.TypeNetWorkException;
import static com.baidu.location.BDLocation.TypeNone;
import static com.baidu.location.BDLocation.TypeOffLineLocationFail;

/**
 * 百度定位结果监听类
 * Created by WDX on 2016/10/23.
 */

public class MyLocationListener implements BDLocationListener {

    public static String locationState = null;   //定位状态

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location.getLocType() == TypeCriteriaException || location.getLocType() == TypeNetWorkException){
            locationState = "无网络链接！";
        }else if (location.getLocType() == TypeNone || location.getLocType() == TypeOffLineLocationFail){
            locationState = "暂无定位结果！";
        }else if (!location.getCountry().equals("中国")){
            locationState = "只支持国内城市！";
        }else {
            String locationDescribe = "";
            if (location.getLocationDescribe() != null){
                locationDescribe = location.getLocationDescribe();
            }
            LocationInfo locationInfo = new LocationInfo(locationDescribe,location.getAddrStr(),location.getCity(),location.getCountry(),
                    location.getDistrict(),location.getProvince(),location.getLatitude(),location.getLongitude());
            //全局变量
            MyApplication.getMyApplication().setLocationInfo(locationInfo);
            locationState = "定位成功！";
        }
        Intent intent = new Intent(Constants.LOCATION_STATE);
        MyApplication.mContext.sendBroadcast(intent);
    }
}
