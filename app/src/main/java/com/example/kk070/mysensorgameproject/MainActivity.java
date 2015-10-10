package com.example.kk070.mysensorgameproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Vibrator mVibrator;
    private SensorManager sensorManager;
    private SensorView sensorView;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private int stage = 1;
    private Sensor prox;
    private boolean win;

    private Ball largeBall;
    private Ball smallBall;
    private Obstacle[] obstacle;
    private int amountOfTime =0;
    private float proximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Create");
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        super.onCreate(savedInstanceState);
        sensorView = new SensorView(this);
        sensorView.setBackgroundResource(R.drawable.background);
        setContentView(sensorView);
    }


    @Override
    protected void onResume(){
        System.out.println("Resume");
        super.onResume();
        if(sensorManager != null){
            sensorManager.registerListener(this, prox, SensorManager.SENSOR_DELAY_NORMAL);
            List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if (sensors.size() > 0) {
                sensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
            }
        }
    }

    @Override
    protected void onPause(){
        System.out.println("Pause");
        if(sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        System.out.println("Destroy");
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_PROXIMITY:
                proximity = event.values[0];
                checkDetection();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                checkCollision();
                sensorView.move(event.values[0], event.values[1]);
                break;
            default:
                break;
        }
    }

    public void checkDetection(){
        if(proximity < 1){
            turnOffScreen();
        }
    }

    public void checkCollision(){
        int distance = (int)getDistance((int)smallBall.getBallX(), (int)smallBall.getBallY(), (int)largeBall.getBallX(), (int)largeBall.getBallY());
        if(distance+smallBall.getBallRadius() <= largeBall.getBallRadius()){
            win =  true;
            mVibrator.vibrate(100);
            this.dialogSimple();
            this.onPause();
        }
        for(int i =0; i< obstacle.length-1 ; i++) {
            if(overlapCircleRectangle(smallBall, obstacle[i])){
                win = false;
                this.dialogSimple();
                this.onPause();
        }
        }
    /*
        for(int i =0; i< obstacle.length-1 ; i++){
             if((smallBall.getBallX()+smallBall.getBallRadius() >= obstacle[i].getxMin())  && (obstacle[i].getyMin() <= smallBall.getBallY() + smallBall.getBallRadius()) && (smallBall.getBallY() - smallBall.getBallRadius() <= obstacle[i].getyMin()+ obstacle[i].getHeight())){
                 win = false;
                 this.dialogSimple();
                 this.onPause();
             }

            if ((smallBall.getBallX() - smallBall.getBallRadius() <= obstacle[i].getxMin() + obstacle[i].getWidth()) && (obstacle[i].getyMin() <= smallBall.getBallY() + smallBall.getBallRadius()) && (smallBall.getBallY() - smallBall.getBallRadius() <= obstacle[i].getyMin() + obstacle[i].getHeight())) {
                  win = false;
                  this.dialogSimple();
                  this.onPause();
            }

        }
        */
    }

    private boolean overlapCircleRectangle(Ball c, Obstacle r) {
        float closestX = c.getBallX()-c.getBallRadius();
        float closestY = c.getBallY()-c.getBallRadius();

        if(c.getBallX()-c.getBallRadius() < r.getxMin()) {
            closestX = r.getxMin();
        }
        else if(c.getBallX()-c.getBallRadius() > r.getxMin() + r.getWidth()) {
            closestX =  r.getxMin() + r.getWidth();
        }

        if(c.getBallY()-c.getBallRadius() < r.getyMin()) {
            closestY = r.getyMin();
        }
        else if(c.getBallY()-c.getBallRadius()> r.getyMin() + r.getHeight()) {
            closestY = r.getyMin() + r.getHeight();
        }

        return distSquared(c, closestX, closestY) < c.getBallRadius() * c.getBallRadius();
    }

    private float distSquared(Ball c, float x, float y) {
        float distX = c.getBallX()-c.getBallRadius() - x;
        float distY = c.getBallY()-c.getBallRadius() - y;
        return distX*distX + distY*distY;
    }

    public void onAccuracyChanged(final Sensor sensor, int accuracy){

    }

    private void dialogSimple(){
        final Activity activity = this;
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("게임을 다시 시작하시겠습니까?").setCancelable(false).setPositiveButton("네",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sensorView = new SensorView(activity);
                sensorView.setBackgroundResource(R.drawable.background);
                setContentView(sensorView);
                onResume();
            }
            }).setNegativeButton("프로그램 종료", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Action for 'NO' Button
                dialog.cancel();
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        if(win) {
            alert.setTitle("게임 승리!");
            stage++;
        }else{
            alert.setTitle("게임 패배!");
            stage = 1;
        }
        // Icon for AlertDialog
        alert.setIcon(R.drawable.icon);
        alert.show();
    }

    private void newCreate(){
        if(sensorManager == null) {
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            prox = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
            if (sensors.size() > 0) {
                sensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
            }
        }
    }

    private double getDistance(int x, int y, int a, int b) {
        return Math.sqrt((x-a)* (x-a)+(y-b)*(y-b));
    }

   // @TargetApi(22) //Suppress lint error for PROXIMITY_SCREEN_OFF_WAKE_LOCK
    public void turnOffScreen(){
        // turn off screen
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "tag");
        mWakeLock.acquire();
        SystemClock.sleep(3000);
    }

    class SensorView extends View {

        private int w;
        private int h;
        private int randomX;
        private int randomY;
        private MainActivity activity;
        private Integer[] colorArray = {Color.YELLOW,Color.BLUE,Color.GREEN, Color.RED, Color.MAGENTA, Color.CYAN};

        public SensorView(Context context){
            super(context) ;
            this.activity = (MainActivity)context;
            //requestWindowFeature(Window.FEATURE_NO_TITLE);
            setKeepScreenOn(true);
            largeBall = new Ball(Color.BLACK);
            smallBall = new Ball(Color.GREEN);
            obstacle = new Obstacle[stage];
            for(int i = 0; i<obstacle.length-1; i++){
                obstacle[i] = new Obstacle(Color.RED);
                obstacle[i].setColor(colorArray[randomRange(0, 5)]);
            }

        }


        public void move(float mx, float my){
           smallBall.setBallX(smallBall.getBallX() - (mx * 3f));
           smallBall.setBallY(smallBall.getBallY() + (my * 3f));

            if(smallBall.getBallX() <0){
                smallBall.setBallX(0);
            }else if(smallBall.getBallX()+ smallBall.getBallRadius() > this.w){
                smallBall.setBallX(this.w - smallBall.getBallRadius());
            }

            if(smallBall.getBallY() <0){
                smallBall.setBallY(0);
            }else if(smallBall.getBallY() + smallBall.getBallRadius() > this.h){
                 smallBall.setBallY(this.h - smallBall.getBallRadius());
            }

            invalidate();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh){
            this.w = w;
            this.h = h;

            smallBall.setBallRadius(15);
            largeBall.setBallRadius(30);

            randomX = randomRange(10, w);
            randomY = randomRange(10, h);

            while(true) {
                if (randomX == 200 && randomY == 330) {
                    randomX = randomRange(10, w);
                    randomY = randomRange(10, h);
                    continue;
                }
                break;
            }

            largeBall.setBallX(randomX);
            largeBall.setBallY(randomY);

            for(int i = 0; i<obstacle.length-1; i++) {
                randomX = randomRange(10, w);
                randomY = randomRange(10, h);
                while(true) {
                    if ((randomX == 200  && randomY == 330) && (randomX == largeBall.getBallX() && randomY == largeBall.getBallY())) {
                        randomX = randomRange(10, w);
                        randomY = randomRange(10, h);
                        continue;
                    }
                    break;
                }
                obstacle[i].setxMin(randomX);
                obstacle[i].setyMin(randomY);
            }
        }

        @Override
        protected void onDraw(Canvas canvas){
            largeBall.set(canvas);
            smallBall.set(canvas);
            for(int i = 0; i<obstacle.length-1; i++){
                obstacle[i].set(canvas);
                obstacle[i].update();
            }
            activity.newCreate();

        }

        private int randomRange(int n1, int n2) {
            return (int) (Math.random() * (n2 - n1 + 1)) + n1;
        }



    }
}
