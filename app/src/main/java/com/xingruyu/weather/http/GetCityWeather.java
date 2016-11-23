package com.xingruyu.weather.http;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.xingruyu.weather.MyApplication;
import com.xingruyu.weather.bean.CityWeather;
import com.xingruyu.weather.bean.ForecastWeather;
import com.xingruyu.weather.config.Constants;
import com.xingruyu.weather.db.DBManager;
import com.xingruyu.weather.utils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取城市天气信息
 * Created by WDX on 2016/10/23.
 */

public class GetCityWeather {

    private static boolean isAdd = false;   //是否为新增天气，ture为新增天气，false为更新天气
    private static String district = "";
    private static String city = "";
    private static String province = "";
    private static boolean isLocation = false;  //是否为定位天气

    /**
     * 获取天气数据(非定位天气更新数据、热门城市直接调用，其余全部从queryCityInfo方法处调用)
     * @param cityid
     * @param districtName
     * @param cityName
     * @param provinceName
     * @param state  是否为新增天气，ture为新增天气，false为更新天气
     * @param location 是否为定位天气
     */
    public static void getWeather(String cityid,String districtName,String cityName,String provinceName,boolean state,boolean location){
        isAdd = state;
        district = districtName;
        city = cityName;
        province = provinceName;
        isLocation = location;
        if(!NetUtils.isConnected(MyApplication.mContext)){
            Toast.makeText(MyApplication.mContext,"无网络链接！",Toast.LENGTH_LONG);
            return;
        }
        new getWeatherTask().execute(cityid);
    }

    /**
     * 获取天气数据异步类
     */
    private static class getWeatherTask extends AsyncTask<String,Void,CityWeather>{

        @Override
        protected CityWeather doInBackground(String... params) {
            String httpUrl = Constants.WEATHER_URL + "?city=" + params[0] + "&key=" + Constants.WEATHER_KEY;
            //获取天气
            String resultJSON = NetUtils.request(httpUrl);
            CityWeather cityWeather = null;
            if (resultJSON != null){
                //解析成CityWeather
                cityWeather = analysisJSON(resultJSON);
                //存数据库
                if (isAdd && cityWeather != null){  //插入数据
                    MyApplication.cityWeatherList.add(cityWeather);
                    DBManager.insertToWeatherDB(cityWeather,isLocation);
                }else if (!isAdd && cityWeather != null){    //更新数据
                    DBManager.updateWeatherToDB(cityWeather);
                    MyApplication.cityWeatherList.set(MyApplication.updateIndex,cityWeather);
                }
            }
            return cityWeather;
        }

        @Override
        protected void onPostExecute(CityWeather cityWeather) {
            super.onPostExecute(cityWeather);
            //发送广播，更新界面
            Intent intent = new Intent(Constants.UPDATE_MAIN_VIEW_ACTION);
            if (cityWeather != null){
                intent.putExtra("isNull",false);
                MyApplication.mContext.sendBroadcast(intent);
            }else {
                Toast.makeText(MyApplication.mContext,"获取天气信息失败！",Toast.LENGTH_SHORT);
                intent.putExtra("isNull",true);
                MyApplication.mContext.sendBroadcast(intent);
            }
        }
    }

