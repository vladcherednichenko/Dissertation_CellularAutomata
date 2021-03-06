package com.cellular.automata.cellularautomata.data;

import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.utils.CubeCenter;

public class Cube {

    // a simple cube to represent the automata CELL

    private boolean isAlive;

    private int iterations = 0;

    private int [] coords;

    private String defaultColor = Settings.defaultCubeColor;

    private String color;

    public Cube(){

        color = defaultColor;

    }

    public Cube (String color, int[] coords){

        this.color = color;
        this.coords = coords;

    }

    public Cube (CubeCenter center){

        this.coords = new int []{
                (int) center.x,
                (int) center.y,
                (int) center.z};

        this.color = defaultColor;

    }

    public Cube (String color, CubeCenter center){

        this.coords = new int []{
                (int) center.x,
                (int) center.y,
                (int) center.z};

        this.color = color;

    }

    public Cube (String color, CubeCenter center, boolean isAlive){

        this.coords = new int []{
                (int) center.x,
                (int) center.y,
                (int) center.z};

        this.color = color;

        this.isAlive = isAlive;

    }

    public boolean isAlive(){return isAlive;}

    public void setAlive(boolean b){this.isAlive = b;}

    public void setColor(String color){this.color = color;}

    public String getColor() {
        return color;
    }

    public int[] getCoords() { return coords;}

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getIterations() {
        return iterations;
    }

    public void plusIteration(){
        iterations ++;
    }

    public void resetIterations(){
        iterations = 0;
    }

    public boolean samePosition(Cube cube){

        int [] newCoords = cube.getCoords();
        return newCoords[0] == coords[0] && newCoords[1] == coords[1] && newCoords [2] == coords[2];

    }

    public Cube copy(){

        Cube cube = new Cube (this.color, this.coords);
        cube.setAlive(this.isAlive);

        return cube;

    }
}
