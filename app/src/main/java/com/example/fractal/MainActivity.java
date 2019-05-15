package com.example.fractal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.example.fractal.fractal.FractalWrapper;
import com.example.fractal.fractal.MandelbrotFractal;
import com.example.fractal.ogl.OGL_ActionView;
import com.example.fractal.utils.MotionType;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity{
    public static final int FRACTAL_OPTIONS_REQUEST = 1;
    public static final int FRACTAL_LOAD_REQUEST = 2;

    private final FractalWrapper fractal = new FractalWrapper();

    private OGL_ActionView view;

    public FrameLayout frameLayout;
    public ConstraintLayout fadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fadingLayout = findViewById(R.id.fadingLayout);
        //fadingLayout.setVisibility(View.INVISIBLE);
        view = new OGL_ActionView(this, fractal);
        frameLayout = findViewById(R.id.frameLayout);
        frameLayout.addView(view, 0);
    }

    public void reset(View view){
        this.fractal.reset();
        this.view.requestRender();
    }
    public void center(View view){
        this.fractal.center();
        this.view.requestRender();
    }
    public void launchOptions(View view){
        Intent intent = new Intent(this, MandelbrotOptionsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("MandelbrotFractal", fractal.get().getSelf());
        intent.putExtras(bundle);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, MainActivity.FRACTAL_OPTIONS_REQUEST);
        }
    }
    public void launchGallery(View view){
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, "fractal_gallery");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, MainActivity.FRACTAL_LOAD_REQUEST);
        }
    }
    /*
    public void popupSave(View view){
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("Save Fractal");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        ab.setView(input);


        ab.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        ab.show();
    }
    */

    private final int[] radioIds = {
        R.id.noneRadio, R.id.panRadio, R.id.zoomrotRadio,
        R.id.clampradRadio, R.id.symbailRadio, R.id.itersRadio,
        R.id.seedRadio, R.id.powerRadio, R.id.addRadio, R.id.mulRadio
    };
    public void onRadioButtonClicked(View view){
        int id = view.getId();
        correctRadioButtons(id);
    }
    private void correctRadioButtons(int id){
        for(int i=0; i< radioIds.length; i++){
            int rid = radioIds[i];
            RadioButton rb = findViewById(rid);
            if(rid == id){
                this.view.motiontype = MotionType.values()[i];
                rb.setChecked(true);
            }
            else{
                rb.setChecked(false);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                MandelbrotFractal mandelbrot_fractal = (MandelbrotFractal)bundle.getSerializable("MandelbrotFractal");
                if(mandelbrot_fractal != null){
                    view.requestRender();
                    fractal.set(mandelbrot_fractal);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putSerializable("fractal", fractal.get());
        bundle.putSerializable("original", fractal.getOriginal());
        bundle.putInt("radioID", this.view.motiontype.ordinal());
        super.onSaveInstanceState(bundle);
    }
    @Override
    public void onRestoreInstanceState(Bundle bundle){
        super.onRestoreInstanceState(bundle);
        Serializable ser = bundle.getSerializable("fractal");
        Serializable ser2 = bundle.getSerializable("original");
        if(ser != null && ser2 != null){
            fractal.setBoth((MandelbrotFractal)ser, (MandelbrotFractal)ser2);
        }
        else{
            fractal.set(new MandelbrotFractal());
        }
        Integer rid = bundle.getInt("radioID");
        if(rid != null){
            correctRadioButtons(rid);
        }
    }




}
