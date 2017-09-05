package com.youshi.tuixiangzi.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.youshi.tuixiangzi.R;

import java.io.InputStream;

/**
 * Created by 典杰 on 2017/8/10.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private int level;
    private int columns,rows;
    private int[][] layerOneArray;
    private int[][] layerTwoArray;
    private int boxNums;
   private String[] layer01str;
   private String[] layer02str;
    private MyThread myThread;
    public OnFinishListener onFinishListener;
    private Bitmap bmpWall;
    private Bitmap bmpFloor;
    private Bitmap bmpTarget;
    private Bitmap bmpPlayer;
    private Bitmap bmpTargetBox;
    private Bitmap bmpBox;
    private Bitmap bmpWater;
    private boolean isLeft,isRight,isUp,isDown,isFinish;
    private Context context;
    private SoundPlay soundPlay;
    //帧率
    private static final int m_FPS = 20;

    public GameView(Context context,int level,int boxNum) {
        super(context);
       // Log.e("初始化","执行了");
        this.context = context;
        this.level = level;
        this.boxNums = boxNum;
        this.columns = 10;
        this.rows = 10;
        init();
    }
    public GameView(Context context,int level,int boxNum,int rows,int columns) {
        super(context);
        this.context = context;
        this.level = level;
        this.boxNums = boxNum;
        this.columns = columns;
        this.rows = rows;
        init();
    }
    public GameView(Context context,int level,int boxNum,int rows,int columns,SoundPlay soundPlay) {
        super(context);
        this.context = context;
        this.level = level;
        this.boxNums = boxNum;
        this.soundPlay = soundPlay;
        this.columns = columns;
        this.rows = rows;
        init();
    }

    private void init(){
        mHolder = getHolder();
       mHolder.addCallback(this);
        setZOrderOnTop(true);

        mHolder.setFormat(PixelFormat.TRANSPARENT);
        setZOrderMediaOverlay(true);
        layer01str = getResources().getStringArray(R.array.layer_one_array);
        layer02str = getResources().getStringArray(R.array.layer_two_array);
        layerOneArray = MyHelperUtils.StrToIntArray(layer01str[level],rows,columns);
        layerTwoArray = MyHelperUtils.StrToIntArray(layer02str[level],rows,columns);
        initBMPRes();

    }

    private void initBMPRes(){
        bmpFloor = getBitmap(R.raw.floor);
        bmpTarget = getBitmap(R.raw.target);
        bmpWall = getBitmap(R.raw.wall);
        bmpBox = getBitmap(R.raw.box);
        bmpTargetBox = getBitmap(R.raw.target_box);
        bmpPlayer = getBitmap(R.raw.player);
        bmpWater = getBitmap(R.raw.water);
        isDown=false;isUp=false;isLeft=false;isRight=false;
        isFinish = false;
    }
    class MyThread extends Thread{

        //初始化自定义线程
        SurfaceHolder surfaceHolder;
        Context context;
        boolean isRunning;
        Paint paint;

        public MyThread(SurfaceHolder surfaceHolder,Context context) {

            this.surfaceHolder = surfaceHolder;
            this.context = context;
            isRunning = false;
        }

        //停止线程
        public void stopThread(){
            isRunning = false;
            boolean isFinish = false;
            while (!isFinish) {
                try {
                    this.join();// 保证run方法执行完毕
                    isFinish = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //开始执行线程体
        @Override
        public void run() {
            long deltaTime = 0;
            long tickTime = 0;
            tickTime = System.currentTimeMillis();
            while (isRunning) {
                Canvas canvas = null;
                try {
                    synchronized (surfaceHolder) {
                        canvas = surfaceHolder.lockCanvas();
                        if(canvas!=null) {
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                            //进行绘制操作
                            updatePos();
                            DrawBaseLayer(canvas);
                            DrawSecondLayer(canvas);
                            isFinished(canvas, boxNums);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                deltaTime = System.currentTimeMillis() - tickTime;
                if(deltaTime < 1000/m_FPS) {
                    try {
                        Thread.sleep(1000/m_FPS - deltaTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tickTime = System.currentTimeMillis();
            }
        }
    }

    public void DrawBaseLayer(Canvas canvas){
        for(int i=0;i<layerOneArray.length;i++){
            for(int j = 0;j<layerOneArray[i].length;j++){
                if(layerOneArray[i][j]==0){
                    canvas.drawBitmap(bmpWater,bmpWater.getWidth()*j,bmpWater.getHeight()*i,null);
                    //canvas.drawBitmap(bmpWall,bmpWall.getWidth()*j,bmpWall.getHeight()*i,null);
                }else if(layerOneArray[i][j]==1){
                    canvas.drawBitmap(bmpWall,bmpWall.getWidth()*j,bmpWall.getHeight()*i,null);
                }else if(layerOneArray[i][j]==2){
                    canvas.drawBitmap(bmpFloor,bmpFloor.getWidth()*j,bmpFloor.getHeight()*i,null);
                }else if(layerOneArray[i][j]==3){
                    canvas.drawBitmap(bmpTarget,bmpTarget.getWidth()*j,bmpTarget.getHeight()*i,null);
                }
            }
        }
    }

    private void DrawSecondLayer(Canvas canvas){
        for(int i=0;i<layerTwoArray.length;i++){
            for(int j = 0;j<layerTwoArray[i].length;j++){
              if(layerTwoArray[i][j] == 3){
                  canvas.drawBitmap(bmpBox,bmpBox.getWidth()*j,bmpBox.getHeight()*i,null);

              }else if(layerTwoArray[i][j]==5){
                  canvas.drawBitmap(bmpPlayer,bmpPlayer.getWidth()*j,bmpPlayer.getHeight()*i,null);
              }
            }
        }
    }

    public void isFinished(Canvas canvas,final int boxCount){
        int count=0;
        for(int i=0;i<layerTwoArray.length;i++){
            for(int j=0;j<layerTwoArray[i].length;j++){
                if(layerTwoArray[i][j] == 3 && layerOneArray[i][j]==3){
                    count++;
                    canvas.drawBitmap(bmpTargetBox,bmpTargetBox.getWidth()*j,bmpTargetBox.getHeight()*i,null);
                    if(count == boxCount){
                        if(!isFinish) {
                            onFinishListener.finished();
                            isFinish = true;
                        }
                        return;
                    }
                }
            }
        }
    }

    private void updatePos(){

            for(int i=0;i<layerTwoArray.length;i++){
                for(int j=0;j<layerTwoArray[i].length;j++) {
                    if(layerTwoArray[i][j]==5) {
                        //下
                        if (isDown) {
                            if (layerOneArray[i + 1][j] == 1) {
                                soundPlay.playSound("touchwall");
                            } else if (layerTwoArray[i + 1][j] == 3 && layerOneArray[i + 2][j] != 1 && layerTwoArray[i+2][j] != 3) {
                                layerTwoArray[i + 1][j] = 5;
                                layerTwoArray[i][j] = 2;
                                layerTwoArray[i + 2][j] = 3;
                                if(layerOneArray[i+2][j]==3){
                                    soundPlay.playSound("rightplace");
                                }
                            } else if(layerOneArray[i+1][j]!=1 && layerTwoArray[i+1][j]!=3) {
                                layerTwoArray[i + 1][j] = 5;
                                layerTwoArray[i][j] = 2;
                            }else {
                                soundPlay.playSound("touchwall");
                            }
                        }

                        //上
                        if(isUp){
                            if(layerOneArray[i-1][j]==1){
                                soundPlay.playSound("touchwall");
                            }else if(layerTwoArray[i-1][j]==3 && layerOneArray[i-2][j]!=1 && layerTwoArray[i-2][j] != 3){
                                layerTwoArray[i-1][j]=5;
                                layerTwoArray[i][j]=2;
                                layerTwoArray[i-2][j]=3;
                                if(layerOneArray[i-2][j]==3){
                                    soundPlay.playSound("rightplace");
                                }
                            }else if(layerOneArray[i-1][j]!=1 && layerTwoArray[i-1][j]!=3){
                                layerTwoArray[i-1][j]=5;
                                layerTwoArray[i][j]=2;
                            }else {
                                soundPlay.playSound("touchwall");
                            }
                        }
                        //左
                        if(isLeft){
                            if(layerOneArray[i][j-1]==1){
                                soundPlay.playSound("touchwall");
                            }else if(layerTwoArray[i][j-1]==3 && layerOneArray[i][j-2]!=1 && layerTwoArray[i][j-2] != 3){
                                layerTwoArray[i][j-1]=5;
                                layerTwoArray[i][j]=2;
                                layerTwoArray[i][j-2]=3;
                                if(layerOneArray[i][j-2]==3){
                                    soundPlay.playSound("rightplace");
                                }
                            }else if(layerOneArray[i][j-1]!=1 && layerTwoArray[i][j-1]!=3){
                                layerTwoArray[i][j-1]=5;
                                layerTwoArray[i][j]=2;
                            }else {
                                soundPlay.playSound("touchwall");
                            }
                        }
                        //右
                        if(isRight){
                            if(layerOneArray[i][j+1]==1){
                                soundPlay.playSound("touchwall");
                            }else if(layerTwoArray[i][j+1]==3 && layerOneArray[i][j+2]!=1 && layerTwoArray[i][j+2] != 3){
                                layerTwoArray[i][j+1]=5;
                                layerTwoArray[i][j]=2;
                                layerTwoArray[i][j+2]=3;
                                if(layerOneArray[i][j+2]==3){
                                    soundPlay.playSound("rightplace");
                                }
                            }else if(layerOneArray[i][j+1]!=1 && layerTwoArray[i][j+1]!=3){
                                layerTwoArray[i][j+1]=5;
                                layerTwoArray[i][j]=2;
                            }else {
                                soundPlay.playSound("touchwall");
                            }
                        }
                        isLeft=false;isDown=false;isRight=false;isUp=false;
                        return;
                    }
                }
        }
    }
    public void setOnFinishListener(OnFinishListener finishListener){
        onFinishListener = finishListener;
    }

    public interface OnFinishListener{
        public void finished();
    }


    private Bitmap getBitmap(int resId){
        Bitmap bmp;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inPreferredConfig = Bitmap.Config.ARGB_4444;
        InputStream is = context.getResources().openRawResource(resId);
        bmp = BitmapFactory.decodeStream(is,null,opt);
        return bmp;
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
            myThread = new MyThread(mHolder,getContext());
        myThread.isRunning = true;
        myThread.start();
    }

    public void setDirections(boolean isLeft,boolean isRight,boolean isUp,boolean isDown){
        this.isDown=isDown;
        this.isUp=isUp;
        this.isLeft=isLeft;
        this.isRight=isRight;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            myThread.stopThread();
       bmpFloor=null;bmpPlayer=null;bmpTargetBox=null;
        bmpBox=null;bmpWall=null;bmpTarget=null;bmpWater=null;
        System.gc();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       int height = View.MeasureSpec.getSize(heightMeasureSpec);
      int width = View.MeasureSpec.getSize(widthMeasureSpec);
        //Log.e("测量","执行了");
        setMeasuredDimension(columns*100,rows*100);  //这里面是原始的大小，需要重新计算可以修改本行
    }
}
