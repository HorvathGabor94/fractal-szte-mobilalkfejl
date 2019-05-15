package com.example.fractal.utils;

import java.io.Serializable;

public enum FractalType  implements Serializable {
    MANDELBROT(0, "mandelbrot"),
    COLLATZ(1, "collatz");

    private int id;
    private String name;


    FractalType(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public static FractalType getByName(String name){
        for(FractalType ft : FractalType.values()){
            if(ft.name.equals(name)){
                return ft;
            }
        }
        return MANDELBROT;
    }
    public static FractalType getById(int id){
        for(FractalType ft : FractalType.values()){
            if(ft.id == id){
                return ft;
            }
        }
        return MANDELBROT;
    }
}
