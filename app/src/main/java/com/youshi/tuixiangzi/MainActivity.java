package com.youshi.tuixiangzi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youshi.tuixiangzi.util.CustomScrollView;
import com.youshi.tuixiangzi.util.GameView;
import com.youshi.tuixiangzi.util.MyHelperUtils;
import com.youshi.tuixiangzi.util.MyPassGameDialog;
import com.youshi.tuixiangzi.util.PassAllLevesDialog;
import com.youshi.tuixiangzi.util.ReturnMainDialog;
import com.youshi.tuixiangzi.util.SoundPlay;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by 典杰 on 2017/8/10.
 */

public class MainActivity extends Activity {
    private LinearLayout layoutGameView;
    private GameView gameView;
    private Chronometer gameTimer;
    private int[][] levels;
    private int currentLevel;
    private MyHandler myHandler;
    private int gameSteps;
    private TextView gameSteps_tv;
    private TextView gameLevelTv;
    private MyPassGameDialog passDialog;
    private static final int FINISH_CODE=0x801;
    private SoundPlay soundPlay;
    private LinearLayout linearLayout;
    private HorizontalScrollView scrollGameView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        init_gameLevels();
        init();
    }


    private void init()
    {
        layoutGameView = (LinearLayout) findViewById(R.id.main_gameview_layout);
        gameTimer = (Chronometer) findViewById(R.id.gameTimer);
        gameSteps_tv = (TextView) findViewById(R.id.gameSteps);
        gameLevelTv = (TextView) findViewById(R.id.game_level_tv);
        linearLayout = (LinearLayout) findViewById(R.id.main_activity_linear);
        linearLayout.setBackground(SetBackground(R.raw.game_background));
        scrollGameView = (HorizontalScrollView) findViewById(R.id.main_gameview_scrollview);
        soundPlay = new SoundPlay(MainActivity.this,loadSounds());
        myHandler = new MyHandler();
        gameView = new GameView(this,levels[currentLevel][0],levels[currentLevel][1],
                levels[currentLevel][2],levels[currentLevel][3],soundPlay);
        gameSteps_tv.setText(gameSteps+"");
        gameLevelTv.setText("第"+(currentLevel+1)+"关");
        setTimer();
        //layoutGameView.addView(gameView);
        scrollGameView.addView(gameView);
        gameView.setOnFinishListener(new GameView.OnFinishListener() {
            @Override
            public void finished() {
                //此处发出弹出恭喜过关窗口信息
                myHandler.sendEmptyMessage(FINISH_CODE);
            }
        });
    }

    //加载声音
    private HashMap<String,Integer> loadSounds(){
        HashMap<String,Integer> map = new HashMap<>();
        map.put("rightplace",R.raw.right_place);
        map.put("walk",R.raw.walk);
        map.put("touchwall",R.raw.touchwall);
        map.put("saywin",R.raw.saywin);
        map.put("clicksound",R.raw.click_sound);
        return map;
    }

    //重置计时器
    private void setTimer(){
       // Log.e("shijian", getChronometerSeconds(gameTimer));
        gameTimer.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - gameTimer.getBase()) / 1000 / 60);
        gameTimer.setFormat("0"+String.valueOf(hour)+":%s");
        gameTimer.start();
    }

    //初始化关卡
    private void init_gameLevels(){
        String[] temp = getResources().getStringArray(R.array.game_level);
        levels = MyHelperUtils.LevelStr2LevelArray(temp);
        currentLevel = 0;
        gameSteps = 0;
    }


    //各种按钮相应回调函数
    public void main_btn_right_click(View view){
        gameSteps++;
        gameSteps_tv.setText(gameSteps+"");
        gameView.setDirections(false,true,false,false);
        soundPlay.playSound("walk");
    }
    public void main_btn_left_click(View view){
        gameSteps++;
        gameSteps_tv.setText(gameSteps+"");
        gameView.setDirections(true,false,false,false);
        soundPlay.playSound("walk");
    }
    public void main_btn_up_click(View view){
        gameSteps++;
        gameSteps_tv.setText(gameSteps+"");
        gameView.setDirections(false,false,true,false);
        soundPlay.playSound("walk");
    }
    public void main_btn_down_click(View view){
        gameSteps++;
        gameSteps_tv.setText(gameSteps+"");
gameView.setDirections(false,false,false,true);
        soundPlay.playSound("walk");
    }

    //返回主菜单按钮
    public void main_return_btn_click(View view){
        soundPlay.playSound("clicksound");
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    //下一关按钮
    public void main_next_level_btn_click(View view){
        soundPlay.playSound("clicksound");
       // layoutGameView.removeAllViews();
        scrollGameView.removeAllViews();
        gameView = null;
        if(currentLevel<levels.length-1) {
            currentLevel++;
        }else {
            Toast.makeText(this,"已经是最后一关！",Toast.LENGTH_SHORT).show();
        }
        gameSteps=0;
        gameSteps_tv.setText(gameSteps+"");
        gameLevelTv.setText("第"+(currentLevel+1)+"关");
        setTimer();
        gameView = new GameView(this,levels[currentLevel][0],levels[currentLevel][1],
                levels[currentLevel][2],levels[currentLevel][3],soundPlay);
        //layoutGameView.addView(gameView);
        scrollGameView.addView(gameView);
        gameView.setOnFinishListener(new GameView.OnFinishListener() {
            @Override
            public void finished() {
                //此处发送弹出恭喜过关窗口信息
               myHandler.sendEmptyMessage(FINISH_CODE);
            }
        });
    }

    //上一关按钮
    public void main_pre_level_btn_click(View view){
        soundPlay.playSound("clicksound");
        //layoutGameView.removeAllViews();
        scrollGameView.removeAllViews();
        gameView = null;
        if(currentLevel>0) {
            currentLevel--;
        }
        gameSteps=0;
        gameSteps_tv.setText(gameSteps+"");
        gameLevelTv.setText("第"+(currentLevel+1)+"关");
        setTimer();
        gameView = new GameView(this,levels[currentLevel][0],levels[currentLevel][1],
                levels[currentLevel][2],levels[currentLevel][3],soundPlay);
        //layoutGameView.addView(gameView);
        scrollGameView.addView(gameView);
        gameView.setOnFinishListener(new GameView.OnFinishListener() {
            @Override
            public void finished() {
                //此处发送弹出恭喜过关窗口信息
                myHandler.sendEmptyMessage(FINISH_CODE);
            }
        });
    }

    //重置关卡按钮
    public void main_reset_level_btn_click(View view){
        soundPlay.playSound("clicksound");
        //layoutGameView.removeAllViews();
        scrollGameView.removeAllViews();
        gameView = null;
        gameSteps=0;
        gameSteps_tv.setText(gameSteps+"");
        gameLevelTv.setText("第"+(currentLevel+1)+"关");
        setTimer();
        gameView = new GameView(this,levels[currentLevel][0],levels[currentLevel][1],
                levels[currentLevel][2],levels[currentLevel][3],soundPlay);
        //layoutGameView.addView(gameView);
        scrollGameView.addView(gameView);
        gameView.setOnFinishListener(new GameView.OnFinishListener() {
            @Override
            public void finished() {
                //此处弹出恭喜过关窗口
                myHandler.sendEmptyMessage(FINISH_CODE);
            }
        });
    }


//内部handler类
    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == currentLevel){
            //layoutGameView.removeAllViews();
                scrollGameView.removeAllViews();
                gameView = null;
                if(currentLevel<levels.length-1) {
                    currentLevel++;
                    gameSteps=0;
                    gameSteps_tv.setText(gameSteps+"");
                    gameLevelTv.setText("第"+(currentLevel+1)+"关");
                    setTimer();
                    gameView = new GameView(MainActivity.this,levels[currentLevel][0],levels[currentLevel][1],
                            levels[currentLevel][2],levels[currentLevel][3],soundPlay);
                    gameView.setBackgroundColor(getResources().getColor(R.color.transparent));
                    scrollGameView.addView(gameView);
                    //layoutGameView.addView(gameView);
                    gameView.setOnFinishListener(new GameView.OnFinishListener() {
                        @Override
                        public void finished() {
                            //此处弹出恭喜过关窗口
                            myHandler.sendEmptyMessage(FINISH_CODE);
                            // pushScoreDialog();
                        }
                    });
                }else {
                    pushPassAllGameDialog();
                }

            }
            if(msg.what==FINISH_CODE){
                pushScoreDialog();
            }
        }
    }

    //计算成绩
    private int calcuScore(){
        int time=0;
        int score=0;
        try{
            time = Integer.parseInt(getChronometerSeconds(gameTimer));;
        }catch (Exception e){
            e.printStackTrace();
        }
         int stepScore = (500-gameSteps)>0?(500-gameSteps):10;
        double timeScore = ((1-time/500.0)*600)>0?((1-time/500.0)*500):10;
        score = (int)timeScore + stepScore;
        return score;
    }

