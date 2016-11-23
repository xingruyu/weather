package com.xingruyu.weather.bean;

/**
 * 城市信息
 * Created by WDX on 2016/10/23.
 */
public class CityInfo {

    private String id;          //城市ID
    private String district;    //县、区域
    private String city;        //市
    private String province;    //省

    public CityInfo(){}

    public CityInfo(String id, String district, String city, String province) {
        this.id = id;
        this.district = district;
        this.city = city;
        this.province = province;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
