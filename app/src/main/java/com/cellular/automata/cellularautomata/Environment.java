package com.cellular.automata.cellularautomata;

import com.cellular.automata.cellularautomata.interfaces.EnvironmentListener;
import com.cellular.automata.cellularautomata.objects.RenderBuilder;
import com.cellular.automata.cellularautomata.utils.ObjectSelectHelper;

import java.util.ArrayList;

public class Environment implements EnvironmentListener{

    //class keeps track of all the actions on the scene

    private ArrayList<RenderBuilder> buildersList;


    public Environment(){

        GRFX.renderer.setEnvironmentListener(this);

    }


    public void addBuilder(RenderBuilder builder){

        if(builder == null) return;
        if(buildersList == null) buildersList = new ArrayList<RenderBuilder>();

        buildersList.add(builder);

    }


    @Override
    public void onScreenTouched(float normalizedX, float normalizedY) {

        if(buildersList == null || buildersList.size() == 0) return;

        for(RenderBuilder builder: buildersList){

            ObjectSelectHelper.TouchResult touchResult = GRFX.renderer.getTouchedResult(normalizedX, normalizedY, builder.getCellCentersList());
            if(touchResult.cubeTouched){
                builder.handleTouch(touchResult);
                GRFX.rendererController.cubeTouched();
            }

        }

    }

    public void draw(){

        for(RenderBuilder builder: buildersList){
            builder.draw();
        }

    }
}
