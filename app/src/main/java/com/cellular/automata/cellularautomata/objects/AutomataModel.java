package com.cellular.automata.cellularautomata.objects;

import android.graphics.Bitmap;
import android.util.Log;

import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.data.Cube;
import com.cellular.automata.cellularautomata.data.CubeMap;
import com.cellular.automata.cellularautomata.data.CellColor;
import com.cellular.automata.cellularautomata.utils.CoordsHelper;

import java.util.ArrayList;

public class AutomataModel {

    private String TAG = "MODEL";

    private String screenshotName = "";
    private String name = "";
    private String rule = "";
    private int iteration;

    private CubeMap map;


    // initialized only when save
    private Bitmap screenshot = null;

    // Setters


    public void setName(String name) { this.name = name; }

    public void setScreenshotName(String screenshotName){this.screenshotName = screenshotName;}

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public void setAliveNumber(int aliveNumber) {
        this.map.setAlive(aliveNumber);
    }

    public void setScreenshot(Bitmap screenshot) {
        this.screenshot = screenshot;
    }

    public void setRule(String rule){this.rule = rule;}

    public void setMap(CubeMap map) { this.map = map; }

    public void setRadius(int radius){
        this.map.setRadius(radius);
    }



    // Getters

    public String getName() {
        return name;
    }

    public int getAliveNumber() {
        return map.getAliveNumber();
    }

    public CubeMap getMap() {
        return map;
    }

    public Bitmap getScreenshot() {
        return screenshot;
    }

    public int getIteration() {
        return iteration;
    }

    public int getRadius(){
        return map.getAutomataRadius();
    }

    public String getScreenshotName(){return screenshotName;}

    public String getRule() {return rule; }



    // Main
    public AutomataModel(){

        this.map = new CubeMap(Settings.automataRadius);

    }

    public AutomataModel(int automataRadius){

        this.map = new CubeMap(automataRadius);

    }

    public AutomataModel(float[] coords, CellColor[] colors, int radius){

        if(coords.length % 3 != 0){

            Log.d(TAG,"wrong coordinates array length");
            return;

        }

        if(colors.length != coords.length / 3) {

            Log.d(TAG,"wrong colors array length");
            return;

        }


        int coordsPointer = 0;
        int colorsPointer = 0;
        int cubeNumber = colors.length;

        map = new CubeMap(radius);

        for(int i = 0; i< cubeNumber; i++){

            Cube cube = new Cube(colors[colorsPointer++].hexColor, CoordsHelper.getCubeCoordsByNumber(coords, coordsPointer++));
            cube.setAlive(true);
            map.add(cube);

        }


    }


    public void addCube(Cube cube){

        if(map == null) map = new CubeMap(Settings.automataRadius);

        map.add(cube);

    }

    public void addCubes(ArrayList<Cube> cubes){

        map.addAll(cubes);

    }

    public void removeCube(Cube cube){

        map.remove(cube);

    }

    public void paintCube(Cube cube){

        map.paint(cube);

    }

    public static AutomataModel fromCoordsArray(float [] coords, int automataRadius){

        CellColor colors[] = new CellColor[coords.length/3];
        for(int i = 0; i< colors.length; i++){
            colors[i] = new CellColor(Settings.defaultCubeColor);
        }

        return new AutomataModel(coords, colors, automataRadius);

    }


    public String toString (){

        if(map == null) return "";

        return map.toString();

    }




}
