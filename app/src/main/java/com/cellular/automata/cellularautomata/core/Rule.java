package com.cellular.automata.cellularautomata.core;

import com.cellular.automata.cellularautomata.data.CubeMap;
import com.cellular.automata.cellularautomata.objects.Cube;

import java.util.ArrayList;

public class Rule {


    private int minAliveNeighboursValue = 2;
    private int maxAliveNeighboursValue = 3;

    //GETTERS
    public int getNeighboursAmount(Cube cube, CubeMap map){
        return neighbours(cube, map).size();
    }

    //MAIN METHODS

    //calculate the next iteration of the automata
    //and return the list of cubes to render on screen
    public ArrayList<Cube> nextIterations(CubeMap map){

        ArrayList<Cube> nextIteration = new ArrayList<>();
        for (Cube cube: map.getCubeList()){

            int neighboursAmount = getNeighboursAmount(cube, map);

        }

        return nextIteration;

    }

    public ArrayList<Cube> neighbours(Cube cube, CubeMap map){

        ArrayList<Cube> result = new ArrayList<Cube>();

        if (map.size() == 0)
            return result;

        int [] coords = new int[]{(int)cube.center.x, (int)cube.center.y, (int) cube.center.z};

        for (int x = coords[0]-1; x<= coords[0]+1; x++){
            for (int y = coords[1]-1; y<= coords[1]+1; y++){
                for (int z = coords[2]-1; z<= coords[2]+1; z++){

                    Cube returnedCube = map.getCubeAt(new int []{x, y, z});

                    if(returnedCube!= null)
                        result.add(returnedCube);

                }
            }
        }

        return result;

    }


}
