package com.cellular.automata.cellularautomata.data;

import android.graphics.Color;
import android.util.Log;

import com.cellular.automata.cellularautomata.objects.RenderCube;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CubeCenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class RenderCubeMap {

    private String TAG = "RENDER_CUBE_MAP";

    private int automataRadius = 0;

    //the main array - represents the 3-D models
    private RenderCube[][][] map;
    //just a list of all the cubes
    private ArrayList<RenderCube> renderCubeList = new ArrayList<>();

    public RenderCubeMap(int automataRadius){

        this.automataRadius = automataRadius;
        this.map = new RenderCube[automataRadius * 2 - 1][automataRadius * 2 - 1][automataRadius * 2 - 1];

    }

    //GETTERS
    public ArrayList<RenderCube> getRenderCubeList() {
        return sort();
    }

    public ArrayList<CubeCenter> getCubeCenters(){

        ArrayList<CubeCenter> result = new ArrayList<>();
        for (RenderCube renderCube : renderCubeList){
            result.add(renderCube.center);
        }

        return result;

    }

    public ArrayList<CubeCenter> getLayer(int height){

        // can rewrite to make it faster

        ArrayList<CubeCenter> result = new ArrayList<>();

        for (RenderCube renderCube : renderCubeList){
            if((int)renderCube.center.y == height){

                result.add(renderCube.center);

            }

        }

        return result;

    }

    public int size(){
        return renderCubeList.size();
    }

    public RenderCube getCubeAt(int[] coords){

        if (!cubeExists(new CubeCenter(coords[0], coords[1], coords[2])))
            return null;

        int[] mapCoords = cubeCoordsToMapCoords(coords);
        return map[mapCoords[0]][mapCoords[1]][mapCoords[2]];

    }

    public RenderCube getCubeByCenter(CubeCenter center){

        if (!cubeExists(center))
            return null;

        int[] mapCoords = cubeCoordsToMapCoords(new int[]{(int)center.x, (int)center.y, (int)center.z});
        return map[mapCoords[0]][mapCoords[1]][mapCoords[2]];

    }

    public int height(){

        int low = 10000;
        int high = -10000;

        for (RenderCube renderCube : renderCubeList){

            if(renderCube.center.y < low)
                low = (int) renderCube.center.y;

            if(renderCube.center.y > high)
                high = (int) renderCube.center.y;

        }

        return high - low +1;

    }

    public CubeMap toCubeMap (){

        CubeMap cubeMap = new CubeMap(automataRadius);

        for(RenderCube cube: renderCubeList){

            Cube newCube = new Cube(cube.color.hexColor, new int[]{(int)cube.center.x, (int)cube.center.y, (int)cube.center.z});
            newCube.setAlive(true);
            cubeMap.add(newCube);

        }

        return cubeMap;

    }

    // Converts CubeMap to RenderCubeMap
    public static RenderCubeMap fromCubeMap(CubeMap map){

        return fromCubeList(map.getAlive(), map.automataRadius());

    }

    public static RenderCubeMap fromCubeList(ArrayList<Cube> cubeList, int automataRadius){

        RenderCubeMap newMap = new RenderCubeMap(automataRadius);

        for (Cube cube: cubeList){

            if(cube.isAlive()){

                int [] coords = cube.getCoords();
                RenderCube renderCube = new RenderCube(new CubeCenter((float) coords[0], (float)coords[1], (float)coords[2]), new CellColor(cube.getColor()), true);
                newMap.add(renderCube);

            }


        }

        return newMap;

    }


    //MAIN METHODS

    public void add(RenderCube renderCube){

        //checking renderCube stuff

        if(cubeExists(renderCube)) {
            Log.d(TAG, "renderCube already exists");
            return;
        }

        //actual adding the renderCube

        int [] coordsVector = cubeToMapCoords(renderCube);

        map[coordsVector[0]][coordsVector[1]][coordsVector[2]] = renderCube;

        renderCubeList.add(renderCube);

    }

    public void addAll(ArrayList<RenderCube> renderCubeList){

        for(RenderCube renderCube : renderCubeList){

            add(renderCube);

        }

    }


    public void remove(RenderCube renderCube){

        //checking renderCube stuff

        if(!cubeExists(renderCube)) {
            Log.d(TAG, "renderCube does not exist");
            return;
        }

        //actually removing renderCube

        int[] coords = cubeToMapCoords(renderCube);

        map[coords[0]][coords[1]][coords[2]] = null;

        Iterator<RenderCube> iterator = renderCubeList.iterator();

        while(iterator.hasNext()){

            if(iterator.next().center.equals(renderCube.center)){
                iterator.remove();
            }

        }

    }


    //ADDITIONAL STUFF
    public boolean cubeExists(CubeCenter center){

        return cubeExists(new RenderCube(center, null, false));

    }

    public boolean cubeExists(RenderCube renderCube){

        int[] coords = cubeToMapCoords(renderCube);
        return cubeInBounds(renderCube) && map[coords[0]][coords[1]][coords[2]] != null;

    }

    private int[] cubeToMapCoords(RenderCube renderCube){

        return cubeCoordsToMapCoords(new int[]{
                (int) renderCube.center.x,
                (int) renderCube.center.y,
                (int) renderCube.center.z});

    }

    private int[] cubeCoordsToMapCoords(int[] coords){

        return new int[] {
                coords[0] + automataRadius -1,
                coords[1] + automataRadius -1,
                coords[2] + automataRadius -1
        };

    }

    public boolean cubeInBounds(RenderCube renderCube){

        int[] coordsVector = cubeToMapCoords(renderCube);

        int pos_x = Math.abs((int) renderCube.center.x);
        int pos_y = Math.abs((int) renderCube.center.y);
        int pos_z = Math.abs((int) renderCube.center.z);

        if(Math.abs(pos_x) > automataRadius -1 ||
                Math.abs(pos_y) > automataRadius -1 ||
                Math.abs(pos_z) > automataRadius -1){

            Log.d(TAG, "RenderCube center is out of automata bounds");
            Log.d(TAG, "Automata radius = " + automataRadius + " | RenderCube center: " +
                    String.valueOf(renderCube.center.x) + "," +
                    String.valueOf(renderCube.center.y) + "," +
                    String.valueOf(renderCube.center.x));
            return false;

        }

        return true;

    }

    public ArrayList<RenderCube> sort(){

        Collections.sort(renderCubeList);
        return renderCubeList;

    }

    public void clear(){

        this.map = new RenderCube[automataRadius * 2][automataRadius * 2][automataRadius * 2];
        this.renderCubeList.clear();

    }

}
