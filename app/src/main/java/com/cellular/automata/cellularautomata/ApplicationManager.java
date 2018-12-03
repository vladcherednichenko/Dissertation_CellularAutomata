package com.cellular.automata.cellularautomata;


import android.util.Log;

import com.cellular.automata.cellularautomata.activity.MainActivity;
import com.cellular.automata.cellularautomata.interfaces.ApplicationListener;
import com.cellular.automata.cellularautomata.interfaces.CellSelectListener;
import com.cellular.automata.cellularautomata.objects.Model;
import com.cellular.automata.cellularautomata.objects.AutomataBuilder;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CellPoint;
import com.cellular.automata.cellularautomata.utils.ObjectSelectHelper;

import java.util.Random;

public class ApplicationManager implements ApplicationListener, MainActivity.ActivityInterface {


    private AutomataBuilder builder;
    private Environment environment;

    private long time;
    private long timePast;
    private boolean isGenerating = false;

    private Random random;

    @Override
    public void create() {

        builder = new AutomataBuilder();
        environment = new Environment();

        CellColor colors[] = new CellColor[Settings.testAutomataCoords.length/3];
        for(int i = 0; i< colors.length; i++){
            colors[i] = new CellColor("#4286f4");
        }

        Model testModel = new Model(Settings.testAutomataCoords, colors);

        builder.setModel(testModel);
        builder.build();
        builder.bindAttributesData();
        environment.addBuilder(builder);

        random = new Random();

    }

    @Override
    public void render() {

        if(builder.isTouched()){

            builder.addNewCube(builder.getTouchResult().newCubeCenter, new CellColor("#4286f4"));

        }

        if(isGenerating){

            timePast = System.currentTimeMillis() - time;
            if(timePast > 100){
                time = System.currentTimeMillis();
                builder.addNewCube(new CellPoint(random.nextInt(20)-9,random.nextInt(20)-9, random.nextInt(20)-9),

                        new CellColor(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                Log.d("Timer", "half a second");
            }

        }

        builder.draw();

    }

    @Override
    public void goBtnPressed(boolean b) {
        isGenerating = !isGenerating;
    }

    @Override
    public void resetBtnPressed() {
        GRFX.renderer.resetCam();
    }

    @Override
    public void speedUpBtnPressed() {

    }

    @Override
    public void resize() {

        //not needed

    }

}
