package com.xingruyu.weather.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xingruyu.weather.MyApplication;
import com.xingruyu.weather.MyLocationListener;
import com.xingruyu.weather.R;
import com.xingruyu.weather.base.BaseFragmentActivity;
import com.xingruyu.weather.bean.CityInfo;
import com.xingruyu.weather.config.Constants;
import com.xingruyu.weather.db.DBManager;
import com.xingruyu.weather.http.GetCityWeather;
import com.xingruyu.weather.utils.NetUtils;
import com.xingruyu.weather.utils.SharedPreferanceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 城市选择界面
 * Created by WDX on 2016/11/5.
 */

public class CitySelectActivity extends BaseFragmentActivity {

    @BindView(R.id.city_select_iv_close)
    ImageView citySelectIvClose;
    @BindView(R.id.city_select_et_search)
    EditText citySelectEtSearch;
    @BindView(R.id.city_select_lv_citylist)
    ListView citySelectLvCitylist;
    @BindView(R.id.city_select_tv_location)
    TextView citySelectTvLocation;
    @BindView(R.id.city_select_tv_beijing)
    TextView citySelectTvBeijing;
    @BindView(R.id.city_select_tv_shanghai)
    TextView citySelectTvShanghai;
    @BindView(R.id.city_select_tv_tianjin)
    TextView citySelectTvTianjin;
    @BindView(R.id.city_select_tv_chongqing)
    TextView citySelectTvChongqing;
    @BindView(R.id.city_select_tv_shenzhen)
    TextView citySelectTvShenzhen;
    @BindView(R.id.city_select_tv_chengdu)
    TextView citySelectTvChengdu;
    @BindView(R.id.city_select_tv_guangzhou)
    TextView citySelectTvGuangzhou;
    @BindView(R.id.city_select_tv_hangzhou)
    TextView citySelectTvHangzhou;
    @BindView(R.id.city_select_tv_zhengzhou)
    TextView citySelectTvZhengzhou;
    @BindView(R.id.city_select_tv_nanjing)
    TextView citySelectTvNanjing;
    @BindView(R.id.city_select_tv_xian)
    TextView citySelectTvXian;
    @BindView(R.id.city_select_tv_wuhan)
    TextView citySelectTvWuhan;
    @BindView(R.id.city_select_tv_suzhou)
    TextView citySelectTvSuzhou;
    @BindView(R.id.city_select_tv_dalian)
    TextView citySelectTvDalian;
    @BindView(R.id.city_select_rl_hot_city)
    LinearLayout citySelectRlHotCity;
    @BindView(R.id.city_select_tv_hint)
    TextView citySelectTvHint;
    @BindView(R.id.city_select_ll_searching)
    LinearLayout citySelectLlSearching;
    @BindView(R.id.city_select_iv_clean)
    ImageView citySelectIvClean;