    /**
     * 解析JSON
     * @param resultJSON api返回的数据
     * @return
     */
    private static CityWeather analysisJSON(String resultJSON){
        CityWeather cityWeather = new CityWeather();
        cityWeather.setDistrict(district);
        cityWeather.setCity(city);
        cityWeather.setProvince(province);
        String location = isLocation ? "是" : "否";
        cityWeather.setLocation(location);
        try {
            JSONArray jsonArray = (new JSONObject(resultJSON)).getJSONArray("HeWeather5");
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            JSONObject aqiJSON = jsonObject.getJSONObject("aqi").getJSONObject("city");
            cityWeather.setAqi(aqiJSON.getString("aqi"));
            cityWeather.setPm25(aqiJSON.getString("pm25"));
            cityWeather.setQlty(aqiJSON.getString("qlty"));

            JSONObject basicJSON = jsonObject.getJSONObject("basic");
            cityWeather.setId(basicJSON.getString("id"));
            cityWeather.setLoc(basicJSON.getJSONObject("update").getString("loc"));

            JSONObject nowJSON = jsonObject.getJSONObject("now");
            cityWeather.setfl(nowJSON.getString("fl"));
            cityWeather.sethum(nowJSON.getString("hum"));
            cityWeather.setpcpn(nowJSON.getString("pcpn"));
            cityWeather.setpres(nowJSON.getString("pres"));
            cityWeather.settmp(nowJSON.getString("tmp"));
            cityWeather.setvis(nowJSON.getString("vis"));
            if ((nowJSON.getJSONObject("cond").getString("txt")).equals("毛毛雨/细雨")){
                cityWeather.settxt((nowJSON.getJSONObject("cond").getString("txt")).substring(0,3));
            }else if ((nowJSON.getJSONObject("cond").getString("txt")).equals("雷阵雨伴有冰雹")){
                cityWeather.settxt("雷雨有冰雹");
            }else {
                cityWeather.settxt(nowJSON.getJSONObject("cond").getString("txt"));

            }
            JSONObject windJSON = nowJSON.getJSONObject("wind");
            if (windJSON.getString("dir").equals("无持续风向")){
                cityWeather.setdir("无风向");
            }else {
                cityWeather.setdir(windJSON.getString("dir"));
            }
            cityWeather.setsc(windJSON.getString("sc"));
            cityWeather.setspd(windJSON.getString("spd"));

            //暂时不清楚为啥有的地区没有suggestion
            if (jsonObject.has("suggestion")) {
                JSONObject suggestionJSON = jsonObject.getJSONObject("suggestion");
                JSONObject comfJSON = suggestionJSON.getJSONObject("comf");
                cityWeather.setComf_brf(comfJSON.getString("brf"));
                cityWeather.setComf_txt(comfJSON.getString("txt"));
                JSONObject cwJSON = suggestionJSON.getJSONObject("cw");
                cityWeather.setCw_brf(cwJSON.getString("brf"));
                cityWeather.setCw_txt(cwJSON.getString("txt"));
                JSONObject drsgJSON = suggestionJSON.getJSONObject("drsg");
                cityWeather.setDrsg_brf(drsgJSON.getString("brf"));
                cityWeather.setDrsg_txt(drsgJSON.getString("txt"));
                JSONObject fluJSON = suggestionJSON.getJSONObject("flu");
                cityWeather.setFlu_brf(fluJSON.getString("brf"));
                cityWeather.setFlu_txt(fluJSON.getString("txt"));
                JSONObject sportJSON = suggestionJSON.getJSONObject("sport");
                cityWeather.setSport_brf(sportJSON.getString("brf"));
                cityWeather.setSport_txt(sportJSON.getString("txt"));
                JSONObject uvJSON = suggestionJSON.getJSONObject("uv");
                cityWeather.setUv_brf(uvJSON.getString("brf"));
                cityWeather.setUv_txt(uvJSON.getString("txt"));
            }else {
                cityWeather.setComf_brf("");
                cityWeather.setComf_txt("");
                cityWeather.setCw_brf("");
                cityWeather.setCw_txt("");
                cityWeather.setDrsg_brf("");
                cityWeather.setDrsg_txt("");
                cityWeather.setFlu_brf("");
                cityWeather.setFlu_txt("");
                cityWeather.setSport_brf("");
                cityWeather.setSport_txt("");
                cityWeather.setUv_brf("");
                cityWeather.setUv_txt("");
            }

            JSONArray jsonArrayDaily = jsonObject.getJSONArray("daily_forecast");
            List<ForecastWeather> forecastWeatherList = new ArrayList<>();
            for (int i=0;i<jsonArrayDaily.length()-1;i++){       //JSON返回7天数据，这里取前6天
                ForecastWeather forecastWeather = new ForecastWeather();
                JSONObject DailyJSON = jsonArrayDaily.getJSONObject(i);
                forecastWeather.setDate(DailyJSON.getString("date"));
                forecastWeather.setHum(DailyJSON.getString("hum"));
                forecastWeather.setPcpn(DailyJSON.getString("pcpn"));
                forecastWeather.setPres(DailyJSON.getString("pres"));
                forecastWeather.setVis(DailyJSON.getString("vis"));

                JSONObject astroJSON = DailyJSON.getJSONObject("astro");
                forecastWeather.setSr(astroJSON.getString("sr"));
                forecastWeather.setSs(astroJSON.getString("ss"));

                JSONObject condJSON = DailyJSON.getJSONObject("cond");
                if ((condJSON.getString("txt_d")).equals("毛毛雨/细雨")){
                    forecastWeather.setTxt_d((condJSON.getString("txt_d")).substring(0,3));
                }else if ((condJSON.getString("txt_d")).equals("雷阵雨伴有冰雹")){
                    forecastWeather.setTxt_d("雷雨有冰雹");
                }else {
                    forecastWeather.setTxt_d(condJSON.getString("txt_d"));
                }
                if ((condJSON.getString("txt_n")).equals("毛毛雨/细雨")){
                    forecastWeather.setTxt_n((condJSON.getString("txt_n")).substring(0,3));
                }else if ((condJSON.getString("txt_n")).equals("雷阵雨伴有冰雹")){
                    forecastWeather.setTxt_n("雷雨有冰雹");
                }else {
                    forecastWeather.setTxt_n(condJSON.getString("txt_n"));
                }

                JSONObject tmpJSON = DailyJSON.getJSONObject("tmp");
                forecastWeather.setMax_tem(tmpJSON.getString("max"));
                forecastWeather.setMin_tem(tmpJSON.getString("min"));

                JSONObject windForJSON = DailyJSON.getJSONObject("wind");
                if (windForJSON.getString("dir").equals("无持续风向")){
                    forecastWeather.setDir("无风向");
                }else {
                    forecastWeather.setDir(windForJSON.getString("dir"));
                }
                forecastWeather.setSc(windForJSON.getString("sc"));
                forecastWeather.setSpd(windForJSON.getString("spd"));

                forecastWeatherList.add(forecastWeather);
            }
            cityWeather.setForecastWeatherList(forecastWeatherList);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return cityWeather;
    }
}
