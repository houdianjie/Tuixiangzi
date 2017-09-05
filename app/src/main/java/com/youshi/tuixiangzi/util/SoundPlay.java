package com.youshi.tuixiangzi.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 典杰 on 2017/8/12.
 */

public class SoundPlay {
    private  SoundPool soundPool;
    private  Context m_context;
    private  HashMap<String,Integer> soundMap;
    private int currentSound=-1;
    public  SoundPlay(Context context,HashMap<String,Integer> map){
        m_context = context;
        init();
        loadSound(map);

    }
    private  void init(){
        SoundPool.Builder spb = new SoundPool.Builder();
        spb.setMaxStreams(10);
        AudioAttributes.Builder attrBuild = new AudioAttributes.Builder();
        attrBuild.setLegacyStreamType(AudioManager.STREAM_MUSIC);
        spb.setAudioAttributes(attrBuild.build());
        soundPool = spb.build();

    }

    private  void loadSound(HashMap<String,Integer> maps){
        if(maps.size()==0)
            return;
        soundMap = new HashMap<String, Integer>();
        for(Map.Entry<String,Integer> map:maps.entrySet()){
                soundMap.put(map.getKey(),soundPool.load(m_context,map.getValue(),1));
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                Log.d("LoadSound","Complete");
            }
        });
    }

    public void playSound(String name){
        if(currentSound!=-1){
            soundPool.stop(currentSound);
        }
        soundPool.play(soundMap.get(name),1.0f,1.0f,1,0,1.0f);
        currentSound = soundMap.get(name);
    }

    public void closePlaySound(){
        soundPool.release();
        soundPool = null;
        m_context = null;
    }
}