    /**
     * 注意事项：1、根据定位结果查询城市信息前，先判断定位结果是否为空。
     * 2、首次使用没有定位城市，显示定位对话框；城市列表中有定位城市时，不显示定位对话框且定位text不可点击
     * 3、如果在此界面选择了城市，则在关闭Activity之前去获取天气，在进入主界面根据广播接收器更新界面
     */
    private Dialog dialog;
    private Handler handler;
    private TextView tv_dialog_location_message;
    private CityInfo mCityInfo;   //定位成功查询数据库后返回城市信息
    private LocationStateReceiver locationStateReceiver;
    private TextView[] hotCityTextView;   //热门城市TextView
    private String[] hotCityID;    //热门城市ID
    private ArrayAdapter<String> arrayAdapter;   //查询结果适配器
    private List<CityInfo> queryResult;     //查询结果
    //数据库名称
    public final String DB_NAME = "weather.db";
    //在手机里存放数据库的位置
    public final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/"
            + MyApplication.mContext.getPackageName() + "/databases";
    private SQLiteDatabase db;
    private String searchName = "";    //要搜索的城市名称

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        if (SharedPreferanceUtils.get(Constants.LocationCity, "").equals("")) {   //城市列表中没有定位城市
            //注册广播接收器
            locationStateReceiver = new LocationStateReceiver();
            registerReceiver(locationStateReceiver, new IntentFilter(Constants.LOCATION_STATE));

            dialog = new Dialog(CitySelectActivity.this);
            View view = getLayoutInflater().inflate(R.layout.dialog_location, null);
            tv_dialog_location_message = (TextView) view.findViewById(R.id.tv_dialog_location_message);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            if (!getIntent().getBooleanExtra("formMainActivity", false)) {
                //从启动页打开时显示对话框
                dialog.show();
            }
            if (MyLocationListener.locationState != null) {
                //定位已经返回
                getLocationInfo();
            }
        } else {   //城市列表中有定位城市
            citySelectTvLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastShowShot("不能重复添加城市！");
                }
            });
            citySelectTvLocation.setTextColor(getResources().getColor(R.color.cornflowerblue));
            citySelectTvLocation.setBackgroundResource(R.drawable.shape_city_select_hot_added);
        }

        if (getIntent().getBooleanExtra("formMainActivity", false)) {
            citySelectIvClose.setVisibility(View.VISIBLE);
        }
        //为已加入城市列表的热门城市做标记
        hotCityTextView = new TextView[]{citySelectTvBeijing, citySelectTvShanghai, citySelectTvTianjin,
                citySelectTvChongqing
                , citySelectTvShenzhen, citySelectTvChengdu, citySelectTvGuangzhou, citySelectTvHangzhou,
                citySelectTvZhengzhou
                , citySelectTvNanjing, citySelectTvXian, citySelectTvWuhan, citySelectTvSuzhou,
                citySelectTvDalian};
        hotCityID = new String[]{"CN101010100", "CN101020100", "CN101030100", "CN101040100", "CN101280601",
                "CN101270101", "CN101280101", "CN101210101", "CN101180101", "CN101190101", "CN101110101",
                "CN101200101", "CN101190401", "CN101070201"};
        for (int i = 0; i < hotCityID.length; i++) {
            for (int j = 0; j < MyApplication.cityWeatherList.size(); j++) {
                if (hotCityID[i].equals(MyApplication.cityWeatherList.get(j).getId())) {
                    hotCityTextView[i].setTextColor(getResources().getColor(R.color.cornflowerblue));
                    hotCityTextView[i].setBackgroundResource(R.drawable.shape_city_select_hot_added);
                }
            }
        }
        queryResult = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.item_query_result);
        citySelectLvCitylist.setAdapter(arrayAdapter);

        //监听搜索框文字输入变化
        citySelectEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (citySelectLlSearching.getVisibility() == View.VISIBLE) {
                    return;
                }
                //清空适配器
                arrayAdapter.clear();
                if (s.length() == 0) {
                    citySelectIvClean.setVisibility(View.GONE);
                    citySelectLvCitylist.setVisibility(View.GONE);
                    citySelectLlSearching.setVisibility(View.GONE);
                    citySelectTvHint.setVisibility(View.GONE);
                    citySelectRlHotCity.setVisibility(View.VISIBLE);
                } else {
                    searchName = citySelectEtSearch.getText().toString();
                    new searchTask().execute();   //发起查询
                    citySelectIvClean.setVisibility(View.VISIBLE);
                    citySelectRlHotCity.setVisibility(View.GONE);
                    citySelectLlSearching.setVisibility(View.VISIBLE);
                }
            }
        });
        //在这里直接打开数据库，搜索城市时不用频繁的打开关闭数据库
        db = SQLiteDatabase.openDatabase(DB_PATH + "/" + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);

        citySelectLvCitylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getCityWeather(queryResult.get(position).getId(), queryResult.get(position).getDistrict()
                        , queryResult.get(position).getCity(), queryResult.get(position).getProvince(), false);
            }
        });
    }

    @OnClick({R.id.city_select_iv_close, R.id.city_select_tv_beijing, R.id.city_select_tv_shanghai, R.id
            .city_select_tv_tianjin, R.id.city_select_tv_chongqing, R.id.city_select_tv_shenzhen, R.id
            .city_select_tv_chengdu, R.id.city_select_tv_guangzhou, R.id.city_select_tv_hangzhou, R.id
            .city_select_tv_zhengzhou, R.id.city_select_tv_nanjing, R.id.city_select_tv_xian, R.id
            .city_select_tv_wuhan, R.id.city_select_tv_suzhou, R.id.city_select_tv_dalian,R.id.city_select_iv_clean})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.city_select_iv_close:
                CitySelectActivity.this.finish();
                break;
            case R.id.city_select_tv_beijing:
                getCityWeather("CN101010100", "北京", "北京", "北京", false);
                break;
            case R.id.city_select_tv_shanghai:
                getCityWeather("CN101020100", "上海", "上海", "上海", false);
                break;
            case R.id.city_select_tv_tianjin:
                getCityWeather("CN101030100", "天津", "天津", "天津", false);
                break;
            case R.id.city_select_tv_chongqing:
                getCityWeather("CN101040100", "重庆", "重庆", "重庆", false);
                break;
            case R.id.city_select_tv_shenzhen:
                getCityWeather("CN101280601", "深圳", "深圳", "广东", false);
                break;
            case R.id.city_select_tv_chengdu:
                getCityWeather("CN101270101", "成都", "成都", "四川", false);
                break;
            case R.id.city_select_tv_guangzhou:
                getCityWeather("CN101280101", "广州", "广州", "广东", false);
                break;
            case R.id.city_select_tv_hangzhou:
                getCityWeather("CN101210101", "杭州", "杭州", "浙江", false);
                break;
            case R.id.city_select_tv_zhengzhou:
                getCityWeather("CN101180101", "郑州", "郑州", "河南", false);
                break;
            case R.id.city_select_tv_nanjing:
                getCityWeather("CN101190101", "南京", "南京", "江苏", false);
                break;
            case R.id.city_select_tv_xian:
                getCityWeather("CN101110101", "西安", "西安", "陕西", false);
                break;
            case R.id.city_select_tv_wuhan:
                getCityWeather("CN101200101", "武汉", "武汉", "湖北", false);
                break;
            case R.id.city_select_tv_suzhou:
                getCityWeather("CN101190401", "苏州", "苏州", "江苏", false);
                break;
            case R.id.city_select_tv_dalian:
                getCityWeather("CN101070201", "大连", "大连", "辽宁", false);
                break;
            case R.id.city_select_iv_clean:
                citySelectEtSearch.setText("");
                break;
        }
    }

    /**
     * 获取天气信息
     *
     * @param id
     * @param district
     * @param city
     * @param province
     * @param isLocation
     */
    private void getCityWeather(String id, String district, String city, String province, boolean
            isLocation) {
        if (MyApplication.cityWeatherList.size() > 8){
            ToastShowShot("最多只能添加9个城市！");
            return;
        }
        if (NetUtils.isConnected(MyApplication.mContext)) {
            for (int i = 0; i < MyApplication.cityWeatherList.size(); i++) {
                if (id.equals(MyApplication.cityWeatherList.get(i).getId())) {
                    ToastShowShot("不能重复添加城市！");
                    return;
                }
            }
            CitySelectActivity.this.finish();
            MyApplication.getMyApplication().getMainActivity().setRefreshState(true);
            GetCityWeather.getWeather(id, district, city, province, true, isLocation);
        }else {
            ToastShowShot("请检查你的网络连接！");
        }
    }

    /**
     * 定位状态接收器
     */
    class LocationStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SharedPreferanceUtils.get(Constants.LocationCity, "").equals("")) {
                getLocationInfo();
            }
        }
    }

    /**
     * 根据定位信息，获取城市code
     */
    private void getLocationInfo() {
        if (MyLocationListener.locationState.equals("定位成功！")) {
            tv_dialog_location_message.setText("正在定位……");
            //查询城市信息表
            if (MyApplication.getMyApplication().getLocationInfo() != null) {
                new queryTask().execute();
            }
        } else {
//            btn_dialog_location_cancel.setVisibility(View.GONE);
            tv_dialog_location_message.setText(MyLocationListener.locationState);
            citySelectTvLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //重新定位
                    dialog.show();
                    MyApplication.mLocationClient.start();
                }
            });
            //启动定时器，2秒后关闭对话框
            handler = new Handler();
            handler.postDelayed(runnable, 1500);
        }
    }

    /**
     * 根据定位结果查询城市信息
     */
    class queryTask extends AsyncTask<Void, Void, List<CityInfo>> {
        @Override
        protected List<CityInfo> doInBackground(Void... params) {
            return DBManager.queryCityInfo(true, "", null);
        }

        @Override
        protected void onPostExecute(final List<CityInfo> cityInfos) {
            super.onPostExecute(cityInfos);
//            btn_dialog_location_cancel.setVisibility(View.GONE);
            if (cityInfos.size() != 0) {
                mCityInfo = cityInfos.get(0);
                tv_dialog_location_message.setText("定位成功！");
                citySelectTvLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //从主界面进来的，则关闭当前界面并获取城市天气
                        getCityWeather(mCityInfo.getId(), mCityInfo.getDistrict(),
                                mCityInfo.getCity(), mCityInfo.getProvince(), true);
                    }
                });
            } else {
                tv_dialog_location_message.setText("定位区域暂无天气信息！");
                citySelectTvLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastShowShot("定位区域暂无天气信息！");
                    }
                });
            }
            //启动定时器，2秒后关闭对话框
            handler = new Handler();
            handler.postDelayed(runnable, 1500);
        }
    }

    /**
     * 根据搜索框输入的城市查询城市信息
     */
    class searchTask extends AsyncTask<Void, Void, List<CityInfo>> {

        @Override
        protected List<CityInfo> doInBackground(Void... params) {
            return DBManager.queryCityInfo(false, searchName, db);
        }

        @Override
        protected void onPostExecute(List<CityInfo> cityInfos) {
            super.onPostExecute(cityInfos);
            citySelectLlSearching.setVisibility(View.GONE);
            queryResult.clear();
            if (cityInfos.size() == 0) {
                arrayAdapter.clear();
                citySelectLvCitylist.setVisibility(View.GONE);
                citySelectTvHint.setVisibility(View.VISIBLE);
            } else {
                citySelectTvHint.setVisibility(View.GONE);
                citySelectLvCitylist.setVisibility(View.VISIBLE);
                List<String> result = new ArrayList<>();
                for (int i = 0; i < cityInfos.size(); i++) {
                    result.add(cityInfos.get(i).getDistrict() + "  " + cityInfos.get(i).getCity() + "  "
                            + cityInfos.get(i).getProvince());
                }
                queryResult.addAll(cityInfos);
                arrayAdapter.addAll(result);
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 计时2秒后关闭对话框
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (dialog.isShowing()) {
                dialog.cancel();
            }
            handler.removeCallbacks(this);
            //如果是从启动页进来的，且定位成功，则自动跳转到主界面
            if (getIntent().getBooleanExtra("LaunchActivity", false) && mCityInfo != null && NetUtils
                    .isConnected(MyApplication.mContext)) {
                //跳转并获取城市天气
                startActivity(new Intent(CitySelectActivity.this, MainActivity.class));
                GetCityWeather.getWeather(mCityInfo.getId(), mCityInfo.getDistrict(),
                        mCityInfo.getCity(), mCityInfo.getProvince(), true, true);
                CitySelectActivity.this.finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();    //关闭数据库
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        if (locationStateReceiver != null) {
            unregisterReceiver(locationStateReceiver);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CitySelectActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
