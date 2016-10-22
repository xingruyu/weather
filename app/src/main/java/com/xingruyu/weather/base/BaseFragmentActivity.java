package com.xingruyu.weather.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.xingruyu.weather.utils.AppManagerUtils;

/**
 * 所有Activity的基类
 * Created by WDX on 2016/10/16.
 */

public class BaseFragmentActivity extends FragmentActivity {

    public ProgressDialog progressDialog;
    public Context mContext;  // 上下文实例
    public Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 添加Activity到堆栈
        AppManagerUtils.getAppManager().addActivity(this);

        mContext = getApplicationContext();
        mActivity = this;
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }



    protected void toastShowShot(String text){
        Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
    }

    protected void toastShowLong(String text){
        Toast.makeText(mContext,text,Toast.LENGTH_LONG).show();
    }
}
