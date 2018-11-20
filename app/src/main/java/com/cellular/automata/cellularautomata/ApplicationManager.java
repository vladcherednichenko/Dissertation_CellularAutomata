package com.cellular.automata.cellularautomata;


import android.util.Log;

import com.cellular.automata.cellularautomata.objects.Model;
import com.cellular.automata.cellularautomata.objects.AutomataBuilder;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CellularPoint;

import java.util.Random;

public class ApplicationManager implements ApplicationListener, MainActivity.ActivityInterface {


    private AutomataBuilder builder;

    private long time;
    private long timePast;
    private boolean isGenerating = false;

    private Random random;

    @Override
    public void create() {

        builder = new AutomataBuilder();

        CellColor colors[] = new CellColor[]{new CellColor("#4286f4")};
        Model testModel = new Model(Settings.testAutomataCoords, colors);

        builder.setModel(testModel);
        builder.build();
        builder.bindAttributesData();

        random = new Random();

    }

    @Override
    public void resize() {

    }

    @Override
    public void render() {

        if(isGenerating){

            timePast = System.currentTimeMillis() - time;
            if(timePast > 100){
                time = System.currentTimeMillis();
                builder.addNewCube(new CellularPoint(random.nextInt(20)-9,random.nextInt(20)-9, random.nextInt(20)-9),

                        new CellColor(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                Log.d("Timer", "half a second");
            }

        }




        builder.draw();

    }

    @Override
    public void goBtnPressed() {
        isGenerating = !isGenerating;
    }
}
