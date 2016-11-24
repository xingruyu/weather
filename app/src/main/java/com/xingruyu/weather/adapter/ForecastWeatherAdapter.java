package com.xingruyu.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingruyu.weather.R;
import com.xingruyu.weather.base.MyBaseAdapter;
import com.xingruyu.weather.bean.ForecastWeather;
import com.xingruyu.weather.utils.MainAssistUtils;

import java.util.Calendar;
import java.util.List;

/**
 * 主界面未来天气适配器
 * Created by WDX on 2016/11/12.
 */

public class ForecastWeatherAdapter extends MyBaseAdapter<ForecastWeather>{

    private String[] dayOfWeek = new String[]{"周日","周一","周二","周三","周四","周五","周六"};
    Calendar calendar = Calendar.getInstance();

    public ForecastWeatherAdapter(Context mContext, List<ForecastWeather> date) {
        super(mContext, date);
    }

    @Override
    public View createView(int position, View convertView, ViewGroup parent, LayoutInflater inflater) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_forecast_weather, null);
            viewHolder = new ViewHolder();
            viewHolder.item_forecast_weather_tv_data = (TextView)convertView.findViewById(R.id.item_forecast_weather_tv_data);
            viewHolder.item_forecast_weather_tv_describe = (TextView)convertView.findViewById(R.id.item_forecast_weather_tv_describe);
            viewHolder.item_forecast_weather_tv_temperature = (TextView)convertView.findViewById(R.id.item_forecast_weather_tv_temperature);
            viewHolder.item_forecast_weather_iv_describe = (ImageView) convertView.findViewById(R.id.item_forecast_weather_iv_describe);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ForecastWeather forecastWeather = getItem(position);
        calendar.setTimeInMillis(System.currentTimeMillis());  //System.currentTimeMillis()取得系统时间
        if (position == 0){
            viewHolder.item_forecast_weather_tv_data.setText("今天");
        }else if (position == 1){
            viewHolder.item_forecast_weather_tv_data.setText("明天");
        }else {
            if ((calendar.get(Calendar.DAY_OF_WEEK) + position-1) >=7){
                viewHolder.item_forecast_weather_tv_data.setText(dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) + position-8]);
            }else {
                viewHolder.item_forecast_weather_tv_data.setText(dayOfWeek[calendar.get(Calendar.DAY_OF_WEEK) + position-1]);
            }
        }
        if (forecastWeather.getTxt_d().equals(forecastWeather.getTxt_n())){
            viewHolder.item_forecast_weather_tv_describe.setText(forecastWeather.getTxt_d());
        }else {
            viewHolder.item_forecast_weather_tv_describe.setText(forecastWeather.getTxt_d() + "转" + forecastWeather.getTxt_n());
        }
        viewHolder.item_forecast_weather_tv_temperature.setText(forecastWeather.getMin_tem()+"~"+forecastWeather.getMax_tem()+"℃");
        viewHolder.item_forecast_weather_iv_describe.setImageResource(
                MainAssistUtils.getMainAssistUtils().selectWeatherPic(forecastWeather.getTxt_d(),true,false,false));
        return convertView;
    }

    static class ViewHolder {
        private TextView item_forecast_weather_tv_data,item_forecast_weather_tv_describe,item_forecast_weather_tv_temperature;
        private ImageView item_forecast_weather_iv_describe;
    }
}
