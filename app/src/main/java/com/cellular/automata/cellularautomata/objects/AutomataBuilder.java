package com.cellular.automata.cellularautomata.objects;

import android.opengl.GLES20;
import android.util.Log;

import com.cellular.automata.cellularautomata.GRFX;
import com.cellular.automata.cellularautomata.GraphicsRenderer;
import com.cellular.automata.cellularautomata.data.CubeDataHolder;
import com.cellular.automata.cellularautomata.data.VertexArray;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CellularPoint;

import java.util.ArrayList;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDeleteBuffers;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.cellular.automata.cellularautomata.Constants.COLOR_COMPONENT_COUNT;
import static com.cellular.automata.cellularautomata.Constants.NORMAL_COMPONENT_COUNT;
import static com.cellular.automata.cellularautomata.Constants.POSITION_COMPONENT_COUNT;

public class AutomataBuilder {

    private String TAG = "AUTOMATA BUILDER";

    private VertexArray vertexPosArray;
    private VertexArray vertexColorArray;
    private VertexArray vertexNormalArray;

    private float[] vertexPositionData;
    private float[] vertexColorData;
    private float[] vertexNormalData;

    private int vertexDataOffset = 0;
    private int vertexColorDataOffset = 0;
    private int vertexNormalDataOffset = 0;

    private int vertexBufferPositionIdx = 0;
    private int vertexBufferColorIdx = 0;
    private int vertexBufferNormalIdx = 0;

    // Attribute locations
    private  int aPositionLocation;
    private  int aColorLocation;
    private  int aNormalLocation;

    private ArrayList<Cube> cellsList;

    public AutomataBuilder(){

    }

    //sets the cellList
    public void setModel(Model modelToLoad){

        this.cellsList = new ArrayList<>();
        float [] rawCoords = modelToLoad.getAutomataCoords();
        CellColor[] cellColors = modelToLoad.getAutomataColors();

        for(int i = 0; i< modelToLoad.getCellsNumber(); i++){

             cellsList.add(new Cube(new CellularPoint(rawCoords[i*3], rawCoords[i*3 + 1], rawCoords[i*3 + 2]), cellColors[i]));

        }

    }

    public void addNewCube(CellularPoint center, CellColor color){

        //check if this cube already exists
        if(cubeExists(center)) return;

        cellsList.add(new Cube(center, color));

        //rebuild figure
        build();
        bindAttributesData();

    }

    //here all the vertex data should be generated
    public void build(){

        if(howManyCells() <= 0){
            Log.d(TAG, "no cells to build");
            return;
        }

        vertexColorDataOffset = 0;
        vertexDataOffset = 0;
        vertexNormalDataOffset = 0;

        vertexPositionData = new float[CubeDataHolder.getInstance().sizeInVertex * POSITION_COMPONENT_COUNT * howManyCells()];
        vertexNormalData = new float[CubeDataHolder.getInstance().sizeInVertex * NORMAL_COMPONENT_COUNT * howManyCells()];
        vertexColorData = new float[(vertexPositionData.length / POSITION_COMPONENT_COUNT) * COLOR_COMPONENT_COUNT];

        for (Cube cell: cellsList){

            appendCell(cell);

        }

        vertexPosArray = new VertexArray(vertexPositionData);
        vertexColorArray = new VertexArray(vertexColorData);
        vertexNormalArray = new VertexArray(vertexNormalData);

        vertexPositionData = null;
        vertexNormalData = null;
        vertexColorData = null;

    }

    private void appendCell(Cube cube){

        for (float f: cube.getCubePositionData()){
            vertexPositionData[vertexDataOffset++] = f;
        }

        for (float f: cube.getCubeNormalData()){
            vertexNormalData[vertexNormalDataOffset++] = f;
        }

        for (float f: cube.getCubeColorData()){
            vertexColorData[vertexColorDataOffset++] = f;
        }

        cube.releaseCubeData();

    }

    //This method can be called only inside the GL context
    //binds data to the VBOs
    public void bindAttributesData(){

        glDeleteBuffers(3, new int[]{vertexBufferPositionIdx, vertexBufferColorIdx, vertexBufferNormalIdx}, 0);

        final int buffers[] = new int[3];
        glGenBuffers(3, buffers, 0);
        glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        vertexBufferPositionIdx = buffers[0];
        vertexBufferColorIdx = buffers[1];
        vertexBufferNormalIdx = buffers[2];

        vertexPosArray.bindBufferToVBO(vertexBufferPositionIdx);
        vertexColorArray.bindBufferToVBO(vertexBufferColorIdx);
        vertexNormalArray.bindBufferToVBO(vertexBufferNormalIdx);

    }

    public void draw(){

        GraphicsRenderer renderer = GRFX.renderer;

        if(howManyCells() <= 0){
            Log.d(TAG, "No cells to draw");
            return;
        }

        //draw figure
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferPositionIdx);
        glEnableVertexAttribArray(renderer.getShader().getPositionAttributeLocation());
        glVertexAttribPointer(renderer.getShader().getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, 0);


        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferColorIdx);
        glEnableVertexAttribArray(renderer.getShader().getColorAttributeLocation());
        glVertexAttribPointer(renderer.getShader().getColorAttributeLocation(), COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, 0);


        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferNormalIdx);
        glEnableVertexAttribArray(renderer.getShader().getNormalAttributeLocation());
        glVertexAttribPointer(renderer.getShader().getNormalAttributeLocation(), NORMAL_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, 0);


        glDrawArrays(GLES20.GL_TRIANGLES, 0, CubeDataHolder.getInstance().sizeInVertex * howManyCells());


    }

    private int howManyCells(){

        if(cellsList == null) return -1;
        return cellsList.size();

    }

    private boolean cubeExists(CellularPoint cubeCenter){
        for (Cube cube: cellsList){
            if (cube.center.equals(cubeCenter)){
                return true;
            }
        }
        return false;
    }

}
