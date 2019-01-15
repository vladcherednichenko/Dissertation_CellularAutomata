package com.cellular.automata.cellularautomata.data;

import com.cellular.automata.cellularautomata.objects.RenderCube;

import java.util.ArrayList;

public class CubeMap {

    private String tag = "CUBE_MAP";

    private int automataRadius = 0;

    // Main cube storage
    // The main array - represents the 3-D models
    private Cube[][][] map;

    // Additional cube storage
    // Just a list of all cubes
    private ArrayList<Cube> cubeList = new ArrayList<>();

    public CubeMap(int radius){

        this.automataRadius = radius;
        initializeMap();

    }


    // GETTERS

    public ArrayList<Cube> toList() {

        cubeList.clear();
        for(int i = 0; i< map.length; i++){
            for (int j = 0; j<map[0].length; j++){
                for(int k = 0; k< map[0][0].length; k++){
                    cubeList.add(map[i][j][k]);
                }
            }
        }
        return cubeList;
    }

    public int numberAllAlive(){

        toList();
        int size = 0;
        for (Cube cube: cubeList){

            if(cube.isAlive()) size++;

        }

        return size;
    }

    public int numberAllDead(){

        toList();
        int size = 0;
        for(Cube cube: cubeList){
            if(!cube.isAlive()) size++;
        }

        return size;

    }

    public Cube getCubeAt(int [] cubeCoords){

        if(cubeOutOfBounds(cubeCoords)) return null;

        int [] mapCoords = cubeCoordsToMapCoords(cubeCoords);
        return map[mapCoords[0]][mapCoords[1]][mapCoords[2]];

    }


    // MAIN

    public boolean add(Cube cube){

        if(cubeOutOfBounds(cube.getCoords())) return false;

        int [] mapCoords = cubeCoordsToMapCoords(cube.getCoords());

        map[mapCoords[0]][mapCoords[1]][mapCoords[2]] = cube;

        return true;

    }

    public boolean addAll(){

        // to do
        return true;

    }

    public void clear(){

        cubeList.clear();
        initializeMap();

    }



    // UTILS

    private void initializeMap(){

        map = new Cube[automataRadius * 2 - 1][automataRadius * 2 - 1][automataRadius * 2 - 1];
        for(int i = 0; i< map.length; i++){
            for (int j = 0; j<map[0].length; j++){
                for(int k = 0; k< map[0][0].length; k++){
                    map[i][j][k] = new Cube();
                }
            }
        }

    }

    private boolean cubeOutOfBounds(int[] cubeCoords){

        return Math.abs(cubeCoords[0]) > automataRadius - 1 ||
                Math.abs(cubeCoords[1]) > automataRadius - 1 ||
                Math.abs(cubeCoords[2]) > automataRadius - 1;

    }

    private int[] cubeCoordsToMapCoords(int[] cubeCoords){

        return new int[] {
                cubeCoords[0] + automataRadius -1,
                cubeCoords[1] + automataRadius -1,
                cubeCoords[2] + automataRadius -1
        };

    }
}
