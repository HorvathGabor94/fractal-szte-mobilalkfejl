package com.example.fractal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fractal.fractal.MandelbrotFractal;
import com.example.fractal.utils.Color;
import com.example.fractal.utils.ColorDefinitionMode;
import com.example.fractal.utils.ColoringAlgorithm;
import com.example.fractal.utils.Complex;
import com.example.fractal.utils.FractalAlgorithm;
import com.example.fractal.utils.SweepAlgorithm;

import java.io.Serializable;

public class MandelbrotOptionsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String FLOAT_FORMATTER = "%.4f";
    private static final String COLOR_FORMATTER = "%.2f";

    private static boolean isPolar = false;
    private static boolean inDegrees = false;
    private static ColorDefinitionMode colorform = ColorDefinitionMode.DEFAULT;


    private MandelbrotFractal fractal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandelbrot_options);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Serializable ser = bundle.getSerializable("MandelbrotFractal");
        if(ser != null){
            fractal = (MandelbrotFractal)ser;
        }
        else{
            fractal = new MandelbrotFractal();
        }

        setupHandles();

        setupNavigation();
        setupAesthetics();
        setupParameters();
        //setupColoring();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putSerializable("fractal", fractal);
        super.onSaveInstanceState(bundle);
    }
    @Override
    public void onRestoreInstanceState(Bundle bundle){
        super.onRestoreInstanceState(bundle);
        Serializable ser = bundle.getSerializable("MandelbrotFractal");
        if(ser != null){
            fractal = (MandelbrotFractal)ser;
        }
        else{
            fractal = new MandelbrotFractal();
        }
    }
	
	private Float parseFloat(String value){
		try{
			return Float.parseFloat(value);
		}
		catch(NumberFormatException exc){
		    return null;
        }
	}
	private Integer parseInt(String value){
		try{
			return Integer.parseInt(value);
		}
		catch(NumberFormatException exc){
            return null;
        }
	}

    private void setupHandles(){
        Switch degreesSwitch = findViewById(R.id.degreesSwitch);
        degreesSwitch.setChecked(inDegrees);
        degreesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                inDegrees = isChecked;
                recalculateAngles();
            }
        });
        Switch polarformSwitch = findViewById(R.id.polarformSwitch);
        polarformSwitch.setChecked(isPolar);
        polarformSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPolar = isChecked;
                recalculateComplexNumbers();
            }
        });

        /*Spinner colorspaceSpinner = findViewById(R.id.colorspaceSpinner);
        if(colorspaceSpinner != null){
            colorspaceSpinner.setOnItemSelectedListener(this);
            colorspaceSpinner.setSelection(colorform.getId());
            colorspaceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    colorform = ColorDefinitionMode.getById((int)id);
                    recalculateColors();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    colorform = ColorDefinitionMode.DEFAULT;
                    recalculateColors();
                }
            });
        }*/
    }

    private void setupNavigation(){
        EditText zoomInput = findViewById(R.id.zoomInput);
        zoomInput.setText(String.format(FLOAT_FORMATTER, fractal.getZoom()));
        zoomInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.zoomInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setZoom(value)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getZoom()));
                        }
                    }
                }
            }
        });

        EditText rotationInput = findViewById(R.id.rotationInput);
        rotationInput.setText(String.format(FLOAT_FORMATTER, fractal.getRotation(inDegrees)));
        rotationInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.rotationInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setRotation(value, inDegrees)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getRotation(inDegrees)));
                        }
                    }
                }
            }
        });

        EditText panXInput = findViewById(R.id.panXInput);
        panXInput.setText(String.format(FLOAT_FORMATTER, fractal.getPan()[0]));
        panXInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.panXInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setPan(value, fractal.getPan()[1])){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getPan()[0]));
                        }
                    }
                }
            }
        });
        EditText panYInput = findViewById(R.id.panYInput);
        panYInput.setText(String.format(FLOAT_FORMATTER, fractal.getPan()[1]));
        panYInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.panYInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setPan(fractal.getPan()[0], value)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getPan()[1]));
                        }
                    }
                }
            }
        });
    }

    private void setupAesthetics(){
        EditText clampInput = findViewById(R.id.clampInput);
        clampInput.setText(String.format(FLOAT_FORMATTER, fractal.getClamp()));
        clampInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.clampInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setClamp(value)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getClamp()));
                        }
                    }
                }
            }
        });

        EditText radiusInput = findViewById(R.id.radiusInput);
        radiusInput.setText(String.format(FLOAT_FORMATTER, fractal.getRadius()));
        radiusInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.radiusInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setRadius(value)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getRadius()));
                        }
                    }
                }
            }
        });

        EditText symmetryInput = findViewById(R.id.symmetryInput);
        symmetryInput.setText(String.format(FLOAT_FORMATTER, fractal.getSymmetry()));
        symmetryInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.symmetryInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setSymmetry(value)){
                            et.setText(String.format("FLOAT_FORMATTER", fractal.getSymmetry()));
                        }
                    }
                }
            }
        });

        EditText bailoutInput = findViewById(R.id.bailoutInput);
        bailoutInput.setText(String.format(FLOAT_FORMATTER, fractal.getBailout()));
        bailoutInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.bailoutInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setBailout(value)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getBailout()));
                        }
                    }
                }
            }
        });

        EditText iterationMinimumInput = findViewById(R.id.iterationMinimumInput);
        iterationMinimumInput.setText(String.format("%s", fractal.getMinimumIteration()));
        iterationMinimumInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.iterationMinimumInput);
                    Integer value = parseInt(et.getText().toString());
                    if(value != null){
                        if(!fractal.setMinimumIteration(value)){
                            et.setText(String.format("%s", fractal.getMinimumIteration()));
                        }
                    }
                }
            }
        });

        EditText iterationMaximumInput = findViewById(R.id.iterationMaximumInput);
        iterationMaximumInput.setText(String.format("%s", fractal.getMaximumIteration()));
        iterationMaximumInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.iterationMaximumInput);
                    Integer value = parseInt(et.getText().toString());
                    if(value != null){
                        if(!fractal.setMaximumIteration(value)){
                            et.setText(String.format("%s", fractal.getMaximumIteration()));
                        }
                    }
                }
            }
        });
    }

    private void setupParameters(){
        /*Spinner sweepSpinner = findViewById(R.id.sweepSpinner);
        if(sweepSpinner != null){
            sweepSpinner.setOnItemSelectedListener(this);
            sweepSpinner.setSelection(fractal.getSweepAlgorithm().getId());
            sweepSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fractal.setSweepAlgorithm(SweepAlgorithm.getById((int)id));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ((Spinner)findViewById(R.id.sweepSpinner)).setSelection(0);
                }
            });
        }*/

        Spinner prefixSpinner = findViewById(R.id.prefixSpinner);
        if(prefixSpinner != null){
            prefixSpinner.setOnItemSelectedListener(this);
            prefixSpinner.setSelection(fractal.getPrefixAlgorithm().getId());
            prefixSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fractal.setPrefixAlgorithm(FractalAlgorithm.getById((int)id));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ((Spinner)findViewById(R.id.prefixSpinner)).setSelection(0);
                }
            });
        }

        Spinner infixSpinner = findViewById(R.id.infixSpinner);
        if(infixSpinner != null){
            infixSpinner.setOnItemSelectedListener(this);
            infixSpinner.setSelection(fractal.getInfixAlgorithm().getId());
            infixSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fractal.setInfixAlgorithm(FractalAlgorithm.getById((int)id));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ((Spinner)findViewById(R.id.infixSpinner)).setSelection(0);
                }
            });
        }

        Spinner algorithmSpinner = findViewById(R.id.algorithmSpinner);
        if(algorithmSpinner != null){
            algorithmSpinner.setOnItemSelectedListener(this);
            algorithmSpinner.setSelection(fractal.getFractalAlgorithm().getId());
            algorithmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fractal.setFractalAlgorithm(FractalAlgorithm.getById((int)id));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ((Spinner)findViewById(R.id.algorithmSpinner)).setSelection(0);
                }
            });
        }

        Switch juliaSwitch = findViewById(R.id.juliaSwitch);
        juliaSwitch.setChecked(fractal.isJulia());
        juliaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            fractal.setJulia(isChecked);
            }
        });

        Switch invertedSwitch = findViewById(R.id.invertedSwitch);
        invertedSwitch.setChecked(fractal.isInverted());
        invertedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            fractal.setInverted(isChecked);
            }
        });

        EditText seedReInput = findViewById(R.id.seedReInput);
        seedReInput.setText(String.format(FLOAT_FORMATTER, fractal.getSeed(isPolar, inDegrees).getReal()));
        seedReInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.seedReInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setSeed(new Complex(value, fractal.getSeed(isPolar, inDegrees).getImaginary()), isPolar, inDegrees)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getSeed(isPolar, inDegrees).getReal()));
                        }
                    }
                }
            }
        });
        EditText seedImInput = findViewById(R.id.seedImInput);
        seedImInput.setText(String.format(FLOAT_FORMATTER, fractal.getSeed(isPolar, inDegrees).getImaginary()));
        seedImInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.seedImInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setSeed(new Complex(fractal.getSeed(isPolar, inDegrees).getReal(), value), isPolar, inDegrees)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getSeed(isPolar, inDegrees).getImaginary()));
                        }
                    }
                }
            }
        });

        EditText powerReInput = findViewById(R.id.powerReInput);
        powerReInput.setText(String.format(FLOAT_FORMATTER, fractal.getPower(isPolar, inDegrees).getReal()));
        powerReInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.powerReInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setPower(new Complex(value, fractal.getPower(isPolar, inDegrees).getImaginary()), isPolar, inDegrees)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getPower(isPolar, inDegrees).getReal()));
                        }
                    }
                }
            }
        });
        EditText powerImInput = findViewById(R.id.powerImInput);
        powerImInput.setText(String.format(FLOAT_FORMATTER, fractal.getPower(isPolar, inDegrees).getImaginary()));
        powerImInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.powerImInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setPower(new Complex(fractal.getPower(isPolar, inDegrees).getReal(), value), isPolar, inDegrees)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getPower(isPolar, inDegrees).getImaginary()));
                        }
                    }
                }
            }
        });

        EditText addReInput = findViewById(R.id.addReInput);
        addReInput.setText(String.format(FLOAT_FORMATTER, fractal.getAdd(isPolar, inDegrees).getReal()));
        addReInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.addReInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setAdd(new Complex(value, fractal.getAdd(isPolar, inDegrees).getImaginary()), isPolar, inDegrees)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getAdd(isPolar, inDegrees).getReal()));
                        }
                    }
                }
            }
        });
        EditText addImInput = findViewById(R.id.addImInput);
        addImInput.setText(String.format(FLOAT_FORMATTER, fractal.getAdd(isPolar, inDegrees).getImaginary()));
        addImInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.addImInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setAdd(new Complex(fractal.getAdd(isPolar, inDegrees).getReal(), value), isPolar, inDegrees)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getAdd(isPolar, inDegrees).getImaginary()));
                        }
                    }
                }
            }
        });

        EditText multiplyReInput = findViewById(R.id.multiplyReInput);
        multiplyReInput.setText(String.format(FLOAT_FORMATTER, fractal.getMul(isPolar, inDegrees).getReal()));
        multiplyReInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.multiplyReInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setMul(new Complex(value, fractal.getMul(isPolar, inDegrees).getImaginary()), isPolar, inDegrees)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getMul(isPolar, inDegrees).getReal()));
                        }
                    }
                }
            }
        });
        EditText multiplyImInput = findViewById(R.id.multiplyImInput);
        multiplyImInput.setText(String.format(FLOAT_FORMATTER, fractal.getMul(isPolar, inDegrees).getImaginary()));
        multiplyImInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    EditText et = findViewById(R.id.multiplyImInput);
                    Float value = parseFloat(et.getText().toString());
                    if(value != null){
                        if(!fractal.setMul(new Complex(fractal.getMul(isPolar, inDegrees).getReal(), value), isPolar, inDegrees)){
                            et.setText(String.format(FLOAT_FORMATTER, fractal.getMul(isPolar, inDegrees).getImaginary()));
                        }
                    }
                }
            }
        });
    }
