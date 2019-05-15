package com.example.fractal.utils;

import android.view.MotionEvent;

import java.util.Calendar;
import java.util.TimeZone;

public class InputHandler {
    public static long LONG_CLICK_MS = 200;
    public static float STATIONARY_DISTANCE_LIMIT = 50;



    private long[] clickEpoch = {0, 0};

    private float[] lastPosition = null;
    private float[] currentPosition = null;
    private float[] changes = {0, 0};
    private float distanceTraveled = 0;

    private DefinitiveAction act = DefinitiveAction.NONE;



    public void handle(MotionEvent event){
        if(currentPosition != null){
            lastPosition = new float[]{currentPosition[0], currentPosition[1]};
        }
        currentPosition = new float[]{event.getX(), event.getY()};

        if(lastPosition != null){
            float dX = currentPosition[0] - lastPosition[0];
            float dY = currentPosition[1] - lastPosition[1];
            changes = new float[]{dX, dY};
            distanceTraveled += (float)Math.sqrt(dX*dX + dY*dY);
        }

        long epoch = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                clickEpoch[0] = epoch;
                distanceTraveled = 0;
                break;
            case MotionEvent.ACTION_UP:
                clickEpoch[1] = epoch;
                break;
        }

        evaluate();
    }
    public DefinitiveAction retrieveAction(){
        DefinitiveAction result = act;
        act = DefinitiveAction.NONE;
        return result;
    }

    public void evaluate(){
        if(distanceTraveled < STATIONARY_DISTANCE_LIMIT) {                 // not much motion
            if (clickEpoch[0] < clickEpoch[1]) {                           // released
                if (clickEpoch[1] - clickEpoch[0] > LONG_CLICK_MS) {       // definitely a long click
                    act = DefinitiveAction.CLICK;
                }
            }
        }
        else{
            act = DefinitiveAction.MOVEMENT;
        }
    }
    public float[] getMovement(){
        return changes;
    }
}
