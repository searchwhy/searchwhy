package com.example.aaron.searchwhy;

import android.graphics.Bitmap;

/**
 * Created by Aaron on 2016-11-25.
 */
public class gameInfo {
    Bitmap buf;
    int index;
    int time;
    int area;
    int spotArea;

    public gameInfo(Bitmap bitmap, int index, int spotArea){
        this.buf = bitmap;
        this.index = index -1;
        this.spotArea = spotArea;
        this.area = buf.getHeight()*buf.getWidth();
        this.time = (int)((index*index*area)/(12*spotArea));
    }
}