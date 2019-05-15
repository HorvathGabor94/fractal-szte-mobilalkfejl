package com.example.fractal.ogl;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;

import com.example.fractal.fractal.FractalWrapper;

import java.io.IOException;
import java.io.InputStream;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;



public class OGL_Renderer implements GLSurfaceView.Renderer {
    private static String vsc;
    private static String fsc;

    private AppCompatActivity activity;
    private FractalWrapper fractal;

    private int mProgram;
    private int width = 1;
    private int height = 1;

    private FloatBuffer vertexBuffer;
    private static final int  COORDS_PER_VERTEX = 4;
    private static final float[] coords = {
            -0.1f, +0.1f, 0.0f, 0.0f,
            -0.1f, -0.1f, 0.0f, 0.0f,
            +0.1f, -0.1f, 0.0f, 0.0f,
            +0.1f, +0.1f, 0.0f, 0.0f
    };
    private ShortBuffer indexBuffer;
    private static short[] order = {
            0, 1, 2,
            0, 2, 3
    };



    public OGL_Renderer(AppCompatActivity activity, FractalWrapper fractal){
        this.activity = activity;
        this.fractal = fractal;
    }
	
    public void draw(){
        GLES20.glUseProgram(mProgram);

        int mPos = GLES20.glGetAttribLocation(mProgram, "vPos");
        GLES20.glEnableVertexAttribArray(mPos);
        GLES20.glVertexAttribPointer(mPos, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, COORDS_PER_VERTEX*4, vertexBuffer);

        fractal.bindUniforms(mProgram, width, height);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, order.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);
        GLES20.glDisableVertexAttribArray(mPos);
    }

    public static int loadShader(int type, String code){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);
        return shader;
    }


    private String readFile(String path){
        InputStream input;
        try{
            input = this.activity.getAssets().open(path);
            byte[] buffer = new byte[input.available()];
            input.read(buffer);
            input.close();
            return new String(buffer);
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }
        return "";
    }
    private void init(){
        ByteBuffer cb = ByteBuffer.allocateDirect(coords.length * 4);
        cb.order(ByteOrder.nativeOrder());
        vertexBuffer = cb.asFloatBuffer();
        vertexBuffer.put(coords);
        vertexBuffer.position(0);


        ByteBuffer ob = ByteBuffer.allocateDirect(order.length * 2);
        ob.order(ByteOrder.nativeOrder());
        indexBuffer = ob.asShortBuffer();
        indexBuffer.put(order);
        indexBuffer.position(0);


        vsc = readFile("shaders/vertex_shader.vs");
        fsc = readFile("shaders/mandelbrot_attributes.fs");
        fsc += readFile("shaders/complex_math.fs");
        fsc += readFile("shaders/mandelbrot_fragment_shader.fs");
        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, loadShader(GLES20.GL_VERTEX_SHADER, vsc));
        GLES20.glAttachShader(mProgram, loadShader(GLES20.GL_FRAGMENT_SHADER, fsc));
        GLES20.glLinkProgram(mProgram);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        init();

        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glFrontFace(GLES20.GL_CCW);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_BACK);
        //System.out.println("onSurfaceCreated");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        this.width = width;
        this.height = height;
        //System.out.println("onSurfaceChanged");
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        draw();
        //System.out.println("onDrawFrame");
    }
}
