package com.cellular.automata.cellularautomata.core;

import android.util.Log;
import android.util.SparseArray;

import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.data.Cube;
import com.cellular.automata.cellularautomata.data.CubeMap;
import com.cellular.automata.cellularautomata.data.RenderCubeMap;
import com.cellular.automata.cellularautomata.objects.RenderCube;
import com.cellular.automata.cellularautomata.utils.CellColor;

import java.util.ArrayList;
import java.util.Iterator;

public class Rule {

    private String Tag = "Rule";

    private int [] keepAliveNeighboursNumber = {1};
    private int [] reviveNeighboursNumber = {1};
    private float darkerColorPercent = 0.5f ;

    //GETTERS
    public int getNeighboursAmount(Cube cube, CubeMap map){
        return neighbours(cube, map).size();
    }

    //MAIN METHODS

    //calculate the next iteration of the automata
    //and return the list of cubes to render on screen
    public CubeMap nextIterations(CubeMap map){

        ArrayList<Cube> nextIteration = map.toList();

        for(Iterator<Cube> iterator = nextIteration.iterator(); iterator.hasNext();){

            Cube cube = iterator.next();
            ArrayList<Cube> neighbours = neighbours(cube, map);

            countStatus(cube, neighbours);
            countColor(cube, neighbours);


        }

        map.clear();
        map.addAll(nextIteration);

        return map;

    }

    private void countStatus(Cube cube, ArrayList<Cube> neighbours){

        int neighboursAmount = neighbours.size();

        if(inKeepAlive(neighboursAmount) || inRevive(neighboursAmount)){


            if(cube.isAlive()){
                cube.plusIteration();
            }

            cube.setAlive(true);

        }else{

            cube.setAlive(false);
            cube.resetIterations();

        }

    }

    private void countColor(Cube cube, ArrayList<Cube> neighbours){

        if(!cube.isAlive()) return;

        String newColor;

        if(cube.getIterations() > 0){

            newColor = darkerColor(cube.getColor(), darkerColorPercent);

        }else{

            newColor = Settings.defaultCubeColor;

        }

        cube.setColor(newColor);

    }

    public ArrayList<Cube> neighbours(Cube cube, CubeMap map){

        ArrayList<Cube> result = new ArrayList<>();

        for (int x = cube.getCoords()[0]-1; x<= cube.getCoords()[0]+1; x++){
            for (int y = cube.getCoords()[1]-1; y<= cube.getCoords()[1]+1; y++){
                for (int z = cube.getCoords()[2]-1; z<= cube.getCoords()[2]+1; z++){

                    Cube returnCube = map.getCubeAt(new int []{x, y, z});

                    if(returnCube != null && returnCube.isAlive())
                        result.add(returnCube);

                }
            }
        }

        return result;

    }


    // UTILS

    private boolean inKeepAlive(int amount){

        for(int i = 0; i< keepAliveNeighboursNumber.length; i++){
            if(keepAliveNeighboursNumber[i] == amount){
                return true;
            }
        }

        return false;

    }


    private boolean inRevive(int amount){

        for(int i = 0; i< reviveNeighboursNumber.length; i++){
            if(reviveNeighboursNumber[i] == amount){
                return true;
            }
        }

        return false;

    }

    // percent : 0 - 1 where 0 is black, 1 - original color
    private String darkerColor(String hexColor, float percent){

        CellColor color = new CellColor(hexColor);

        color.RED = color.RED * percent < 0? 0: color.RED * percent;
        color.GREEN = color.GREEN * percent < 0? 0: color.GREEN * percent;
        color.BLUE = color.BLUE * percent < 0? 0: color.BLUE * percent;

        return String.format("#%02x%02x%02x", (int)(color.RED * 255), (int)(color.GREEN * 255), (int)(color.BLUE * 255));

    }

}
