package com.example.fractal.fractal;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

public abstract class Fractal implements FractalInterface, Serializable {
    protected String name;
    protected Long epoch = null;

    protected Fractal(){
        initName();
    }

    private boolean isSaved(){
        return epoch != null;
    }
    public long getEpoch(){
        return epoch;
    }
    public void save(){
        if(!isSaved()){
            epoch = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000L;
        }
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public abstract Fractal getSelf();
    protected abstract void initName();
}
