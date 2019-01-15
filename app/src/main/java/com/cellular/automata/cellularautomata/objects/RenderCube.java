package com.cellular.automata.cellularautomata.objects;

import android.support.annotation.NonNull;

import com.cellular.automata.cellularautomata.Constants;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.data.CubeDataHolder;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CubeCenter;

public class RenderCube implements Comparable<RenderCube> {

    private static final String TAG = "RenderCube";

    private float cubeSize = 1;
    private int STRIDE = 0;

    public CubeCenter center;
    public CellColor color;

    private float[] cubePositionData;
    private float[] cubeColorData;
    private float[] cubeNormalData;

    public float[] getCubePositionData() {

        if(!cubeDataCreated){

            createCubeData();

        }

        return cubePositionData;

    }

    public float[] getCubeColorData() {

        if(!cubeDataCreated){

            createCubeData();

        }

        return cubeColorData;
    }

    public float[] getCubeNormalData() {

        if(!cubeDataCreated){

            createCubeData();

        }

        return cubeNormalData;
    }

    private boolean cubeDataCreated = false;

    public RenderCube(CubeCenter center, CellColor color){

        this.center = center;
        this.color = color;

        if(Settings.generateCellsDataAfterCreation){
            createCubeData();
        }

    }

    public RenderCube(CubeCenter center, CellColor color, boolean shouldGenerateData){

        this.center = center;
        this.color = color;

        if(shouldGenerateData){
            createCubeData();
        }

    }


    private void createCubeData(){

        if(cubeDataCreated) return;

        cubePositionData = CubeDataHolder.getInstance().getVertices().clone();
        cubeNormalData = CubeDataHolder.getInstance().getNormals();
        cubeColorData = new float[cubeNormalData.length + cubeNormalData.length/3];
        for (int i = 0; i< cubeColorData.length; i++){
            switch(i%4){
                case 0:{
                    cubeColorData[i] = color.RED;
                    break;
                }
                case 1:{
                    cubeColorData[i] = color.GREEN;
                    break;
                }
                case 2:{
                    cubeColorData[i] = color.BLUE;
                    break;
                }
                case 3:{
                    cubeColorData[i] = 1f;
                    break;
                }
            }
        }


        translateCube(center);

        cubeDataCreated = true;

    }

    public void releaseCubeData(){

        if(!cubeDataCreated) return;

        cubePositionData = null;
        cubeNormalData = null;
        cubeColorData = null;

        cubeDataCreated = false;

    }

    public void paintCube(CellColor color){

        if(color == null) return;
        this.color = color;
        createCubeData();

    }

    public void translateCube(CubeCenter vector){
        for (int i = 0; i< cubePositionData.length; i++){

            switch(i % Constants.POSITION_COMPONENT_COUNT) {
                case 0: {
                    cubePositionData[i] += vector.x;
                    break;
                }
                case 1: {
                    cubePositionData[i] += vector.y;
                    break;
                }
                case 2: {
                    cubePositionData[i] += vector.z;
                    break;
                }
            }

        }
    }

    @Override
    public int compareTo(@NonNull RenderCube renderCube) {
        float compareOrder = renderCube.center.y;

        return Math.round(this.center.y - compareOrder);
    }
}
