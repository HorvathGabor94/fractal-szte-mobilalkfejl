package com.example.fractal.ogl;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.example.fractal.MainActivity;
import com.example.fractal.fractal.FractalWrapper;
import com.example.fractal.fractal.MandelbrotFractal;
import com.example.fractal.utils.Complex;
import com.example.fractal.utils.InputHandler;
import com.example.fractal.utils.MotionType;

public class OGL_ActionView extends OGL_View {
    private final InputHandler inputHandler = new InputHandler();
    public MotionType motiontype = MotionType.NONE;

    public OGL_ActionView(AppCompatActivity activity, FractalWrapper fractal) {
		super(activity, fractal);
    }


    public static float PAN_MULTIPLIER = 0.0025f;
    public static float ZOOM_MULTIPLIER = 0.01f;
    public static float ITER_MULTIPLIER = 0.1f;
    public static float VALUE_MULTIPLIER = 0.001f;
    @Override
    public boolean onTouchEvent(MotionEvent e){
        inputHandler.handle(e);
        MainActivity mainAct = (MainActivity)activity;
        switch(inputHandler.retrieveAction()) {
            case CLICK:
                if (mainAct.fadingLayout.getVisibility() == View.INVISIBLE) {
                    mainAct.fadingLayout.setVisibility(View.VISIBLE);
                } else {
                    mainAct.fadingLayout.setVisibility(View.INVISIBLE);
                }
                break;
            case MOVEMENT:
                if (mainAct.fadingLayout.getVisibility() == View.INVISIBLE) {
                    boolean IS_POLAR = false;
                    boolean IN_DEGREES = true;
                    float[] movement = inputHandler.getMovement();
                    MandelbrotFractal mf = (MandelbrotFractal) fractal.get();
                    switch (motiontype) {
                        case PAN:
                            float[] pan = mf.getPan();
                            mf.setPan(pan[0] + (movement[0] * -PAN_MULTIPLIER), pan[1] + (movement[1] * PAN_MULTIPLIER));
                            requestRender();
                            break;
                        case ZOOM_ROT:
                            mf.setZoom(mf.getZoom() + (ZOOM_MULTIPLIER * movement[1]));
                            mf.setRotation(mf.getRotation(IN_DEGREES) + movement[0], IN_DEGREES);
                            requestRender();
                            break;
                        case CLAMP_RAD:
                            mf.setClamp(mf.getClamp() + (movement[0] * ZOOM_MULTIPLIER));
                            mf.setRadius(mf.getRadius() + (movement[1] * ZOOM_MULTIPLIER));
                            requestRender();
                            break;
                        case SYM_BAIL:
                            mf.setSymmetry(mf.getSymmetry() + (movement[0] * ZOOM_MULTIPLIER));
                            mf.setBailout(mf.getBailout() + (movement[1] * ZOOM_MULTIPLIER));
                            requestRender();
                            break;
                        case ITERS:
                            mf.setMinimumIteration(mf.getMinimumIteration() + (int) (movement[0] * ITER_MULTIPLIER));
                            mf.setMaximumIteration(mf.getMaximumIteration() + (int) (movement[1] * ITER_MULTIPLIER));
                            requestRender();
                            break;
                        case SEED:
                            Complex seed = mf.getSeed(IS_POLAR, IN_DEGREES);
                            mf.setSeed(new Complex(seed.getReal() + (movement[0] * VALUE_MULTIPLIER), seed.getImaginary() + (movement[1] * VALUE_MULTIPLIER)), IS_POLAR, IN_DEGREES);
                            requestRender();
                            break;
                        case POWER:
                            Complex power = mf.getPower(IS_POLAR, IN_DEGREES);
                            mf.setPower(new Complex(power.getReal() + (movement[0] * VALUE_MULTIPLIER), power.getImaginary() + (movement[1] * VALUE_MULTIPLIER)), IS_POLAR, IN_DEGREES);
                            requestRender();
                            break;
                        case ADD:
                            Complex add = mf.getAdd(IS_POLAR, IN_DEGREES);
                            mf.setAdd(new Complex(add.getReal() + (movement[0] * VALUE_MULTIPLIER), add.getImaginary() + (movement[1] * VALUE_MULTIPLIER)), IS_POLAR, IN_DEGREES);
                            requestRender();
                            break;
                        case MUL:
                            Complex mul = mf.getMul(IS_POLAR, IN_DEGREES);
                            mf.setMul(new Complex(mul.getReal() + (movement[0] * VALUE_MULTIPLIER), mul.getImaginary() + (movement[1] * VALUE_MULTIPLIER)), IS_POLAR, IN_DEGREES);
                            requestRender();
                            break;
                        case NONE:
                        default:
                            break;
                    }
                }
                break;
            case NONE:  default: break;
        }
        return true;
    }
}
