package com.xingruyu.weather.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.xingruyu.weather.utils.AppManager;

/**
 * 所有Activity的基类
 * Created by WDX on 2016/10/16.
 */

public class BaseFragmentActivity extends FragmentActivity {

    public ProgressDialog progressDialog;
    public Context mContext;  // 上下文实例

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }
}
