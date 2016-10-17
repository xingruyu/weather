package com.xingruyu.weather.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.xingruyu.weather.R;
import com.xingruyu.weather.base.BaseFragmentActivity;
import com.xingruyu.weather.utils.AppManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 启动界面
 * Created by WDX on 2016/10/16.
 */
public class LaunchActivity extends BaseFragmentActivity {


    @BindView(R.id.iv_launch) ImageView mIv_Launch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_launch);
        initView();
    }

    private void initView(){
        ButterKnife.bind(this);
        // 启动动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(2000);
        mIv_Launch.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AppManager.getAppManager().appExit(mContext);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
