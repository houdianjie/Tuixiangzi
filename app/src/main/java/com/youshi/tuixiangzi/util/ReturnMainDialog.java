package com.youshi.tuixiangzi.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youshi.tuixiangzi.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 典杰 on 2017/8/11.
 */

public class ReturnMainDialog extends Dialog {
    private Context context;

    private TextView scoreTextview;
    private Button confirmBtn;
    private LinearLayout linearLayout;

    public ReturnMainDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public ReturnMainDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected ReturnMainDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init(){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reutrn_main_dialog, null);
            setContentView(view);
         Window dialogWindow = getWindow();
         WindowManager.LayoutParams lp = dialogWindow.getAttributes();
         DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
         lp.width = (int) (d.widthPixels); // 高度设置为屏幕的0.6
         lp.height = (int) (d.heightPixels); // 高度设置为屏幕的0.6
         dialogWindow.setAttributes(lp);
        linearLayout = (LinearLayout) view.findViewById(R.id.return_main_dialog_linear);
        linearLayout.setBackground(SetBackground(R.raw.return_menu_dialog));
    }
    //设置layout背景图片
    private Drawable SetBackground(int resID){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inPreferredConfig = Bitmap.Config.ARGB_4444;
        InputStream is = context.getResources().openRawResource(resID);
        Bitmap bitmaps = BitmapFactory.decodeStream(is,null,opt);
        try {
            is.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        Drawable drawable = new BitmapDrawable(context.getResources(),bitmaps);
        return drawable;
    }

}
