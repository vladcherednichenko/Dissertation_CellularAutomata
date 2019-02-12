package com.cellular.automata.cellularautomata.data;

import com.cellular.automata.cellularautomata.LINKER;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.core.Rule;
import com.cellular.automata.cellularautomata.objects.AutomataModel;
import com.cellular.automata.cellularautomata.objects.ModelRenderBuilder;
import com.cellular.automata.cellularautomata.utils.CubeCenter;

import java.util.ArrayList;
import java.util.Random;

public class Automata {

    private String TAG = "AUTOMATA_BUILDER";

    private boolean generating = false;

    //temporary
    private Random random = new Random();
    private long time;
    private long timePast;
    private float delay = 500;

    private ModelRenderBuilder modelRenderBuilder;
    private AutomataModel model;

    private Rule rule;

    public Automata(){

        model = new AutomataModel();
        modelRenderBuilder = new ModelRenderBuilder(Settings.defaultAutomataRadius);

    }

    // GETTERS

    public ModelRenderBuilder getModelRenderBuilder(){
        return modelRenderBuilder;
    }

    public int getAutomataRadius(){
        return model.getRadius();
    }

    public AutomataModel getModel(){

        return model;

    }

    // SETTERS

    public void setRule(Rule rule){

        this.rule = rule;

    }

    public void setRadius(int radius){

        if(radius == model.getRadius() || radius < Settings.defaultAutomataRadius) return;
        model.setRadius(radius);
        updateRender(model.getMap());

    }

    //sets the cellList
    public void setModel(AutomataModel modelToLoad){

        this.model = modelToLoad;

        updateRender(model.getMap());

    }



    // CONTROLS
    public void start(){

        generating = true;

    }

    public void pause(){

        generating = false;

    }


    // input from user
    public void addNewCube(Cube cube){

        model.addCube(cube);
        updateRender(model.getMap());

    }

    //
    public void removeCube(Cube cube){

        model.removeCube(cube);
        updateRender(model.getMap());

    }

    public void paintCube(Cube cube){

        model.paintCube(cube);
        updateRender(model.getMap());

    }



    // MAIN METHOD NEXT
    public void next(){

        // Get the next iteration map
        model.setMap(rule.nextIterations(model.getMap()));
        model.setIteration(model.getIteration() + 1);

        updateRender(model.getMap());

    }

    // MAIN METHOD EXECUTE
    public void execute(){

        // if start is pressed
        if(generating){

            generateWithCurrentRule();

        }

    }


    private void updateRender(CubeMap map){

        ArrayList<Cube> cubes = map.getAlive();

        updateRender(cubes, map.getAutomataRadius());


    }

    private void updateRender(ArrayList<Cube> cubes, int automataRadius){

        // Convert CubeMap to RenderMap
        RenderCubeMap renderMap = RenderCubeMap.fromCubeList(cubes, automataRadius);

        // Apply RenderMap
        modelRenderBuilder.setRenderMap(renderMap);

        // Build
        modelRenderBuilder.build();
        modelRenderBuilder.bindAttributesData();


        // Log alive cubes
        if(Settings.log_alive_number){
            LINKER.activityListener.logTextTop("renderCubes: " + String.valueOf(model.getAliveNumber()));
        }

    }





    private void generateRandomCubes(int automataRadius){

        if(modelRenderBuilder.getRenderMap().size() >= automataRadius*2 * automataRadius*2 * automataRadius*2 -1) return;

        //generating random stuff
        timePast = System.currentTimeMillis() - time;
        if(timePast > delay){
            time = System.currentTimeMillis();

            modelRenderBuilder.addNewCube(generateCellPoint(),  new CellColor(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            //Log.d("Timer", "half a second");

            LINKER.activityListener.logTextTop("cubes: " + String.valueOf(modelRenderBuilder.getRenderMap().size()));
        }

    }

    private void generateWithCurrentRule(){

        timePast = System.currentTimeMillis() - time;
        if(timePast > delay){
            time = System.currentTimeMillis();

            next();

            //LINKER.activityListener.logTextTop("cubes: " + String.valueOf(modelRenderBuilder.getRenderMap().size()));
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
