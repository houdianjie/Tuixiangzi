package com.youshi.tuixiangzi;

import android.app.Application;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;

/**
 * Created by 典杰 on 2017/8/11.
 */

public class MyApp extends Application {
    public static Typeface typeface_all_used;


    @Override
    public void onCreate() {
        super.onCreate();
        typeface_all_used = Typeface.createFromAsset(getAssets(),"fonts/huakangshaonv.TTF");
    }
}
