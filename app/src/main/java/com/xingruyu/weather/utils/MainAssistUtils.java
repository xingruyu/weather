package com.xingruyu.weather.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xingruyu.weather.R;

import java.util.ArrayList;
import java.util.List;

import static com.xingruyu.weather.MyApplication.screenWidth;

/**
 * 主界面辅助类，主要是一些动画及view的创建
 * Created by WDX on 2016/11/18.
 */

public class MainAssistUtils {

    private static List<ImageView> imageViewList = new ArrayList<>();;

    /**
     * 设置天气动画
     *
     * @param mainRlAnimator
     * @param state 天气状态
     */
    public static void setAnimator(FrameLayout mainRlAnimator, String state, Context mContext) {
        mainRlAnimator.removeAllViews();    //清除所有动画
        imageViewList.clear();
        switch (state) {
            case "晴":
                for (int i=0;i<4;i++){
                    imageViewList.add(new ImageView(mContext));
                    mainRlAnimator.addView(imageViewList.get(i));
                }
                imageViewList.get(0).setImageResource(R.drawable.sunshine_1);
                imageViewList.get(1).setImageResource(R.drawable.sunshine_2);
                imageViewList.get(2).setImageResource(R.drawable.sunshine_3);
                imageViewList.get(3).setImageResource(R.drawable.fire_balloon);
                RelativeLayout.LayoutParams param0 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                param0.setMargins(screenWidth/5,0,0,0);
                imageViewList.get(0).setLayoutParams(param0);
                RelativeLayout.LayoutParams param1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                param1.setMargins(0,0,0,0);
                imageViewList.get(1).setLayoutParams(param1);
                RelativeLayout.LayoutParams param2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT);
                param2.setMargins(0,DensityUtils.dpTopx(mContext,50),0,0);
                imageViewList.get(2).setLayoutParams(param2);
                //热气球曲线运动
                PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("x", screenWidth/3,screenWidth/2);   // 属性1
                // 属性2的三个关键帧
                Keyframe kf0 = Keyframe.ofFloat(0f, screenWidth/2);
                Keyframe kf1 = Keyframe.ofFloat(.5f, screenWidth/3);
                Keyframe kf2 = Keyframe.ofFloat(1f, screenWidth/4);
                // 用三个关键帧构造PropertyValuesHolder对象
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofKeyframe("y", kf0, kf1, kf2);    // 属性2
                // 再用两个PropertyValuesHolder对象构造一个ObjectAnimator对象
                ObjectAnimator objectAnimatorfine = ObjectAnimator.ofPropertyValuesHolder(imageViewList.get(3), pvhX, pvhY);
                objectAnimatorfine.setDuration(18000);
                objectAnimatorfine.setRepeatCount(999);
                objectAnimatorfine.setRepeatMode(ValueAnimator.REVERSE);     //结束后反方向执行
                objectAnimatorfine.setInterpolator(new LinearInterpolator());//匀速
                objectAnimatorfine.start();

                break;
            case "多云":

                break;
            case "少云":

                break;
            case "晴间多云":

                break;
            case "阴":

                break;
            case "阵雨":

                break;
            case "强阵雨":

                break;
            case "雷阵雨":

                break;
            case "强雷阵雨":

                break;
            case "雷阵雨伴有冰雹":

                break;
            case "小雨":

                break;
            case "毛毛雨":

                break;
            case "中雨":

                break;
            case "大雨":

                break;
            case "极端降雨":

                break;
            case "暴雨":

                break;
            case "大暴雨":

                break;
            case "特大暴雨":

                break;
            case "冻雨":

                break;
            case "阵雪":

                break;
            case "阵雨夹雪":

                break;
            case "雨雪天气":

                break;
            case "雨夹雪":

                break;
            case "小雪":

                break;
            case "中雪":

                break;
            case "大雪":

                break;
            case "暴雪":

                break;
            case "薄雾":

                break;
            case "雾":

                break;
            case "霾":

                break;
            case "扬沙":

                break;
            case "浮尘":

                break;
            case "沙尘暴":

                break;
            case "强沙尘暴":

                break;
            default:   //未知

                break;

        }

    }
}
