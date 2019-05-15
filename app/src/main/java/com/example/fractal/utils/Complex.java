package com.example.fractal.utils;

import java.io.Serializable;

public class Complex  implements Serializable {
    private float x;
    private float y;


    public Complex(float real, float imaginary){
        setReal(real);
        setImaginary(imaginary);
    }

    public float[] getFloatArray(){
        return new float[]{x, y};
    }

    public float getReal(){
        return x;
    }
    public float getImaginary(){
        return y;
    }
    public float getAbsolute(){
        return (float)Math.sqrt(x*x + y*y);
    }
    public float getArgument(){
        return (float)Math.atan2(y, x);
    }

    public void setReal(float real){
        if(real != Float.NaN){
            this.x = real;
        }
        else{
            this.x = 0;
        }
    }
    public void setImaginary(float imaginary){
        if(imaginary != Float.NaN){
            this.y = imaginary;
        }
        else{
            this.y = 0;
        }
    }
    public void setAbsolute(float absolute){
        Complex tmp = CartesianToPolar(this);
        tmp.x = absolute;
        tmp = PolarToCartesian(tmp);
        this.x = tmp.x;
        this.y = tmp.y;
    }
    public void setArgument(float argument){
        Complex tmp = CartesianToPolar(this);
        tmp.y = argument;
        tmp = PolarToCartesian(tmp);
        this.x = tmp.x;
        this.y = tmp.y;
    }

    public Complex getSelf(){
        return new Complex (x, y);
    }



    public static Complex CartesianToPolar(Complex value){
        return new Complex(value.getAbsolute(), value.getArgument());
    }
    public static Complex PolarToCartesian(Complex value){
        return new Complex(value.x * (float)Math.cos(value.y), value.x * (float)Math.sin(value.y));
    }
    public static Complex PolarToRadians(Complex value){
        return new Complex(value.x, (float)Math.toRadians(value.y));
    }
    public static Complex PolarToDegrees(Complex value){
        return new Complex(value.x, (float)Math.toDegrees(value.y));
    }

    @Override
    public String toString(){
        return "C-[" + x + ", " + y + "]";
    }
}
