package com.xingruyu.weather;

import android.app.Application;
import android.content.Context;

import com.baidu.location.LocationClient;
import com.xingruyu.weather.activity.MainActivity;
import com.xingruyu.weather.bean.CityWeather;
import com.xingruyu.weather.bean.LocationInfo;
import com.xingruyu.weather.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用程序的入口，该类在所有应用程序组件之前被实例化
 * Created by WDX on 2016/10/16.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;
    public static Context mContext;

    public MainActivity mainActivity;          //主界面对象
    private LocationInfo locationInfo;             //位置信息(每次定位成功后更新)
    public static List<CityWeather> cityWeatherList;     //城市天气列表，与数据库保持一致
    public static LocationClient mLocationClient;   //百度定位
    public static int updateIndex = 0;          //更新天气信息时，默认城市在全局cityWeatherList中的索引，从0开始

    public static int screenWidth = 0;
    public static int screenHeight = 0;

//    private DBHelper mDBHelper;

    public MyApplication() {
        super();
    }

    /**
     * 单一实例
     * @return
     */
    public static MyApplication getMyApplication(){
        if (myApplication == null){
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        cityWeatherList = new ArrayList<>();
        LogUtils.isDebug = true;  //开启Log日志
    }

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    //    public DBHelper getmDBHelper() {
//        return mDBHelper;
//    }
//
//    public void setmDBHelper(DBHelper mDBHelper) {
//        this.mDBHelper = mDBHelper;
//    }
}
