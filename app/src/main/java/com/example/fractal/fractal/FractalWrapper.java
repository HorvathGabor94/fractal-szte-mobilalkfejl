package com.example.fractal.fractal;

import com.example.fractal.utils.FractalType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FractalWrapper implements FractalInterface{

    private Fractal fractal;
    private Fractal original;


    public FractalWrapper(){
        set(new MandelbrotFractal());
    }
    public FractalWrapper(Fractal fractal){
        set(fractal);
        if(this.fractal == null){
            set(new MandelbrotFractal());
            System.out.println("Fractal initialized as Mandelbrot by default.");
        }
    }

    public Fractal get(){
        return this.fractal;
    }
    public Fractal getOriginal(){
        return this.original;
    }
    public void set(Fractal fractal){
        if(fractal != null){
            this.fractal = fractal;
            this.original = fractal.getSelf();
        }
        else{
            System.err.println("Cannot set fractal to NULL!");
        }
    }
    public void setBoth(Fractal fractal, Fractal original){
        this.fractal = fractal;
        this.original = fractal.getSelf();
    }
    public void reset(){
        this.fractal = this.original.getSelf();
    }
    public void center(){
        ((MandelbrotFractal)fractal).setPan(0,0);
        ((MandelbrotFractal)fractal).setZoom(10);
        ((MandelbrotFractal)fractal).setRotation(0,true);
    }

    @Override
    public void bindUniforms(int mProgram, int width, int height) {
        fractal.bindUniforms(mProgram, width, height);
    }
    @Override
    public FractalType getType(){
        return fractal.getType();
    }
    @Override
    public void intoXML(Document doc, Element root) {
		fractal.intoXML(doc, root);
    }
}
