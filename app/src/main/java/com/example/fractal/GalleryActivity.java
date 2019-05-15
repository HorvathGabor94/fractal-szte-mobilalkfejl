package com.example.fractal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fractal.fractal.Fractal;
import com.example.fractal.fractal.FractalWrapper;
import com.example.fractal.fractal.MandelbrotFractal;
import com.example.fractal.ogl.OGL_View;
import com.example.fractal.utils.Complex;
import com.example.fractal.utils.FractalAlgorithm;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class GalleryActivity extends AppCompatActivity {
    private final ArrayList<Fractal> fractals = new ArrayList<>();

    private int current = 1;

    private LinearLayout galleryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        galleryLayout = findViewById(R.id.galleryLayout);

        initFractalDatabase();

        parseFractals();
    }

    private void initFractalDatabase(){
        fractals.clear();
        MandelbrotFractal mandelbrot = new MandelbrotFractal();
        mandelbrot.setName("Mandelbrot");
        fractals.add(mandelbrot);

        MandelbrotFractal glowing_nadir = new MandelbrotFractal(
            4, 0, 0, 0,		/* zoom, rot pan2 */
            new Complex(1, 180), new Complex(2, 0), new Complex(0, 0), new Complex(1.05f, 0),		/* seedC, powerC, addC, mulC */
            25, 25, 0, 10,	/* clamp, radius, symmetry, bailout */
            32, 32,			/* minIter, maxIter */
            true, true,		/* isJulia, isInverted */
            FractalAlgorithm.DEFAULT, FractalAlgorithm.DEFAULT, FractalAlgorithm.DEFAULT,			/* prefix, infix, main */
            true, true		/* inPolarForm, inDegrees */
        );
        glowing_nadir.setName("Glowing Nadir");
        fractals.add(glowing_nadir);
		
        MandelbrotFractal rainbow_helium = new MandelbrotFractal(
            4, 0, 0, 0,
            new Complex(0, 0), new Complex(1.2f, 0), new Complex(0, 0), new Complex(0, 0),
            25, 25, 0, 0,
            32, 32,
            false, true,
            FractalAlgorithm.DEFAULT, FractalAlgorithm.SIN, FractalAlgorithm.DEFAULT,
            true, true
        );
        rainbow_helium.setName("Rainbow Helium");
        fractals.add(rainbow_helium);
		
        MandelbrotFractal helium_split = new MandelbrotFractal(
            8, 0, 0, 0,
            new Complex(0, 0), new Complex(1.2f, 20), new Complex(0, 0), new Complex(0, 0),
            25, 25, 0, 0,
            64, 64,
            false, true,
            FractalAlgorithm.DEFAULT, FractalAlgorithm.SIN, FractalAlgorithm.DEFAULT,
            true, true
        );
        helium_split.setName("Helium Split");
        fractals.add(helium_split);
		
        MandelbrotFractal magnetic_energy = new MandelbrotFractal(
            8, 0, 0, 0,
            new Complex(0, 0), new Complex(1.5f, 0), new Complex(0, 0), new Complex(0, 0),
            25, 25, 0, 0,
            64, 64,
            false, true,
            FractalAlgorithm.DEFAULT, FractalAlgorithm.TAN, FractalAlgorithm.DEFAULT,
            true, true
        );
        magnetic_energy.setName("Magnetic Energy");
        fractals.add(magnetic_energy);
		
        MandelbrotFractal the_bridge = new MandelbrotFractal(
            2, 0, 0, 0,
            new Complex(0, 0), new Complex(2, 0), new Complex(0, 0), new Complex(1, 0),
            25, 25, 1, 0,
            64, 64,
            false, true,
            FractalAlgorithm.DEFAULT, FractalAlgorithm.TAN, FractalAlgorithm.DEFAULT,
            true, true
        );
        the_bridge.setName("The Bridge");
        fractals.add(the_bridge);
		
        MandelbrotFractal double_core = new MandelbrotFractal(
            25, 0, 0, 0,
            new Complex(0, 0), new Complex(2, 0), new Complex(0, 0), new Complex(1, 0),
            25, 25, 1, 0,
            64, 64,
            false, true,
            FractalAlgorithm.DEFAULT, FractalAlgorithm.TAN, FractalAlgorithm.DEFAULT,
            true, true
        );
        double_core.setName("Double Core");
        fractals.add(double_core);
		
        MandelbrotFractal wild_checkers = new MandelbrotFractal(
            8, 0, 0, 0,
            new Complex(0, 0), new Complex(6, 75), new Complex(0, 0), new Complex(1, 0),
            25, 25, 0, 0,
            32, 32,
            false, true,
            FractalAlgorithm.DEFAULT, FractalAlgorithm.CTG, FractalAlgorithm.DEFAULT,
            true, true
        );
        wild_checkers.setName("Wild Checkers");
        fractals.add(wild_checkers);
		
        MandelbrotFractal electron_void = new MandelbrotFractal(
            2, 0, 0, 0,
            new Complex(1, 0), new Complex(2, 0), new Complex(0.01f, 0), new Complex(0.1f, 0),
            25, 25, 0, 0,
            16, 16,
            true, true,
            FractalAlgorithm.DEFAULT, FractalAlgorithm.DEFAULT, FractalAlgorithm.DEFAULT,
            true, true
        );
        electron_void.setName("Electron Void");
        fractals.add(electron_void);
		
        MandelbrotFractal lightning_path = new MandelbrotFractal(
            4, 0, 0, 0,
            new Complex(-0.73f, -118.1f), new Complex(2, 0), new Complex(0, 0), new Complex(1, 0),
            25, 25, 0, 0,
            64, 64,
            true, false,
            FractalAlgorithm.DEFAULT, FractalAlgorithm.DEFAULT, FractalAlgorithm.DEFAULT,
            true, true
        );
        lightning_path.setName("Lightning Path");
        fractals.add(lightning_path);
		
        MandelbrotFractal supernatural_signal = new MandelbrotFractal(
            4, 0, 0, 0,
            new Complex(-1.50f, 0), new Complex(2, 0), new Complex(0, 0), new Complex(1, 0),
            25, 25, 0, 0,
            64, 64,
            true, false,
            FractalAlgorithm.DEFAULT, FractalAlgorithm.DEFAULT, FractalAlgorithm.DEFAULT,
            true, true
        );
        supernatural_signal.setName("Supernatural Signal");
        fractals.add(supernatural_signal);
		/*
        MandelbrotFractal dark_butterflies = new MandelbrotFractal(
            64, 0, 0, 0,
            new Complex(-2.65f, 0), new Complex(2, 0), new Complex(0, 0), new Complex(1, 0),
            25, 25, 0, 0,
            8, 8,
            true, false,
            FractalAlgorithm.DEFAULT, FractalAlgorithm.TANH, FractalAlgorithm.TAN,
            true, true
        );
        dark_butterflies.setName("Dark Butterflies");
        fractals.add(dark_butterflies);
        */
        saveFractals();
    }

    private void parseFractals(){
        //System.out.println("-------- PARSE FRACTALS START --------");
        try{
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(openFileInput("saved_fractals.xml"));
            doc.getDocumentElement().normalize();
            NodeList fractalNodes = doc.getElementsByTagName("fractal");
            for(int i=0; i<fractalNodes.getLength(); i++){
                Node fractalNode = fractalNodes.item(i);
                switch(fractalNode.getAttributes().getNamedItem("type").getNodeValue()){
                    case "mandelbrot":
                        MandelbrotFractal mandelbrot = MandelbrotFractal.fromXML((Element)fractalNode);
                        mandelbrot.setName(fractalNode.getAttributes().getNamedItem("name").getNodeValue());
                        fractals.add(mandelbrot);
                        //mandelbrot.print();

                        OGL_View oglv = new OGL_View(this, new FractalWrapper(mandelbrot));
                        oglv.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view) {
                                setOther(view);
                            }
                        });
                        galleryLayout.addView(oglv);

                        LinearLayout.LayoutParams mlp = new LinearLayout.LayoutParams(oglv.getLayoutParams());
                        mlp.width = 80;
                        mlp.height = 80;
                        mlp.setMargins(10, 10, 10, 10);
                        oglv.setLayoutParams(mlp);
                        oglv.setId(i + 1);

                        break;
                    default: break;
                }
            }
            setCurrent(current);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //System.out.println("-------- PARSE FRACTALS END --------");
    }
    private void saveFractals(){
        //System.out.println("-------- SAVE FRACTALS START --------");

        try{
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = doc.createElement("fractals");
            doc.appendChild(root);
            for(int i=0; i<fractals.size(); i++){
                Element fractalElement = doc.createElement("fractal");
                Fractal fractal = fractals.get(i);

                Attr fractalName = doc.createAttribute("name");
                fractalName.setValue(fractal.getName());
                fractalElement.setAttributeNode(fractalName);

                Attr fractalEpoch = doc.createAttribute("epoch");
                fractal.save();
                fractalEpoch.setValue("" + fractal.getEpoch());
                fractalElement.setAttributeNode(fractalEpoch);

                Attr fractalType = doc.createAttribute("type");
                fractalType.setValue(fractal.getType().getName());
                fractalElement.setAttributeNode(fractalType);

                fractal.intoXML(doc, fractalElement);

                root.appendChild(fractalElement);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(openFileOutput("saved_fractals.xml", MODE_PRIVATE));
            transformer.transform(source, result);

            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //System.out.println("-------- SAVE FRACTALS END --------");
    }

    private void setCurrent(int id){
        current = id;
        OGL_View oglv = findViewById(current);
        LinearLayout.LayoutParams mlp = new LinearLayout.LayoutParams(oglv.getLayoutParams());
        mlp.width = 100;
        mlp.height = 100;
        mlp.setMargins(10, 0, 10, 0);
        oglv.setLayoutParams(mlp);

        MandelbrotFractal fractal = (MandelbrotFractal)fractals.get(current-1);
        ((TextView)findViewById(R.id.nameView)).setText(fractal.getName());
        ((TextView)findViewById(R.id.epochView)).setText((new Date(fractal.getEpoch())).toString());

        ((TextView)findViewById(R.id.zoomView)).setText("zoom= " + fractal.getZoom());
        ((TextView)findViewById(R.id.rotationView)).setText("rot= " + fractal.getRotation(true));
        ((TextView)findViewById(R.id.panView)).setText("pan= (" + fractal.getPan()[0] + "," + fractal.getPan()[1] + ")");
        ((TextView)findViewById(R.id.minIterView)).setText("minIter= " + fractal.getMinimumIteration());
        ((TextView)findViewById(R.id.maxIterView)).setText("maxIter= " + fractal.getMaximumIteration());
        ((TextView)findViewById(R.id.juliaView)).setText(fractal.isJulia() ? "julia" : "normal");
        ((TextView)findViewById(R.id.invertedView)).setText(fractal.isInverted() ? "inverted" : "normal");
        ((TextView)findViewById(R.id.seedView)).setText("seed= (" + fractal.getSeed(false, true).getReal() + "," + fractal.getSeed(false, true).getImaginary() + ")");
        ((TextView)findViewById(R.id.powerView)).setText("power= (" + fractal.getPower(false, true).getReal() + "," + fractal.getPower(false, true).getImaginary() + ")");
        ((TextView)findViewById(R.id.addView)).setText("add= (" + fractal.getAdd(false, true).getReal() + "," + fractal.getAdd(false, true).getImaginary() + ")");
        ((TextView)findViewById(R.id.mulView)).setText("mul= (" + fractal.getMul(false, true).getReal() + "," + fractal.getMul(false, true).getImaginary() + ")");
        ((TextView)findViewById(R.id.clampView)).setText("clamp= " + fractal.getClamp());
        ((TextView)findViewById(R.id.radiusView)).setText("radius= " + fractal.getRadius());
        ((TextView)findViewById(R.id.symmetryView)).setText("symmetry= " + fractal.getSymmetry());
        ((TextView)findViewById(R.id.bailoutView)).setText("bailout= " + fractal.getBailout());
        ((TextView)findViewById(R.id.prefixAlgoView)).setText("prefix= " + fractal.getPrefixAlgorithm().getName());
        ((TextView)findViewById(R.id.infixAlgoView)).setText("infix= " + fractal.getInfixAlgorithm().getName());
        ((TextView)findViewById(R.id.algoView)).setText("algo= " + fractal.getFractalAlgorithm().getName());
    }
    public void setOther(View view){
        OGL_View oglv = findViewById(current);
        LinearLayout.LayoutParams mlp = new LinearLayout.LayoutParams(oglv.getLayoutParams());
        mlp.width = 80;
        mlp.height = 80;
        mlp.setMargins(10, 10, 10, 10);
        oglv.setLayoutParams(mlp);
        setCurrent(view.getId());
    }

    public void load(View view){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        bundle.putSerializable("MandelbrotFractal", fractals.get(current-1).getSelf());
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
    public void back(View view){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
    public void delete(View view){
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("Fractal Deletion");
        ab.setMessage("Are you sure you want to delete the fractal?");

        ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fractals.remove(current-1);
                galleryLayout.removeViewAt(current-1);
                if(current > fractals.size()){
                    setCurrent(fractals.size());
                }
                saveFractals();
            }
        });

        ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });

        ab.show();
    }

}
