package com.example.fractal.utils;

import java.io.Serializable;

public enum FractalAlgorithm  implements Serializable {
    DEFAULT(0, "default"),
    COS(1, "cos"),
    SIN(2, "sin"),
    TAN(3, "tan"),
    CTG(4, "ctg"),
    SEC(5, "sec"),
    CSC(4, "csc"),
    COSH(1, "cosh"),
    SINH(2, "sinh"),
    TANH(3, "tanh"),
    CTGH(4, "ctgh"),
    SECH(5, "sech"),
    CSCH(4, "csch");

    private int id;
    private String name;


    FractalAlgorithm(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public static FractalAlgorithm getByName(String name){
        for(FractalAlgorithm fa : FractalAlgorithm.values()){
            if(fa.name.equals(name)){
                return fa;
            }
        }
        return DEFAULT;
    }
    public static FractalAlgorithm getById(int id){
        for(FractalAlgorithm fa : FractalAlgorithm.values()){
            if(fa.id == id){
                return fa;
            }
        }
        return DEFAULT;
    }
}
