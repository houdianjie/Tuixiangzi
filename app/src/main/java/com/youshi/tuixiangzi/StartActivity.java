package com.youshi.tuixiangzi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.youshi.tuixiangzi.util.HelpDialog;
import com.youshi.tuixiangzi.util.SoundPlay;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by 典杰 on 2017/8/11.
 */

public class StartActivity extends Activity {
    private SoundPlay soundPlay;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        soundPlay = new SoundPlay(this,loadSounds());
        linearLayout = (LinearLayout) findViewById(R.id.start_activity_bg);
        linearLayout.setBackground(SetBackground(R.raw.main_start));
    }

    //按钮响应
    public void main_ui_start_btn_click(View view){
        soundPlay.playSound("clicksound");
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void main_ui_help_btn_click(View view){
        soundPlay.playSound("clicksound");
        //Toast.makeText(this,"这是帮助按钮",Toast.LENGTH_SHORT).show();
        HelpDialog helpDialog = new HelpDialog(this,R.style.PassGameDialog);
        helpDialog.show();
    }
    public void main_ui_exit_btn_click(View view){
        soundPlay.playSound("clicksound");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    //加载声音
    private HashMap<String,Integer> loadSounds(){
        HashMap<String,Integer> map = new HashMap<>();
        map.put("clicksound",R.raw.click_sound);
        return map;
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
        Drawable drawable = new BitmapDrawable( getResources(),bitmaps);
        return drawable;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPlay.closePlaySound();
        soundPlay = null;
    }
}
