package com.xingruyu.weather.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.xingruyu.weather.MyApplication;
import com.xingruyu.weather.MyLocationListener;
import com.xingruyu.weather.R;
import com.xingruyu.weather.adapter.AddedCityAdapt;
import com.xingruyu.weather.adapter.ForecastWeatherAdapter;
import com.xingruyu.weather.base.BaseFragmentActivity;
import com.xingruyu.weather.bean.CityInfo;
import com.xingruyu.weather.bean.CityWeather;
import com.xingruyu.weather.bean.ForecastWeather;
import com.xingruyu.weather.config.Constants;
import com.xingruyu.weather.db.DBManager;
import com.xingruyu.weather.http.GetCityWeather;
import com.xingruyu.weather.utils.DensityUtils;
import com.xingruyu.weather.utils.MainAssistUtils;
import com.xingruyu.weather.utils.NetUtils;
import com.xingruyu.weather.utils.ScreenUtils;
import com.xingruyu.weather.utils.SharedPreferanceUtils;
import com.xingruyu.weather.utils.StatusBarUtil;
import com.xingruyu.weather.utils.WXUtil;
import com.xingruyu.weather.view.CircleImageView;
import com.xingruyu.weather.view.ObserveScrollView;
import com.xingruyu.weather.view.SunriseSunsetView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xingruyu.weather.R.id.main_iv_tendency_firstday_d_describe;
import static com.xingruyu.weather.R.id.main_tv_today_temperature;

/**
 * APP主界面，即天气详情界面
 * Created by WDX on 2016/10/17.
 */