//弹出对话框
    private void pushScoreDialog(){
        int score = calcuScore();
        soundPlay.playSound("saywin");
        gameTimer.stop();
        if(passDialog!=null){
            passDialog = null;
        }
        passDialog = new MyPassGameDialog(MainActivity.this,R.style.PassGameDialog,score);
        passDialog.show();
        Window window = passDialog.getWindow();
        passDialog.setCancelable(false);
        final ImageButton btn1= (ImageButton) window.findViewById(R.id.passgame_cancel_btn);
        final ImageButton btn2 = (ImageButton) window.findViewById(R.id.passgame_confirm_btn);
       btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDialog.cancel();
                passDialog =null;
                myHandler.sendEmptyMessage(currentLevel);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDialog.cancel();
                passDialog=null;
                Intent intent = new Intent(MainActivity.this,StartActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }

    private void pushPassAllGameDialog(){
       final PassAllLevesDialog allLevesDialog = new PassAllLevesDialog(MainActivity.this,R.style.PassGameDialog);
        allLevesDialog.show();
        Window window = allLevesDialog.getWindow();
        allLevesDialog.setCancelable(false);
        final ImageButton btn1= (ImageButton) window.findViewById(R.id.pass_all_cancel_btn);
        final ImageButton btn2 = (ImageButton) window.findViewById(R.id.pass_all_confirm_btn);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentLevel=-1;
                myHandler.sendEmptyMessage(currentLevel);
                allLevesDialog.cancel();

            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allLevesDialog.cancel();
                Intent intent = new Intent(MainActivity.this,StartActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
           final ReturnMainDialog dialog = new ReturnMainDialog(this,R.style.PassGameDialog);
            dialog.show();
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            final ImageButton btn1= (ImageButton) window.findViewById(R.id.return_main_cancel_btn);
            final ImageButton btn2 = (ImageButton) window.findViewById(R.id.return_main_confirm_btn);
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //myHandler.sendEmptyMessage(currentLevel);
                    dialog.cancel();
                    //dialog =null;
                }
            });
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    Intent intent = new Intent(MainActivity.this,StartActivity.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                }
            });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPlay.closePlaySound();
        soundPlay = null;
    }

    //设置layout背景图片
    private Drawable SetBackground(int resID){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inPreferredConfig = Bitmap.Config.ARGB_4444;
        InputStream is = getResources().openRawResource(resID);
        Bitmap bitmaps = BitmapFactory.decodeStream(is,null,opt);
        try {
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        Drawable drawable = new BitmapDrawable(getResources(),bitmaps);
        return drawable;
    }

    /**
     *
     * @param cmt  Chronometer控件
     * @return 小时+分钟+秒数  的所有秒数
     */
    public  String getChronometerSeconds(Chronometer cmt) {
        int totalss = 0;
        String string = cmt.getText().toString();
        if(string.length()==8){

            String[] split = string.split(":");
            String string2 = split[0];
            int hour = Integer.parseInt(string2);
            int Hours =hour*3600;
            String string3 = split[1];
            int min = Integer.parseInt(string3);
            int Mins =min*60;
            int  SS =Integer.parseInt(split[2]);
            totalss = Hours+Mins+SS;
            return String.valueOf(totalss);
        }

        else if(string.length()==5){

            String[] split = string.split(":");
            String string3 = split[0];
            int min = Integer.parseInt(string3);
            int Mins =min*60;
            int  SS =Integer.parseInt(split[1]);

            totalss =Mins+SS;
            return String.valueOf(totalss);
        }
        return String.valueOf(totalss);

    }
}
