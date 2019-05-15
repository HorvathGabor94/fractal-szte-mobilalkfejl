package com.example.fractal.ogl;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import com.example.fractal.fractal.FractalWrapper;

public class OGL_View extends GLSurfaceView {
	protected AppCompatActivity activity;
    protected FractalWrapper fractal;

    public OGL_View(AppCompatActivity activity, FractalWrapper fractal) {
        super(activity);
		
		this.activity = activity;
        this.fractal = fractal;
        setEGLContextClientVersion(2);
        setRenderer(new OGL_Renderer(activity, fractal));
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
