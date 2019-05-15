package com.example.fractal.utils;

import java.io.Serializable;

public class Color implements Serializable {
    private static final float PI2 = (float)Math.PI * 2f;


    public static final Color EMPTY = new Color(0, 0, 0, 0, true);
    public static final Color BLACK = new Color(0, 0, 0, 1, true);
    public static final Color WHITE = new Color(1, 1, 1, 1, true);
    public static final Color RED = new Color(1, 0, 0, 1, true);
    public static final Color YELLOW = new Color(1, 1, 0, 1, true);
    public static final Color GREEN = new Color(0, 1, 0, 1, true);
    public static final Color CYAN = new Color(0, 1, 1, 1, true);
    public static final Color BLUE = new Color(0, 0, 1, 1, true);
    public static final Color MAGENTA = new Color(1, 0, 1, 1, true);

    private boolean finalized = false;

    private float red = 0;
    private float green = 0;
    private float blue = 0;
    private float alpha = 0;


    public Color(){}
    public Color(float red, float green, float blue, float alpha){
        this(red, green, blue, alpha, false);
    }
    private Color(float red, float green, float blue, float alpha, boolean finalized){
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        this.finalized = finalized;
    }

    public float[] getFloatArray(){
        return new float[]{red, green, blue, alpha};
    }

    public float getRed(){
        return red;
    }
    public float getGreen(){
        return green;
    }
    public float getBlue(){
        return blue;
    }
    public float getAlpha(){
        return alpha;
    }

    public void setRed(float red){
        if(!finalized){
            this.red = red;
        }
    }
    public void setGreen(float green){
        if(!finalized){
            this.green = green;
        }
    }
    public void setBlue(float blue){
        if(!finalized){
            this.blue = blue;
        }
    }
    public void setAlpha(float alpha){
        if(!finalized){
            this.alpha = alpha;
        }
    }

    public Color getSelf(){
        return new Color(red, green, blue, alpha);
    }


    public static Color fromRGBA(Color color){
        return new Color(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f);
    }
    public static Color toRGBA(Color color){
        return new Color(color.red * 255f, color.green * 255f, color.blue * 255f, color.alpha * 255f);
    }
    public static Color fromHSVA(Color color, boolean inDegrees){
        if(inDegrees) {
            color.red = color.red / 360f;
        }
        else{
            color.red = color.red / PI2;
        }
        float[] pf = new float[]{(color.red-1f), (color.red-1f/2f), (color.red-1f/3f)};
        float[] f = new float[]{(pf[0]-(long)pf[0])*6f-3f, (pf[1]-(long)pf[1])*6f-3f, (pf[2]-(long)pf[2])*6f-3f};
        float[] p = new float[]{Math.abs(f[0]), Math.abs(f[1]), Math.abs(f[2])};

        float r = color.blue * ( (1f-color.green) + Math.max(Math.min(p[0]-1f, 1), 0) * color.green );
        float g = color.blue * ( (1f-color.green) + Math.max(Math.min(p[1]-1f, 1), 0) * color.green );
        float b = color.blue * ( (1f-color.green) + Math.max(Math.min(p[2]-1f, 1), 0) * color.green );
        return new Color(r, g, b, color.alpha / 100f);
    }
    public static Color toHSVA(Color color, boolean inDegrees){
        System.out.println("WARNING :: toHSVA method is unfinished!");
        return new Color(color.red, color.green, color.blue, color.alpha);
    }


}
