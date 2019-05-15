package com.example.fractal.utils;

import java.io.Serializable;

public enum SweepAlgorithm implements Serializable {
    DEFAULT(0, "default"),
    ALPHA(1, "alpha"),
    BETA(2, "beta");

    private int id;
    private String name;


    SweepAlgorithm(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public static SweepAlgorithm getByName(String name){
        for(SweepAlgorithm sa : SweepAlgorithm.values()){
            if(sa.name.equals(name)){
                return sa;
            }
        }
        return DEFAULT;
    }
    public static SweepAlgorithm getById(int id){
        for(SweepAlgorithm sa : SweepAlgorithm.values()){
            if(sa.id == id){
                return sa;
            }
        }
        return DEFAULT;
    }
}
