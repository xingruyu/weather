package com.xingruyu.weather;

import android.app.Application;

/**
 * 应用程序的入口，该类在所有应用程序组件之前被实例化
 * Created by WDX on 2016/10/16.
 */

public class MyApplication extends Application {

    private static MyApplication myApplication;

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
    }
}
