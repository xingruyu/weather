package com.xingruyu.weather.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingruyu.weather.MyApplication;
import com.xingruyu.weather.R;
import com.xingruyu.weather.bean.ForecastWeather;
import com.xingruyu.weather.view.TendencyLineView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import static com.xingruyu.weather.MyApplication.mContext;

/**
 * 主界面辅助类，主要是一些动画及view的创建
 * Created by WDX on 2016/11/18.
 */

public class MainAssistUtils {

    private static List<ImageView> IVList = new ArrayList<>();
    private static List<ObjectAnimator> OAList = new ArrayList<>();

    private static MainAssistUtils instance;
    private static boolean dayOrNight = true;   //true为白天
    private static String states = "";   //天气状态
    private static int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;
    private static int WIDTH = MyApplication.screenWidth;
    private static int HIGTH = MyApplication.screenHeight;
    private static Random random = new Random();    //产生随机数

    private MainAssistUtils() {
    }

    /**
     * 单一实例
     */
    public static MainAssistUtils getMainAssistUtils() {
        if (instance == null) {
            instance = new MainAssistUtils();
        }
        return instance;
    }

    /**
     * 设置天气动画
     * 注意：动画的暂停与启动（上滑、锁屏，进入后台、从别的界面返回）
     * @param mainRlAnimator
     * @param state 天气状态
     */
    public static void setAnimator(RelativeLayout mainRlAnimator, String state, Context mContext) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());  //System.currentTimeMillis()取得系统时间
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (states.equals(state)){    //与上次的天气状态一样
            if ((hour > 18 || hour < 6) && dayOrNight) {  //判断是否要切换白天与夜晚的动画
                dayOrNight = false;
            }else if (hour >=6 && hour <=18 && !dayOrNight){
                dayOrNight = true;
            } else {   //不需要更新天气动画
                return;
            }
        }else {
            if (hour > 18 || hour < 6) {   //判断是否要切换白天与夜晚的动画
                dayOrNight = false;
            }else {
                dayOrNight = true;
            }
        }
        states = state;
        mainRlAnimator.removeAllViews();    //清除所有动画
        IVList.clear();
        OAList.clear();
        switch (state) {
            case "晴":
                int num = dayOrNight ? 4 : 3;
                for (int i=0;i<num;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                }
                if (dayOrNight){
                    IVList.get(0).setImageResource(R.drawable.sunshine_1);
                    IVList.get(1).setImageResource(R.drawable.sunshine_2);
                    IVList.get(2).setImageResource(R.drawable.sunshine_3);
                    IVList.get(3).setImageResource(R.drawable.fire_balloon);
                    //第一条阳光
                    setLayoutParams(WRAP,WRAP,true, WIDTH/7,0 ,IVList.get(0));
                    OAList.add(ObjectAnimator.ofFloat(IVList.get(0),"alpha",1,0.3f));
                    setAnimatiorParams(OAList.get(0),999,1800,ValueAnimator.REVERSE,new LinearInterpolator());
                    OAList.get(0).start();
                    //第二条阳光
                    setLayoutParams(WRAP,WRAP,true, 0,0 ,IVList.get(1));
                    OAList.add(ObjectAnimator.ofFloat(IVList.get(1),"alpha",0.3f,1));
                    setAnimatiorParams(OAList.get(1),999,2200,ValueAnimator.REVERSE,new LinearInterpolator());
                    OAList.get(1).start();
                    //第三条阳光
                    setLayoutParams(WRAP,WRAP,true,0,DensityUtils.dpTopx(mContext,50) ,IVList.get(2));
                    OAList.add(ObjectAnimator.ofFloat(IVList.get(2),"alpha",1,0.3f));
                    setAnimatiorParams(OAList.get(2),999,2000,ValueAnimator.REVERSE,new LinearInterpolator());
                    OAList.get(2).start();
                    //热气球
                    setLayoutParams(WIDTH/6,WIDTH/6,false,0,0,IVList.get(3));
                    //热气球X方向平移动画
                    OAList.add(ObjectAnimator.ofFloat(IVList.get(3),"translationX",WIDTH/2,WIDTH*2/3));
                    setAnimatiorParams(OAList.get(3),999,0,ValueAnimator.REVERSE,new LinearInterpolator());
                    //热气球Y方向平移动画
                    OAList.add(ObjectAnimator.ofFloat(IVList.get(3),"translationY", WIDTH*2/3,WIDTH/2));
                    setAnimatiorParams(OAList.get(4),999,0,ValueAnimator.REVERSE,new LinearInterpolator());
                    //热气球XY方向缩放动画
                    OAList.add(ObjectAnimator.ofFloat(IVList.get(3),"scaleX",1,0.7f));
                    setAnimatiorParams(OAList.get(5),999,0,ValueAnimator.REVERSE,new LinearInterpolator());
                    OAList.add(ObjectAnimator.ofFloat(IVList.get(3),"scaleY",1,0.7f));
                    setAnimatiorParams(OAList.get(6),999,0,ValueAnimator.REVERSE,new LinearInterpolator());
                    //组合XY方向的动画
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(OAList.get(3),OAList.get(4),OAList.get(5),OAList.get(6));
                    animatorSet.setDuration(12000);
                    animatorSet.start();
                }else {
                    for (int i=0;i<3;i++){
                        setLayoutParams(WRAP,WRAP,true,-WIDTH/4,-WIDTH/4 ,IVList.get(i));
                        AnimatorSet animatorSet = new AnimatorSet();
                        if (i == 0){
                            IVList.get(0).setImageResource(R.drawable.meteor);
                            IVList.get(0).setRotation(-135);  //逆时针旋转135度
                            OAList.add(ObjectAnimator.ofFloat(IVList.get(i),"translationX",WIDTH/5,WIDTH*4));
                            OAList.add(ObjectAnimator.ofFloat(IVList.get(i),"translationY",HIGTH/5,HIGTH*2+WIDTH));
                            animatorSet.setDuration(3800);
                        }else if (i == 1){
                            IVList.get(1).setImageResource(R.drawable.star_meteor);
                            IVList.get(1).setRotation(-90);  //逆时针旋转90度
                            OAList.add(ObjectAnimator.ofFloat(IVList.get(i),"translationX",-WIDTH*2-WIDTH,WIDTH*4));
                            OAList.add(ObjectAnimator.ofFloat(IVList.get(i),"translationY",-HIGTH-WIDTH,HIGTH*2+WIDTH));
                            animatorSet.setDuration(4700);
                        }else {
                            IVList.get(2).setImageResource(R.drawable.star_meteor_small);
                            IVList.get(2).setRotation(-90);  //逆时针旋转90度
                            OAList.add(ObjectAnimator.ofFloat(IVList.get(i),"translationX",-WIDTH/4,WIDTH*4));
                            OAList.add(ObjectAnimator.ofFloat(IVList.get(i),"translationY",-WIDTH/4,HIGTH*2+WIDTH));
                            animatorSet.setDuration(3000);
                            animatorSet.setStartDelay(800);
                        }
                        setAnimatiorParams(OAList.get(i*2),999,0,ValueAnimator.RESTART,new LinearInterpolator());
                        setAnimatiorParams(OAList.get(i*2+1),999,0,ValueAnimator.RESTART,new LinearInterpolator());
                        //组合XY方向的动画
                        animatorSet.playTogether(OAList.get(i*2),OAList.get(i*2+1));
                        animatorSet.start();
                    }
                }
                break;
            case "多云":
                int num1 = dayOrNight ? 4 : 2;
                for (int i=0;i<num1;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                }
                cloudyAnimator();
                break;
            case "少云":
                int num2 = dayOrNight ? 4 : 2;
                for (int i=0;i<num2;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                }
                cloudyAnimator();
                break;
            case "晴间多云":
                int num3 = dayOrNight ? 4 : 2;
                for (int i=0;i<num3;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                }
                cloudyAnimator();
                break;
            case "阴":
                IVList.add(new ImageView(mContext));
                mainRlAnimator.addView(IVList.get(0));
                IVList.get(0).setImageResource(R.drawable.overcast_cloud);
                setLayoutParams(WRAP,WRAP,true, 0,HIGTH/3 ,IVList.get(0));
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(IVList.get(0),"translationX",
                        -ScreenUtils.forceGetViewSize(IVList.get(0))[0]*9/10,WIDTH);
                setAnimatiorParams(objectAnimator,999,65000,ValueAnimator.RESTART,new LinearInterpolator());
                objectAnimator.start();
                break;
            case "阵雨":
                for (int i=0;i<150;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                    if (random.nextInt(2) == 0){
                        IVList.get(i).setImageResource(R.drawable.raindrop_s);
                    }else {
                        IVList.get(i).setImageResource(R.drawable.raindrop_l);
                    }
                }
                rainAnimator();
                break;
            case "强阵雨":
                for (int i=0;i<150;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                    if (random.nextInt(2) == 0){
                        IVList.get(i).setImageResource(R.drawable.raindrop_s);
                    }else {
                        IVList.get(i).setImageResource(R.drawable.raindrop_l);
                    }
                }
                rainAnimator();
                break;
            case "雷阵雨":

                break;
            case "强雷阵雨":

                break;
            case "雷阵雨伴有冰雹":

                break;
            case "小雨":
                for (int i=0;i<150;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                    if (random.nextInt(2) == 0){
                        IVList.get(i).setImageResource(R.drawable.raindrop_s);
                    }else {
                        IVList.get(i).setImageResource(R.drawable.raindrop_l);
                    }
                }
                rainAnimator();
                break;
            case "毛毛雨":
                for (int i=0;i<150;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                    if (random.nextInt(2) == 0){
                        IVList.get(i).setImageResource(R.drawable.raindrop_s);
                    }else {
                        IVList.get(i).setImageResource(R.drawable.raindrop_l);
                    }
                }
                rainAnimator();
                break;
            case "中雨":
                for (int i=0;i<150;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                    if (random.nextInt(2) == 0){
                        IVList.get(i).setImageResource(R.drawable.raindrop_m);
                    }else {
                        IVList.get(i).setImageResource(R.drawable.raindrop_xl);
                    }
                }
                rainAnimator();
                break;
            case "大雨":
                for (int i=0;i<150;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                    if (random.nextInt(2) == 0){
                        IVList.get(i).setImageResource(R.drawable.raindrop_m);
                    }else {
                        IVList.get(i).setImageResource(R.drawable.raindrop_xl);
                    }
                }
                rainAnimator();
                break;
            case "极端降雨":
                for (int i=0;i<150;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                    if (random.nextInt(2) == 0){
                        IVList.get(i).setImageResource(R.drawable.rain_large);
                    }else {
                        IVList.get(i).setImageResource(R.drawable.raindrop_xl);
                    }
                }
                rainAnimator();
                break;
            case "暴雨":
                for (int i=0;i<150;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                    if (random.nextInt(2) == 0){
                        IVList.get(i).setImageResource(R.drawable.rain_large);
                    }else {
                        IVList.get(i).setImageResource(R.drawable.raindrop_xl);
                    }
                }
                rainAnimator();
                break;
            case "大暴雨":
                for (int i=0;i<150;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                    if (random.nextInt(2) == 0){
                        IVList.get(i).setImageResource(R.drawable.rain_large);
                    }else {
                        IVList.get(i).setImageResource(R.drawable.raindrop_xl);
                    }
                }
                rainAnimator();
                break;
            case "特大暴雨":
                for (int i=0;i<150;i++){
                    IVList.add(new ImageView(mContext));
                    mainRlAnimator.addView(IVList.get(i));
                    if (random.nextInt(2) == 0){
                        IVList.get(i).setImageResource(R.drawable.rain_large);
                    }else {
                        IVList.get(i).setImageResource(R.drawable.raindrop_xl);
                    }
                }
                rainAnimator();
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

    /**
     * 设置多云动画
     */
    private static void cloudyAnimator(){
        if (dayOrNight){
            IVList.get(0).setImageResource(R.drawable.cloudy_day_1);
            IVList.get(1).setImageResource(R.drawable.cloudy_day_2);
            IVList.get(2).setImageResource(R.drawable.cloudy_day_3);
            IVList.get(3).setImageResource(R.drawable.cloudy_day_4);
            //第一条云
            setLayoutParams(WRAP,WRAP,true, 0,0 ,IVList.get(0));
            OAList.add(ObjectAnimator.ofFloat(IVList.get(0),"translationX",
                    -ScreenUtils.forceGetViewSize(IVList.get(0))[0]*3/4,WIDTH));
            setAnimatiorParams(OAList.get(0),999,65000,ValueAnimator.RESTART,new LinearInterpolator());
            OAList.get(0).start();
            //第二条云
            setLayoutParams(WRAP,WRAP,true, 0,DensityUtils.dpTopx(mContext,100) ,IVList.get(1));
            OAList.add(ObjectAnimator.ofFloat(IVList.get(1),"translationX", 0 ,WIDTH));
            setAnimatiorParams(OAList.get(1),0,45000,ValueAnimator.RESTART,new LinearInterpolator());
            OAList.get(1).addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    OAList.set(1,(ObjectAnimator.ofFloat(IVList.get(1),"translationX",
                            -ScreenUtils.forceGetViewSize(IVList.get(1))[0]*7/10 ,WIDTH)));
                    setAnimatiorParams(OAList.get(1),999,55000,ValueAnimator.RESTART,new LinearInterpolator());
                    OAList.get(1).start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            OAList.get(1).start();
            //第三条云
            setLayoutParams(WRAP,WRAP,true, 0,HIGTH/2 ,IVList.get(2));
            OAList.add(ObjectAnimator.ofFloat(IVList.get(2),"translationX",
                    -ScreenUtils.forceGetViewSize(IVList.get(2))[0]*3/5,WIDTH));
            setAnimatiorParams(OAList.get(2),999,74000,ValueAnimator.RESTART,new LinearInterpolator());
            OAList.get(2).start();
            //第四条云
            setLayoutParams(WRAP,WRAP,true, 0,HIGTH/2 ,IVList.get(3));
            OAList.add(ObjectAnimator.ofFloat(IVList.get(3),"translationX",
                    -ScreenUtils.forceGetViewSize(IVList.get(3))[0]*3/5,WIDTH));
            setAnimatiorParams(OAList.get(3),999,74000,ValueAnimator.RESTART,new LinearInterpolator());
            OAList.get(3).start();
        }else {
            IVList.get(0).setImageResource(R.drawable.cloudy_night1);
            IVList.get(1).setImageResource(R.drawable.cloudy_night2);
            //第一条云
            setLayoutParams(WRAP,WRAP,true,0,DensityUtils.dpTopx(mContext,35) ,IVList.get(0));
            OAList.add(ObjectAnimator.ofFloat(IVList.get(0),"translationX", 0,WIDTH));
            setAnimatiorParams(OAList.get(0),0,50000,ValueAnimator.RESTART,new LinearInterpolator());
            OAList.get(0).addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    OAList.set(0,ObjectAnimator.ofFloat(IVList.get(0),"translationX",
                            -ScreenUtils.forceGetViewSize(IVList.get(0))[0]*9/10,WIDTH));
                    setAnimatiorParams(OAList.get(0),999,65000,ValueAnimator.RESTART,new LinearInterpolator());
                    OAList.get(0).start();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            OAList.get(0).start();
            //第二条云
            setLayoutParams(WRAP,WRAP,true,0,HIGTH/2 ,IVList.get(1));
            OAList.add(ObjectAnimator.ofFloat(IVList.get(1),"translationX",
                    -ScreenUtils.forceGetViewSize(IVList.get(1))[0]*4/5,WIDTH));
            setAnimatiorParams(OAList.get(1),999,60000,ValueAnimator.RESTART,new LinearInterpolator());
            OAList.get(1).start();
        }
    }

    /**
     * 设置雨动画
     * 雨滴下落速度为1，动画持续时间=距离
     */
    private static void rainAnimator(){
        for (int i=0;i<30;i++){
            int y = random.nextInt(HIGTH/5)-HIGTH/5;
            setLayoutParams(WRAP,WRAP,true,random.nextInt(WIDTH),y,IVList.get(i));
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(IVList.get(i),"translationY",y,HIGTH-y);
            setAnimatiorParams(objectAnimator,9999,HIGTH-y*2,ValueAnimator.RESTART,new LinearInterpolator());
            objectAnimator.start();
        }
        for (int i=30;i<60;i++){
            int y = random.nextInt(HIGTH/5)-HIGTH*2/5;
            setLayoutParams(WRAP,WRAP,true,random.nextInt(WIDTH),y,IVList.get(i));
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(IVList.get(i),"translationY",y,HIGTH-y);
            setAnimatiorParams(objectAnimator,9999,HIGTH-y*2,ValueAnimator.RESTART,new LinearInterpolator());
            objectAnimator.start();
        }
        for (int i=60;i<90;i++){
            int y = random.nextInt(HIGTH/5)-HIGTH*3/5;
            setLayoutParams(WRAP,WRAP,true,random.nextInt(WIDTH),y,IVList.get(i));
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(IVList.get(i),"translationY",y,HIGTH-y);
            setAnimatiorParams(objectAnimator,9999,HIGTH-y*2,ValueAnimator.RESTART,new LinearInterpolator());
            objectAnimator.start();
        }
        for (int i=90;i<120;i++){
            int y = random.nextInt(HIGTH/5)-HIGTH*4/5;
            setLayoutParams(WRAP,WRAP,true,random.nextInt(WIDTH),y,IVList.get(i));
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(IVList.get(i),"translationY",y,HIGTH-y);
            setAnimatiorParams(objectAnimator,9999,HIGTH-y*2,ValueAnimator.RESTART,new LinearInterpolator());
            objectAnimator.start();
        }
        for (int i=120;i<150;i++){
            int y = random.nextInt(HIGTH/5)-HIGTH;
            setLayoutParams(WRAP,WRAP,true,random.nextInt(WIDTH),y,IVList.get(i));
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(IVList.get(i),"translationY",y,HIGTH-y);
            setAnimatiorParams(objectAnimator,9999,HIGTH-y*2,ValueAnimator.RESTART,new LinearInterpolator());
            objectAnimator.start();
        }
    }

    /**
     * 设置动画参数
     * @param objectAnimator
     * @param count
     * @param time
     * @param mode
     * @param timeInterpolator
     */
    private static void setAnimatiorParams(ObjectAnimator objectAnimator, int count, int time,
                   int mode, TimeInterpolator timeInterpolator){
        objectAnimator.setRepeatCount(count);
        objectAnimator.setRepeatMode(mode);
        if (time != 0){
            objectAnimator.setDuration(time);
        }
        objectAnimator.setInterpolator(timeInterpolator);
    }

    /**
     * 设置view的宽高及x,y坐标
     * @param width
     * @param hight
     * @param setXY 是否设置x,y坐标
     * @param x
     * @param y
     * @param view
     */
    private static void setLayoutParams(int width,int hight,boolean setXY,int x,int y,View view){
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(width, hight);
        if (setXY){
            param.setMargins(x,y,0,0);
        }
        view.setLayoutParams(param);
    }

    /**
     * 根据天气状态、空气质量返回对应的图标
     * @param state           天气状态
     * @param dayOrNight      白天或者夜晚，白天为true
     * @param isFuzzy         是否模糊背景图片，true为模糊
     * @param isSetBackGround 是否切换背景图片，true为设置
     * @return
     */
    public int selectWeatherPic(String state, boolean dayOrNight, boolean isFuzzy, boolean isSetBackGround) {
        int result = 0;
        int background = 0;
        switch (state) {
            case "晴":
                if (dayOrNight) {
                    background = isFuzzy ? R.drawable.bg_fine_day_blur : R.drawable.bg_fine_day;
                } else {
                    background = isFuzzy ? R.drawable.bg_fine_night_blur : R.drawable.bg_fine_night;
                }
                result = dayOrNight ? R.drawable.notify_ic_sunny : R.drawable.notify_ic_nightsunny;
                break;
            case "多云":
                if (dayOrNight) {
                    background = isFuzzy ? R.drawable.bg_cloudy_day_blur : R.drawable.bg_cloudy_day;
                } else {
                    background = isFuzzy ? R.drawable.bg_fine_night_blur : R.drawable.bg_fine_night;
                }
                result = dayOrNight ? R.drawable.notify_ic_cloudy : R.drawable.notify_ic_nightcloudy;
                break;
            case "少云":
                if (dayOrNight) {
                    background = isFuzzy ? R.drawable.bg_cloudy_day_blur : R.drawable.bg_cloudy_day;
                } else {
                    background = isFuzzy ? R.drawable.bg_fine_night_blur : R.drawable.bg_fine_night;
                }
                result = dayOrNight ? R.drawable.notify_ic_cloudy : R.drawable.notify_ic_nightcloudy;
                break;
            case "晴间多云":
                if (dayOrNight) {
                    background = isFuzzy ? R.drawable.bg_cloudy_day_blur : R.drawable.bg_cloudy_day;
                } else {
                    background = isFuzzy ? R.drawable.bg_fine_night_blur : R.drawable.bg_fine_night;
                }
                result = dayOrNight ? R.drawable.notify_ic_cloudy : R.drawable.notify_ic_nightcloudy;
                break;
            case "阴":
                background = isFuzzy ? R.drawable.bg_overcast_blur : R.drawable.bg_overcast;
                result = R.drawable.notify_ic_overcast;
                break;
            case "阵雨":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = dayOrNight ? R.drawable.notify_ic_shower : R.drawable.notify_ic_nightrain;
                break;
            case "强阵雨":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = dayOrNight ? R.drawable.notify_ic_shower : R.drawable.notify_ic_nightrain;
                break;
            case "雷阵雨":
                background = isFuzzy ? R.drawable.bg_thunder_storm : R.drawable.bg_thunder_storm;
                result = R.drawable.notify_ic_thundeshower;
                break;
            case "强雷阵雨":
                background = isFuzzy ? R.drawable.bg_thunder_storm : R.drawable.bg_thunder_storm;
                result = R.drawable.notify_ic_thundeshower;
                break;
            case "雷阵雨伴有冰雹":
                background = isFuzzy ? R.drawable.bg_thunder_storm : R.drawable.bg_thunder_storm;
                result = R.drawable.notify_ic_thundeshowerhail;
                break;
            case "小雨":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = R.drawable.notify_ic_lightrain;
                break;
            case "毛毛雨":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = R.drawable.notify_ic_lightrain;
                break;
            case "中雨":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = R.drawable.notify_ic_moderraterain;
                break;
            case "大雨":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = R.drawable.notify_ic_moderraterain;
                break;
            case "极端降雨":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = R.drawable.notify_ic_heavyrain;
                break;
            case "暴雨":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = R.drawable.notify_ic_heavyrain;
                break;
            case "大暴雨":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = R.drawable.notify_ic_heavyrain;
                break;
            case "特大暴雨":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = R.drawable.notify_ic_heavyrain;
                break;
            case "冻雨":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = R.drawable.notify_ic_sleet;
                break;
            case "阵雪":
                if (dayOrNight) {
                    background = isFuzzy ? R.drawable.bg_snow_blur : R.drawable.bg_snow;
                } else {
                    background = isFuzzy ? R.drawable.bg_snow_night_blur : R.drawable.bg_snow_night;
                }
                result = dayOrNight ? R.drawable.notify_ic_shower : R.drawable.notify_ic_nightsown;
                break;
            case "阵雨夹雪":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = R.drawable.notify_ic_rainsnow;
                break;
            case "雨雪天气":
                if (dayOrNight) {
                    background = isFuzzy ? R.drawable.bg_snow_blur : R.drawable.bg_snow;
                } else {
                    background = isFuzzy ? R.drawable.bg_snow_night_blur : R.drawable.bg_snow_night;
                }
                result = R.drawable.notify_ic_rainsnow;
                break;
            case "雨夹雪":
                background = isFuzzy ? R.drawable.bg_rain_blur : R.drawable.bg_rain;
                result = R.drawable.notify_ic_rainsnow;
                break;
            case "小雪":
                if (dayOrNight) {
                    background = isFuzzy ? R.drawable.bg_snow_blur : R.drawable.bg_snow;
                } else {
                    background = isFuzzy ? R.drawable.bg_snow_night_blur : R.drawable.bg_snow_night;
                }
                result = R.drawable.notify_ic_lightsnow;
                break;
            case "中雪":
                if (dayOrNight) {
                    background = isFuzzy ? R.drawable.bg_snow_blur : R.drawable.bg_snow;
                } else {
                    background = isFuzzy ? R.drawable.bg_snow_night_blur : R.drawable.bg_snow_night;
                }
                result = R.drawable.notify_ic_lightsnow;
                break;
            case "大雪":
                if (dayOrNight) {
                    background = isFuzzy ? R.drawable.bg_snow_blur : R.drawable.bg_snow;
                } else {
                    background = isFuzzy ? R.drawable.bg_snow_night_blur : R.drawable.bg_snow_night;
                }
                result = R.drawable.notify_ic_heavysnow;
                break;
            case "暴雪":
                if (dayOrNight) {
                    background = isFuzzy ? R.drawable.bg_snow_blur : R.drawable.bg_snow;
                } else {
                    background = isFuzzy ? R.drawable.bg_snow_night_blur : R.drawable.bg_snow_night;
                }
                result = R.drawable.notify_ic_heavysnow;
                break;
            case "薄雾":
                background = isFuzzy ? R.drawable.bg_fog_blur : R.drawable.bg_fog;
                result = R.drawable.notify_ic_fog;
                break;
            case "雾":
                background = isFuzzy ? R.drawable.bg_fog_blur : R.drawable.bg_fog;
                result = R.drawable.notify_ic_fog;
                break;
            case "霾":
                background = isFuzzy ? R.drawable.bg_haze_blur : R.drawable.bg_haze;
                result = dayOrNight ? R.drawable.notify_ic_haze : R.drawable.notify_ic_nighthaze;
                break;
            case "扬沙":
                background = isFuzzy ? R.drawable.bg_sand_storm_blur : R.drawable.bg_sand_storm;
                result = R.drawable.notify_ic_dust;
                break;
            case "浮尘":
                background = isFuzzy ? R.drawable.bg_sand_storm_blur : R.drawable.bg_sand_storm;
                result = R.drawable.notify_ic_dust;
                break;
            case "沙尘暴":
                background = isFuzzy ? R.drawable.bg_sand_storm_blur : R.drawable.bg_sand_storm;
                result = R.drawable.notify_ic_sandstorm;
                break;
            case "强沙尘暴":
                background = isFuzzy ? R.drawable.bg_sand_storm_blur : R.drawable.bg_sand_storm;
                result = R.drawable.notify_ic_sandstorm;
                break;
            case "优":
                result = R.drawable.ic_pm25_01;
                break;
            case "良":
                result = R.drawable.ic_pm25_02;
                break;
            case "轻度污染":
                result = R.drawable.ic_pm25_03;
                break;
            case "中度污染":
                result = R.drawable.ic_pm25_04;
                break;
            case "重度污染":
                result = R.drawable.ic_pm25_05;
                break;
            case "东风":
                result = R.drawable.ic_main_wind_e;
                break;
            case "西风":
                result = R.drawable.ic_main_wind_w;
                break;
            case "南风":
                result = R.drawable.ic_main_wind_s;
                break;
            case "北风":
                result = R.drawable.ic_main_wind_n;
                break;
            case "东北风":
                result = R.drawable.ic_main_wind_en;
                break;
            case "西北风":
                result = R.drawable.ic_main_wind_wn;
                break;
            case "西南风":
                result = R.drawable.ic_main_wind_ws;
                break;
            case "东南风":
                result = R.drawable.ic_main_wind_es;
                break;
            default:   //未知
                background = isFuzzy ? R.drawable.bg_na_blur : R.drawable.bg_na;
                result = R.drawable.notify_ic_default;
                break;
        }
        if (isSetBackGround) {
            MyApplication.getMyApplication().getMainActivity().ivBackground.setImageResource(background);
        }
        return result;
    }

    /**
     * 设置趋势图中控件所在的位置
     */
    public void setTemPointCoordinate(List<ForecastWeather> forecastWeatherList,View[] TemLines) {
        int maxTemY = DensityUtils.dpTopx(mContext, 22);             //温度最高点的Y（相对其所在的viewgroup）
        int minTemY = DensityUtils.dpTopx(mContext, 130 - 22 - 8);  //温度最低点的Y(圆点的高度为6)
        //每个点的x坐标（相对其所在的viewgroup）
        int intervalX = WIDTH / 12 - DensityUtils.dpTopx(mContext, 4);
        //点所在的viewgroup相对其父控件的Y值
        int topHight = MyApplication.getMyApplication().getMainActivity().mainRlFirstdayTendency.getTop();
        //通过排序得出6天最高温度与最低温度
        int[] hightTem = new int[]{Integer.valueOf(forecastWeatherList.get(0).getMax_tem()), Integer
                .valueOf(forecastWeatherList.get(1).getMax_tem())
                , Integer.valueOf(forecastWeatherList.get(2).getMax_tem()), Integer.valueOf
                (forecastWeatherList.get(3).getMax_tem()),
                Integer.valueOf(forecastWeatherList.get(4).getMax_tem()), Integer.valueOf
                (forecastWeatherList.get(5).getMax_tem())};
        int[] lowTem = new int[]{Integer.valueOf(forecastWeatherList.get(0).getMin_tem()), Integer.valueOf
                (forecastWeatherList.get(1).getMin_tem())
                , Integer.valueOf(forecastWeatherList.get(2).getMin_tem()), Integer.valueOf
                (forecastWeatherList.get(3).getMin_tem()),
                Integer.valueOf(forecastWeatherList.get(4).getMin_tem()), Integer.valueOf
                (forecastWeatherList.get(5).getMin_tem())};
        Arrays.sort(hightTem);
        Arrays.sort(lowTem);
        //先移除视图中动态添加的所有view
        MyApplication.getMyApplication().getMainActivity().mainRlFirstdayTendency.removeAllViews();
        MyApplication.getMyApplication().getMainActivity().rlSeconddayTendency.removeAllViews();
        MyApplication.getMyApplication().getMainActivity().rlThirddayTendency.removeAllViews();
        MyApplication.getMyApplication().getMainActivity().rlFourthdayTendency.removeAllViews();
        MyApplication.getMyApplication().getMainActivity().rlFifthdayTendency.removeAllViews();
        MyApplication.getMyApplication().getMainActivity().rlSixthdayTendency.removeAllViews();
        for (int i = 0; i < 10; i++) {
            MyApplication.getMyApplication().getMainActivity().mainLlForecastWeather.removeView(TemLines[i]);
        }
        //将温度点和温度值添加到相应的RelativeLayout中
        int firstHightY = minTemY - (Integer.valueOf(forecastWeatherList.get(0).getMax_tem()) - lowTem[0])
                * (minTemY - maxTemY) / (hightTem[5] - lowTem[0]);
        setLayout(intervalX, firstHightY, true, 0, mContext,forecastWeatherList);
        int firstLowY = minTemY - (Integer.valueOf(forecastWeatherList.get(0).getMin_tem()) - lowTem[0]) *
                (minTemY - maxTemY) / (hightTem[5] - lowTem[0]);
        setLayout(intervalX, firstLowY, false, 0, mContext,forecastWeatherList);

        int secondHightY = minTemY - (Integer.valueOf(forecastWeatherList.get(1).getMax_tem()) - lowTem[0])
                * (minTemY - maxTemY) / (hightTem[5] - lowTem[0]);
        setLayout(intervalX, secondHightY, true, 1, mContext,forecastWeatherList);
        int secondLowY = minTemY - (Integer.valueOf(forecastWeatherList.get(1).getMin_tem()) - lowTem[0]) *
                (minTemY - maxTemY) / (hightTem[5] - lowTem[0]);
        setLayout(intervalX, secondLowY, false, 1, mContext,forecastWeatherList);

        int thirdHightY = minTemY - (Integer.valueOf(forecastWeatherList.get(2).getMax_tem()) - lowTem[0])
                * (minTemY - maxTemY) / (hightTem[5] - lowTem[0]);
        setLayout(intervalX, thirdHightY, true, 2, mContext,forecastWeatherList);
        int thirdLowY = minTemY - (Integer.valueOf(forecastWeatherList.get(2).getMin_tem()) - lowTem[0]) *
                (minTemY - maxTemY) / (hightTem[5] - lowTem[0]);
        setLayout(intervalX, thirdLowY, false, 2, mContext,forecastWeatherList);

        int fourthHightY = minTemY - (Integer.valueOf(forecastWeatherList.get(3).getMax_tem()) - lowTem[0])
                * (minTemY - maxTemY) / (hightTem[5] - lowTem[0]);
        setLayout(intervalX, fourthHightY, true, 3, mContext,forecastWeatherList);
        int fourthLowY = minTemY - (Integer.valueOf(forecastWeatherList.get(3).getMin_tem()) - lowTem[0]) *
                (minTemY - maxTemY) / (hightTem[5] - lowTem[0]);
        setLayout(intervalX, fourthLowY, false, 3, mContext,forecastWeatherList);

        int fifthHightY = minTemY - (Integer.valueOf(forecastWeatherList.get(4).getMax_tem()) - lowTem[0])
                * (minTemY - maxTemY) / (hightTem[5] - lowTem[0]);
        setLayout(intervalX, fifthHightY, true, 4, mContext,forecastWeatherList);

        int fifthLowY = minTemY - (Integer.valueOf(forecastWeatherList.get(4).getMin_tem()) - lowTem[0]) *
                (minTemY - maxTemY) / (hightTem[5] - lowTem[0]);
        setLayout(intervalX, fifthLowY, false, 4, mContext,forecastWeatherList);

        int sixthHightY = minTemY - (Integer.valueOf(forecastWeatherList.get(5).getMax_tem()) - lowTem[0])
                * (minTemY - maxTemY) / (hightTem[5] - lowTem[0]);
        setLayout(intervalX, sixthHightY, true, 5, mContext,forecastWeatherList);

        int sixthLowY = minTemY - (Integer.valueOf(forecastWeatherList.get(5).getMin_tem()) - lowTem[0]) *
                (minTemY - maxTemY) / (hightTem[5] - lowTem[0]);
        setLayout(intervalX, sixthLowY, false, 5, mContext,forecastWeatherList);
        //将直线添加到FrameLayout中（将点用直线连接起来）
        int[] firstHightXY = new int[2];     //上面第一条线的起点
        int[] firstLowXY = new int[2];       //下面第一条线的起点
        int[] secondHightXY = new int[2];    //上面第二条线的起点（第一条线的终点）
        int[] secondLowXY = new int[2];      //下面第二条线的起点（第一条线的终点）
        int[] thridHightXY = new int[2];
        int[] thridLowXY = new int[2];
        int[] fourthHightXY = new int[2];
        int[] fourthLowXY = new int[2];
        int[] fifthHightXY = new int[2];
        int[] fifthLowXY = new int[2];
        int[] sixthHightXY = new int[2];
        int[] sixthLowXY = new int[2];
        firstHightXY[0] = intervalX + DensityUtils.dpTopx(mContext, 3);                //3为圆点的半径
        firstHightXY[1] = firstHightY + topHight + DensityUtils.dpTopx(mContext, 7);   //7为圆点的半径 +
        // RelativeLayout的paddingtop
        secondHightXY[0] = firstHightXY[0] + WIDTH / 6;
        secondHightXY[1] = secondHightY + topHight + DensityUtils.dpTopx(mContext, 7);
        thridHightXY[0] = secondHightXY[0] + WIDTH / 6;
        thridHightXY[1] = thirdHightY + topHight + DensityUtils.dpTopx(mContext, 7);
        fourthHightXY[0] = thridHightXY[0] + WIDTH / 6;
        fourthHightXY[1] = fourthHightY + topHight + DensityUtils.dpTopx(mContext, 7);
        fifthHightXY[0] = fourthHightXY[0] + WIDTH / 6;
        fifthHightXY[1] = fifthHightY + topHight + DensityUtils.dpTopx(mContext, 7);
        sixthHightXY[0] = fifthHightXY[0] + WIDTH / 6;
        sixthHightXY[1] = sixthHightY + topHight + DensityUtils.dpTopx(mContext, 7);

        firstLowXY[0] = intervalX + DensityUtils.dpTopx(mContext, 3);                //3为圆点的半径
        firstLowXY[1] = firstLowY + topHight + DensityUtils.dpTopx(mContext, 7);   //7为圆点的半径 +
        // RelativeLayout的paddingtop
        secondLowXY[0] = firstHightXY[0] + WIDTH / 6;
        secondLowXY[1] = secondLowY + topHight + DensityUtils.dpTopx(mContext, 7);
        thridLowXY[0] = secondHightXY[0] + WIDTH / 6;
        thridLowXY[1] = thirdLowY + topHight + DensityUtils.dpTopx(mContext, 7);
        fourthLowXY[0] = thridHightXY[0] + WIDTH / 6;
        fourthLowXY[1] = fourthLowY + topHight + DensityUtils.dpTopx(mContext, 7);
        fifthLowXY[0] = fourthHightXY[0] + WIDTH / 6;
        fifthLowXY[1] = fifthLowY + topHight + DensityUtils.dpTopx(mContext, 7);
        sixthLowXY[0] = fifthHightXY[0] + WIDTH / 6;
        sixthLowXY[1] = sixthLowY + topHight + DensityUtils.dpTopx(mContext, 7);

        TendencyLineView tendencyLineView1 = new TendencyLineView(mContext,
                firstHightXY[0], firstHightXY[1], secondHightXY[0], secondHightXY[1], true);  //连接点的线
        TendencyLineView tendencyLineView2 = new TendencyLineView(mContext,
                secondHightXY[0], secondHightXY[1], thridHightXY[0], thridHightXY[1], true);  //连接点的线
        TendencyLineView tendencyLineView3 = new TendencyLineView(mContext,
                thridHightXY[0], thridHightXY[1], fourthHightXY[0], fourthHightXY[1], true);  //连接点的线
        TendencyLineView tendencyLineView4 = new TendencyLineView(mContext,
                fourthHightXY[0], fourthHightXY[1], fifthHightXY[0], fifthHightXY[1], true);  //连接点的线
        TendencyLineView tendencyLineView5 = new TendencyLineView(mContext,
                fifthHightXY[0], fifthHightXY[1], sixthHightXY[0], sixthHightXY[1], true);  //连接点的线

        TendencyLineView tendencyLineView6 = new TendencyLineView(mContext,
                firstLowXY[0], firstLowXY[1], secondLowXY[0], secondLowXY[1], false);  //连接点的线
        TendencyLineView tendencyLineView7 = new TendencyLineView(mContext,
                secondLowXY[0], secondLowXY[1], thridLowXY[0], thridLowXY[1], false);  //连接点的线
        TendencyLineView tendencyLineView8 = new TendencyLineView(mContext,
                thridLowXY[0], thridLowXY[1], fourthLowXY[0], fourthLowXY[1], false);  //连接点的线
        TendencyLineView tendencyLineView9 = new TendencyLineView(mContext,
                fourthLowXY[0], fourthLowXY[1], fifthLowXY[0], fifthLowXY[1], false);  //连接点的线
        TendencyLineView tendencyLineView10 = new TendencyLineView(mContext,
                fifthLowXY[0], fifthLowXY[1], sixthLowXY[0], sixthLowXY[1], false);  //连接点的线
        //将创建的view添加到数组，便于刷新时清除
        TemLines[0] = tendencyLineView1;
        TemLines[1] = tendencyLineView2;
        TemLines[2] = tendencyLineView3;
        TemLines[3] = tendencyLineView4;
        TemLines[4] = tendencyLineView5;
        TemLines[5] = tendencyLineView6;
        TemLines[6] = tendencyLineView7;
        TemLines[7] = tendencyLineView8;
        TemLines[8] = tendencyLineView9;
        TemLines[9] = tendencyLineView10;
        //将线添加到界面中
        for (int i = 0; i < 10; i++) {
            MyApplication.getMyApplication().getMainActivity().mainLlForecastWeather.addView(TemLines[i]);
        }
    }

    /**
     * 向界面中添加趋势图中的温度点和textview
     */
    public void setLayout(int x, int y, boolean dayOrNight, int index, Context mContext,
                          List<ForecastWeather> forecastWeatherList) {
        View viewPoint = new View(mContext);          //点
        TextView textView = new TextView(mContext);   //温度
        textView.setTextColor(mContext.getResources().getColor(R.color.white));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        textView.setGravity(Gravity.CENTER);
        //先将view添加到指定的viewgroup中
        switch (index) {
            case 0:
                MyApplication.getMyApplication().getMainActivity().mainRlFirstdayTendency.addView(viewPoint);
                MyApplication.getMyApplication().getMainActivity().mainRlFirstdayTendency.addView(textView);
                break;
            case 1:
                MyApplication.getMyApplication().getMainActivity().rlSeconddayTendency.addView(viewPoint);
                MyApplication.getMyApplication().getMainActivity().rlSeconddayTendency.addView(textView);
                break;
            case 2:
                MyApplication.getMyApplication().getMainActivity().rlThirddayTendency.addView(viewPoint);
                MyApplication.getMyApplication().getMainActivity().rlThirddayTendency.addView(textView);
                break;
            case 3:
                MyApplication.getMyApplication().getMainActivity().rlFourthdayTendency.addView(viewPoint);
                MyApplication.getMyApplication().getMainActivity().rlFourthdayTendency.addView(textView);
                break;
            case 4:
                MyApplication.getMyApplication().getMainActivity().rlFifthdayTendency.addView(viewPoint);
                MyApplication.getMyApplication().getMainActivity().rlFifthdayTendency.addView(textView);
                break;
            case 5:
                MyApplication.getMyApplication().getMainActivity().rlSixthdayTendency.addView(viewPoint);
                MyApplication.getMyApplication().getMainActivity().rlSixthdayTendency.addView(textView);
                break;
        }
        //设置view在指定viewgroup中的宽高及坐标
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                DensityUtils.dpTopx(mContext, 6), DensityUtils.dpTopx(mContext, 6));
        params.setMargins(x, y, 0, 0);    //通过自定义坐标来放置你的控件
        viewPoint.setLayoutParams(params);

        RelativeLayout.LayoutParams paramsTextView = new RelativeLayout.LayoutParams(
                DensityUtils.dpTopx(mContext, 22), DensityUtils.dpTopx(mContext, 17));
        if (dayOrNight) {
            viewPoint.setBackgroundResource(R.drawable.shape_main_tendency_day);
            paramsTextView.setMargins(x - DensityUtils.dpTopx(mContext, 7), y - DensityUtils.dpTopx
                    (mContext, 19), 0, 0);    //通过自定义坐标来放置你的控件
            textView.setText(forecastWeatherList.get(index).getMax_tem() + "°");
        } else {
            viewPoint.setBackgroundResource(R.drawable.shape_main_tendency_night);
            paramsTextView.setMargins(x - DensityUtils.dpTopx(mContext, 7), y + DensityUtils.dpTopx
                    (mContext, 8), 0, 0);    //通过自定义坐标来放置你的控件
            textView.setText(forecastWeatherList.get(index).getMin_tem() + "°");
        }
        textView.setLayoutParams(paramsTextView);
    }
}
