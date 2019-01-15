package com.cellular.automata.cellularautomata.data;

import com.cellular.automata.cellularautomata.GRFX;
import com.cellular.automata.cellularautomata.core.Rule;
import com.cellular.automata.cellularautomata.objects.Model;
import com.cellular.automata.cellularautomata.objects.RenderBuilder;
import com.cellular.automata.cellularautomata.objects.RenderCube;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CubeCenter;

import java.util.ArrayList;
import java.util.Random;

public class Automata {

    private String TAG = "AUTOMATA_BUILDER";

    private int automataRadius = 30;
    private boolean generating = false;

    //temporary
    private Random random = new Random();
    private long time;
    private long timePast;
    private float delay = 100;

    private RenderBuilder renderBuilder;
    private CubeMap map;

    private Rule rule;

    public Automata(){

        map = new CubeMap(automataRadius);
        renderBuilder = new RenderBuilder(automataRadius);

    }

    // GETTERS

    public RenderBuilder getRenderBuilder(){
        return renderBuilder;
    }

    // SETTERS

    public void setRule(Rule rule){

        this.rule = rule;

    }


    //sets the cellList
    public void setModel(Model modelToLoad){

        // crap method can do better

        float [] rawCoords = modelToLoad.getAutomataCoords();
        CellColor[] cellColors = modelToLoad.getAutomataColors();
        RenderCubeMap renderMap = new RenderCubeMap(automataRadius);

        for(int i = 0; i< modelToLoad.getCellsNumber(); i++){

            CubeCenter cellCenter = new CubeCenter(rawCoords[i*3], rawCoords[i*3 + 1], rawCoords[i*3 + 2]);
            renderMap.add(new RenderCube(cellCenter, cellColors[i]));

        }

        renderBuilder.setRenderMap(renderMap);
        map = renderMap.toCubeMap();

        renderBuilder.build();
        renderBuilder.bindAttributesData();

    }



    // CONTROLS
    public void start(){

        generating = true;
        delay = 100;

    }

    public void pause(){

        generating = false;

    }

    public void speedUp(){

        generating = true;
        delay = delay * 0.5f;

    }

    // MAIN METHOD EXECUTE
    public void execute(){

        // if start is pressed
        if(generating){

            ArrayList<Cube> cubes = rule.nextIterations(map);
            //renderBuilder.setRenderMap();




            GRFX.activityListener.logTextTop("renderCubes: " + String.valueOf(map.numberAllAlive()));

        }else{



        }

    }

    // MAIN METHOD NEXT
    public void next(){

        ArrayList<Cube> cubes = rule.nextIterations(map);



    }

    private void generateRandomCubes(){

        if(renderBuilder.getRenderMap().size() >= automataRadius*2 * automataRadius*2 * automataRadius*2 -1) return;

        //generating random stuff
        timePast = System.currentTimeMillis() - time;
        if(timePast > delay){
            time = System.currentTimeMillis();

            renderBuilder.addNewCube(generateCellPoint(),  new CellColor(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            //Log.d("Timer", "half a second");

            GRFX.activityListener.logTextTop("cubes: " + String.valueOf(renderBuilder.getRenderMap().size()));
        }

    }

    CubeCenter generateCellPoint(){

        CubeCenter point =  new CubeCenter(random.nextInt(20)-9,random.nextInt(20)-9, random.nextInt(20)-9);
        if(renderBuilder.cubeExists(point)){
            generateCellPoint();
        }else{
            return point;
        }

        return point;

    }



}
