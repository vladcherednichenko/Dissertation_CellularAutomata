package com.cellular.automata.cellularautomata.core;

import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.data.Cube;
import com.cellular.automata.cellularautomata.data.CubeMap;
import com.cellular.automata.cellularautomata.data.CellColor;
import com.cellular.automata.cellularautomata.utils.ArrayHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class Rule {

    public static final String TAG = "Rule";
    public static final String ruleDelimeter = "/";
    public static final String numbersDelimeter = ",";

    public int [] keepAliveNeighboursNumber = {1};
    public int [] reviveNeighboursNumber = {1};
    private float darkerColorPercent = 0.5f ;

    // Getters
    public int getNeighboursAmount(Cube cube, CubeMap map){
        return neighbours(cube, map).size();
    }

    // Setters
    public void setKeepAliveNeighboursNumber (int [] i){

        this.keepAliveNeighboursNumber = i;

    }

    public void setReviveNeighboursNumber (int [] i){

        this.reviveNeighboursNumber = i;

    }

    public void setDarkerColorPercent (float percent){

        this.darkerColorPercent = percent;

    }

    public Rule(){}
    public Rule(int [] keepAliveNeighboursNumber, int [] reviveNeighboursNumber){

        if(
                keepAliveNeighboursNumber != null &&
                keepAliveNeighboursNumber.length != 0 &&
                reviveNeighboursNumber!= null &&
                reviveNeighboursNumber.length != 0){

            this.reviveNeighboursNumber = reviveNeighboursNumber;
            this.keepAliveNeighboursNumber = keepAliveNeighboursNumber;

        }

    }

    // Main methods

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

        String newColor = Settings.defaultCubeColor;

        if(Settings.enableColorDarkening){

            if(cube.getIterations() > 0){

                newColor = darkerColor(cube.getColor(), darkerColorPercent);

            }

        }

        if(Settings.enableColorInheritance){

            // TODO

        }


        cube.setColor(newColor);

    }

    public ArrayList<Cube> neighbours(Cube cube, CubeMap map){

        ArrayList<Cube> result = new ArrayList<>();

        for (int x = cube.getCoords()[0] - 1; x <= cube.getCoords()[0]+1; x++){
            for (int y = cube.getCoords()[1] - 1; y <= cube.getCoords()[1]+1; y++){
                for (int z = cube.getCoords()[2] - 1; z <= cube.getCoords()[2]+1; z++){

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

        for(int i = 0; i < keepAliveNeighboursNumber.length; i++) {

            if(keepAliveNeighboursNumber[i] == amount)
            {
                return true;
            }
        }

        return false;

    }


    private boolean inRevive(int amount){

        for(int i = 0; i < reviveNeighboursNumber.length; i++) {
            if(reviveNeighboursNumber[i] == amount) {
                return true;
            }
        }

        return false;

    }

    // percent : 0 - 1 where 0 is black, 1 - original color
    private String darkerColor(String hexColor, float percent) {

        CellColor color = new CellColor(hexColor);

        color.RED = color.RED * percent < 0? 0: color.RED * percent;
        color.GREEN = color.GREEN * percent < 0? 0: color.GREEN * percent;
        color.BLUE = color.BLUE * percent < 0? 0: color.BLUE * percent;

        return String.format("#%02x%02x%02x", (int)(color.RED * 255), (int)(color.GREEN * 255), (int)(color.BLUE * 255));

    }

    public static Rule fromString(String stringRule){

        if(!stringRule.contains(ruleDelimeter)) return null;

        Rule rule = new Rule();

        int [] intKeepAliveNumber = ArrayHelper.stringToIntArray(stringRule.split(ruleDelimeter)[0], numbersDelimeter);
        int [] intReviveAliveNumber = ArrayHelper.stringToIntArray(stringRule.split(ruleDelimeter)[1], numbersDelimeter);

        rule.setKeepAliveNeighboursNumber(intKeepAliveNumber);
        rule.setReviveNeighboursNumber(intReviveAliveNumber);

        return rule;

    }

    public String toString(){

        return ArrayHelper.intArrayToString(keepAliveNeighboursNumber, numbersDelimeter) +
                ruleDelimeter +
                ArrayHelper.intArrayToString(reviveNeighboursNumber, numbersDelimeter);

    }

}
