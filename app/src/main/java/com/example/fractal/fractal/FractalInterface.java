package com.example.fractal.fractal;

import com.example.fractal.utils.FractalType;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface FractalInterface {
    void bindUniforms(int mProgram, int width, int height);
    FractalType getType();
    void intoXML(Document doc, Element root);
}
