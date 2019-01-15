package com.cellular.automata.cellularautomata.core;

import android.util.SparseArray;

import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.data.Cube;
import com.cellular.automata.cellularautomata.data.CubeMap;
import com.cellular.automata.cellularautomata.data.RenderCubeMap;
import com.cellular.automata.cellularautomata.objects.RenderCube;

import java.util.ArrayList;

public class Rule {

    private int [] keepAliveNeighboursNumber = {1};
    private int [] reviveNeighboursNumber = {1};

    //GETTERS
    public int getNeighboursAmount(Cube cube, CubeMap map){
        return neighbours(cube, map).size();
    }

    //MAIN METHODS

    //calculate the next iteration of the automata
    //and return the list of cubes to render on screen
    public ArrayList<Cube> nextIterations(CubeMap map){

        ArrayList<Cube> nextIteration = new ArrayList<>();
        for (Cube cube : map.toList()){

            int neighboursAmount = getNeighboursAmount(cube, map);
            if(inKeepAlive(neighboursAmount) || inRevive(neighboursAmount)){
                cube.setAlive(true);
                cube.setColor(Settings.defaultCubeColor);
            }else{
                cube.setAlive(false);
            }

        }

        return nextIteration;

    }

    public ArrayList<Cube> neighbours(Cube cube, CubeMap map){

        ArrayList<Cube> result = new ArrayList<>();

        if (map.numberAllAlive() == 0)
            return result;

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

}
