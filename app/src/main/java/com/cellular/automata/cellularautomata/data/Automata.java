package com.cellular.automata.cellularautomata.data;

import com.cellular.automata.cellularautomata.GRFX;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.core.Rule;
import com.cellular.automata.cellularautomata.objects.Model;
import com.cellular.automata.cellularautomata.objects.ModelRenderBuilder;
import com.cellular.automata.cellularautomata.objects.RenderCube;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CubeCenter;

import java.util.ArrayList;
import java.util.Random;

public class Automata {

    private String TAG = "AUTOMATA_BUILDER";

    private int automataRadius = 10;
    private boolean generating = false;

    //temporary
    private Random random = new Random();
    private long time;
    private long timePast;
    private float delay = 100;

    private ModelRenderBuilder modelRenderBuilder;
    private CubeMap map;

    private Rule rule;

    public Automata(){

        map = new CubeMap(automataRadius);
        modelRenderBuilder = new ModelRenderBuilder(automataRadius);

    }

    // GETTERS

    public ModelRenderBuilder getModelRenderBuilder(){
        return modelRenderBuilder;
    }

    public int getAutomataRadius(){
        return automataRadius;
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

        modelRenderBuilder.setRenderMap(renderMap);
        map = renderMap.toCubeMap();

        modelRenderBuilder.build();
        modelRenderBuilder.bindAttributesData();

    }



    // CONTROLS
    public void start(){

        generating = true;

    }

    public void pause(){

        generating = false;

    }


    // MAIN METHOD NEXT
    public void next(){

        // Get the next iteration map
        ArrayList<Cube> cubes = rule.nextIterations(map);
        this.map = CubeMap.fromList(cubes, automataRadius);

        // Convert CubeMap to RenderMap
        RenderCubeMap renderMap = RenderCubeMap.fromCubeList(cubes, automataRadius);

        // Apply RenderMap
        modelRenderBuilder.setRenderMap(renderMap);

        // Build
        modelRenderBuilder.build();
        modelRenderBuilder.bindAttributesData();


        // Log alive cubes
        if(Settings.log_alive_number){
            GRFX.activityListener.logTextTop("renderCubes: " + String.valueOf(map.numberAllAlive()));
        }

    }

    // MAIN METHOD EXECUTE
    public void execute(){

        // if start is pressed
        if(generating){




        }else{



        }

    }








    private void generateRandomCubes(){

        if(modelRenderBuilder.getRenderMap().size() >= automataRadius*2 * automataRadius*2 * automataRadius*2 -1) return;

        //generating random stuff
        timePast = System.currentTimeMillis() - time;
        if(timePast > delay){
            time = System.currentTimeMillis();

            modelRenderBuilder.addNewCube(generateCellPoint(),  new CellColor(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            //Log.d("Timer", "half a second");

            GRFX.activityListener.logTextTop("cubes: " + String.valueOf(modelRenderBuilder.getRenderMap().size()));
        }

    }

    CubeCenter generateCellPoint(){

        CubeCenter point =  new CubeCenter(random.nextInt(20)-9,random.nextInt(20)-9, random.nextInt(20)-9);
        if(modelRenderBuilder.cubeExists(point)){
            generateCellPoint();
        }else{
            return point;
        }

        return point;

    }



}
