package com.cellular.automata.cellularautomata.data;

import android.util.Log;

import com.cellular.automata.cellularautomata.objects.Cube;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CellPoint;

import java.util.ArrayList;
import java.util.Iterator;

public class CubeMap {

    private String TAG = "CUBE_MAP";

    private int automataRadius = 0;

    //the main array - represents the 3-D models
    private Cube[][][] map;
    //just a list of all the cubes
    private ArrayList<Cube> cubeList = new ArrayList<>();

    public CubeMap(int automataRadius){

        this.automataRadius = automataRadius;
        this.map = new Cube[automataRadius * 2][automataRadius * 2][automataRadius * 2];

    }

    //GETTERS
    public ArrayList<Cube> getCubeList() {
        return cubeList;
    }

    public ArrayList<CellPoint> getCubeCenters(){

        ArrayList<CellPoint> result = new ArrayList<>();
        for (Cube cube: cubeList){
            result.add(cube.center);
        }

        return result;

    }

    public int size(){
        return cubeList.size();
    }

    public Cube getCubeAt(int[] coords){

        if (!cubeExists(new CellPoint(coords[0], coords[1], coords[2])))
            return null;

        int[] mapCoords = cubeCoordsToMapCoords(coords);
        return map[mapCoords[0]][mapCoords[1]][mapCoords[2]];

    }


    //MAIN METHODS

    public void add(Cube cube){

        //checking cube stuff

        if(cubeExists(cube)) {
            Log.d(TAG, "cube already exists");
            return;
        }

        //actual adding the cube

        int [] coordsVector = cubeToMapCoords(cube);

        map[coordsVector[0]][coordsVector[1]][coordsVector[2]] = cube;

        cubeList.add(cube);

    }

    public void remove(Cube cube){

        //checking cube stuff

        if(!cubeExists(cube)) {
            Log.d(TAG, "cube does not exist");
            return;
        }

        //actually removing cube

        int[] coords = cubeToMapCoords(cube);

        map[coords[0]][coords[1]][coords[2]] = null;

        Iterator<Cube> iterator = cubeList.iterator();

        while(iterator.hasNext()){

            if(iterator.next().center.equals(cube.center)){
                iterator.remove();
            }

        }

    }


    //ADDITIONAL STUFF
    public boolean cubeExists(CellPoint center){

        return cubeExists(new Cube(center, null, false));

    }

    public boolean cubeExists(Cube cube){

        int[] coords = cubeToMapCoords(cube);
        return cubeInBounds(cube) && map[coords[0]][coords[1]][coords[2]] != null;

    }

    private int[] cubeToMapCoords(Cube cube){

        return cubeCoordsToMapCoords(new int[]{
                (int)cube.center.x,
                (int)cube.center.y,
                (int)cube.center.z});

    }

    private int[] cubeCoordsToMapCoords(int[] coords){

        return new int[] {
                coords[0] + automataRadius,
                coords[1] + automataRadius,
                coords[2] + automataRadius
        };

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

    public void clear(){

        this.map = new Cube[automataRadius * 2][automataRadius * 2][automataRadius * 2];
        this.cubeList.clear();

    }

}
