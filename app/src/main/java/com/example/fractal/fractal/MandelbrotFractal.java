package com.example.fractal.fractal;

import android.opengl.GLES20;

import com.example.fractal.utils.Color;
import com.example.fractal.utils.ColorDefinitionMode;
import com.example.fractal.utils.ColoringAlgorithm;
import com.example.fractal.utils.Complex;
import com.example.fractal.utils.FractalAlgorithm;
import com.example.fractal.utils.FractalType;
import com.example.fractal.utils.SweepAlgorithm;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MandelbrotFractal extends Fractal {
    private static final float PI2 = (float)Math.PI * 2f;
    private static final int CONST_MAX_ITER = 64;

    private float zoom = 8;                                         // (0, +inf)
    private float rotation = 0;                                     // [0, PI2) radians
    private float[] pan = new float[]{-0.5f, 0};

    private float clamp = 25;
    private float radius = 25;
    private float symmetry = 0;
    private float bailout = 2;
    private int minimumIteration = 32;
    private int maximumIteration = 32;

    //private SweepAlgorithm sweepAlgorithm = SweepAlgorithm.DEFAULT;
    private FractalAlgorithm prefixAlgorithm = FractalAlgorithm.DEFAULT;
    private FractalAlgorithm infixAlgorithm = FractalAlgorithm.DEFAULT;
    private FractalAlgorithm fractalAlgorithm = FractalAlgorithm.DEFAULT;
    private boolean isJulia = false;
    private boolean isInverted = false;
    private Complex seed = new Complex(0, 0);
    private Complex power = new Complex(2, 0);
    private Complex add = new Complex(0, 0);
    private Complex mul = new Complex(1, 0);

    /*private ColoringAlgorithm coloringAlgorithm = ColoringAlgorithm.COLORFUL;
    private Color externalColor = Color.YELLOW;
    private Color peripheralColor = Color.CYAN;
    private Color internalColor = Color.MAGENTA;*/




    public MandelbrotFractal(){}
    public MandelbrotFractal(
        float zoom, float rotation, float panX, float panY,
        Complex seed, Complex power, Complex add, Complex mul,
        float clamp, float radius, float symmetry, float bailout,
        /*Color externalColor, Color peripheralColor, Color internalColor,*/
        int minimumIteration, int maximumIteration,
        boolean isJulia, boolean isInverted,
        /*SweepAlgorithm sweepAlgorithm, */FractalAlgorithm prefixAlgorithm, FractalAlgorithm infixAlgorithm, FractalAlgorithm fractalAlgorithm,
        /*ColoringAlgorithm coloringAlgorithm,*/
        boolean inPolarForm, boolean inDegrees/*, ColorDefinitionMode coldefmode*/
    ){
        setZoom(zoom);
        setRotation(rotation, inDegrees);
        setPan(panX, panY);
        setSeed(seed, inPolarForm, inDegrees);
        setPower(power, inPolarForm, inDegrees);
        setAdd(add, inPolarForm, inDegrees);
        setMul(mul, inPolarForm, inDegrees);
        setClamp(clamp);
        setRadius(radius);
        setSymmetry(symmetry);
        setBailout(bailout);
        /*setExternalColor(externalColor, coldefmode, inDegrees);
        setPeripheralColor(peripheralColor, coldefmode, inDegrees);
        setInternalColor(internalColor, coldefmode, inDegrees);*/
        setMaximumIteration(maximumIteration);
        setMinimumIteration(minimumIteration);
        setJulia(isJulia);
        setInverted(isInverted);
        //setSweepAlgorithm(sweepAlgorithm);
        setPrefixAlgorithm(prefixAlgorithm);
        setInfixAlgorithm(infixAlgorithm);
        setFractalAlgorithm(fractalAlgorithm);
        //setColoringAlgorithm(coloringAlgorithm);
    }

    @Override
    public MandelbrotFractal getSelf(){
        return new MandelbrotFractal(
            zoom, rotation, pan[0], pan[1],
            seed.getSelf(), power.getSelf(), add.getSelf(), mul.getSelf(),
            clamp, radius, symmetry, bailout,
            /*externalColor.getSelf(), peripheralColor.getSelf(), internalColor.getSelf(),*/
            minimumIteration, maximumIteration,
            isJulia, isInverted,
            /*sweepAlgorithm, */prefixAlgorithm, infixAlgorithm, fractalAlgorithm,/* coloringAlgorithm,*/
            false, false/*, ColorDefinitionMode.DEFAULT*/
        );
    }

    @Override
    protected void initName() {
        setName("Default Mandelbrot");
    }


    @Override
    public void bindUniforms(int mProgram, int width, int height) {
        float EPSILON = 0.0000001f;

        float vZoomer = (1f / zoom) * 20;
        float mRotation = rotation;

        float mClamp = clamp;
        float mRadius = radius;
        float mBailout = bailout * bailout;

        int[] mIteration = new int[]{Math.abs(minimumIteration), Math.abs(maximumIteration)};
        if(mIteration[1] < mIteration[0]){
            mIteration[0] = mIteration[1];
        }
        if(mIteration[0] == 0){
            mIteration = new int[]{1, 1};
        }

        int vMaxIter = mIteration[1];
        float[] vRealIter = new float[]{mIteration[0], mIteration[1]};

        Complex vZ = seed.getSelf();
        Complex vP = power.getSelf();
        if(vP.getImaginary() < EPSILON){
            vP.setImaginary(EPSILON);
        }

        float vRad = (float)Math.pow(mClamp + 1, mRadius) - 1;
        float vDe = (float)Math.exp(-vZ.getAbsolute());

        float vDim = Math.min(width, height);
        Complex vOffset = new Complex(0.5f, 0.5f);
        float ratio = (float)height / (float)width;
        if(vDim == width){
            vOffset.setImaginary(vOffset.getImaginary() * ratio);
        }
        else{
            vOffset.setReal(vOffset.getReal() / ratio);
        }



        GLES20.glUniform2fv(GLES20.glGetUniformLocation(mProgram, "vRealIter"), 1, vRealIter, 0);
        GLES20.glUniform2fv(GLES20.glGetUniformLocation(mProgram, "vZ"), 1, vZ.getFloatArray(), 0);
        GLES20.glUniform2fv(GLES20.glGetUniformLocation(mProgram, "vP"), 1, vP.getFloatArray(), 0);
        GLES20.glUniform2fv(GLES20.glGetUniformLocation(mProgram, "vPan"), 1, pan, 0);
        GLES20.glUniform2fv(GLES20.glGetUniformLocation(mProgram, "vAdd"), 1, add.getFloatArray(), 0);
        GLES20.glUniform2fv(GLES20.glGetUniformLocation(mProgram, "vMul"), 1, mul.getFloatArray(), 0);
        GLES20.glUniform2fv(GLES20.glGetUniformLocation(mProgram, "vOffset"), 1, vOffset.getFloatArray(), 0);

        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "vZoomer"), vZoomer);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "vSymmetry"), symmetry);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "vRotation"), mRotation);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "vBailout"), mBailout);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "vRad"), vRad);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "vDe"), vDe);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "vDim"), vDim);

        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "vMaxIter"), vMaxIter);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "vIsInverted"), isInverted ? 1 : 0);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "vIsJulia"), isJulia ? 1 : 0);

        //GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "vSweepAlgorithm"), sweepAlgorithm.getId());
        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "vPrefixAlgorithm"), prefixAlgorithm.getId());
        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "vInfixAlgorithm"), infixAlgorithm.getId());
        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "vFractalAlgorithm"), fractalAlgorithm.getId());
        /*GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram, "vColoringAlgorithm"), coloringAlgorithm.getId());

        GLES20.glUniform4fv(GLES20.glGetUniformLocation(mProgram, "vExternalColor"), 1, externalColor.getFloatArray(), 0);
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(mProgram, "vPeripheralColor"), 1, peripheralColor.getFloatArray(), 0);
        GLES20.glUniform4fv(GLES20.glGetUniformLocation(mProgram, "vInternalColor"), 1, internalColor.getFloatArray(), 0);*/
    }
    @Override
    public FractalType getType(){
        return FractalType.MANDELBROT;
    }


    /* G E T T E R S */
    public float getZoom() {
        return zoom;
    }
    public float getRotation(boolean inDegrees) {
        if(inDegrees){
            return rotation / PI2 * 360f;
        }
        else{
            return rotation;
        }
    }
    public float[] getPan() {
        return pan;
    }
    public Complex getSeed(boolean isPolar, boolean inDegrees) {
        if(isPolar){
            if(inDegrees){
                return Complex.PolarToDegrees(Complex.CartesianToPolar(seed));
            }
            else{
                return Complex.CartesianToPolar(seed);
            }
        }
        return seed;
    }
    public Complex getPower(boolean isPolar, boolean inDegrees) {
        if(isPolar){
            if(inDegrees){
                return Complex.PolarToDegrees(Complex.CartesianToPolar(power));
            }
            else{
                return Complex.CartesianToPolar(power);
            }
        }
        return power;
    }
    public Complex getAdd(boolean isPolar, boolean inDegrees) {
        if(isPolar){
            if(inDegrees){
                return Complex.PolarToDegrees(Complex.CartesianToPolar(add));
            }
            else{
                return Complex.CartesianToPolar(add);
            }
        }
        return add;
    }
    public Complex getMul(boolean isPolar, boolean inDegrees) {
        if(isPolar){
            if(inDegrees){
                return Complex.PolarToDegrees(Complex.CartesianToPolar(mul));
            }
            else{
                return Complex.CartesianToPolar(mul);
            }
        }
        return mul;
    }
    public float getClamp() {
        return clamp;
    }
    public float getRadius() {
        return radius;
    }
    public float getSymmetry() {
        return symmetry;
    }
    public float getBailout() {
        return bailout;
    }
    /*public Color getExternalColor(ColorDefinitionMode mode, boolean inDegrees) {
        switch(mode){
            case HSVA: return Color.toHSVA(externalColor, inDegrees);
            case RGBA: return Color.toRGBA(externalColor);
            case DEFAULT: default: return externalColor;
        }
    }
    public Color getPeripheralColor(ColorDefinitionMode mode, boolean inDegrees) {
        switch(mode){
            case HSVA: return Color.toHSVA(peripheralColor, inDegrees);
            case RGBA: return Color.toRGBA(peripheralColor);
            case DEFAULT: default: return peripheralColor;
        }
    }
    public Color getInternalColor(ColorDefinitionMode mode, boolean inDegrees) {
        switch(mode){
            case HSVA: return Color.toHSVA(internalColor, inDegrees);
            case RGBA: return Color.toRGBA(internalColor);
            case DEFAULT: default: return internalColor;
        }
    }*/
    public int getMinimumIteration() {
        return minimumIteration;
    }
    public int getMaximumIteration() {
        return maximumIteration;
    }
    public boolean isJulia() {
        return isJulia;
    }
    public boolean isInverted() {
        return isInverted;
    }
    /*public SweepAlgorithm getSweepAlgorithm() {
        return sweepAlgorithm;
    }*/
    public FractalAlgorithm getPrefixAlgorithm() {
        return prefixAlgorithm;
    }
    public FractalAlgorithm getInfixAlgorithm() {
        return infixAlgorithm;
    }
    public FractalAlgorithm getFractalAlgorithm() {
        return fractalAlgorithm;
    }
    /*public ColoringAlgorithm getColoringAlgorithm() {
        return coloringAlgorithm;
    }*/



    private static float correctAngle(float angle, boolean inDegrees){
        if(inDegrees){
            angle %= 360f;
            if(angle < 0){
                angle += 360f;
            }
            // convert degrees to radians
            angle /=  360f;
            angle *= PI2;
        }
        else{
            angle %= PI2;
            if(angle < 0){
                angle += PI2;
            }
        }
        return angle;
    }

    /* S E T T E R S */
    public boolean setZoom(float zoom) {
        boolean correct = true;
        if(Float.isNaN(zoom) || zoom == 0){
            zoom = 1;
            correct = false;
        }
        if(zoom < 0){
            zoom = -zoom;
            correct = false;
        }
        this.zoom = zoom;
        return correct;
    }
    public boolean setRotation(float rotation, boolean inDegrees) {
        boolean correct = true;
        if(Float.isNaN(rotation)){
            rotation = 0;
            correct = false;
        }
        this.rotation = correctAngle(rotation, inDegrees);
        return correct;
    }
    public boolean setPan(float panX, float panY) {
        boolean correct = true;
        if(Float.isNaN(panX)){
            panX = 0;
            correct = false;
        }
        if(Float.isNaN(panY)){
            panY = 0;
            correct = false;
        }
        this.pan[0] = panX;
        this.pan[1] = panY;
        return correct;
    }
    public boolean setSeed(Complex seed, boolean isPolar, boolean inDegrees) {
        if(isPolar){
            seed.setImaginary(correctAngle(seed.getImaginary(), inDegrees));
            seed = Complex.PolarToCartesian(seed);
        }
        if(Math.abs(this.seed.getReal() - seed.getReal()) > 0.00001f || Math.abs(this.seed.getImaginary() - seed.getImaginary()) > 0.00001f){
            this.seed = seed;
            return false;
        }
        return true;
    }
    public boolean setPower(Complex power, boolean isPolar, boolean inDegrees) {
        if(isPolar){
            power.setImaginary(correctAngle(power.getImaginary(), inDegrees));
            power = Complex.PolarToCartesian(power);
        }
        if(Math.abs(this.power.getReal() - power.getReal()) > 0.00001f || Math.abs(this.power.getImaginary() - power.getImaginary()) > 0.00001f){
            this.power = power;
            return false;
        }
        return true;
    }
    public boolean setAdd(Complex add, boolean isPolar, boolean inDegrees) {
        if(isPolar){
            add.setImaginary(correctAngle(add.getImaginary(), inDegrees));
            add = Complex.PolarToCartesian(add);
        }
        if(Math.abs(this.add.getReal() - add.getReal()) > 0.00001f || Math.abs(this.add.getImaginary() - add.getImaginary()) > 0.00001f){
            this.add = add;
            return false;
        }
        return true;
    }
    public boolean setMul(Complex mul, boolean isPolar, boolean inDegrees) {
        if(isPolar){
            mul.setImaginary(correctAngle(mul.getImaginary(), inDegrees));
            mul = Complex.PolarToCartesian(mul);
        }
        if(Math.abs(this.mul.getReal() - mul.getReal()) > 0.00001f || Math.abs(this.mul.getImaginary() - mul.getImaginary()) > 0.00001f){
            this.mul = mul;
            return false;
        }
        return true;
    }
    public boolean setClamp(float clamp) {
        boolean correct = true;
        if(Float.isNaN(clamp) || clamp == 0){
            clamp = 1;
            correct = false;
        }
        if(clamp < 0){
            clamp = -clamp;
            correct = false;
        }
        this.clamp = clamp;
        return correct;
    }
    public boolean setRadius(float radius) {
        boolean correct = true;
        if(Float.isNaN(radius) || radius == 0){
            radius = 1;
            correct = false;
        }
        if(radius < 0){
            radius = -radius;
            correct = false;
        }
        this.radius = radius;
        return correct;
    }
    public boolean setSymmetry(float symmetry) {
        if(Float.isNaN(symmetry)){
            this.symmetry = 0;
            return false;
        }
        else{
            this.symmetry = symmetry;
            return true;
        }
    }
    public boolean setBailout(float bailout) {
        if(Float.isNaN(bailout)){
            this.bailout = 2;
            return false;
        }
        else{
            this.bailout = bailout;
            return true;
        }
    }
    /*public void setExternalColor(Color externalColor, ColorDefinitionMode mode, boolean inDegrees) {
        switch(mode){
            case RGBA:
                this.externalColor = Color.fromRGBA(externalColor);
                break;
            case HSVA:
                this.externalColor = Color.fromHSVA(externalColor, inDegrees);
                break;
            case DEFAULT: default:
                this.externalColor = externalColor;
                break;
        }
    }
    public void setPeripheralColor(Color peripheralColor, ColorDefinitionMode mode, boolean inDegrees) {
        switch(mode){
            case RGBA:
                this.peripheralColor = Color.fromRGBA(peripheralColor);
                break;
            case HSVA:
                this.peripheralColor = Color.fromHSVA(peripheralColor, inDegrees);
                break;
            case DEFAULT: default:
                this.peripheralColor = peripheralColor;
                break;
        }
    }
    public void setInternalColor(Color internalColor, ColorDefinitionMode mode, boolean inDegrees) {
        switch(mode){
            case RGBA:
                this.internalColor = Color.fromRGBA(internalColor);
                break;
            case HSVA:
                this.internalColor = Color.fromHSVA(internalColor, inDegrees);
                break;
            case DEFAULT: default:
                this.internalColor = internalColor;
                break;
        }
    }*/
    public boolean setMinimumIteration(int minimumIteration) {
        boolean correct = true;
        if(minimumIteration > CONST_MAX_ITER){
            minimumIteration = CONST_MAX_ITER;
            correct = false;
        }
        if(minimumIteration > this.maximumIteration){
            minimumIteration = this.maximumIteration;
            correct = false;
        }
        if(minimumIteration <= 0){
            minimumIteration = 1;
            correct = false;
        }
        this.minimumIteration = minimumIteration;
        return correct;
    }
    public boolean setMaximumIteration(int maximumIteration) {
        boolean correct = true;
        if (maximumIteration < this.minimumIteration){
            maximumIteration = this.minimumIteration;
            correct = false;
        }
        if(maximumIteration > CONST_MAX_ITER){
            maximumIteration = CONST_MAX_ITER;
            correct = false;
        }
        this.maximumIteration = maximumIteration;
        return correct;
    }
    public void setJulia(boolean julia) {
        isJulia = julia;
    }
    public void setInverted(boolean inverted) {
        isInverted = inverted;
    }
    /*public void setSweepAlgorithm(SweepAlgorithm sweepAlgorithm) {
        this.sweepAlgorithm = sweepAlgorithm;
    }*/
    public void setPrefixAlgorithm(FractalAlgorithm prefixAlgorithm) {
        this.prefixAlgorithm = prefixAlgorithm;
    }
    public void setInfixAlgorithm(FractalAlgorithm infixAlgorithm) {
        this.infixAlgorithm = infixAlgorithm;
    }
    public void setFractalAlgorithm(FractalAlgorithm fractalAlgorithm) {
        this.fractalAlgorithm = fractalAlgorithm;
    }
    /*public void setColoringAlgorithm(ColoringAlgorithm coloringAlgorithm) {
        this.coloringAlgorithm = coloringAlgorithm;
    }*/
	
	
	
	public void intoXML(Document doc, Element root){
		Element navigationRoot = doc.createElement("navigation");
		
		Element zoomElement = doc.createElement("zoom");
		Attr zoomValue = doc.createAttribute("value");
		zoomValue.setValue("" + this.zoom);
		zoomElement.setAttributeNode(zoomValue);
		navigationRoot.appendChild(zoomElement);
		
		Element rotationElement = doc.createElement("rotation");
		Attr rotationValue = doc.createAttribute("value");
		rotationValue.setValue("" + this.rotation);
		rotationElement.setAttributeNode(rotationValue);
		navigationRoot.appendChild(rotationElement);
		
		Element panElement = doc.createElement("pan");
		Attr panX = doc.createAttribute("x");
		panX.setValue("" + this.pan[0]);
		panElement.setAttributeNode(panX);
		Attr panY = doc.createAttribute("y");
		panY.setValue("" + this.pan[1]);
		panElement.setAttributeNode(panY);
		navigationRoot.appendChild(panElement);
		
		root.appendChild(navigationRoot);
		
		
		
		Element aestheticsRoot = doc.createElement("aesthetics");
		
		Element clampElement = doc.createElement("clamp");
		Attr clampValue = doc.createAttribute("value");
		clampValue.setValue("" + this.clamp);
		clampElement.setAttributeNode(clampValue);
		aestheticsRoot.appendChild(clampElement);
		
		Element radiusElement = doc.createElement("radius");
		Attr radiusValue = doc.createAttribute("value");
		radiusValue.setValue("" + this.radius);
		radiusElement.setAttributeNode(radiusValue);
		aestheticsRoot.appendChild(radiusElement);
		
		Element symmetryElement = doc.createElement("symmetry");
		Attr symmetryValue = doc.createAttribute("value");
		symmetryValue.setValue("" + this.symmetry);
		symmetryElement.setAttributeNode(symmetryValue);
		aestheticsRoot.appendChild(symmetryElement);
		
		Element bailoutElement = doc.createElement("bailout");
		Attr bailoutValue = doc.createAttribute("value");
		bailoutValue.setValue("" + this.bailout);
		bailoutElement.setAttributeNode(bailoutValue);
		aestheticsRoot.appendChild(bailoutElement);
		
		Element iterationElement = doc.createElement("iteration");
		Attr iterationMin = doc.createAttribute("min");
		iterationMin.setValue("" + this.minimumIteration);
		iterationElement.setAttributeNode(iterationMin);
		Attr iterationMax = doc.createAttribute("max");
		iterationMax.setValue("" + this.maximumIteration);
		iterationElement.setAttributeNode(iterationMax);
        aestheticsRoot.appendChild(iterationElement);
		
		root.appendChild(aestheticsRoot);
		
		
		
		Element parametersRoot = doc.createElement("parameters");
		
		/*Element sweepElement = doc.createElement("sweep");
		Attr sweepValue = doc.createAttribute("mode");
		sweepValue.setValue(this.sweepAlgorithm.getName());
		sweepElement.setAttributeNode(sweepValue);
        parametersRoot.appendChild(sweepElement);*/
		
		Element prefixElement = doc.createElement("prefix");
		Attr prefixValue = doc.createAttribute("mode");
		prefixValue.setValue(this.prefixAlgorithm.getName());
		prefixElement.setAttributeNode(prefixValue);
        parametersRoot.appendChild(prefixElement);
		
		Element infixElement = doc.createElement("infix");
		Attr infixValue = doc.createAttribute("mode");
		infixValue.setValue(this.infixAlgorithm.getName());
		infixElement.setAttributeNode(infixValue);
        parametersRoot.appendChild(infixElement);
		
		Element algorithmElement = doc.createElement("algorithm");
		Attr algorithmValue = doc.createAttribute("mode");
		algorithmValue.setValue(this.fractalAlgorithm.getName());
		algorithmElement.setAttributeNode(algorithmValue);
        parametersRoot.appendChild(algorithmElement);
		
		Element juliaElement = doc.createElement("julia");
		Attr juliaChecked = doc.createAttribute("checked");
		juliaChecked.setValue(this.isJulia ? "1" : "0");
		juliaElement.setAttributeNode(juliaChecked);
        parametersRoot.appendChild(juliaElement);
		
		Element invertedElement = doc.createElement("inverted");
		Attr invertedChecked = doc.createAttribute("checked");
		invertedChecked.setValue(this.isInverted ? "1" : "0");
		invertedElement.setAttributeNode(invertedChecked);
        parametersRoot.appendChild(invertedElement);
		
		Element seedElement = doc.createElement("seed");
		Attr seedR = doc.createAttribute("r");
		seedR.setValue("" + this.seed.getReal());
		seedElement.setAttributeNode(seedR);
		Attr seedI = doc.createAttribute("i");
		seedI.setValue("" + this.seed.getImaginary());
		seedElement.setAttributeNode(seedI);
        parametersRoot.appendChild(seedElement);
		
		Element powerElement = doc.createElement("power");
		Attr powerR = doc.createAttribute("r");
		powerR.setValue("" + this.power.getReal());
		powerElement.setAttributeNode(powerR);
		Attr powerI = doc.createAttribute("i");
		powerI.setValue("" + this.power.getImaginary());
		powerElement.setAttributeNode(powerI);
        parametersRoot.appendChild(powerElement);
		
		Element addElement = doc.createElement("add");
		Attr addR = doc.createAttribute("r");
		addR.setValue("" + this.add.getReal());
		addElement.setAttributeNode(addR);
		Attr addI = doc.createAttribute("i");
		addI.setValue("" + this.add.getImaginary());
		addElement.setAttributeNode(addI);
        parametersRoot.appendChild(addElement);
		
		Element mulElement = doc.createElement("mul");
		Attr mulR = doc.createAttribute("r");
		mulR.setValue("" + this.mul.getReal());
		mulElement.setAttributeNode(mulR);
		Attr mulI = doc.createAttribute("i");
		mulI.setValue("" + this.mul.getImaginary());
		mulElement.setAttributeNode(mulI);
        parametersRoot.appendChild(mulElement);
		
		root.appendChild(parametersRoot);
		
		
		
		/*Element coloringRoot = doc.createElement("coloring");
		
		Element colormodeElement = doc.createElement("colormode");
		Attr colormodeValue = doc.createAttribute("mode");
		colormodeValue.setValue(this.coloringAlgorithm.getName());
		colormodeElement.setAttributeNode(colormodeValue);
        coloringRoot.appendChild(colormodeElement);
		
		Element externalElement = doc.createElement("external");
		Attr externalR = doc.createAttribute("r");
		externalR.setValue("" + this.externalColor.getRed());
		externalElement.setAttributeNode(externalR);
		Attr externalG = doc.createAttribute("g");
		externalG.setValue("" + this.externalColor.getGreen());
		externalElement.setAttributeNode(externalG);
		Attr externalB = doc.createAttribute("b");
		externalB.setValue("" + this.externalColor.getBlue());
		externalElement.setAttributeNode(externalB);
		Attr externalA = doc.createAttribute("a");
		externalA.setValue("" + this.externalColor.getAlpha());
		externalElement.setAttributeNode(externalA);
        coloringRoot.appendChild(externalElement);
		
		Element peripheralElement = doc.createElement("peripheral");
		Attr peripheralR = doc.createAttribute("r");
		peripheralR.setValue("" + this.peripheralColor.getRed());
		peripheralElement.setAttributeNode(peripheralR);
		Attr peripheralG = doc.createAttribute("g");
		peripheralG.setValue("" + this.peripheralColor.getGreen());
		peripheralElement.setAttributeNode(peripheralG);
		Attr peripheralB = doc.createAttribute("b");
		peripheralB.setValue("" + this.peripheralColor.getBlue());
		peripheralElement.setAttributeNode(peripheralB);
		Attr peripheralA = doc.createAttribute("a");
		peripheralA.setValue("" + this.peripheralColor.getAlpha());
		peripheralElement.setAttributeNode(peripheralA);
        coloringRoot.appendChild(peripheralElement);
		
		Element internalElement = doc.createElement("internal");
		Attr internalR = doc.createAttribute("r");
		internalR.setValue("" + this.internalColor.getRed());
		internalElement.setAttributeNode(internalR);
		Attr internalG = doc.createAttribute("g");
		internalG.setValue("" + this.internalColor.getGreen());
		internalElement.setAttributeNode(internalG);
		Attr internalB = doc.createAttribute("b");
		internalB.setValue("" + this.internalColor.getBlue());
		internalElement.setAttributeNode(internalB);
		Attr internalA = doc.createAttribute("a");
		internalA.setValue("" + this.internalColor.getAlpha());
		internalElement.setAttributeNode(internalA);
        coloringRoot.appendChild(internalElement);
		
		root.appendChild(coloringRoot);*/
	}
	public static MandelbrotFractal fromXML(Element node){
		MandelbrotFractal result = new MandelbrotFractal();

		result.epoch = Long.parseLong(node.getAttributes().getNamedItem("epoch").getNodeValue());

        Element navigationElement = (Element)node.getElementsByTagName("navigation").item(0);
		result.setZoom(Float.parseFloat(navigationElement.getElementsByTagName("zoom").item(0).getAttributes().getNamedItem("value").getNodeValue()));
        result.setRotation(Float.parseFloat(navigationElement.getElementsByTagName("rotation").item(0).getAttributes().getNamedItem("value").getNodeValue()), false);
        result.setPan(
			Float.parseFloat(navigationElement.getElementsByTagName("pan").item(0).getAttributes().getNamedItem("x").getNodeValue()),
			Float.parseFloat(navigationElement.getElementsByTagName("pan").item(0).getAttributes().getNamedItem("y").getNodeValue())
		);

		Element aestheticElement = (Element)node.getElementsByTagName("aesthetics").item(0);
        result.setClamp(Float.parseFloat(aestheticElement.getElementsByTagName("clamp").item(0).getAttributes().getNamedItem("value").getNodeValue()));
        result.setRadius(Float.parseFloat(aestheticElement.getElementsByTagName("radius").item(0).getAttributes().getNamedItem("value").getNodeValue()));
        result.setSymmetry(Float.parseFloat(aestheticElement.getElementsByTagName("symmetry").item(0).getAttributes().getNamedItem("value").getNodeValue()));
        result.setBailout(Float.parseFloat(aestheticElement.getElementsByTagName("bailout").item(0).getAttributes().getNamedItem("value").getNodeValue()));
        result.setMaximumIteration(Integer.parseInt(aestheticElement.getElementsByTagName("iteration").item(0).getAttributes().getNamedItem("max").getNodeValue()));
        result.setMinimumIteration(Integer.parseInt(aestheticElement.getElementsByTagName("iteration").item(0).getAttributes().getNamedItem("min").getNodeValue()));

		Element parametersElement = (Element)node.getElementsByTagName("parameters").item(0);
        //result.setSweepAlgorithm(SweepAlgorithm.getByName(parametersElement.getElementsByTagName("sweep").item(0).getAttributes().getNamedItem("mode").getNodeValue()));
        result.setPrefixAlgorithm(FractalAlgorithm.getByName(parametersElement.getElementsByTagName("prefix").item(0).getAttributes().getNamedItem("mode").getNodeValue()));
        result.setInfixAlgorithm(FractalAlgorithm.getByName(parametersElement.getElementsByTagName("infix").item(0).getAttributes().getNamedItem("mode").getNodeValue()));
        result.setFractalAlgorithm(FractalAlgorithm.getByName(parametersElement.getElementsByTagName("algorithm").item(0).getAttributes().getNamedItem("mode").getNodeValue()));
        result.setJulia(parametersElement.getElementsByTagName("julia").item(0).getAttributes().getNamedItem("checked").getNodeValue().equals("1"));
        result.setInverted(parametersElement.getElementsByTagName("inverted").item(0).getAttributes().getNamedItem("checked").getNodeValue().equals("1"));
        result.setSeed(new Complex(
			Float.parseFloat(parametersElement.getElementsByTagName("seed").item(0).getAttributes().getNamedItem("r").getNodeValue()),
			Float.parseFloat(parametersElement.getElementsByTagName("seed").item(0).getAttributes().getNamedItem("i").getNodeValue())
		), false, false);
        result.setPower(new Complex(
			Float.parseFloat(parametersElement.getElementsByTagName("power").item(0).getAttributes().getNamedItem("r").getNodeValue()),
			Float.parseFloat(parametersElement.getElementsByTagName("power").item(0).getAttributes().getNamedItem("i").getNodeValue())
		), false, false);
        result.setAdd(new Complex(
			Float.parseFloat(parametersElement.getElementsByTagName("add").item(0).getAttributes().getNamedItem("r").getNodeValue()),
			Float.parseFloat(parametersElement.getElementsByTagName("add").item(0).getAttributes().getNamedItem("i").getNodeValue())
		), false, false);
        result.setMul(new Complex(
			Float.parseFloat(parametersElement.getElementsByTagName("mul").item(0).getAttributes().getNamedItem("r").getNodeValue()),
			Float.parseFloat(parametersElement.getElementsByTagName("mul").item(0).getAttributes().getNamedItem("i").getNodeValue())
		), false, false);

		/*Element coloringElement = (Element)node.getElementsByTagName("coloring").item(0);
        result.setColoringAlgorithm(ColoringAlgorithm.getByName(coloringElement.getElementsByTagName("colormode").item(0).getAttributes().getNamedItem("mode").getNodeValue()));
        result.setExternalColor(new Color(
			Float.parseFloat(coloringElement.getElementsByTagName("external").item(0).getAttributes().getNamedItem("r").getNodeValue()),
			Float.parseFloat(coloringElement.getElementsByTagName("external").item(0).getAttributes().getNamedItem("g").getNodeValue()),
			Float.parseFloat(coloringElement.getElementsByTagName("external").item(0).getAttributes().getNamedItem("b").getNodeValue()),
			Float.parseFloat(coloringElement.getElementsByTagName("external").item(0).getAttributes().getNamedItem("a").getNodeValue())
		), ColorDefinitionMode.DEFAULT, false);
        result.setPeripheralColor(new Color(
			Float.parseFloat(coloringElement.getElementsByTagName("peripheral").item(0).getAttributes().getNamedItem("r").getNodeValue()),
			Float.parseFloat(coloringElement.getElementsByTagName("peripheral").item(0).getAttributes().getNamedItem("g").getNodeValue()),
			Float.parseFloat(coloringElement.getElementsByTagName("peripheral").item(0).getAttributes().getNamedItem("b").getNodeValue()),
			Float.parseFloat(coloringElement.getElementsByTagName("peripheral").item(0).getAttributes().getNamedItem("a").getNodeValue())
		), ColorDefinitionMode.DEFAULT, false);
        result.setInternalColor(new Color(
			Float.parseFloat(coloringElement.getElementsByTagName("internal").item(0).getAttributes().getNamedItem("r").getNodeValue()),
			Float.parseFloat(coloringElement.getElementsByTagName("internal").item(0).getAttributes().getNamedItem("g").getNodeValue()),
			Float.parseFloat(coloringElement.getElementsByTagName("internal").item(0).getAttributes().getNamedItem("b").getNodeValue()),
			Float.parseFloat(coloringElement.getElementsByTagName("internal").item(0).getAttributes().getNamedItem("a").getNodeValue())
		), ColorDefinitionMode.DEFAULT, false);
        */
		return result;
	}


	public void print(){
        System.out.println("-------- START --------");
        System.out.println("MANDELBROT");
        System.out.println("\tNavigation");
        System.out.println("\t\tzoom = " + this.zoom);
        System.out.println("\t\trotation = " + this.rotation);
        System.out.println("\t\tpan = ( " + this.pan[0] + " , " + this.pan[1] + " )");
        System.out.println("\tAesthetics");
        System.out.println("\t\tclamp = " + this.clamp);
        System.out.println("\t\tradius = " + this.radius);
        System.out.println("\t\tsymmetry = " + this.symmetry);
        System.out.println("\t\tbailout = " + this.bailout);
        System.out.println("\t\titeration = [ " + this.minimumIteration + " , " + this.maximumIteration + " ]");
        System.out.println("\tParameters");
        //System.out.println("\t\tsweep_algorithm = " + this.sweepAlgorithm.getName());
        System.out.println("\t\tprefix_algorithm = " + this.prefixAlgorithm.getName());
        System.out.println("\t\tinfix_algorithm = " + this.infixAlgorithm.getName());
        System.out.println("\t\tfractal_algorithm = " + this.fractalAlgorithm.getName());
        System.out.println("\t\tis_julia = " + (this.isJulia ? "1" : "0"));
        System.out.println("\t\tis_inverted = " + (this.isInverted ? "1" : "0"));
        System.out.println("\t\tseed = ( " + this.seed.getReal() + " , " + this.seed.getImaginary() + "i )");
        System.out.println("\t\tpower = ( " + this.power.getReal() + " , " + this.power.getImaginary() + "i )");
        System.out.println("\t\tadd = ( " + this.add.getReal() + " , " + this.add.getImaginary() + "i )");
        System.out.println("\t\tmul = ( " + this.mul.getReal() + " , " + this.mul.getImaginary() + "i )");
        /*System.out.println("\tColoring");
        System.out.println("\t\tcolor_mode = " + this.coloringAlgorithm.getName());
        System.out.println("\t\texternal_color = { " + this.externalColor.getRed() + " , " + this.externalColor.getGreen() + " , " + this.externalColor.getBlue() + " , " + this.externalColor.getAlpha() + " }");
        System.out.println("\t\tperipheral_color = { " + this.peripheralColor.getRed() + " , " + this.peripheralColor.getGreen() + " , " + this.peripheralColor.getBlue() + " , " + this.peripheralColor.getAlpha() + " }");
        System.out.println("\t\tinternal_color = { " + this.internalColor.getRed() + " , " + this.internalColor.getGreen() + " , " + this.internalColor.getBlue() + " , " + this.internalColor.getAlpha() + " }");*/
        System.out.println("-------- END --------");
    }

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
