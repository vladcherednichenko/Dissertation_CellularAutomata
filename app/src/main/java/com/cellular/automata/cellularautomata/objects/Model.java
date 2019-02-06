package com.cellular.automata.cellularautomata.objects;

import android.util.Log;

import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.utils.CellColor;

public class Model {

    private String TAG = "MODEL";

    private int cellsNumber;
    private float[] coords;
    private CellColor[] colors;

    public int getCellsNumber() {
        return cellsNumber;
    }

    public float[] getCoords() {
        return coords;
    }

    public CellColor[] getColors() {
        return colors;
    }


    public Model(){

    }

    public Model(float[] coords, CellColor[] colors){

        if(coords.length % 3 != 0){

            Log.d(TAG,"wrong coordinates array length");
            return;

        }

        if(colors.length != coords.length / 3) {

            Log.d(TAG,"wrong colors array length");
            return;

        }

        this.coords = coords;
        this.colors = colors;
        cellsNumber = this.coords.length / 3;

    }

    public static Model fromCoordsArray(float [] coords){

        CellColor colors[] = new CellColor[coords.length/3];
        for(int i = 0; i< colors.length; i++){
            colors[i] = new CellColor(Settings.defaultCubeColor);
        }

        return new Model(coords, colors);

    }

}
