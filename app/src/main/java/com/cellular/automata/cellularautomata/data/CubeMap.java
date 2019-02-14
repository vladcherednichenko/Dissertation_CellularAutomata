package com.cellular.automata.cellularautomata.data;

import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.objects.AutomataModel;
import com.cellular.automata.cellularautomata.objects.RenderCube;

import java.util.ArrayList;

public class CubeMap {

    private String tag = "CUBE_MAP";
    private int coordsNumber = 3;
    private char cubeDeviderSymbol = '|';
    private char coordsDeviderSymbol = ':';

    private int automataRadius;
    private int aliveNumber;

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

    public CubeMap(){

        this.automataRadius = Settings.defaultAutomataRadius;
        initializeMap();

    }

    // SETTERS

    public void setRadius(int radius){

        if (radius < Settings.minimumAutomataRadius) return;

        this.automataRadius = radius;

        adaptToNewRadius();

    }

    public void setAlive(int number){

        this.aliveNumber = number;

    }


    // GETTERS

    public static CubeMap fromList(ArrayList<Cube> list, int automataRadius){

        CubeMap map = new CubeMap(automataRadius);
        map.addAll(list);
        return map;

    }

    public int getAutomataRadius(){

        return automataRadius;

    }

    public int getAliveNumber(){

        return aliveNumber;

    }

    public ArrayList<Cube> toList() {

        cubeList.clear();
        for(int i = 0; i< map.length; i++){
            for (int j = 0; j<map[0].length; j++){
                for(int k = 0; k< map[0][0].length; k++){
                    cubeList.add(map[i][j][k]);
                }
            }
        }

        ArrayList<Cube> list = new ArrayList<>();
        for(Cube cube: cubeList){

            list.add(cube.copy());

        }

        return list;

    }

    public ArrayList<Cube> getAlive() {
        ArrayList<Cube> result = new ArrayList<>();
        ArrayList<Cube> all = toList();

        for(Cube cube: all){
            if(cube.isAlive()){
                result.add(cube);
            }
        }

        return result;
    }

    public int numberAllAlive(){

        int size = 0;
        for(int i = 0; i< map.length; i++){
            for (int j = 0; j<map[0].length; j++){
                for(int k = 0; k< map[0][0].length; k++){
                    if(map[i][j][k].isAlive()) size++;
                }
            }
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

        if(cube.isAlive()) aliveNumber ++;

        return true;

    }

    public boolean addAll(ArrayList<Cube> list){


        for (Cube cube: list){

            add(cube);

        }

        return true;

    }

    public boolean remove(Cube cube){

        if(cubeOutOfBounds(cube.getCoords())) return false;

        int [] mapCoords = cubeCoordsToMapCoords(cube.getCoords());

        if(map[mapCoords[0]][mapCoords[1]][mapCoords[2]].isAlive()) aliveNumber --;

        map[mapCoords[0]][mapCoords[1]][mapCoords[2]] = new Cube(Settings.defaultCubeColor, cube.getCoords());


        return true;

    }

    public boolean paint(Cube cube){

        if(cubeOutOfBounds(cube.getCoords())) return false;

        int [] mapCoords = cubeCoordsToMapCoords(cube.getCoords());

        if(!map[mapCoords[0]][mapCoords[1]][mapCoords[2]].isAlive()) return false;

        map[mapCoords[0]][mapCoords[1]][mapCoords[2]].setColor(cube.getColor());

        return true;

    }

    public void clear(){

        aliveNumber = 0;
        cubeList.clear();
        initializeMap();

    }



    // UTILS

    private void initializeMap(){

        map = new Cube[automataRadius * 2 - 1][automataRadius * 2 - 1][automataRadius * 2 - 1];
        for(int i = 0; i< map.length; i++){
            for (int j = 0; j<map[0].length; j++){
                for(int k = 0; k< map[0][0].length; k++){

                    map[i][j][k] = new Cube(Settings.defaultCubeColor, new int[]{i - automataRadius +1, j - automataRadius +1, k - automataRadius +1});

                }
            }
        }

    }

    private void adaptToNewRadius(){

        ArrayList<Cube> oldCubeList = toList();
        initializeMap();
        addAll(oldCubeList);

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

    public String toString(){

        ArrayList<Cube> aliveCubes = getAlive();

        StringBuilder result = new StringBuilder();

        for(Cube cube: aliveCubes){

            result.append(cube.getCoords()[0]).append(coordsDeviderSymbol);
            result.append(cube.getCoords()[1]).append(coordsDeviderSymbol);
            result.append(cube.getCoords()[2]).append(cubeDeviderSymbol);

        }

        for(Cube cube: aliveCubes){

            result.append(cube.getColor());
            result.append(cubeDeviderSymbol);

        }

        return result.substring(0, result.length()-1);


    }

    public static CubeMap fromString(String stringAutomataForm, int automataRadius){


        CubeMap map = new CubeMap(automataRadius);

        return map;

    }
}
