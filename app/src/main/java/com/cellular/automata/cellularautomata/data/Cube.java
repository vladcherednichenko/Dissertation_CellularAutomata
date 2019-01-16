package com.cellular.automata.cellularautomata.data;

import android.graphics.Color;

import com.cellular.automata.cellularautomata.Settings;

public class Cube {

    // a simple cube to represent the automata CELL

    private boolean isAlive;

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

    public boolean isAlive(){return isAlive;}

    public void setAlive(boolean b){this.isAlive = b;}

    public void setColor(String color){this.color = color;}

    public String getColor() {
        return color;
    }

    public int[] getCoords() { return coords;}




    public boolean samePosition(Cube cube){

        int [] newCoords = cube.getCoords();
        return newCoords[0] == coords[0] && newCoords[1] == coords[1] && newCoords [2] == coords[2];

    }

    public Cube copy(){

        return new Cube(this.color, this.coords);

    }
}
