package com.cellular.automata.cellularautomata.objects;

import android.support.annotation.NonNull;

import com.cellular.automata.cellularautomata.data.CubeDataHolder;
import com.cellular.automata.cellularautomata.utils.PixioColor;
import com.cellular.automata.cellularautomata.utils.PixioPoint;

public class Cube implements Comparable<Cube> {

    private static final String TAG = "Cube";

    private float cubeSize = 1;
    private int POSITION_COMPONENT_COUNT = 3;
    private int COLOR_COORDINATES_COMPONENT_COUNT = 4;
    private int STRIDE = 0;

    public PixioPoint center;
    public PixioColor color;

    public float[] cubePositionData;
    public float[] cubeColorData;
    public float[] cubeNormalData;


    public Cube(PixioPoint center, PixioColor color){

        this.center = center;
        this.color = color;

        createCubeData();

    }

    public Cube(PixioPoint center, PixioColor color, boolean autoCreateVertexData){

        this.center = center;
        this.color = color;

        if(autoCreateVertexData){
            createCubeData();
        }


    }

    public void createCubeData(){

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

    }

    public void releaseCubeData(){

        cubePositionData = null;
        cubeNormalData = null;
        cubeColorData = null;

    }


    public void translateCube(PixioPoint vector){
        for (int i = 0; i< cubePositionData.length; i++){

            switch(i % POSITION_COMPONENT_COUNT) {
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
    public int compareTo(@NonNull Cube cube) {
        float compareOrder = cube.center.y;

        return Math.round(this.center.y - compareOrder);
    }
}
