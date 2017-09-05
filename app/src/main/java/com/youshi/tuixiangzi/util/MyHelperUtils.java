package com.youshi.tuixiangzi.util;

import android.util.Log;

import java.text.ParseException;

/**
 * Created by 典杰 on 2017/8/10.
 */

public class MyHelperUtils {
    private static  MyHelperUtils myHelperUtils = new MyHelperUtils();
    public static MyHelperUtils NewHelperUtils(){
        if(myHelperUtils==null){
            myHelperUtils = new MyHelperUtils();
        }
        return myHelperUtils;
    }

    /**
     *
     * @param str 要转换的字符串,格式:{[0-9]#...}
     * @return int[][]//转换后的整形数组
     */
    public static int[][] StrToIntArray(String str,int rows,int columns){
        String[] temp_str = str.replace(" ","").split("#");
        int[][] temp_array = new int[rows][columns];
        System.out.println(str);
        try{
            for(int i=0;i<rows;i++){
                for(int k=0;k<columns;k++){
                    temp_array[i][k] = Integer.parseInt(temp_str[i * columns + k]);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return temp_array;
    }

    /**
     *
     * @param strs  等级数组
     * @return  int[strs.length][2]，其中int[strs][0]代表关数，int[strs][1]代表每关的箱子数
     */
    public static int[][] LevelStr2LevelArray(String[] strs){
        int[][] levels = new int[strs.length][4];
        for(int i=0;i<strs.length;i++){
            String[] temp = strs[i].split("#");
            try{
                levels[i][0] = Integer.parseInt(temp[0]);
                levels[i][1] = Integer.parseInt(temp[1]);
                levels[i][2] = Integer.parseInt(temp[2]);
                levels[i][3] = Integer.parseInt(temp[3]);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return levels;
    }
}
