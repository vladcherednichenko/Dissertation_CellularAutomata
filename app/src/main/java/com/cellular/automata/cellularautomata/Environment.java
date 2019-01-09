package com.cellular.automata.cellularautomata;

import com.cellular.automata.cellularautomata.interfaces.EnvironmentListener;
import com.cellular.automata.cellularautomata.objects.AutomataBuilder;
import com.cellular.automata.cellularautomata.utils.CellPoint;
import com.cellular.automata.cellularautomata.utils.ObjectSelectHelper;

import java.util.ArrayList;

import static com.cellular.automata.cellularautomata.GRFX.renderer;

public class Environment implements EnvironmentListener{

    //class keeps track of all the actions on the scene

    private ArrayList<AutomataBuilder> buildersList;


    public Environment(){

        GRFX.renderer.setEnvironmentListener(this);

    }


    public void addBuilder(AutomataBuilder builder){

        if(builder == null) return;
        if(buildersList == null) buildersList = new ArrayList<AutomataBuilder>();

        buildersList.add(builder);

    }


    @Override
    public void onScreenTouched(float normalizedX, float normalizedY) {

        if(buildersList == null || buildersList.size() == 0) return;

        for(AutomataBuilder builder: buildersList){

            ObjectSelectHelper.TouchResult touchResult = GRFX.renderer.getTouchedResult(normalizedX, normalizedY, builder.getCellCentersList());
            if(touchResult.cubeTouched){
                builder.handleTouch(touchResult);
                GRFX.rendererController.cubeTouched();
            }

        }

    }

    public void draw(){

        for(AutomataBuilder builder: buildersList){
            builder.draw();
        }

    }
}
