package com.xingruyu.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingruyu.weather.MyApplication;
import com.xingruyu.weather.R;
import com.xingruyu.weather.base.MyBaseAdapter;
import com.xingruyu.weather.bean.CityWeather;

import java.util.Calendar;
import java.util.List;

/**
 * 已添加的城市列表
 * Created by WDX on 2016/11/20.
 */

public class AddedCityAdapt extends MyBaseAdapter<CityWeather>{

    private Calendar calendar;
    private int defaultCityIndx = MyApplication.updateIndex;   //默认城市的索引

    public AddedCityAdapt(Context mContext, List<CityWeather> date) {
        super(mContext, date);
        calendar = Calendar.getInstance();
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_added_city, null);
            viewHolder = new ViewHolder();
            viewHolder.item_added_city_tv_city = (TextView)convertView.findViewById(R.id.item_added_city_tv_city);
            viewHolder.item_added_city_tv_describe = (TextView)convertView.findViewById(R.id.item_added_city_tv_describe);
            viewHolder.item_added_city_tv_temperature = (TextView)convertView.findViewById(R.id.item_added_city_tv_temperature);
            viewHolder.item_added_city_iv_describe = (ImageView) convertView.findViewById(R.id.item_added_city_iv_describe);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        boolean dayOrNight = true;
        calendar.setTimeInMillis(System.currentTimeMillis());  //System.currentTimeMillis()取得系统时间
        if (calendar.get(Calendar.HOUR_OF_DAY) > 18 && calendar.get(Calendar.HOUR_OF_DAY) < 6) {
            dayOrNight = false;
        }

        CityWeather cityWeather = getItem(position);
        viewHolder.item_added_city_tv_city.setText(cityWeather.getDistrict());
        viewHolder.item_added_city_tv_describe.setText(cityWeather.gettxt());
        viewHolder.item_added_city_tv_temperature.setText(cityWeather.getForecastWeatherList().get(0).getMin_tem()
        +"~"+cityWeather.getForecastWeatherList().get(0).getMax_tem()+"℃");
        viewHolder.item_added_city_iv_describe.setImageResource(
                MyApplication.getMyApplication().getMainActivity().selectWeatherPic(cityWeather.gettxt(),
                        dayOrNight,false,false));
        if (position == defaultCityIndx){
            viewHolder.item_added_city_tv_city.setTextColor(MyApplication.mContext.getResources().getColor(R.color.cornflowerblue));
            viewHolder.item_added_city_tv_describe.setTextColor(MyApplication.mContext.getResources().getColor(R.color.cornflowerblue));
            viewHolder.item_added_city_tv_temperature.setTextColor(MyApplication.mContext.getResources().getColor(R.color.cornflowerblue));
        }else {
            viewHolder.item_added_city_tv_city.setTextColor(MyApplication.mContext.getResources().getColor(R.color.gray));
            viewHolder.item_added_city_tv_describe.setTextColor(MyApplication.mContext.getResources().getColor(R.color.gray));
            viewHolder.item_added_city_tv_temperature.setTextColor(MyApplication.mContext.getResources().getColor(R.color.gray));
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView item_added_city_tv_city,item_added_city_tv_describe,item_added_city_tv_temperature;
        private ImageView item_added_city_iv_describe;
    }

    public void setDefaultCity(int position){
        defaultCityIndx = position;
    }
}