public class MainActivity extends BaseFragmentActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.main_iv_menu)
    ImageView ivMenu;
    @BindView(R.id.main_tv_city)
    TextView tvCity;
    @BindView(R.id.main_iv_location)
    ImageView ivLocation;
    @BindView(R.id.main_tv_refresh_time)
    TextView tvRefreshTime;
    @BindView(R.id.main_iv_share)
    ImageView ivShare;
    @BindView(R.id.main_iv_pm5)
    ImageView ivPm5;
    @BindView(R.id.main_tv_pm5)
    TextView tvPm5;
    @BindView(R.id.main_view_interval)
    View viewInterval;
    @BindView(R.id.main_tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.main_tv_describe)
    TextView tvDescribe;
    @BindView(R.id.main_iv_wind_direction)
    ImageView ivWindDirection;
    @BindView(R.id.main_tv_wind_power)
    TextView tvWindPower;
    @BindView(R.id.main_tv_humidity)
    TextView tvHumidity;
    @BindView(R.id.main_ll_wind_hum)
    LinearLayout llWindHum;
    @BindView(R.id.main_tv_sendible)
    TextView tvSendible;
    @BindView(R.id.main_tv_pressure)
    TextView tvPressure;
    @BindView(R.id.main_ll_pressure_sendible)
    LinearLayout llPressureSendible;
    @BindView(R.id.main_tv_today)
    TextView tvToday;
    @BindView(main_tv_today_temperature)
    TextView tvTodayTemperature;
    @BindView(R.id.main_tv_today_describe)
    TextView tvTodayDescribe;
    @BindView(R.id.main_iv_today_describe)
    ImageView ivTodayDescribe;
    @BindView(R.id.main_tv_tomorrow)
    TextView tvTomorrow;
    @BindView(R.id.main_tv_tomorrow_temperature)
    TextView tvTomorrowTemperature;
    @BindView(R.id.main_tv_tomorrow_describe)
    TextView tvTomorrowDescribe;
    @BindView(R.id.main_iv_td_describe)
    ImageView ivTdDescribe;
    @BindView(R.id.main_tv_today_sendible)
    TextView tvTodaySendible;
    @BindView(R.id.main_tv_today_humidity)
    TextView tvTodayHumidity;
    @BindView(R.id.main_tv_today_pressure)
    TextView tvTodayPressure;
    @BindView(R.id.main_tv_today_visibility)
    TextView tvTodayVisibility;
    @BindView(R.id.main_tv_tendency)
    TextView tvTendency;
    @BindView(R.id.main_tv_list)
    TextView tvList;
    @BindView(R.id.main_tv_tendency_firstday)
    TextView tvTendencyFirstday;
    @BindView(R.id.main_tv_tendency_firstday_data)
    TextView tvTendencyFirstdayData;
    @BindView(R.id.main_tv_tendency_firstday_d_describe)
    TextView tvTendencyFirstdayDDescribe;
    @BindView(main_iv_tendency_firstday_d_describe)
    ImageView ivTendencyFirstdayDDescribe;
    @BindView(R.id.main_tv_firstday_d_tem)
    TextView tvFirstdayDTem;
    @BindView(R.id.main_view_firstday_d)
    View viewFirstdayD;
    @BindView(R.id.main_view_firstday_n)
    View viewFirstdayN;
    @BindView(R.id.main_tv_firstday_n_tem)
    TextView tvFirstdayNTem;
    @BindView(R.id.main_iv_tendency_firstday_n_describe)
    ImageView ivTendencyFirstdayNDescribe;
    @BindView(R.id.main_tv_tendency_firstday_n_describe)
    TextView tvTendencyFirstdayNDescribe;
    @BindView(R.id.main_tv_tendency_firstday_wind_direction)
    TextView tvTendencyFirstdayWindDirection;
    @BindView(R.id.main_tv_tendency_firstday_wind_power)
    TextView tvTendencyFirstdayWindPower;
    @BindView(R.id.main_tv_tendency_secondday)
    TextView tvTendencySecondday;
    @BindView(R.id.main_tv_tendency_secondday_data)
    TextView tvTendencySeconddayData;
    @BindView(R.id.main_tv_tendency_secondday_d_describe)
    TextView tvTendencySeconddayDDescribe;
    @BindView(R.id.main_iv_tendency_secondday_d_describe)
    ImageView ivTendencySeconddayDDescribe;
    @BindView(R.id.main_tv_secondday_d_tem)
    TextView tvSeconddayDTem;
    @BindView(R.id.main_view_secondday_d)
    View viewSeconddayD;
    @BindView(R.id.main_view_secondday_n)
    View viewSeconddayN;
    @BindView(R.id.main_tv_secondday_n_tem)
    TextView tvSeconddayNTem;
    @BindView(R.id.main_rl_secondday_tendency)
    public RelativeLayout rlSeconddayTendency;
    @BindView(R.id.main_iv_tendency_secondday_n_describe)
    ImageView ivTendencySeconddayNDescribe;
    @BindView(R.id.main_tv_tendency_secondday_n_describe)
    TextView tvTendencySeconddayNDescribe;
    @BindView(R.id.main_tv_tendency_secondday_wind_direction)
    TextView tvTendencySeconddayWindDirection;
    @BindView(R.id.main_tv_tendency_secondday_wind_power)
    TextView tvTendencySeconddayWindPower;
    @BindView(R.id.main_tv_tendency_thirdday)
    TextView tvTendencyThirdday;
    @BindView(R.id.main_tv_tendency_thirdday_data)
    TextView tvTendencyThirddayData;
    @BindView(R.id.main_tv_tendency_thirdday_d_describe)
    TextView tvTendencyThirddayDDescribe;
    @BindView(R.id.main_iv_tendency_thirdday_d_describe)
    ImageView ivTendencyThirddayDDescribe;
    @BindView(R.id.main_tv_thirdday_d_tem)
    TextView tvThirddayDTem;
    @BindView(R.id.main_view_thirdday_d)
    View viewThirddayD;
    @BindView(R.id.main_view_thirdday_n)
    View viewThirddayN;
    @BindView(R.id.main_tv_thirdday_n_tem)
    TextView tvThirddayNTem;
    @BindView(R.id.main_rl_thirdday_tendency)
    public RelativeLayout rlThirddayTendency;
    @BindView(R.id.main_iv_tendency_thirdday_n_describe)
    ImageView ivTendencyThirddayNDescribe;
    @BindView(R.id.main_tv_tendency_thirdday_n_describe)
    TextView tvTendencyThirddayNDescribe;
    @BindView(R.id.main_tv_tendency_thirdday_wind_direction)
    TextView tvTendencyThirddayWindDirection;
    @BindView(R.id.main_tv_tendency_thirdday_wind_power)
    TextView tvTendencyThirddayWindPower;
    @BindView(R.id.main_tv_tendency_fourthday)
    TextView tvTendencyFourthday;
    @BindView(R.id.main_tv_tendency_fourthday_data)
    TextView tvTendencyFourthdayData;
    @BindView(R.id.main_tv_tendency_fourthday_d_describe)
    TextView tvTendencyFourthdayDDescribe;
    @BindView(R.id.main_iv_tendency_fourthday_d_describe)
    ImageView ivTendencyFourthdayDDescribe;
    @BindView(R.id.main_tv_fourthday_d_tem)
    TextView tvFourthdayDTem;
    @BindView(R.id.main_view_fourthday_d)
    View viewFourthdayD;
    @BindView(R.id.main_view_fourthday_n)
    View viewFourthdayN;
    @BindView(R.id.main_tv_fourthday_n_tem)
    TextView tvFourthdayNTem;
    @BindView(R.id.main_rl_fourthday_tendency)
    public RelativeLayout rlFourthdayTendency;
    @BindView(R.id.main_iv_tendency_fourthday_n_describe)
    ImageView ivTendencyFourthdayNDescribe;
    @BindView(R.id.main_tv_tendency_fourthday_n_describe)
    TextView tvTendencyFourthdayNDescribe;
    @BindView(R.id.main_tv_tendency_fourthday_wind_direction)
    TextView tvTendencyFourthdayWindDirection;
    @BindView(R.id.main_tv_tendency_fourthday_wind_power)
    TextView tvTendencyFourthdayWindPower;
    @BindView(R.id.main_tv_tendency_fifthday)
    TextView tvTendencyFifthday;
    @BindView(R.id.main_tv_tendency_fifthday_data)
    TextView tvTendencyFifthdayData;
    @BindView(R.id.main_tv_tendency_fifthday_d_describe)
    TextView tvTendencyFifthdayDDescribe;
    @BindView(R.id.main_iv_tendency_fifthday_d_describe)
    ImageView ivTendencyFifthdayDDescribe;
    @BindView(R.id.main_tv_fifthday_d_tem)
    TextView tvFifthdayDTem;
    @BindView(R.id.main_view_fifthday_d)
    View viewFifthdayD;
    @BindView(R.id.main_view_fifthday_n)
    View viewFifthdayN;
    @BindView(R.id.main_tv_fifthday_n_tem)
    TextView tvFifthdayNTem;
    @BindView(R.id.main_rl_fifthday_tendency)
    public RelativeLayout rlFifthdayTendency;
    @BindView(R.id.main_iv_tendency_fifthday_n_describe)
    ImageView ivTendencyFifthdayNDescribe;
    @BindView(R.id.main_tv_tendency_fifthday_n_describe)
    TextView tvTendencyFifthdayNDescribe;
    @BindView(R.id.main_tv_tendency_fifthday_wind_direction)
    TextView tvTendencyFifthdayWindDirection;
    @BindView(R.id.main_tv_tendency_fifthday_wind_power)
    TextView tvTendencyFifthdayWindPower;
    @BindView(R.id.main_tv_tendency_sixthday)
    TextView tvTendencySixthday;
    @BindView(R.id.main_tv_tendency_sixthday_data)
    TextView tvTendencySixthdayData;
    @BindView(R.id.main_tv_tendency_sixthday_d_describe)
    TextView tvTendencySixthdayDDescribe;
    @BindView(R.id.main_iv_tendency_sixthday_d_describe)
    ImageView ivTendencySixthdayDDescribe;
    @BindView(R.id.main_tv_sixthday_d_tem)
    TextView tvSixthdayDTem;
    @BindView(R.id.main_view_sixthday_d)
    View viewSixthdayD;
    @BindView(R.id.main_view_sixthday_n)
    View viewSixthdayN;
    @BindView(R.id.main_tv_sixthday_n_tem)
    TextView tvSixthdayNTem;
    @BindView(R.id.main_rl_sixthday_tendency)
    public RelativeLayout rlSixthdayTendency;
    @BindView(R.id.main_iv_tendency_sixthday_n_describe)
    ImageView ivTendencySixthdayNDescribe;
    @BindView(R.id.main_tv_tendency_sixthday_n_describe)
    TextView tvTendencySixthdayNDescribe;
    @BindView(R.id.main_tv_tendency_sixthday_wind_direction)
    TextView tvTendencySixthdayWindDirection;
    @BindView(R.id.main_tv_tendency_sixthday_wind_power)
    TextView tvTendencySixthdayWindPower;
    @BindView(R.id.main_iv_cold)
    ImageView ivCold;
    @BindView(R.id.main_tv_cold_index)
    TextView tvColdIndex;
    @BindView(R.id.main_tv_cold)
    TextView tvCold;
    @BindView(R.id.main_iv_comfort)
    ImageView ivComfort;
    @BindView(R.id.main_tv_comfort_index)
    TextView tvComfortIndex;
    @BindView(R.id.main_tv_comfort)
    TextView tvComfort;
    @BindView(R.id.main_iv_dress)
    ImageView ivDress;
    @BindView(R.id.main_tv_dress_index)
    TextView tvDressIndex;
    @BindView(R.id.main_tv_dress)
    TextView tvDress;
    @BindView(R.id.main_iv_car)
    ImageView ivCar;
    @BindView(R.id.main_tv_car_index)
    TextView tvCarIndex;
    @BindView(R.id.main_tv_car)
    TextView tvCar;
    @BindView(R.id.main_iv_ultravioletrays)
    ImageView ivUltravioletrays;
    @BindView(R.id.main_tv_ultravioletrays_index)
    TextView tvUltravioletraysIndex;
    @BindView(R.id.main_tv_ultravioletrays)
    TextView tvUltravioletrays;
    @BindView(R.id.main_iv_sport)
    ImageView ivSport;
    @BindView(R.id.main_tv_sport_index)
    TextView tvSportIndex;
    @BindView(R.id.main_tv_sport)
    TextView tvSport;
    @BindView(R.id.main_dl_details)
    DrawerLayout dlDetails;
    @BindView(R.id.main_sv_sunrise_sunset)
    SunriseSunsetView svSunrise_sunset;
    @BindView(R.id.main_rl_air_quality)
    RelativeLayout rlAirQuality;
    @BindView(R.id.main_rl_now)
    RelativeLayout rlNow;
    @BindView(R.id.main_ll_Today_tomorrow_preview)
    LinearLayout llTodayTomorrowPreview;
    @BindView(R.id.main_tv_sunrise)
    TextView tvSunrise;
    @BindView(R.id.main_tv_sunset)
    TextView tvSunset;
    @BindView(R.id.main_ll_now_details)
    LinearLayout llNowDetails;
    @BindView(R.id.main_observe_sv)
    ObserveScrollView observeSv;
    @BindView(R.id.main_view_bottom_background)
    View viewBottomBackground;
    @BindView(R.id.main_iv_background)
    public ImageView ivBackground;
    @BindView(R.id.main_rl_top_city)
    RelativeLayout rlTopCity;
    @BindView(R.id.main_iv_top_describe_hide)
    ImageView ivTopDescribeHide;
    @BindView(R.id.main_tv_city_hide)
    TextView tvCityHide;
    @BindView(R.id.main_tv_top_temperature_hide)
    TextView tvTopTemperatureHide;
    @BindView(R.id.main_ll_top_city_hide)
    LinearLayout llTopCityHide;
    @BindView(R.id.main_rl_titlebar)
    RelativeLayout rlTitlebar;
    @BindView(R.id.main_srl_update)
    SwipeRefreshLayout mainSrlUpdate;
    @BindView(R.id.main_ll_forecast_weather)
    public FrameLayout mainLlForecastWeather;
    @BindView(R.id.main_lv_forecast_weather)
    ListView mainLvForecastWeather;
    @BindView(R.id.main_rl_firstday_tendency)
    public RelativeLayout mainRlFirstdayTendency;
    @BindView(R.id.main_ll_top)
    LinearLayout mainLlTop;
    @BindView(R.id.main_rl_animator)
    RelativeLayout mainRlAnimator;
    @BindView(R.id.main_rl_side)
    RelativeLayout mainRlSide;
    @BindView(R.id.main_iv_avatar)
    CircleImageView mainIvAvatar;
    @BindView(R.id.main_iv_addcity)
    ImageView mainIvAddcity;
    @BindView(R.id.main_lv_city_list)
    ListView mainLvCityList;
    @BindView(R.id.main_iv_setting)
    ImageView mainIvSetting;
    @BindView(R.id.main_rl2)
    RelativeLayout mainrl2;
    @BindView(R.id.main_rl1)
    RelativeLayout mainrl1;
    @BindView(R.id.main_iv_deletecity)
    ImageView mainIvDeletecity;

    private GestureDetector mGestureDetector;    //手势检测器
    private UpdateViewReceiver updateViewReceiver;
    private Handler handler;
    private IWXAPI iwxapi;
    private Tencent mTencent;

    private String[] dayOfWeek = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private AddedCityAdapt addedCityAdapt;    //侧边栏城市列表的适配器
    private ForecastWeatherAdapter forecastWeatherAdapter;   //主界面未来天气适配器
    private List<ForecastWeather> forecastWeatherList;   //未来6天的天气
    private CityWeather cityWeather;       //当前天气

    View[] TemLines = new View[10];        //趋势图中温度点间的连线
    private boolean mSunAnim = true;       //是否重新播放太阳升起落下动画
