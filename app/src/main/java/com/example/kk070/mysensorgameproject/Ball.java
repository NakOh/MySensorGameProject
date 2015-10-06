package com.example.kk070.mysensorgameproject;

/**
 * Created by kk070 on 2015-10-05.
 */
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;

public class Ball {

    private float ballRadius = 30;

    private float ballX = ballRadius + 200;
    private float ballY = ballRadius + 330;


    private float ballSpeedX = 30;
    private float ballSpeedY = 1;
    private float previousX;
    private float previousY;
    private RectF ballBounds;
    private Paint paint;

    public Ball(int color){
        ballBounds = new RectF();
        paint = new Paint();
        paint.setColor(color);
    }

    public void set(Canvas canvas){
        ballBounds.set(ballX - ballRadius, ballY - ballRadius, ballX + ballRadius, ballY + ballRadius);
        canvas.drawOval(ballBounds, paint);
    }
    public void update(){
        //가속센서 값을 넘겨받아 증가시킨다.
        /*
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        if (ballX + ballRadius > xMax) {
            ballSpeedX = -ballSpeedX;
            ballX = xMax - ballRadius;
        } else if (ballX - ballRadius < xMin) {
            ballSpeedX = -ballSpeedX;
            ballX = xMin + ballRadius;
        }

        if (ballY + ballRadius > yMin) {
            ballSpeedY = -ballSpeedY;
            ballY = yMin - ballRadius;
        } else if (ballY - ballRadius < yMax) {
            ballSpeedY = -ballSpeedY;
            ballY = yMax + ballRadius;
        }
        */

    }


    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public float getPreviousX(){
        return previousX;
    }

    public float getPreviousY(){
        return previousY;
    }

    public void setPreviousX(float previousX){
        this.previousX = previousX;
    }

    public void setPreviousY(float previousY){
        this.previousY = previousY;
    }


    public float getBallSpeedX() {
        return ballSpeedX;
    }

    public void setBallSpeedX(float ballSpeedX) {
        this.ballSpeedX = ballSpeedX;
    }

    public float getBallSpeedY() {
        return ballSpeedY;
    }

    public void setBallSpeedY(float ballSpeedY) {
        this.ballSpeedY = ballSpeedY;
    }


    public float getBallX() {
        return ballX;
    }

    public void setBallX(float ballX) {
        this.ballX = ballX;
    }

    public float getBallY() {
        return ballY;
    }

    public void setBallY(float ballY) {
        this.ballY = ballY;
    }

    public float getBallRadius() {
        return ballRadius;
    }

    public void setBallRadius(float ballRadius) {
        this.ballRadius = ballRadius;
    }
}