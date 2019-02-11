package com.cellular.automata.cellularautomata.utils;

public class CoordsHelper {

    public static int COORDS_NUMBER = 3;

    public static int[] getCubeCoordsByNumber(float [] coords, int cubeNumber){

        if(cubeNumber -1 > coords.length / COORDS_NUMBER) return null;

        return new int[]{

                (int)coords[COORDS_NUMBER * cubeNumber],
                (int)coords[COORDS_NUMBER * cubeNumber + 1],
                (int)coords[COORDS_NUMBER * cubeNumber + 2]

        };

    }

}
