package com.youshi.tuixiangzi.util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.youshi.tuixiangzi.MyApp;

/**
 * Created by 典杰 on 2017/8/11.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
        this.setTypeface(MyApp.typeface_all_used);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(MyApp.typeface_all_used);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(MyApp.typeface_all_used);
    }
}
