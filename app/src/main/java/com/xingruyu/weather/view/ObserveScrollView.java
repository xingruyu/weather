package com.xingruyu.weather.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 自定义ScrollView，实现滑动时Y值监听
 * Created by WDX on 2016/11/2.
 */

public class ObserveScrollView extends ScrollView {

    private ScrollListener mListener;

    public ObserveScrollView(Context context) {
        super(context);
    }

    public ObserveScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObserveScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (mListener != null) {
            mListener.scrollOritention(x, y, oldX, oldY);
        }
    }

    //声明接口，用于传递数据
    public static interface ScrollListener {
        public void scrollOritention(int x, int y, int oldX, int oldY);
    }

    public void setScrollListener(ScrollListener scrollListener) {
        this.mListener = scrollListener;
    }
}
