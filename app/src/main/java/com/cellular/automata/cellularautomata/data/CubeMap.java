package com.cellular.automata.cellularautomata.data;

import android.util.Log;

import com.cellular.automata.cellularautomata.objects.Cube;

import java.util.ArrayList;

public class CubeMap {

    private String TAG = "CUBE_MAP";

    private int automataRadius = 0;
    private Cube[][][] map;
    private ArrayList<Cube> cubeList = new ArrayList<>();

    public CubeMap(int automataRadius){

        this.automataRadius = automataRadius;
        this.map = new Cube[automataRadius * 2][automataRadius * 2][automataRadius * 2];

    }

    public void add(Cube cube){

        if(cubeExists(cube)) {
            Log.d(TAG, "cube already exists");
            return;
        }

        int [] coordsVector = cubeToMapCoords(cube);

        map[coordsVector[0]][coordsVector[1]][coordsVector[2]] = cube;

    }


    public void remove(Cube cube){

        if(!cubeExists(cube)) {
            Log.d(TAG, "cube does not exist");
            return;
        }

        int[] coords = cubeToMapCoords(cube);

        map[coords[0]][coords[1]][coords[2]] = null;

    }

    public boolean cubeExists(Cube cube){

        int[] coords = cubeToMapCoords(cube);
        return cubeInBounds(cube) && map[coords[0]][coords[1]][coords[2]] != null;

    }

    private int[] cubeToMapCoords(Cube cube){

        return new int []{
                (int)cube.center.x + automataRadius,
                (int)cube.center.y + automataRadius,
                (int)cube.center.z + automataRadius};

    }

    public boolean cubeInBounds(Cube cube){

        int[] coordsVector = cubeToMapCoords(cube);

        int pos_x = (int)cube.center.x + automataRadius;
        int pos_y = (int)cube.center.y + automataRadius;
        int pos_z = (int)cube.center.z + automataRadius;

        if(Math.abs(pos_x) > automataRadius * 2 ||
                Math.abs(pos_y) > automataRadius * 2 ||
                Math.abs(pos_z) > automataRadius * 2){

            Log.d(TAG, "Cube center is out of automata bounds");
            Log.d(TAG, "Automata radius = " + automataRadius + " | Cube center: " +
                    String.valueOf(cube.center.x) + "," +
                    String.valueOf(cube.center.y) + "," +
                    String.valueOf(cube.center.x));
            return false;

        }

        return true;

    }


}