//    private boolean isUpdate = false;   //是否为更新数据
    private boolean dayOrNight = true;     //当前时间是白天还是夜晚，true为白天
    private boolean isRefreshByLocation = false;    //是否根据定位结果更新天气
    private boolean isSetViewCoordinate = true;    //是否设置趋势图各点的坐标
    private boolean firstOpen = true;      //每次打开时，需要动态设置天气列表的高度（只需设置一次）

    /**
     * 注意事项：1、更新天气时，如果是定位的天气，先判断定位结果是否为空
     * 2、城市列表中最后一个城市不能删除
     * 3、在进入该activity之前，已经发起获取天气请求
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTranslucentForDrawerLayout(this,(DrawerLayout) findViewById(R.id.main_dl_details),0);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        MyApplication.getMyApplication().setMainActivity(this);
        forecastWeatherList = new ArrayList<>();
        forecastWeatherAdapter = new ForecastWeatherAdapter(this, forecastWeatherList);
        if (MyApplication.cityWeatherList != null){
            addedCityAdapt = new AddedCityAdapt(this, MyApplication.cityWeatherList);
            mainLvCityList.setAdapter(addedCityAdapt);
        }
        mainLvForecastWeather.setAdapter(forecastWeatherAdapter);
        listViewListener();

        //注册广播接收器
        updateViewReceiver = new UpdateViewReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.UPDATE_MAIN_VIEW_ACTION);
        intentFilter.addAction(Constants.LOCATION_STATE);
        registerReceiver(updateViewReceiver, intentFilter);

        //设置viewInterval的高度
        int viewInterval_hight = MyApplication.screenHeight - ScreenUtils.forceGetViewSize(rlTitlebar)[1] -
                ScreenUtils.forceGetViewSize(rlAirQuality)[1] - ScreenUtils.forceGetViewSize(rlNow)[1] -
                ScreenUtils.forceGetViewSize(llTodayTomorrowPreview)[1] - StatusBarUtil.getStatusBarHeight
                (mContext) - DensityUtils.dpTopx(mContext, 12);
        //根据要操作的view在布局文件中的父控件的类型选择LinearLayout、RelativeLayout或ViewGroup
        LinearLayout.LayoutParams linearLayoutParams = (LinearLayout.LayoutParams) viewInterval
                .getLayoutParams();
        linearLayoutParams.height = viewInterval_hight;
        viewInterval.setLayoutParams(linearLayoutParams);

        //加载本地天气数据
        if (!SharedPreferanceUtils.get(Constants.DefaultCity, "").equals("")) {
            fillData();
        }

        mGestureDetector = new GestureDetector(mContext, gestureListener);
        //ScrollView滑动监听器
        observeSv.setScrollListener(new ObserveScrollView.ScrollListener() {
            @Override
            public void scrollOritention(int x, final int y, int oldX, int oldY) {
                //透明度变化
                if (y <= MyApplication.screenHeight / 2 && y >= 0) {
                    //RGB中144为16进制的90
                    int slpha = y * 288 / MyApplication.screenHeight;
                    viewBottomBackground.setBackgroundColor(Color.argb(slpha, 0, 0, 0));
                }

                if (y > oldY) {    //上滑
                    //日出日落的太阳动画
                    if (oldY == 0 && y > 0 && mSunAnim) {
                        mSunAnim = false;
                        svSunrise_sunset.startAnim();
                    }
                    if (y < MyApplication.screenHeight * 17 / 20 && y > MyApplication.screenHeight * 16 / 20
                            && rlTopCity.getVisibility() == View.VISIBLE && llTopCityHide.getVisibility()
                            == View.GONE) {
                        Animation animation_rlTopCity = new AlphaAnimation(1, 0);
                        animation_rlTopCity.setDuration(100);
                        animation_rlTopCity.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation animation_llTopCityHide_up = new AlphaAnimation(0, 1);
                                animation_llTopCityHide_up.setDuration(200);
                                llTopCityHide.setAnimation(animation_llTopCityHide_up);
                                llTopCityHide.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        rlTopCity.setAnimation(animation_rlTopCity);
                        rlTopCity.setVisibility(View.GONE);
                        MainAssistUtils.getMainAssistUtils().selectWeatherPic(cityWeather.gettxt(), dayOrNight, true, true);  //切换背景为模糊效果
                    }
                    //防止播放动画造成显示错乱
                    if (y > MyApplication.screenHeight * 19 / 20) {
                        rlTopCity.setVisibility(View.GONE);
                        llTopCityHide.setVisibility(View.VISIBLE);
                    }
                } else {     //下滑
                    if (y < MyApplication.screenHeight * 12 / 20 && y > MyApplication.screenHeight * 11 /
                            20 &&
                            rlTopCity.getVisibility() == View.GONE && llTopCityHide.getVisibility() == View
                            .VISIBLE) {
                        Animation animation_llTopCityHide = new AlphaAnimation(1, 0);
                        animation_llTopCityHide.setDuration(100);
                        animation_llTopCityHide.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                Animation animation_rlTopCityDown = new AlphaAnimation(0, 1);
                                animation_rlTopCityDown.setDuration(200);
                                rlTopCity.setAnimation(animation_rlTopCityDown);
                                rlTopCity.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        });
                        llTopCityHide.setAnimation(animation_llTopCityHide);
                        llTopCityHide.setVisibility(View.GONE);
                        MainAssistUtils.getMainAssistUtils().selectWeatherPic(cityWeather.gettxt(), dayOrNight, false, true);  //切换背景为清晰效果
                    }
                    //防止播放动画造成显示错乱
                    if (y < MyApplication.screenHeight * 1 / 20) {
                        rlTopCity.setVisibility(View.VISIBLE);
                        llTopCityHide.setVisibility(View.GONE);
                    }
                }
            }
        });
        //改变加载显示的颜色
        mainSrlUpdate.setColorSchemeColors(Color.GRAY);
        //设置初始时的大小
        mainSrlUpdate.setSize(SwipeRefreshLayout.DEFAULT);
        //设置监听
        mainSrlUpdate.setOnRefreshListener(this);
        //设置向下拉多少出现刷新
        mainSrlUpdate.setDistanceToTriggerSync(160);
        //设置刷新出现的位置
        mainSrlUpdate.setProgressViewEndTarget(false, 200);

        if (NetUtils.isConnected(MyApplication.mContext)) {
            mainSrlUpdate.setRefreshing(true);
        }

        //从启动页进入，并且默认城市为定位城市时，需要重新根据定位结果查询城市信息，再获取天气信息
        if (getIntent().getBooleanExtra("formLaunchActivity", false) && !SharedPreferanceUtils.get
                (Constants.DefaultCity, "").equals("") &&
                SharedPreferanceUtils.get(Constants.DefaultCity, "").equals(SharedPreferanceUtils.get
                        (Constants.LocationCity, ""))) {
            if (MyLocationListener.locationState != null && MyLocationListener.locationState.equals
                    ("定位成功！")) {
                new queryTassk().execute();
            } else if (MyLocationListener.locationState != null) {   //定位失败
                ToastShowLong(MyLocationListener.locationState);
                mainSrlUpdate.setRefreshing(false);
            } else {
                isRefreshByLocation = true;   //定位结果还没有返回
            }
        }

        //启动定时器，每隔5秒切换一次气压风力等数据
        handler = new Handler();
        handler.postDelayed(runnable, 5000);

        //设置侧边栏的宽度
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mainRlSide.getLayoutParams();
        layoutParams.width = MyApplication.screenWidth * 3 / 4;
        mainRlSide.setLayoutParams(layoutParams);
        //设置侧边栏顶部背景的高度
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mainrl2.getLayoutParams();
        params.height = ScreenUtils.forceGetViewSize(mainrl1)[1] ;
        mainrl2.setLayoutParams(params);

        //将应用注册到微信
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID,true);
        iwxapi.registerApp(Constants.WX_APP_ID);
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this.getApplicationContext());
    }

    /**
     * 填充界面数据
     */
    private void fillData() {
        observeSv.fullScroll(ScrollView.FOCUS_UP);   //滚动到顶部
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());  //System.currentTimeMillis()取得系统时间
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 18 || hour < 6) {
            dayOrNight = false;
        }
        //为天气列表及已添加城市列表填充数据
        cityWeather = MyApplication.cityWeatherList.get(MyApplication.updateIndex);
        forecastWeatherList.clear();
        forecastWeatherList.addAll(cityWeather.getForecastWeatherList());
        forecastWeatherAdapter.notifyDataSetChanged();
        if (addedCityAdapt == null){
            addedCityAdapt = new AddedCityAdapt(this, MyApplication.cityWeatherList);
            mainLvCityList.setAdapter(addedCityAdapt);
        }else {
            addedCityAdapt.notifyDataSetChanged();
        }
        if (firstOpen){
            setListViewHeightBasedOnChildren(mainLvForecastWeather);
            firstOpen = false;
        }

        tvCity.setText(cityWeather.getDistrict());
        tvCityHide.setText(cityWeather.getDistrict());
        tvRefreshTime.setText(cityWeather.getLoc().substring(11, 16) + "更新");   //截取时分
        if (cityWeather.getLocation().equals("是")) {
            ivLocation.setVisibility(View.VISIBLE);
        } else {
            ivLocation.setVisibility(View.GONE);
        }
        ivTopDescribeHide.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(cityWeather.gettxt(), dayOrNight, false, false));
        tvTopTemperatureHide.setText(cityWeather.gettmp() + "℃");
        ivPm5.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(cityWeather.getQlty(), dayOrNight, false, false));
        tvPm5.setText(cityWeather.getAqi() + " " + cityWeather.getQlty());
        tvTemperature.setText(cityWeather.gettmp());
        tvDescribe.setText(cityWeather.gettxt());
        ivWindDirection.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(cityWeather.getdir(), dayOrNight, false, false));
        tvWindPower.setText(cityWeather.getsc() + "级");
        tvHumidity.setText(cityWeather.gethum() + "%");
        tvSendible.setText(cityWeather.getfl() + "℃");
        tvPressure.setText(cityWeather.getpres() + "hpa");
        tvTodayTemperature.setText(forecastWeatherList.get(0).getMin_tem() + "~" + forecastWeatherList.get
                (0).getMax_tem() + "℃");
        tvTomorrowTemperature.setText(forecastWeatherList.get(1).getMin_tem() + "~" + forecastWeatherList
                .get(1).getMax_tem() + "℃");
        if (dayOrNight) {
            tvTodayDescribe.setText(forecastWeatherList.get(0).getTxt_d());
            ivTodayDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(0).getTxt_d(),
                    dayOrNight, false, false));
            tvTomorrowDescribe.setText(forecastWeatherList.get(1).getTxt_d());
            ivTdDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(1).getTxt_d(),
                    dayOrNight, false, false));
        } else {
            tvTodayDescribe.setText(forecastWeatherList.get(0).getTxt_n());
            ivTodayDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(0).getTxt_n(),
                    dayOrNight, false, false));
            tvTomorrowDescribe.setText(forecastWeatherList.get(1).getTxt_n());
            ivTdDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(1).getTxt_n(),
                    dayOrNight, false, false));
        }
        //设置日出日落动画参数
        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm");
        try {
            Date srTime = sDateFormat.parse(forecastWeatherList.get(0).getSr());  //日出时间
            Date ssTime = sDateFormat.parse(forecastWeatherList.get(0).getSs());  //日落时间
            Date nowTime = sDateFormat.parse(sDateFormat.format(new Date()));     //当前时间
            if ((nowTime.getTime() - srTime.getTime()) > 0 && (ssTime.getTime() - nowTime.getTime()) > 0) {
                int alreadyMinute = (int) (nowTime.getTime() - srTime.getTime()) / 60000;   //当前已经日出多少分钟
                int totleMinute = (int) (ssTime.getTime() - srTime.getTime()) / 60000;   //今天从日出到日落共多少分钟
                int pointX = svSunrise_sunset.getArcWidth() * alreadyMinute / totleMinute + 30;
                svSunrise_sunset.setStopPoint(pointX);
            } else if ((nowTime.getTime() - ssTime.getTime()) >= 0) {
                svSunrise_sunset.setStopPoint(1000000);   //设置一个很大的值，即动画一直播放到结束才停止
            }
//            mSunAnim = true;   //重新播放日出日落动画
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvSunrise.setText(forecastWeatherList.get(0).getSr());
        tvSunset.setText(forecastWeatherList.get(0).getSs());
        tvTodaySendible.setText(cityWeather.getfl() + "℃");
        tvTodayHumidity.setText(cityWeather.gethum() + "%");
        tvTodayPressure.setText(cityWeather.getpres() + "hpa");
        tvTodayVisibility.setText(cityWeather.getvis() + "km");

        tvTendencyFirstdayData.setText(forecastWeatherList.get(0).getDate().substring(5, 7) + "/" +
                forecastWeatherList.get(0).getDate().substring(8, 10));
        tvTendencyFirstdayDDescribe.setText(forecastWeatherList.get(0).getTxt_d());
        ivTendencyFirstdayDDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(0).getTxt_d()
                , dayOrNight, false, false));
        tvFirstdayDTem.setText(forecastWeatherList.get(0).getMax_tem() + "°");
        tvFirstdayNTem.setText(forecastWeatherList.get(0).getMin_tem() + "°");
        ivTendencyFirstdayNDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(0).getTxt_n()
                , dayOrNight, false, false));
        tvTendencyFirstdayNDescribe.setText(forecastWeatherList.get(0).getTxt_n());
        tvTendencyFirstdayWindDirection.setText(forecastWeatherList.get(0).getDir());
        tvTendencyFirstdayWindPower.setText(forecastWeatherList.get(0).getSc() + "级");

        tvTendencySeconddayData.setText(forecastWeatherList.get(1).getDate().substring(5, 7) + "/" +
                forecastWeatherList.get(1).getDate().substring(8, 10));
        tvTendencySeconddayDDescribe.setText(forecastWeatherList.get(1).getTxt_d());
        ivTendencySeconddayDDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(1).getTxt_d
                (), dayOrNight, false, false));
        tvSeconddayDTem.setText(forecastWeatherList.get(1).getMax_tem() + "°");
        tvSeconddayNTem.setText(forecastWeatherList.get(1).getMin_tem() + "°");
        ivTendencySeconddayNDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(1).getTxt_n
                (), dayOrNight, false, false));
        tvTendencySeconddayNDescribe.setText(forecastWeatherList.get(1).getTxt_n());
        tvTendencySeconddayWindDirection.setText(forecastWeatherList.get(1).getDir());
        tvTendencySeconddayWindPower.setText(forecastWeatherList.get(1).getSc() + "级");

        if ((calendar.get(Calendar.DAY_OF_WEEK) + 1) >= 7) {
            tvTendencyThirdday.setText(dayOfWeek[(calendar.get(Calendar.DAY_OF_WEEK) - 6)]);
        } else {
            tvTendencyThirdday.setText(dayOfWeek[(calendar.get(Calendar.DAY_OF_WEEK) + 1)]);
        }
        tvTendencyThirddayData.setText(forecastWeatherList.get(2).getDate().substring(5, 7) + "/" +
                forecastWeatherList.get(2).getDate().substring(8, 10));
        tvTendencyThirddayDDescribe.setText(forecastWeatherList.get(2).getTxt_d());
        ivTendencyThirddayDDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(2).getTxt_d()
                , dayOrNight, false, false));
        tvThirddayDTem.setText(forecastWeatherList.get(2).getMax_tem() + "°");
        tvThirddayNTem.setText(forecastWeatherList.get(2).getMin_tem() + "°");
        ivTendencyThirddayNDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(2).getTxt_n()
                , dayOrNight, false, false));
        tvTendencyThirddayNDescribe.setText(forecastWeatherList.get(2).getTxt_n());
        tvTendencyThirddayWindDirection.setText(forecastWeatherList.get(2).getDir());
        tvTendencyThirddayWindPower.setText(forecastWeatherList.get(2).getSc() + "级");

        if ((calendar.get(Calendar.DAY_OF_WEEK) + 2) >= 7) {
            tvTendencyFourthday.setText(dayOfWeek[(calendar.get(Calendar.DAY_OF_WEEK) - 5)]);
        } else {
            tvTendencyFourthday.setText(dayOfWeek[(calendar.get(Calendar.DAY_OF_WEEK) + 2)]);
        }
        tvTendencyFourthdayData.setText(forecastWeatherList.get(3).getDate().substring(5, 7) + "/" +
                forecastWeatherList.get(3).getDate().substring(8, 10));
        tvTendencyFourthdayDDescribe.setText(forecastWeatherList.get(3).getTxt_d());
        ivTendencyFourthdayDDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(3).getTxt_d
                (), dayOrNight, false, false));
        tvFourthdayDTem.setText(forecastWeatherList.get(3).getMax_tem() + "°");
        tvFourthdayNTem.setText(forecastWeatherList.get(3).getMin_tem() + "°");
        ivTendencyFourthdayNDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(3).getTxt_n
                (), dayOrNight, false, false));
        tvTendencyFourthdayNDescribe.setText(forecastWeatherList.get(3).getTxt_n());
        tvTendencyFourthdayWindDirection.setText(forecastWeatherList.get(3).getDir());
        tvTendencyFourthdayWindPower.setText(forecastWeatherList.get(3).getSc() + "级");

        if ((calendar.get(Calendar.DAY_OF_WEEK) + 3) >= 7) {
            tvTendencyFifthday.setText(dayOfWeek[(calendar.get(Calendar.DAY_OF_WEEK) - 4)]);
        } else {
            tvTendencyFifthday.setText(dayOfWeek[(calendar.get(Calendar.DAY_OF_WEEK) + 3)]);
        }
        tvTendencyFifthdayData.setText(forecastWeatherList.get(4).getDate().substring(5, 7) + "/" +
                forecastWeatherList.get(4).getDate().substring(8, 10));
        tvTendencyFifthdayDDescribe.setText(forecastWeatherList.get(4).getTxt_d());
        ivTendencyFifthdayDDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(4).getTxt_d()
                , dayOrNight, false, false));
        tvFifthdayDTem.setText(forecastWeatherList.get(4).getMax_tem() + "°");
        tvFifthdayNTem.setText(forecastWeatherList.get(4).getMin_tem() + "°");
        ivTendencyFifthdayNDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(4).getTxt_n()
                , dayOrNight, false, false));
        tvTendencyFifthdayNDescribe.setText(forecastWeatherList.get(4).getTxt_n());
        tvTendencyFifthdayWindDirection.setText(forecastWeatherList.get(4).getDir());
        tvTendencyFifthdayWindPower.setText(forecastWeatherList.get(4).getSc() + "级");

        if ((calendar.get(Calendar.DAY_OF_WEEK) + 4) >= 7) {
            tvTendencySixthday.setText(dayOfWeek[(calendar.get(Calendar.DAY_OF_WEEK) - 3)]);
        } else {
            tvTendencySixthday.setText(dayOfWeek[(calendar.get(Calendar.DAY_OF_WEEK) + 4)]);
        }
        tvTendencySixthdayData.setText(forecastWeatherList.get(5).getDate().substring(5, 7) + "/" +
                forecastWeatherList.get(5).getDate().substring(8, 10));
        tvTendencySixthdayDDescribe.setText(forecastWeatherList.get(5).getTxt_d());
        ivTendencySixthdayDDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(5).getTxt_d()
                , dayOrNight, false, false));
        tvSixthdayDTem.setText(forecastWeatherList.get(5).getMax_tem() + "°");
        tvSixthdayNTem.setText(forecastWeatherList.get(5).getMin_tem() + "°");
        ivTendencySixthdayNDescribe.setImageResource(MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeatherList.get(5).getTxt_n()
                , dayOrNight, false, false));
        tvTendencySixthdayNDescribe.setText(forecastWeatherList.get(5).getTxt_n());
        tvTendencySixthdayWindDirection.setText(forecastWeatherList.get(5).getDir());
        tvTendencySixthdayWindPower.setText(forecastWeatherList.get(5).getSc() + "级");

        tvColdIndex.setText(cityWeather.getFlu_brf());
        tvComfortIndex.setText(cityWeather.getComf_brf());
        tvDressIndex.setText(cityWeather.getDrsg_brf());
        tvCarIndex.setText(cityWeather.getCw_brf());
        tvUltravioletraysIndex.setText(cityWeather.getUv_brf());
        tvSportIndex.setText(cityWeather.getSport_brf());

        MainAssistUtils.getMainAssistUtils().setTemPointCoordinate(forecastWeatherList,TemLines);
        MainAssistUtils.getMainAssistUtils().selectWeatherPic(cityWeather.gettxt(), dayOrNight, false, true);   //根据天气信息切换背景图片
    }

    @OnClick({R.id.main_tv_tendency, R.id.main_tv_list, R.id.main_iv_menu,R.id.main_iv_avatar,
    R.id.main_iv_addcity,R.id.main_iv_deletecity,R.id.main_iv_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_tv_tendency:
                tvTendency.setBackgroundResource(R.drawable.shape_main_select_dark);
                tvList.setBackgroundResource(R.drawable.shape_main_translucence_totle);
                mainLvForecastWeather.setVisibility(View.GONE);
                mainLlForecastWeather.setVisibility(View.VISIBLE);
                break;
            case R.id.main_tv_list:
                tvTendency.setBackgroundResource(R.drawable.shape_main_translucence_totle);
                tvList.setBackgroundResource(R.drawable.shape_main_select_dark);
                mainLlForecastWeather.setVisibility(View.GONE);
                mainLvForecastWeather.setVisibility(View.VISIBLE);
                break;
            case R.id.main_iv_menu:
                dlDetails.openDrawer(Gravity.LEFT);
                break;
            case R.id.main_iv_avatar:
                ToastShowShot("用户系统正在开发中……");
                break;
            case R.id.main_iv_addcity:
                Intent intent = new Intent(MainActivity.this,CitySelectActivity.class);
                intent.putExtra("formMainActivity",true);
                startActivity(intent);
                break;
            case R.id.main_iv_deletecity:
                break;
            case R.id.main_iv_share:
                //截屏
                final Bitmap bitmap = ScreenUtils.snapShotWithStatusBar(this);

                SimpleDateFormat sDateFormat = new SimpleDateFormat("MM-dd");
                String date = sDateFormat.format(new java.util.Date());
                final String content = "今天"+date+"，"+cityWeather.getDistrict()+"，"+
                        cityWeather.gettxt()+"，"+forecastWeatherList.get(0).getMin_tem()+
                        "~"+forecastWeatherList.get(0).getMax_tem()+"℃，当前温度"+
                        cityWeather.gettmp()+"℃。——四季天";

                //显示分享对话框
                final Dialog dialog = new Dialog(MainActivity.this);
                View shareView = getLayoutInflater().inflate(R.layout.dialog_share_weather,null,false);
                (shareView.findViewById(R.id.dialog_share_wxfriend)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!iwxapi.isWXAppInstalled()) {
                            ToastShowShot("您还未安装微信客户端!");
                            return;
                        }
                        WXUtil.share(bitmap,iwxapi,true);
                        dialog.cancel();
                    }
                });
                (shareView.findViewById(R.id.dialog_share_wxgroup)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!iwxapi.isWXAppInstalled()) {
                            ToastShowShot("您还未安装微信客户端!");
                            return;
                        }
                        WXUtil.share(bitmap,iwxapi,false);
                        dialog.cancel();
                    }
                });
                (shareView.findViewById(R.id.dialog_share_qq)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Bundle params = new Bundle();
                        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                        params.putString(QQShare.SHARE_TO_QQ_TITLE, "四季天");
                        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  content);
                        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  "www.baidu.com");
                        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  "四季天");
                        mTencent.shareToQQ(MainActivity.this, params, new IUiListener() {
                                    @Override
                                    public void onComplete(Object o) {
                                    }

                                    @Override
                                    public void onError(UiError uiError) {
                                    }

                                    @Override
                                    public void onCancel() {
                                    }
                                });
                        dialog.cancel();
                    }
                });
                (shareView.findViewById(R.id.dialog_share_sms)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
                        intent.putExtra("sms_body", content);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.setContentView(shareView);
                dialog.show();
                break;
        }
    }

    private void listViewListener(){
        mainLvCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((MyApplication.cityWeatherList.get(position).getId())
                        .equals(SharedPreferanceUtils.get(Constants.DefaultCity,""))){
                    dlDetails.closeDrawers();
                    return;
                }
                SharedPreferanceUtils.put(Constants.DefaultCity,MyApplication.cityWeatherList.get(position).getId());
                MyApplication.updateIndex = position;
                fillData();   //先将数据填充
                mainSrlUpdate.setRefreshing(true);
                dlDetails.closeDrawers();
                //获取新的天气信息
                boolean isLocation = false;
                if (MyApplication.cityWeatherList.get(position).getId().
                        equals(SharedPreferanceUtils.get(Constants.LocationCity,""))){
                    isLocation = true;
                }
                GetCityWeather.getWeather(MyApplication.cityWeatherList.get(position).getId(),
                        MyApplication.cityWeatherList.get(position).getDistrict(),
                        MyApplication.cityWeatherList.get(position).getCity(),
                        MyApplication.cityWeatherList.get(position).getProvince(), false, isLocation);
                observeSv.fullScroll(ScrollView.FOCUS_UP);   //滚动到顶部
            }
        });
        mainLvCityList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (MyApplication.updateIndex == position){
                    ToastShowShot("不能删除默认城市！");
                    return true;
                }
                final Dialog dialog = new Dialog(MainActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_delete_city,null,false);
                (dialogView.findViewById(R.id.dialog_delete_city_tv)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((MyApplication.cityWeatherList.get(position).getId())
                                .equals(SharedPreferanceUtils.get(Constants.LocationCity,""))){
                            SharedPreferanceUtils.put(Constants.LocationCity,"");   //将本地定位城市清空
                        }
                        String id = MyApplication.cityWeatherList.get(position).getId();  //删除城市的ID
                        new deleteTassk().execute(id);
                        if (position < MyApplication.updateIndex){
                            MyApplication.updateIndex = MyApplication.updateIndex - 1;
                        }
                        MyApplication.cityWeatherList.remove(position);   //从内存中移除
                        ToastShowShot("删除成功！");
                        dialog.cancel();
                        //更新城市列表中当前城市的索引
                        addedCityAdapt.setDefaultCity(MyApplication.updateIndex);
                        addedCityAdapt.notifyDataSetChanged();
                    }
                });
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(dialogView);
                dialog.setCancelable(true);
                dialog.show();
                return true;
            }
        });
    }

    /**
     * 根据定位结果查询城市信息
     */
    class queryTassk extends AsyncTask<Void, Void, List<CityInfo>> {
        @Override
        protected List<CityInfo> doInBackground(Void... params) {
            return DBManager.queryCityInfo(true, "",null);
        }

        @Override
        protected void onPostExecute(final List<CityInfo> cityInfos) {
            super.onPostExecute(cityInfos);
            if (cityInfos.size() != 0) {
                GetCityWeather.getWeather(cityInfos.get(0).getId(), cityInfos.get(0).getDistrict(),
                        cityInfos.get(0).getCity(), cityInfos.get(0).getProvince(), false, true);
            } else {
                ToastShowLong("当前定位区域暂无天气信息！");
                mainSrlUpdate.setRefreshing(false);
            }
        }
    }

    /**
     * 根据指定ID删除数据库中的记录
     */
    class deleteTassk extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            DBManager.removeCity(params[0]);
            return null;
        }
    }

    @Override
    public void onRefresh() {
        //更新天气信息
        if (!SharedPreferanceUtils.get(Constants.DefaultCity, "").equals(SharedPreferanceUtils.get
                (Constants.LocationCity, ""))) {
            //默认城市和定位城市不相同，直接更新天气
            GetCityWeather.getWeather(MyApplication.cityWeatherList.get(MyApplication.updateIndex).getId(),
                    MyApplication.cityWeatherList.get(MyApplication.updateIndex).getDistrict(),
                    MyApplication.cityWeatherList.get(MyApplication.updateIndex).getCity(),
                    MyApplication.cityWeatherList.get(MyApplication.updateIndex).getProvince(), false, false);
        } else {
            if (MyLocationListener.locationState != null && MyLocationListener.locationState.equals
                    ("定位成功！")) {
                new queryTassk().execute();
            } else if (MyLocationListener.locationState != null) {   //定位失败
                MyApplication.mLocationClient.start();  //重新定位
                isRefreshByLocation = true;
            }
        }
    }

    /**
     * 接收更新界面的广播及定位返回结果的广播
     */
    private class UpdateViewReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (dlDetails.isDrawerOpen(Gravity.LEFT)){
                dlDetails.closeDrawers();
            }
            //更新界面
            if (intent.getAction().equals(Constants.UPDATE_MAIN_VIEW_ACTION)) {
                if (!intent.getBooleanExtra("isNull", false)) {
//                    isUpdate = true;
                    //添加城市或更新天气时，更新城市列表中当前城市的索引
                    addedCityAdapt.setDefaultCity(MyApplication.updateIndex);
                    fillData();
                    mainSrlUpdate.setRefreshing(false);
                    ToastShowShot("天气更新成功！");
                } else {
                    //本地有数据，且第一次获取新数据失败时设置趋势图中温度点的坐标
                    if (!SharedPreferanceUtils.get(Constants.DefaultCity, "").equals("") &&
                            isSetViewCoordinate) {
                        MainAssistUtils.getMainAssistUtils().setTemPointCoordinate(forecastWeatherList,TemLines);
                    }
                    isSetViewCoordinate = false;
                }
            } else if (intent.getAction().equals(Constants.LOCATION_STATE) && isRefreshByLocation) {
                isRefreshByLocation = false;
                if (MyLocationListener.locationState.equals("定位成功！")) {
                    new queryTassk().execute();
                } else {
                    ToastShowLong(MyLocationListener.locationState);
                    mainSrlUpdate.setRefreshing(false);
                }
            }
        }
    }

    /**
     * 外部控制SwipeRefreshLayout的刷新状态
     *
     * @param state
     */
    public void setRefreshState(boolean state) {
        if (mainSrlUpdate != null) {
            if (state) {
                mainSrlUpdate.setRefreshing(true);
            } else {
                mainSrlUpdate.setRefreshing(false);
            }
        }
    }

    /**
     * 手势监听器
     */
    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener
            () {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if ((e2.getX() - e1.getX()) > DensityUtils.dpTopx(mContext, 90) && (e2.getY() - e1.getY()) <
                    DensityUtils.dpTopx(mContext, 60)
                    && (e1.getY() - e2.getY()) < DensityUtils.dpTopx(mContext, 60)) {   //手势从左向右滑
                dlDetails.openDrawer(Gravity.LEFT);
                return true;
            }
            return false;
        }
    };
    

    /**
     * 计时每隔5秒切换一次气压风力等数据
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Animation animationSendible = new AlphaAnimation(1, 0);
            animationSendible.setDuration(1000);
            final Animation animationWindHum = new AlphaAnimation(0, 1);
            animationWindHum.setDuration(1000);
            if (llWindHum.isShown()) {
                animationSendible.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        llPressureSendible.setAnimation(animationWindHum);
                        llPressureSendible.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                llWindHum.setAnimation(animationSendible);
                llWindHum.setVisibility(View.GONE);
            } else {
                animationSendible.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        llWindHum.setAnimation(animationWindHum);
                        llWindHum.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                llPressureSendible.setAnimation(animationSendible);
                llPressureSendible.setVisibility(View.GONE);
            }
            handler.postDelayed(this, 5000);
        }
    };

    /**
     * 重写此方法将触控事件优先分发给GestureDetector
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        this.mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据listview的item数量，动态设置listview的高度。注意：子item的最外层必须是LinearLayout
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 按返回键后程序进入后台
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateViewReceiver);
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
