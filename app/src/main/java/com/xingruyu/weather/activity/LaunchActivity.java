package com.xingruyu.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.xingruyu.weather.R;
import com.xingruyu.weather.base.BaseFragmentActivity;
import com.xingruyu.weather.utils.AppManagerUtils;
import com.xingruyu.weather.utils.StatusBarUtil;

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
        StatusBarUtil.fullScreen(mActivity);

        setContentView(R.layout.activity_launch);
        initView();
    }

    private void initView(){
        ButterKnife.bind(this);
        // 启动透明度动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(1800);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(mContext,MainActivity.class));
                //在Activity切换时，载入淡出淡入的动画效果
                overridePendingTransition(R.anim.set_startactivity,R.anim.set_finishactivity);
                AppManagerUtils.getAppManager().appExit(mContext);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mIv_Launch.startAnimation(alphaAnimation);
    }
}
