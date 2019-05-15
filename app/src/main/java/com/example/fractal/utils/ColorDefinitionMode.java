package com.example.fractal.utils;

import java.io.Serializable;

public enum ColorDefinitionMode implements Serializable {
    DEFAULT(0, "default"),          // [0.0, 1.0] x 4
    RGBA(1, "rgba"),                // [0, 255] x 4
    HSVA(2, "hsva");                // { [0, 360) / [0, PI2) ; [0, 100] x 3 }

    private int id;
    private String name;


     ColorDefinitionMode(int id, String name){
        this.id = id;
        this.name = name;
    }


    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public static ColorDefinitionMode getIdByName(String name){
         for(ColorDefinitionMode cdm : ColorDefinitionMode.values()){
             if(cdm.name.equals(name)){
                 return cdm;
             }
         }
         return RGBA;
    }
    public static ColorDefinitionMode getById(int id){
        for(ColorDefinitionMode cdm : ColorDefinitionMode.values()){
            if(cdm.id == id){
                return cdm;
            }
        }
        return RGBA;
    }
}
