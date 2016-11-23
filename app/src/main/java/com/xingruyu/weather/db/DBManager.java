package com.xingruyu.weather.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;

import com.xingruyu.weather.config.Constants;
import com.xingruyu.weather.MyApplication;
import com.xingruyu.weather.bean.CityInfo;
import com.xingruyu.weather.bean.CityWeather;
import com.xingruyu.weather.bean.ForecastWeather;
import com.xingruyu.weather.bean.LocationInfo;
import com.xingruyu.weather.utils.LogUtils;
import com.xingruyu.weather.utils.SharedPreferanceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作类
 * Created by WDX on 2016/10/24.
 */

public class DBManager {

    //数据库名称
    public static final String DB_NAME = "weather.db";
    //在手机里存放数据库的位置
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/"
            + MyApplication.mContext.getPackageName()+"/databases";

    /**
     * 第一次运行时把assets目录下的数据库拷贝到sd上
     */
    public static void CopySqliteFileFromAssetsToDatabases() throws IOException {
        File dir = new File(DB_PATH);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        File file= new File(dir, DB_NAME);
        InputStream inputStream = null;
        OutputStream outputStream =null;
        SQLiteDatabase db = null;
        //通过IO流的方式，将assets目录下的数据库文件，写入到SD卡中。
        if (!file.exists()) {
            try {
                file.createNewFile();
                inputStream = MyApplication.mContext.getClass().getClassLoader().getResourceAsStream("assets/" + DB_NAME);
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[2048];
                int len ;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer,0,len);
                }
                //创建城市天气表
                db = SQLiteDatabase.openDatabase(DB_PATH + "/" + DB_NAME , null, SQLiteDatabase.OPEN_READWRITE);
                String sqlWeather = "CREATE TABLE cityweather(id TEXT,district TEXT,city TEXT,province TEXT,location TEXT," +
                        "aqi TEXT,pm25 TEXT,qlty TEXT,loc TEXT,fl TEXT,hum TEXT,pcpn TEXT,pres TEXT,tmp TEXT,vis TEXT,txt TEXT,dir TEXT," +
                        "sc TEXT,spd TEXT,comf_brf TEXT,comf_txt TEXT,cw_brf TEXT,cw_txt TEXT,drsg_brf TEXT,drsg_txt TEXT,flu_brf TEXT," +
                        "flu_txt TEXT,sport_brf TEXT,sport_txt TEXT,uv_brf TEXT,uv_txt TEXT,sr1 TEXT,ss1 TEXT,txt_d1 TEXT,txt_n1 TEXT," +
                        "max_tem1 TEXT,min_tem1 TEXT,dir1 TEXT,sc1 TEXT,spd1 TEXT,date1 TEXT,hum1 TEXT,pcpn1 TEXT,pres1 TEXT,vis1 TEXT," +
                        "sr2 TEXT,ss2 TEXT,txt_d2 TEXT,txt_n2 TEXT,max_tem2 TEXT,min_tem2 TEXT,dir2 TEXT,sc2 TEXT,spd2 TEXT,date2 TEXT," +
                        "hum2 TEXT,pcpn2 TEXT,pres2 TEXT,vis2 TEXT,sr3 TEXT,ss3 TEXT,txt_d3 TEXT,txt_n3 TEXT,max_tem3 TEXT,min_tem3 TEXT," +
                        "dir3 TEXT,sc3 TEXT,spd3 TEXT,date3 TEXT,hum3 TEXT,pcpn3 TEXT,pres3 TEXT,vis3 TEXT,sr4 TEXT,ss4 TEXT,txt_d4 TEXT," +
                        "txt_n4 TEXT,max_tem4 TEXT,min_tem4 TEXT,dir4 TEXT,sc4 TEXT,spd4 TEXT,date4 TEXT,hum4 TEXT,pcpn4 TEXT,pres4 TEXT," +
                        "vis4 TEXT,sr5 TEXT,ss5 TEXT,txt_d5 TEXT,txt_n5 TEXT,max_tem5 TEXT,min_tem5 TEXT,dir5 TEXT,sc5 TEXT,spd5 TEXT," +
                        "date5 TEXT,hum5 TEXT,pcpn5 TEXT,pres5 TEXT,vis5 TEXT,sr6 TEXT,ss6 TEXT,txt_d6 TEXT,txt_n6 TEXT,max_tem6 TEXT," +
                        "min_tem6 TEXT,dir6 TEXT,sc6 TEXT,spd6 TEXT,date6 TEXT,hum6 TEXT,pcpn6 TEXT,pres6 TEXT,vis6 TEXT)";
                db.execSQL(sqlWeather);
            } catch (IOException e) {
                LogUtils.e("DBManager_CopySqliteFileFromAssetsToDatabases",e.toString(),true);
                e.printStackTrace();
            }finally {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (db != null){
                    db.close();
                }
            }
        }
    }

    /**
     * 向城市天气表中添加一条天气数据
     */
    public static void insertToWeatherDB(CityWeather cityWeather,boolean isLocation){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + "/" + DB_NAME , null, SQLiteDatabase.OPEN_READWRITE);
        try {
            ContentValues values = new ContentValues();
            values.put("id", cityWeather.getId());
            values.put("district", cityWeather.getDistrict());
            values.put("city", cityWeather.getCity());
            values.put("province", cityWeather.getProvince());
            values.put("location",cityWeather.getLocation());
            values.put("txt", cityWeather.gettxt());
            values.put("aqi", cityWeather.getAqi());
            values.put("pm25", cityWeather.getPm25());
            values.put("qlty", cityWeather.getQlty());
            values.put("loc", cityWeather.getLoc());
            values.put("fl", cityWeather.getfl());
            values.put("hum", cityWeather.gethum());
            values.put("pcpn", cityWeather.getpcpn());
            values.put("pres", cityWeather.getpres());
            values.put("tmp", cityWeather.gettmp());
            values.put("vis", cityWeather.getvis());
            values.put("dir", cityWeather.getdir());
            values.put("sc", cityWeather.getsc());
            values.put("spd", cityWeather.getspd());
            values.put("comf_brf", cityWeather.getComf_brf());
            values.put("comf_txt", cityWeather.getComf_txt());
            values.put("cw_brf", cityWeather.getCw_brf());
            values.put("cw_txt", cityWeather.getCw_txt());
            values.put("drsg_brf", cityWeather.getDrsg_brf());
            values.put("drsg_txt", cityWeather.getDrsg_txt());
            values.put("flu_brf", cityWeather.getFlu_brf());
            values.put("flu_txt", cityWeather.getFlu_txt());
            values.put("sport_brf", cityWeather.getSport_brf());
            values.put("sport_txt", cityWeather.getSport_txt());
            values.put("uv_brf", cityWeather.getUv_brf());
            values.put("uv_txt", cityWeather.getUv_txt());
            for (int i=1;i<7;i++){
                values.put("sr" +i, cityWeather.getForecastWeatherList().get(i-1).getSr());
                values.put("ss" +i, cityWeather.getForecastWeatherList().get(i-1).getSs());
                values.put("txt_d" +i, cityWeather.getForecastWeatherList().get(i-1).getTxt_d());
                values.put("txt_n" +i, cityWeather.getForecastWeatherList().get(i-1).getTxt_n());
                values.put("max_tem" +i, cityWeather.getForecastWeatherList().get(i-1).getMax_tem());
                values.put("min_tem" +i, cityWeather.getForecastWeatherList().get(i-1).getMin_tem());
                values.put("dir" +i, cityWeather.getForecastWeatherList().get(i-1).getDir());
                values.put("sc" +i, cityWeather.getForecastWeatherList().get(i-1).getSc());
                values.put("spd" +i, cityWeather.getForecastWeatherList().get(i-1).getSpd());
                values.put("date" +i, cityWeather.getForecastWeatherList().get(i-1).getDate());
                values.put("hum" +i, cityWeather.getForecastWeatherList().get(i-1).getHum());
                values.put("pcpn" +i, cityWeather.getForecastWeatherList().get(i-1).getPcpn());
                values.put("pres" +i, cityWeather.getForecastWeatherList().get(i-1).getPres());
                values.put("vis" +i, cityWeather.getForecastWeatherList().get(i-1).getVis());
            }
            db.insert("cityweather", null, values);
        } catch (SQLiteException e) {
            LogUtils.e("DBManager_insertToWeatherDB",e.toString(),true);
        } catch (Exception e){
            LogUtils.e("DBManager_insertToWeatherDB",e.toString(),true);
        }finally {
            db.close();
        }
        if (isLocation){
            SharedPreferanceUtils.put(Constants.LocationCity,cityWeather.getId());
        }
        SharedPreferanceUtils.put(Constants.DefaultCity,cityWeather.getId());
        //增加天气时，默认城市在城市列表中的索引为最后一个
        MyApplication.updateIndex = MyApplication.cityWeatherList.size() - 1;
    }

    /**
     * 更新城市天气表中的数据
     * @param cityWeather
     */
    public static void updateWeatherToDB(CityWeather cityWeather){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + "/" + DB_NAME , null, SQLiteDatabase.OPEN_READWRITE);
        List<ForecastWeather> lists = new ArrayList<>();
        lists.addAll(cityWeather.getForecastWeatherList());

        Object[] bindargs = new Object[]{cityWeather.getId(),cityWeather.getDistrict(),cityWeather.getCity(),cityWeather.getProvince(),
        cityWeather.getLocation(),cityWeather.getAqi(),cityWeather.getPm25(),cityWeather.getQlty(),cityWeather.getLoc(),
        cityWeather.getfl(),cityWeather.gethum(),cityWeather.getpcpn(),cityWeather.getpres(),cityWeather.gettmp(),
        cityWeather.getvis(),cityWeather.gettxt(),cityWeather.getdir(),cityWeather.getsc(),cityWeather.getspd(),
        cityWeather.getComf_brf(),cityWeather.getComf_txt(),cityWeather.getCw_brf(),cityWeather.getCw_txt(),cityWeather.getDrsg_brf(),
        cityWeather.getDrsg_txt(),cityWeather.getFlu_brf(),cityWeather.getFlu_txt(),cityWeather.getSport_brf(),cityWeather.getSport_txt(),
        cityWeather.getUv_brf(),cityWeather.getUv_txt()
        ,lists.get(0).getSr(),lists.get(0).getSs(),lists.get(0).getTxt_d(),lists.get(0).getTxt_n(),lists.get(0).getMax_tem()
                ,lists.get(0).getMin_tem(),lists.get(0).getDir(),lists.get(0).getSc(), lists.get(0).getSpd(),lists.get(0).getDate()
                ,lists.get(0).getHum(),lists.get(0).getPcpn(),lists.get(0).getPres(),lists.get(0).getVis()
        ,lists.get(1).getSr(),lists.get(1).getSs(),lists.get(1).getTxt_d(),lists.get(1).getTxt_n(),lists.get(1).getMax_tem()
                ,lists.get(1).getMin_tem(),lists.get(1).getDir(),lists.get(1).getSc(), lists.get(1).getSpd(),lists.get(1).getDate()
                ,lists.get(1).getHum(),lists.get(1).getPcpn(),lists.get(1).getPres(),lists.get(1).getVis()
        ,lists.get(2).getSr(),lists.get(2).getSs(),lists.get(2).getTxt_d(),lists.get(2).getTxt_n(),lists.get(2).getMax_tem()
                ,lists.get(2).getMin_tem(),lists.get(2).getDir(),lists.get(2).getSc(), lists.get(2).getSpd(),lists.get(2).getDate()
                ,lists.get(2).getHum(),lists.get(2).getPcpn(),lists.get(2).getPres(),lists.get(2).getVis()
        ,lists.get(3).getSr(),lists.get(3).getSs(),lists.get(3).getTxt_d(),lists.get(3).getTxt_n(),lists.get(3).getMax_tem()
                ,lists.get(3).getMin_tem(),lists.get(3).getDir(),lists.get(3).getSc(), lists.get(3).getSpd(),lists.get(3).getDate()
                ,lists.get(3).getHum(),lists.get(3).getPcpn(),lists.get(3).getPres(),lists.get(3).getVis()
        ,lists.get(4).getSr(),lists.get(4).getSs(),lists.get(4).getTxt_d(),lists.get(4).getTxt_n(),lists.get(4).getMax_tem()
                ,lists.get(4).getMin_tem(),lists.get(4).getDir(),lists.get(4).getSc(), lists.get(4).getSpd(),lists.get(4).getDate()
                ,lists.get(4).getHum(),lists.get(4).getPcpn(),lists.get(4).getPres(),lists.get(4).getVis()
        ,lists.get(5).getSr(),lists.get(5).getSs(),lists.get(5).getTxt_d(),lists.get(5).getTxt_n(),lists.get(5).getMax_tem()
                ,lists.get(5).getMin_tem(),lists.get(5).getDir(),lists.get(5).getSc(), lists.get(5).getSpd(),lists.get(5).getDate()
                ,lists.get(5).getHum(),lists.get(5).getPcpn(),lists.get(5).getPres(),lists.get(5).getVis(),
        SharedPreferanceUtils.get(Constants.DefaultCity,"").toString()
        };
        String sql = "update cityweather set id =?,district =?,city =?,province =?,location =?," +
        "aqi =?,pm25 =?,qlty =?,loc =?,fl =?,hum =?,pcpn =?,pres =?,tmp =?,vis =?,txt =?,dir =?," +
                "sc =?,spd =?,comf_brf =?,comf_txt =?,cw_brf =?,cw_txt =?,drsg_brf =?,drsg_txt =?,flu_brf =?," +
                "flu_txt =?,sport_brf =?,sport_txt =?,uv_brf =?,uv_txt =?,sr1 =?,ss1 =?,txt_d1 =?,txt_n1 =?," +
                "max_tem1 =?,min_tem1 =?,dir1 =?,sc1 =?,spd1 =?,date1 =?,hum1 =?,pcpn1 =?,pres1 =?,vis1 =?," +
                "sr2 =?,ss2 =?,txt_d2 =?,txt_n2 =?,max_tem2 =?,min_tem2 =?,dir2 =?,sc2 =?,spd2 =?,date2 =?," +
                "hum2 =?,pcpn2 =?,pres2 =?,vis2 =?,sr3 =?,ss3 =?,txt_d3 =?,txt_n3 =?,max_tem3 =?,min_tem3 =?," +
                "dir3 =?,sc3 =?,spd3 =?,date3 =?,hum3 =?,pcpn3 =?,pres3 =?,vis3 =?,sr4 =?,ss4 =?,txt_d4 =?," +
                "txt_n4 =?,max_tem4 =?,min_tem4 =?,dir4 =?,sc4 =?,spd4 =?,date4 =?,hum4 =?,pcpn4 =?,pres4 =?," +
                "vis4 =?,sr5 =?,ss5 =?,txt_d5 =?,txt_n5 =?,max_tem5 =?,min_tem5 =?,dir5 =?,sc5 =?,spd5 =?," +
                "date5 =?,hum5 =?,pcpn5 =?,pres5 =?,vis5 =?,sr6 =?,ss6 =?,txt_d6 =?,txt_n6 =?,max_tem6 =?," +
                "min_tem6 =?,dir6 =?,sc6 =?,spd6 =?,date6 =?,hum6 =?,pcpn6 =?,pres6 =?,vis6 =? where id =?" ;
        db.execSQL(sql,bindargs);
        db.close();
        //更新天气时，默认城市在城市列表中的索引不变
