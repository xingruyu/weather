package com.xingruyu.weather.bean;

import java.util.List;

/**
 * 城市天气信息
 * Created by WDX on 2016/10/23.
 */

public class CityWeather {

    /**
     * aqi : 50
     * pm25 : 26
     * qlty : 优
     * id: CN101010100
     * district
     * city
     * province
     * loc : 2016-10-23 13:09
     * fl：体感温度
     * hum：相对湿度(%)
     * pcpn：降水量(单位：mm)
     * pres：气压
     * tmp：当前温度
     * vis：能见度(km)
     * txt：天气描述
     * dir：风向
     * sc：风力
     * spd：风速(Kmph)
     */
    private String aqi;
    private String pm25;
    private String qlty;
    private String id;
    private String district;    //县、区域
    private String city;        //市
    private String province;    //省
    private String location;    //是否为定位城市，“是”或者“否”
    private String loc;
    private String fl;
    private String hum;
    private String pcpn;
    private String pres;
    private String tmp;
    private String vis;
    private String txt;
    private String dir;
    private String sc;
    private String spd;

    private String comf_brf;  //舒适度指数
    private String comf_txt;
    private String cw_brf;   //洗车指数
    private String cw_txt;
    private String drsg_brf;   //穿衣指数
    private String drsg_txt;
    private String flu_brf;     //感冒指数
    private String flu_txt;
    private String sport_brf;  //运动指数
    private String sport_txt;
    private String uv_brf;      //紫外线指数
    private String uv_txt;

    private List<ForecastWeather> forecastWeatherList;

    public CityWeather(){}

    public CityWeather(String aqi, String pm25, String qlty, String id,String district,String city,String province,String location, String loc, String fl, String
            hum, String pcpn, String pres, String tmp, String vis, String txt,
                       String dir, String sc, String spd, String comf_brf, String comf_txt,
                       String cw_brf, String cw_txt, String drsg_brf, String drsg_txt, String flu_brf,
                       String flu_txt, String sport_brf, String sport_txt, String uv_brf, String uv_txt,
                       List<ForecastWeather> forecastWeatherList) {
        this.aqi = aqi;
        this.pm25 = pm25;
        this.qlty = qlty;
        this.id = id;
        this.district = district;
        this.city = city;
        this.province = province;
        this.location = location;
        this.loc = loc;
        this.fl = fl;
        this.hum = hum;
        this.pcpn = pcpn;
        this.pres = pres;
        this.tmp = tmp;
        this.vis = vis;
        this.txt = txt;
        this.dir = dir;
        this.sc = sc;
        this.spd = spd;
        this.comf_brf = comf_brf;
        this.comf_txt = comf_txt;
        this.cw_brf = cw_brf;
        this.cw_txt = cw_txt;
        this.drsg_brf = drsg_brf;
        this.drsg_txt = drsg_txt;
        this.flu_brf = flu_brf;
        this.flu_txt = flu_txt;
        this.sport_brf = sport_brf;
        this.sport_txt = sport_txt;
        this.uv_brf = uv_brf;
        this.uv_txt = uv_txt;
        this.forecastWeatherList = forecastWeatherList;
    }

    public List<ForecastWeather> getForecastWeatherList() {
        return forecastWeatherList;
    }

    public void setForecastWeatherList(List<ForecastWeather> forecastWeatherList) {
        this.forecastWeatherList = forecastWeatherList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getQlty() {
        return qlty;
    }

    public void setQlty(String qlty) {
        this.qlty = qlty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getfl() {
        return fl;
    }

    public void setfl(String fl) {
        this.fl = fl;
    }

    public String gethum() {
        return hum;
    }

    public void sethum(String hum) {
        this.hum = hum;
    }

    public String getpcpn() {
        return pcpn;
    }

    public void setpcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public String getpres() {
        return pres;
    }

    public void setpres(String pres) {
        this.pres = pres;
    }

    public String gettmp() {
        return tmp;
    }

    public void settmp(String tmp) {
        this.tmp = tmp;
    }

    public String getvis() {
        return vis;
    }

    public void setvis(String vis) {
        this.vis = vis;
    }

    public String gettxt() {
        return txt;
    }

    public void settxt(String txt) {
        this.txt = txt;
    }

    public String getdir() {
        return dir;
    }

    public void setdir(String dir) {
        this.dir = dir;
    }

    public String getsc() {
        return sc;
    }

    public void setsc(String sc) {
        this.sc = sc;
    }

    public String getspd() {
        return spd;
    }

    public void setspd(String spd) {
        this.spd = spd;
    }

    public String getComf_brf() {
        return comf_brf;
    }

    public void setComf_brf(String comf_brf) {
        this.comf_brf = comf_brf;
    }

    public String getComf_txt() {
        return comf_txt;
    }

    public void setComf_txt(String comf_txt) {
        this.comf_txt = comf_txt;
    }

    public String getCw_brf() {
        return cw_brf;
    }

    public void setCw_brf(String cw_brf) {
        this.cw_brf = cw_brf;
    }

    public String getCw_txt() {
        return cw_txt;
    }

    public void setCw_txt(String cw_txt) {
        this.cw_txt = cw_txt;
    }

    public String getDrsg_brf() {
        return drsg_brf;
    }

    public void setDrsg_brf(String drsg_brf) {
        this.drsg_brf = drsg_brf;
    }

    public String getDrsg_txt() {
        return drsg_txt;
    }

    public void setDrsg_txt(String drsg_txt) {
        this.drsg_txt = drsg_txt;
    }

    public String getFlu_brf() {
        return flu_brf;
    }

    public void setFlu_brf(String flu_brf) {
        this.flu_brf = flu_brf;
    }

    public String getFlu_txt() {
        return flu_txt;
    }

    public void setFlu_txt(String flu_txt) {
        this.flu_txt = flu_txt;
    }

    public String getSport_brf() {
        return sport_brf;
    }

    public void setSport_brf(String sport_brf) {
        this.sport_brf = sport_brf;
    }

    public String getSport_txt() {
        return sport_txt;
    }

    public void setSport_txt(String sport_txt) {
        this.sport_txt = sport_txt;
    }

    public String getUv_brf() {
        return uv_brf;
    }

    public void setUv_brf(String uv_brf) {
        this.uv_brf = uv_brf;
    }

    public String getUv_txt() {
        return uv_txt;
    }

    public void setUv_txt(String uv_txt) {
        this.uv_txt = uv_txt;
    }
}
