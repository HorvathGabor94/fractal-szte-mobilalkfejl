package com.example.fractal.utils;

import java.io.Serializable;

public enum ColoringAlgorithm implements Serializable {
    MINIMAL(0, "minimal"),
    DISCRETE(1, "discrete"),
    LINEAR(2, "linear"),
    COLORFUL(3, "colorful"),
    TEST_1(4, "test_1"),
    TEST_2(5, "test_2");

    private int id;
    private String name;


     ColoringAlgorithm(int id, String name){
        this.id = id;
        this.name = name;
    }


    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }

    public static ColoringAlgorithm getByName(String name){
         for(ColoringAlgorithm ca : ColoringAlgorithm.values()){
             if(ca.name.equals(name)){
                 return ca;
             }
         }
         return COLORFUL;
    }
    public static ColoringAlgorithm getById(int id){
        for(ColoringAlgorithm ca : ColoringAlgorithm.values()){
            if(ca.id == id){
                return ca;
            }
        }
        return COLORFUL;
    }
}
