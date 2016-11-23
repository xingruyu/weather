package com.xingruyu.weather.bean;

/**
 * 定位信息
 * Created by WDX on 2016/10/23.
 */

public class LocationInfo {

    private String locationDescribe;    // 位置语义化信息,没有的话返回NULL
    private String addrStr;      //详细地址信息
    private String city;     //城市
    private String country;   //国家
    private String district;  //区/县
    private String province;    //省份
    private double latitude;   //纬度坐标
    private double longitude;  //经度坐标

    public LocationInfo(){}

    public LocationInfo(String locationDescribe, String addrStr, String city, String country, String
            district, String province, double latitude, double longitude) {
        this.locationDescribe = locationDescribe;
        this.addrStr = addrStr;
        this.city = city;
        this.country = country;
        this.district = district;
        this.province = province;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocationDescribe() {
        return locationDescribe;
    }

    public void setLocationDescribe(String locationDescribe) {
        this.locationDescribe = locationDescribe;
    }

    public String getAddrStr() {
        return addrStr;
    }

    public void setAddrStr(String addrStr) {
        this.addrStr = addrStr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
