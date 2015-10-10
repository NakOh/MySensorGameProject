package com.example.kk070.mysensorgameproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by kk070 on 2015-10-10.
 */
public class Obstacle {

    private Paint paint;
    private Rect bounds;

    private int width = 30;
    private int height = 110;

    private float speedY = 10;

    private int xMin,yMin;


    public Obstacle(int color){
        paint = new Paint();
        paint.setColor(color);
        bounds = new Rect();
    }

    public void set( Canvas canvas){
        bounds.set(xMin, yMin, xMin + width, yMin + height);
        canvas.drawRect(bounds, paint);
    }


    public void setColor(int color){
        paint.setColor(color);
    }

    public void update(){
        yMin += speedY;
        if(yMin>1600){
            speedY = -speedY;
        }

        if(yMin<0){
            speedY = -speedY;
        }
    }




    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public int getxMin() {
        return xMin;
    }

    public void setxMin(int xMin) {
        this.xMin = xMin;
    }

    public int getyMin() {
        return yMin;
    }

    public void setyMin(int yMin) {
        this.yMin = yMin;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
