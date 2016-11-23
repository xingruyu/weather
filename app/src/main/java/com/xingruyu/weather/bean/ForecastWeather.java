package com.xingruyu.weather.bean;

/**
 * 未来天气
 * Created by WDX on 2016/10/23.
 */

public class ForecastWeather {

    private String sr;  //sr : 06:33
    private String ss;  //ss : 17:23
    private String txt_d;  //白天天气描述
    private String txt_n;  //夜晚天气描述
    private String max_tem;   //最高温
    private String min_tem;   //最低温
    private String dir;  //风向
    private String sc;   //风力
    private String spd;  //风速
    private String date;  //日期
    private String hum;   //相对湿度
    private String pcpn;   //降水量
    private String pres;  //气压
    private String vis;   //能见度（单位Km）

    public ForecastWeather(){}

    public ForecastWeather(String sr, String ss, String txt_d, String txt_n, String max_tem, String
            min_tem, String dir, String sc, String spd, String date, String hum, String pcpn, String pres,
                           String vis) {
        this.sr = sr;
        this.ss = ss;
        this.txt_d = txt_d;
        this.txt_n = txt_n;
        this.max_tem = max_tem;
        this.min_tem = min_tem;
        this.dir = dir;
        this.sc = sc;
        this.spd = spd;
        this.date = date;
        this.hum = hum;
        this.pcpn = pcpn;
        this.pres = pres;
        this.vis = vis;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getSs() {
        return ss;
    }

    public void setSs(String ss) {
        this.ss = ss;
    }

    public String getTxt_d() {
        return txt_d;
    }

    public void setTxt_d(String txt_d) {
        this.txt_d = txt_d;
    }

    public String getTxt_n() {
        return txt_n;
    }

    public void setTxt_n(String txt_n) {
        this.txt_n = txt_n;
    }

    public String getMax_tem() {
        return max_tem;
    }

    public void setMax_tem(String max_tem) {
        this.max_tem = max_tem;
    }

    public String getMin_tem() {
        return min_tem;
    }

    public void setMin_tem(String min_tem) {
        this.min_tem = min_tem;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getSpd() {
        return spd;
    }

    public void setSpd(String spd) {
        this.spd = spd;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getPcpn() {
        return pcpn;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }
}
