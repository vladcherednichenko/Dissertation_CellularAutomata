package com.cellular.automata.cellularautomata.objects;

import android.opengl.GLES20;

import com.cellular.automata.cellularautomata.LINKER;
import com.cellular.automata.cellularautomata.GraphicsRenderer;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.data.VertexArray;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CubeCenter;

import java.util.ArrayList;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDeleteBuffers;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glVertexAttribPointer;

public class Square {

    private int POSITION_COMPONENT_COUNT = 3;
    private int COLOR_COORDINATES_COMPONENT_COUNT = 4;

    private int POINTS_IN_SQUARE = 6;

    public int size;

    private CubeCenter squareCenter;

    private VertexArray vertexPosArray;
    private VertexArray vertexColorArray;

    private float[] vertexPositionData;
    private float[] vertexColorData;

    private int vertexBufferPositionIdx = 0;
    private int vertexBufferColorIdx = 0;


    // Main

    public Square(CubeCenter center, int size) {

        this.squareCenter = center;
        this.size = size;



    }

    public void build(){

        buildSquare(squareCenter, size);

    }


    private void buildSquare(CubeCenter rectangleCenter, int size){

        vertexPositionData = new float[POINTS_IN_SQUARE * POSITION_COMPONENT_COUNT ];
        vertexColorData = new float[POINTS_IN_SQUARE * COLOR_COORDINATES_COMPONENT_COUNT];

        CubeCenter pointA = rectangleCenter.clone().translateX_(-(float)size / 2).translateZ_(-(float)size / 2);
        CubeCenter pointB = rectangleCenter.clone().translateX_((float)size / 2).translateZ_(-(float)size / 2);
        CubeCenter pointC = rectangleCenter.clone().translateX_((float)size / 2).translateZ_((float)size / 2);
        CubeCenter pointD = rectangleCenter.clone().translateX_(-(float)size / 2).translateZ_((float)size / 2);


        ArrayList<CubeCenter> points = new ArrayList<>();
        points.add(rectangleCenter.clone());
        points.add(pointA);
        points.add(pointB);
        points.add(pointC);
        points.add(pointD);
        points.add(pointA.clone());


        CellColor color = new CellColor(Settings.gridBGColor);

        for (int i = 0; i< POINTS_IN_SQUARE; i++){

            CubeCenter point = points.get(i);
            vertexPositionData[POSITION_COMPONENT_COUNT * i] = point.x;
            vertexPositionData[POSITION_COMPONENT_COUNT * i + 1] = point.y;
            vertexPositionData[POSITION_COMPONENT_COUNT * i + 2] = point.z;

            vertexColorData[COLOR_COORDINATES_COMPONENT_COUNT * i] = color.RED;
            vertexColorData[COLOR_COORDINATES_COMPONENT_COUNT * i + 1] = color.GREEN;
            vertexColorData[COLOR_COORDINATES_COMPONENT_COUNT * i + 2] = color.BLUE;
            vertexColorData[COLOR_COORDINATES_COMPONENT_COUNT * i + 3] = 0.5f;

        }


        vertexPosArray = new VertexArray(vertexPositionData);
        vertexColorArray = new VertexArray(vertexColorData);

        vertexColorData = null;
        vertexPositionData = null;

    }


    public void bindAttributesData(){

        if(vertexPosArray == null || vertexColorArray == null) return;

        glDeleteBuffers(2, new int[]{vertexBufferPositionIdx, vertexBufferColorIdx}, 0);

        final int buffers[] = new int[2];
        glGenBuffers(2, buffers, 0);

        vertexBufferPositionIdx = buffers[0];
        vertexBufferColorIdx = buffers[1];

        vertexPosArray.bindBufferToVBO(vertexBufferPositionIdx);
        vertexColorArray.bindBufferToVBO(vertexBufferColorIdx);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        vertexPosArray = null;
        vertexColorArray = null;


    }

    public void draw(){

        GraphicsRenderer renderer = LINKER.renderer;

        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferPositionIdx);
        glEnableVertexAttribArray(renderer.getGridShader().getPositionAttributeLocation());
        glVertexAttribPointer(renderer.getGridShader().getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, 0);


        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferColorIdx);
        glEnableVertexAttribArray(renderer.getGridShader().getColorAttributeLocation());
        glVertexAttribPointer(renderer.getGridShader().getColorAttributeLocation(), COLOR_COORDINATES_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, 0);

        glDrawArrays(GL_TRIANGLE_FAN, 0, POINTS_IN_SQUARE);


    }



}
