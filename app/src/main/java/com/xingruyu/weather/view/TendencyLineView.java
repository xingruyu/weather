package com.xingruyu.weather.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.xingruyu.weather.R;
import com.xingruyu.weather.utils.DensityUtils;

/**
 * 主界面天气趋势图中的直线
 * Created by WDX on 2016/11/16.
 */

public class TendencyLineView extends View {

    private int startX = 0;
    private int startY = 0;
    private int endX = 0;
    private int endY = 0;
    private boolean dayOrNight;
    private Paint paint;

    public TendencyLineView(Context context,int startX,int startY,int endX,int endY,boolean dayOrNight) {
        super(context);
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.dayOrNight = dayOrNight;

        paint = new Paint();
        paint.setAntiAlias(true);                            //消除锯齿
//        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtils.dpTopx(context,DensityUtils.dpTopx(context,1))*2/3); //线宽
        if (this.dayOrNight){
            paint.setColor(getResources().getColor(R.color.orange));
        }else {
            paint.setColor(getResources().getColor(R.color.skyblue));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(startX,startY,endX,endY,paint);
    }
}
