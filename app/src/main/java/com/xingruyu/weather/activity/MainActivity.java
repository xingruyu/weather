package com.xingruyu.weather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingruyu.weather.R;
import com.xingruyu.weather.base.BaseFragmentActivity;
import com.xingruyu.weather.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * APP主界面，即天气详情界面
 * Created by WDX on 2016/10/17.
 */

public class MainActivity extends BaseFragmentActivity {

    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.tv_refresh_time)
    TextView tvRefreshTime;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.iv_pm5)
    ImageView ivPM5;
    @BindView(R.id.tv_pm5)
    TextView tvPM5;
    @BindView(R.id.dl_details)
    DrawerLayout dlDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTranslucentForDrawerLayout(this, (DrawerLayout) findViewById(R.id.dl_details), 0);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

    }

    @OnClick({R.id.iv_pm5, R.id.tv_pm5, R.id.dl_details,R.id.iv_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pm5:
                break;
            case R.id.tv_pm5:
                break;
            case R.id.dl_details:
                break;
            case R.id.iv_menu:
                dlDetails.openDrawer(Gravity.LEFT);
                break;
        }
    }

}
