package com.cellular.automata.cellularautomata;

import com.cellular.automata.cellularautomata.interfaces.EnvironmentListener;
import com.cellular.automata.cellularautomata.objects.ModelRenderBuilder;
import com.cellular.automata.cellularautomata.utils.ObjectSelectHelper;

import java.util.ArrayList;

public class Environment implements EnvironmentListener{

    //class keeps track of all the actions on the scene

    private ArrayList<ModelRenderBuilder> buildersList;


    public Environment(){

        GRFX.renderer.setEnvironmentListener(this);

    }


    public void addBuilder(ModelRenderBuilder builder){

        if(builder == null) return;
        if(buildersList == null) buildersList = new ArrayList<ModelRenderBuilder>();

        buildersList.add(builder);

    }


    @Override
    public void onScreenTouched(float normalizedX, float normalizedY) {

        if(buildersList == null || buildersList.size() == 0) return;

        for(ModelRenderBuilder builder: buildersList){

            ObjectSelectHelper.TouchResult touchResult = GRFX.renderer.getTouchedResult(normalizedX, normalizedY, builder.getCellCentersList());
            if(touchResult.cubeTouched){
                builder.handleTouch(touchResult);
                GRFX.rendererController.cubeTouched();
            }

        }

    }

    public void draw(){

        for(ModelRenderBuilder builder: buildersList){
            builder.draw();
        }

    }
}
