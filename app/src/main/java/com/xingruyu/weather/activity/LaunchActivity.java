package com.xingruyu.weather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.umeng.analytics.MobclickAgent;
import com.xingruyu.weather.config.Constants;
import com.xingruyu.weather.MyApplication;
import com.xingruyu.weather.MyLocationListener;
import com.xingruyu.weather.R;
import com.xingruyu.weather.base.BaseFragmentActivity;
import com.xingruyu.weather.bean.CityInfo;
import com.xingruyu.weather.db.DBManager;
import com.xingruyu.weather.http.GetCityWeather;
import com.xingruyu.weather.utils.LogUtils;
import com.xingruyu.weather.utils.ScreenUtils;
import com.xingruyu.weather.utils.SharedPreferanceUtils;
import com.xingruyu.weather.utils.StatusBarUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 启动界面
 * Created by WDX on 2016/10/16.
 */
public class LaunchActivity extends BaseFragmentActivity {

    @BindView(R.id.launch_iv_launch)
    ImageView mIv_Launch;
    @BindView(R.id.launch_vp_welcome)
    ViewPager vpWelcome;
    @BindView(R.id.launch_view_page1)
    View viewPage1;
    @BindView(R.id.launch_view_page2)
    View viewPage2;
    @BindView(R.id.launch_view_page3)
    View viewPage3;
    @BindView(R.id.launch_fl_welcome)
    FrameLayout flWelcome;
    @BindView(R.id.launch_btn_open)
    Button btnOpen;
    private int images[];
    private boolean mGotoMain = false;  //是否进入主界面

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        StatusBarUtil.fullScreen(mActivity);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        //开启友盟统计日志加密，防止网络攻击
        MobclickAgent.enableEncrypt(true);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        //声明LocationClient类
        MyApplication.mLocationClient = new LocationClient(mContext);
        //注册定位监听函数
        MyApplication.mLocationClient.registerLocationListener(new MyLocationListener());
        initLocation();
        MyApplication.mLocationClient.start();

        //第一次使用载入数据库
        if ((boolean) SharedPreferanceUtils.get(Constants.firstUse, true)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DBManager.CopySqliteFileFromAssetsToDatabases();
                    } catch (IOException e) {
                        LogUtils.e("LaunchActivity_CopySqliteFileFromAssetsToDatabases",e.toString(),true);
                        e.printStackTrace();
                    }
                }
            });
            thread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
            thread.start();
        }
        //判断默认城市是否为空（第一次使用时，在进入主界面之前就退出app，会为空）
        if (SharedPreferanceUtils.get(Constants.DefaultCity,"").equals("")){
            mGotoMain = false;
        }else {
            //不为空则将城市天气表中的数据读取出来存入全局cityWeatherList
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    DBManager.getAllWeatherToList();
                }
            });
            thread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
            thread.start();
            mGotoMain = true;
        }

        // 启动透明度动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if ((boolean) SharedPreferanceUtils.get(Constants.firstUse, true)) {
                    goWelcome();
                } else {
                    //在Activity切换时，载入淡出淡入的动画效果
                    overridePendingTransition(R.anim.set_startactivity, R.anim.set_finishactivity);
                    if (mGotoMain){
                        CityInfo cityInfo = null;
                        for (int i=0;i<MyApplication.cityWeatherList.size();i++){
                            if (MyApplication.cityWeatherList.get(i).getId().equals(SharedPreferanceUtils.get(Constants.DefaultCity,""))){
                                MyApplication.updateIndex = i ;
                                cityInfo = new CityInfo(MyApplication.cityWeatherList.get(i).getId(),
                                        MyApplication.cityWeatherList.get(i).getDistrict(),MyApplication.cityWeatherList.get(i).getCity(),
                                        MyApplication.cityWeatherList.get(i).getProvince());
                                break;
                            }
                        }
                        if (cityInfo == null){
                            ToastShowShot("程序出错！");
                            LaunchActivity.this.finish();
                            return;
                        }
                        Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                        intent.putExtra("formLaunchActivity",true);
                        startActivity(intent);
                        if (!SharedPreferanceUtils.get(Constants.DefaultCity,"").equals(SharedPreferanceUtils.get(Constants.LocationCity,""))){
                            //默认城市和定位城市不相同，直接更新天气
                            GetCityWeather.getWeather(cityInfo.getId(), cityInfo.getDistrict(),
                                    cityInfo.getCity(), cityInfo.getProvince(),false,false);
                        }
                    }else {
                        Intent intent = new Intent(LaunchActivity.this, CitySelectActivity.class);
                        intent.putExtra("LaunchActivity",true);
                        startActivity(intent);
                    }
                    LaunchActivity.this.finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mIv_Launch.startAnimation(alphaAnimation);
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(Constants.LOCATION_SPAN);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        MyApplication.mLocationClient.setLocOption(option);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取屏幕宽高(像素)
        MyApplication.screenWidth = ScreenUtils.getScreenWidth(mContext);
        MyApplication.screenHeight = ScreenUtils.getScreenHeight(mContext);
    }

    /**
     * 第一次使用时，走欢迎页
     */
    private void goWelcome() {
        images = new int[]{R.drawable.welcome1, R.drawable.welcome2, R.drawable.welcome3};
        flWelcome.setVisibility(View.VISIBLE);
        vpWelcome.setAdapter(new MyPagerAdadpt());
        vpWelcome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeView(position);
                if (position == 2){
                    btnOpen.setVisibility(View.VISIBLE);
                }else {
                    btnOpen.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        SharedPreferanceUtils.put(Constants.firstUse,false);
    }

    @OnClick(R.id.launch_btn_open)
    public void onClick() {
        //在Activity切换时，载入淡出淡入的动画效果
        overridePendingTransition(R.anim.set_startactivity, R.anim.set_finishactivity);
        Intent intent = new Intent(LaunchActivity.this, CitySelectActivity.class);
        intent.putExtra("LaunchActivity",true);
        startActivity(intent);
        LaunchActivity.this.finish();
    }

    /**
     * viewPager适配器
     */
    class MyPagerAdadpt extends PagerAdapter {

        /**
         * 初始化指定位置的页面
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(LaunchActivity.this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(images[position]);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        /**
         * 判断一个视图是否与一个给定的key相对应
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }

    /**
     * 界面切换时改变底部小圆点
     */
    private void changeView(int position) {
        switch (position) {
            case 0:
                viewPage1.setBackgroundResource(R.drawable.shape_launch_pick_on);
                viewPage2.setBackgroundResource(R.drawable.shape_launch_pick_off);
                viewPage3.setBackgroundResource(R.drawable.shape_launch_pick_off);
                break;
            case 1:
                viewPage1.setBackgroundResource(R.drawable.shape_launch_pick_off);
                viewPage2.setBackgroundResource(R.drawable.shape_launch_pick_on);
                viewPage3.setBackgroundResource(R.drawable.shape_launch_pick_off);
                break;
            case 2:
                viewPage1.setBackgroundResource(R.drawable.shape_launch_pick_off);
                viewPage2.setBackgroundResource(R.drawable.shape_launch_pick_off);
                viewPage3.setBackgroundResource(R.drawable.shape_launch_pick_on);
                break;
        }
    }
}
