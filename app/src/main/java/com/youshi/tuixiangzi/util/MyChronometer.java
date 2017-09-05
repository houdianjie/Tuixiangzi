package com.youshi.tuixiangzi.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Chronometer;

import com.youshi.tuixiangzi.MyApp;

/**
 * Created by 典杰 on 2017/8/11.
 */

public class MyChronometer extends Chronometer {
    public MyChronometer(Context context) {
        super(context);
        this.setTypeface(MyApp.typeface_all_used);
    }

    public MyChronometer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(MyApp.typeface_all_used);
    }

    public MyChronometer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(MyApp.typeface_all_used);
    }

    public MyChronometer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setTypeface(MyApp.typeface_all_used);
    }
}