//        SharedPreferanceUtils.put(Constants.LocationCity, cityWeather.getId());
        SharedPreferanceUtils.put(Constants.DefaultCity, cityWeather.getId());
    }

    /**
     * 查询城市信息表(定位的天气、手动搜索添加城市)
     * @param isLocation 是否为定位
     * @param name 查询的城市名称
     * @param database 查询城市时，传入的数据库
     */
    public static List<CityInfo> queryCityInfo(boolean isLocation, String name,SQLiteDatabase database){
        List<CityInfo> cityInfoList = new ArrayList<>();
        if(isLocation){
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + "/" + DB_NAME , null, SQLiteDatabase.OPEN_READWRITE);
            //根据定位结果，查询城市CODE（已经判断过定位结果是否为空）
            LocationInfo locationInfo = MyApplication.getMyApplication().getLocationInfo();
            String sql = "select * from cityinfo where province like \"" + locationInfo.getProvince().substring(0, 2) + "%\" " +
                    "and city like \"" + locationInfo.getCity().substring(0, 2) + "%\" " +
                    "and district like \"" + locationInfo.getDistrict().substring(0, 2) + "%\"";
            Cursor cursor = db.rawQuery(sql, null);
            //由于城市信息表的数据和所选择的天气api相关，固百度定位取得的区名有时不在城市信息数据中
            if (cursor.getCount() == 0) {
                sql = "select * from cityinfo where province like \"" + locationInfo.getProvince().substring(0, 2) + "%\" " +
                        "and city like \"" + locationInfo.getCity().substring(0, 2) + "%\"";
                cursor = db.rawQuery(sql, null);
                if (cursor.getCount() != 0) {
                    //取从数据表中查询到的第一条信息
                    cursor.moveToFirst();
                    cityInfoList.add(new CityInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                }
            } else {
                cursor.moveToFirst();
                cityInfoList.add(new CityInfo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }
            db.close();
        }else {   //城市选择界面，根据输入的文字，查询数据库中相似的城市并显示在list中
            //先将区县中符合条件的加入列表
            String sqlDistrict = "select * from cityinfo where district like \"" + name + "%\"";
            Cursor cursorDistrict = database.rawQuery(sqlDistrict, null);
            if (cursorDistrict.getCount() != 0){
                cursorDistrict.moveToFirst();
                do {
                    cityInfoList.add(new CityInfo(cursorDistrict.getString(0), cursorDistrict.getString(1),
                            cursorDistrict.getString(2), cursorDistrict.getString(3)));
                }while (cursorDistrict.moveToNext());
            }
            //再将市中符合条件并且没有添加到列表的添加到列表
            String sqlCity = "select * from cityinfo where city like \"" + name + "%\"";
            Cursor cursorCity = database.rawQuery(sqlCity, null);
            if (cursorCity.getCount() != 0){
                cursorCity.moveToFirst();
                boolean isAdd = true;   //是否已被添加到城市列表
                do {
                    for (int i=0;i<cityInfoList.size();i++){
                        if (cursorCity.getString(0).equals(cityInfoList.get(i).getId())){
                            isAdd = true;
                            break;
                        }
                        if (i == cityInfoList.size()-1){
                            isAdd = false;
                        }
                    }
                    if (!isAdd){
                        cityInfoList.add(new CityInfo(cursorCity.getString(0), cursorCity.getString(1),
                                cursorCity.getString(2), cursorCity.getString(3)));
                    }
                }while (cursorCity.moveToNext());
            }

        }
        return cityInfoList;
    }

    /**
     * 删除数据库中指定行的数据
     * @param id
     */
    public static void removeCity(String id){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + "/" + DB_NAME , null, SQLiteDatabase.OPEN_READWRITE);
        String sql = "delete from cityweather where id = \"" + id +"\"";
        db.execSQL(sql);
        db.close();
    }

    /**
     * 打开app时将数据库中的天气信息存入内存
     */
    public static void getAllWeatherToList(){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + "/" + DB_NAME , null, SQLiteDatabase.OPEN_READWRITE);
        String sql = "select * from cityweather";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                CityWeather cityWeather = new CityWeather();
                cityWeather.setId(cursor.getString(0));
                cityWeather.setDistrict(cursor.getString(1));
                cityWeather.setCity(cursor.getString(2));
                cityWeather.setProvince(cursor.getString(3));
                cityWeather.setLocation(cursor.getString(4));
                cityWeather.setAqi(cursor.getString(5));
                cityWeather.setPm25(cursor.getString(6));
                cityWeather.setQlty(cursor.getString(7));
                cityWeather.setLoc(cursor.getString(8));
                cityWeather.setfl(cursor.getString(9));
                cityWeather.sethum(cursor.getString(10));
                cityWeather.setpcpn(cursor.getString(11));
                cityWeather.setpres(cursor.getString(12));
                cityWeather.settmp(cursor.getString(13));
                cityWeather.setvis(cursor.getString(14));
                cityWeather.settxt(cursor.getString(15));
                cityWeather.setdir(cursor.getString(16));
                cityWeather.setsc(cursor.getString(17));
                cityWeather.setspd(cursor.getString(18));
                cityWeather.setComf_brf(cursor.getString(19));
                cityWeather.setComf_txt(cursor.getString(20));
                cityWeather.setCw_brf(cursor.getString(21));
                cityWeather.setCw_txt(cursor.getString(22));
                cityWeather.setDrsg_brf(cursor.getString(23));
                cityWeather.setDrsg_txt(cursor.getString(24));
                cityWeather.setFlu_brf(cursor.getString(25));
                cityWeather.setFlu_txt(cursor.getString(26));
                cityWeather.setSport_brf(cursor.getString(27));
                cityWeather.setSport_txt(cursor.getString(28));
                cityWeather.setUv_brf(cursor.getString(29));
                cityWeather.setUv_txt(cursor.getString(30));
                int num = 30;
                List<ForecastWeather> forecastWeatherList = new ArrayList<>();
                for (int j=0;j<6;j++){
                    num = j == 0 ? num : num + 14;
                    ForecastWeather forecastWeather = new ForecastWeather();
                    forecastWeather.setSr(cursor.getString(num + 1));
                    forecastWeather.setSs(cursor.getString(num + 2));
                    forecastWeather.setTxt_d(cursor.getString(num + 3));
                    forecastWeather.setTxt_n(cursor.getString(num + 4));
                    forecastWeather.setMax_tem(cursor.getString(num + 5));
                    forecastWeather.setMin_tem(cursor.getString(num + 6));
                    forecastWeather.setDir(cursor.getString(num + 7));
                    forecastWeather.setSc(cursor.getString(num + 8));
                    forecastWeather.setSpd(cursor.getString(num + 9));
                    forecastWeather.setDate(cursor.getString(num + 10));
                    forecastWeather.setHum(cursor.getString(num + 11));
                    forecastWeather.setPcpn(cursor.getString(num + 12));
                    forecastWeather.setPres(cursor.getString(num + 13));
                    forecastWeather.setVis(cursor.getString(num + 14));
                    forecastWeatherList.add(forecastWeather);
                }
                cityWeather.setForecastWeatherList(forecastWeatherList);
                MyApplication.cityWeatherList.add(cityWeather);
            }while (cursor.moveToNext());
        }
        db.close();
    }
}
