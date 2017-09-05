package com.youshi.tuixiangzi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 典杰 on 2017/8/10.
 */

public class StartUI extends Activity {
    private ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_ui);
        imageView = (ImageView) findViewById(R.id.start_ui_img);
        imageView.setBackground(SetBackground(R.raw.start_ui));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartUI.this,StartActivity.class);
                StartUI.this.startActivity(intent);
                StartUI.this.finish();
            }
        },3000);
    }
    //设置layout背景图片
    private Drawable SetBackground(int resID){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inPreferredConfig = Bitmap.Config.ARGB_4444;
        InputStream is = this.getResources().openRawResource(resID);
        Bitmap bitmaps = BitmapFactory.decodeStream(is,null,opt);
        try {
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        Drawable drawable = new BitmapDrawable(getResources(),bitmaps);
        return drawable;
    }
}