/*
    private void setupColoring(){
        Spinner colormodeSpinner = findViewById(R.id.colormodeSpinner);
        if(colormodeSpinner != null){
            colormodeSpinner.setOnItemSelectedListener(this);
            colormodeSpinner.setSelection(fractal.getColoringAlgorithm().getId());
            colormodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    fractal.setColoringAlgorithm(ColoringAlgorithm.getById((int)id));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ((Spinner)findViewById(R.id.colormodeSpinner)).setSelection(0);
                }
            });
        }

        EditText externalRedInput = findViewById(R.id.externalRedInput);
        externalRedInput.setText(String.format(COLOR_FORMATTER, fractal.getExternalColor(colorform, inDegrees).getRed()));
        externalRedInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Float value = parseFloat(((EditText)findViewById(R.id.externalRedInput)).getText().toString());
                    if(value != null){
                        Color c = fractal.getExternalColor(colorform, inDegrees);
                        fractal.setExternalColor(new Color(value, c.getGreen(), c.getBlue(), c.getAlpha()), colorform, inDegrees);
                    }
                }
            }
        });
        EditText externalGreenInput = findViewById(R.id.externalGreenInput);
        externalGreenInput.setText(String.format(COLOR_FORMATTER, fractal.getExternalColor(colorform, inDegrees).getGreen()));
        externalGreenInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Float value = parseFloat(((EditText)findViewById(R.id.externalGreenInput)).getText().toString());
                    if(value != null) {
                        Color c = fractal.getExternalColor(colorform, inDegrees);
                        fractal.setExternalColor(new Color(c.getRed(), value, c.getBlue(), c.getAlpha()), colorform, inDegrees);
                    }
                }
            }
        });
        EditText externalBlueInput = findViewById(R.id.externalBlueInput);
        externalBlueInput.setText(String.format(COLOR_FORMATTER, fractal.getExternalColor(colorform, inDegrees).getBlue()));
        externalBlueInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Float value = parseFloat(((EditText)findViewById(R.id.externalBlueInput)).getText().toString());
                    if(value != null) {
                        Color c = fractal.getExternalColor(colorform, inDegrees);
                        fractal.setExternalColor(new Color(c.getRed(), c.getGreen(), value, c.getAlpha()), colorform, inDegrees);
                    }
                }
            }
        });
        EditText externalAlphaInput = findViewById(R.id.externalAlphaInput);
        externalAlphaInput.setText(String.format(COLOR_FORMATTER, fractal.getExternalColor(colorform, inDegrees).getAlpha()));
        externalAlphaInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Float value = parseFloat(((EditText)findViewById(R.id.externalAlphaInput)).getText().toString());
                    if(value != null){
                        Color c = fractal.getExternalColor(colorform, inDegrees);
                        fractal.setExternalColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), value), colorform, inDegrees);
                    }
                }
            }
        });

        EditText peripheralRedInput = findViewById(R.id.peripheralRedInput);
        peripheralRedInput.setText(String.format(COLOR_FORMATTER, fractal.getPeripheralColor(colorform, inDegrees).getRed()));
        peripheralRedInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Float value = parseFloat(((EditText)findViewById(R.id.peripheralRedInput)).getText().toString());
                    if(value != null) {
                        Color c = fractal.getPeripheralColor(colorform, inDegrees);
                        fractal.setPeripheralColor(new Color(value, c.getGreen(), c.getBlue(), c.getAlpha()), colorform, inDegrees);
                    }
                }
            }
        });
        EditText peripheralGreenInput = findViewById(R.id.peripheralGreenInput);
        peripheralGreenInput.setText(String.format(COLOR_FORMATTER, fractal.getPeripheralColor(colorform, inDegrees).getGreen()));
        peripheralGreenInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Float value = parseFloat(((EditText)findViewById(R.id.peripheralGreenInput)).getText().toString());
                    if(value != null){
                        Color c = fractal.getPeripheralColor(colorform, inDegrees);
                        fractal.setPeripheralColor(new Color(c.getRed(), value, c.getBlue(), c.getAlpha()), colorform, inDegrees);
                    }
                }
            }
        });
        EditText peripheralBlueInput = findViewById(R.id.peripheralBlueInput);
        peripheralBlueInput.setText(String.format(COLOR_FORMATTER, fractal.getPeripheralColor(colorform, inDegrees).getBlue()));
        peripheralBlueInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Float value = parseFloat(((EditText)findViewById(R.id.peripheralBlueInput)).getText().toString());
                    if(value != null) {
                        Color c = fractal.getPeripheralColor(colorform, inDegrees);
                        fractal.setPeripheralColor(new Color(c.getRed(), c.getGreen(), value, c.getAlpha()), colorform, inDegrees);
                    }
                }
            }
        });
        EditText peripheralAlphaInput = findViewById(R.id.peripheralAlphaInput);
        peripheralAlphaInput.setText(String.format(COLOR_FORMATTER, fractal.getPeripheralColor(colorform, inDegrees).getAlpha()));
        peripheralAlphaInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Float value = parseFloat(((EditText)findViewById(R.id.peripheralAlphaInput)).getText().toString());
                    if(value != null){
                        Color c = fractal.getPeripheralColor(colorform, inDegrees);
                        fractal.setPeripheralColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), value), colorform, inDegrees);
                    }
                }
            }
        });

        EditText internalRedInput = findViewById(R.id.internalRedInput);
        internalRedInput.setText(String.format(COLOR_FORMATTER, fractal.getInternalColor(colorform, inDegrees).getRed()));
        internalRedInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Float value = parseFloat(((EditText)findViewById(R.id.internalRedInput)).getText().toString());
                    if(value != null) {
                        Color c = fractal.getInternalColor(colorform, inDegrees);
                        fractal.setInternalColor(new Color(value, c.getGreen(), c.getBlue(), c.getAlpha()), colorform, inDegrees);
                    }
                }
            }
        });
        EditText internalGreenInput = findViewById(R.id.internalGreenInput);
        internalGreenInput.setText(String.format(COLOR_FORMATTER, fractal.getInternalColor(colorform, inDegrees).getGreen()));
        internalGreenInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Float value = parseFloat(((EditText)findViewById(R.id.internalGreenInput)).getText().toString());
                    if(value != null) {
                        Color c = fractal.getInternalColor(colorform, inDegrees);
                        fractal.setInternalColor(new Color(c.getRed(), value, c.getBlue(), c.getAlpha()), colorform, inDegrees);
                    }
                }
            }
        });
        EditText internalBlueInput = findViewById(R.id.internalBlueInput);
        internalBlueInput.setText(String.format(COLOR_FORMATTER, fractal.getInternalColor(colorform, inDegrees).getBlue()));
        internalBlueInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Float value = parseFloat(((EditText)findViewById(R.id.internalBlueInput)).getText().toString());
                    if(value != null) {
                        Color c = fractal.getInternalColor(colorform, inDegrees);
                        fractal.setInternalColor(new Color(c.getRed(), c.getGreen(), value, c.getAlpha()), colorform, inDegrees);
                    }
                }
            }
        });
        EditText internalAlphaInput = findViewById(R.id.internalAlphaInput);
        internalAlphaInput.setText(String.format(COLOR_FORMATTER, fractal.getInternalColor(colorform, inDegrees).getAlpha()));
        internalAlphaInput.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    Float value = parseFloat(((EditText)findViewById(R.id.internalAlphaInput)).getText().toString());
                    if(value != null) {
                        Color c = fractal.getInternalColor(colorform, inDegrees);
                        fractal.setInternalColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), value), colorform, inDegrees);
                    }
                }
            }
        });
    }
*/


    private void recalculateAngles(){
        ((EditText)findViewById(R.id.rotationInput)).setText(String.format(FLOAT_FORMATTER, fractal.getRotation(inDegrees)));
        if(isPolar){
            recalculateComplexNumbers();
        }
        /*if(colorform == ColorDefinitionMode.HSVA){
            recalculateColors();
        }*/
    }
    private void recalculateComplexNumbers(){
        Complex seed = fractal.getSeed(isPolar, inDegrees);
        ((EditText)findViewById(R.id.seedReInput)).setText(String.format(FLOAT_FORMATTER, seed.getReal()));
        ((EditText)findViewById(R.id.seedImInput)).setText(String.format(FLOAT_FORMATTER, seed.getImaginary()));

        Complex power = fractal.getPower(isPolar, inDegrees);
        ((EditText)findViewById(R.id.powerReInput)).setText(String.format(FLOAT_FORMATTER, power.getReal()));
        ((EditText)findViewById(R.id.powerImInput)).setText(String.format(FLOAT_FORMATTER, power.getImaginary()));

        Complex add = fractal.getAdd(isPolar, inDegrees);
        ((EditText)findViewById(R.id.addReInput)).setText(String.format(FLOAT_FORMATTER, add.getReal()));
        ((EditText)findViewById(R.id.addImInput)).setText(String.format(FLOAT_FORMATTER, add.getImaginary()));

        Complex mul = fractal.getMul(isPolar, inDegrees);
        ((EditText)findViewById(R.id.multiplyReInput)).setText(String.format(FLOAT_FORMATTER, mul.getReal()));
        ((EditText)findViewById(R.id.multiplyImInput)).setText(String.format(FLOAT_FORMATTER, mul.getImaginary()));
    }
    /*
    private void recalculateColors(){
        Color externalColor = fractal.getExternalColor(colorform, inDegrees);
        ((EditText)findViewById(R.id.externalRedInput)).setText(String.format(FLOAT_FORMATTER, externalColor.getRed()));
        ((EditText)findViewById(R.id.externalGreenInput)).setText(String.format(FLOAT_FORMATTER, externalColor.getGreen()));
        ((EditText)findViewById(R.id.externalBlueInput)).setText(String.format(FLOAT_FORMATTER, externalColor.getBlue()));
        ((EditText)findViewById(R.id.externalAlphaInput)).setText(String.format(FLOAT_FORMATTER, externalColor.getAlpha()));

        Color peripheralColor = fractal.getPeripheralColor(colorform, inDegrees);
        ((EditText)findViewById(R.id.peripheralRedInput)).setText(String.format(FLOAT_FORMATTER, peripheralColor.getRed()));
        ((EditText)findViewById(R.id.peripheralGreenInput)).setText(String.format(FLOAT_FORMATTER, peripheralColor.getGreen()));
        ((EditText)findViewById(R.id.peripheralBlueInput)).setText(String.format(FLOAT_FORMATTER, peripheralColor.getBlue()));
        ((EditText)findViewById(R.id.peripheralAlphaInput)).setText(String.format(FLOAT_FORMATTER, peripheralColor.getAlpha()));

        Color internalColor = fractal.getInternalColor(colorform, inDegrees);
        ((EditText)findViewById(R.id.internalRedInput)).setText(String.format("%s", internalColor.getRed()));
        ((EditText)findViewById(R.id.internalGreenInput)).setText(String.format("%s", internalColor.getGreen()));
        ((EditText)findViewById(R.id.internalBlueInput)).setText(String.format("%s", internalColor.getBlue()));
        ((EditText)findViewById(R.id.internalAlphaInput)).setText(String.format("%s", internalColor.getAlpha()));
    }
*/


    public void createToast(CharSequence msg) {
        Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onApplyButton(View view){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        bundle.putSerializable("MandelbrotFractal", fractal.getSelf());
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
    public void onCancelButton(View view){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
