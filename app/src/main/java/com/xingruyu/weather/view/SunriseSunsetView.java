package com.xingruyu.weather.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.xingruyu.weather.utils.DensityUtils;

/**
 * 主界面日出日落类
 * Created by WDX on 2016/10/31.
 */

public class SunriseSunsetView extends View{

    private Context context;
    private Paint paint;       //画笔
    private Path path;
    private PathMeasure mPathMeasure;   //测量路径的坐标位置
    private float[] coords = new float[2];  //运动目标的x，y坐标
    private ValueAnimator valueAnimator;
    private int stopPoint = 0;    //动画停止在何处
    private int width = 0;        //椭圆弧的长边距

    public SunriseSunsetView(Context context) {
        this(context,null);
    }

    public SunriseSunsetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        mPathMeasure = new PathMeasure();

        paint.setAntiAlias(true);                            //消除锯齿
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtils.dpTopx(context,1)); //线宽
        paint.setColor(Color.WHITE);

        //初始值x=20,y=130
        coords[0] = 20;
        coords[1] = 130;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //定义的椭圆弧外接矩形的四个角的位置。
        RectF rectF = new RectF(30, 15, getWidth()-30, getHeight()-15);
        path.addArc(rectF, 180, 180);
        //绘制圆弧
        canvas.drawPath(path, paint);
        //绘制运动目标
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), com.xingruyu.weather.R.drawable.ic_sun);
        canvas.drawBitmap(bitmap,coords[0],coords[1], paint);
        bitmap.recycle();
        //绘制底部直线
        canvas.drawLine(14,getHeight()/2,getWidth()-14,getHeight()/2,paint);
        //定义太阳运动的轨迹（直接用圆弧的轨迹会有较大的偏差，不清楚为什么）
        RectF rectF1 = new RectF(5, 0, getWidth()-50, getHeight()-50);
        Path path1 = new Path();
        path1.addArc(rectF1, 180, 180);
        mPathMeasure.setPath(path1,false);   //第二个参数设置为false后，动画会结束后会停止在结束地

        width = getWidth()-30*2;
    }

    // 开启动画
    public void startAnim() {
        valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到coords
                mPathMeasure.getPosTan(value, coords, null);
                postInvalidate();
                if (coords[0] > stopPoint){
                    valueAnimator.cancel();
                }
            }
        });
        valueAnimator.start();
    }

    /**
     * 设置动画运动到何处停止
     * @param stopPoint 运动目标运动到某个X值
     */
    public void setStopPoint(int stopPoint){
        this.stopPoint = stopPoint;
    }

    /**
     * 获取椭圆弧的长边距，用来计算动画运动到何处停止
     * @return
     */
    public int getArcWidth(){
        return width;
    }
}
