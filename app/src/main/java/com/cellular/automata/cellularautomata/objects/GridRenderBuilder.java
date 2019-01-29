package com.cellular.automata.cellularautomata.objects;

import android.opengl.GLES20;

import com.cellular.automata.cellularautomata.GRFX;
import com.cellular.automata.cellularautomata.GraphicsRenderer;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.data.VertexArray;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CubeCenter;

import java.util.ArrayList;

import static android.opengl.GLES10.GL_LINE_SMOOTH;
import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDeleteBuffers;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glVertexAttribPointer;

public class GridRenderBuilder {

    private int POSITION_COMPONENT_COUNT = 3;
    private int COLOR_COORDINATES_COMPONENT_COUNT = 4;
    private int STRIDE = 0;
    private float cubeSize = 1f;

    private int dimensionsNumber = 4;

    public int gridRadius;

    private CubeCenter gridCenter = new CubeCenter(Settings.gridOffsetX, Settings.gridHeight, Settings.gridOffsetZ);
    private int vertexNumber;

    private VertexArray gridVertexPosArray;
    private VertexArray vertexColorArray;

    private float[] vertexPositionData;
    private float[] vertexColorData;

    private int vertexBufferPositionIdx = 0;
    private int vertexBufferColorIdx = 0;

    private int vertexDataOffset = 0;
    private int vertexColorDataOffset = 0;


    private ArrayList<CubeCenter> tileCenters;
    private ArrayList<Line> grid;
    private Square square;

    // Getters

    public ArrayList<CubeCenter> getTileCenters(){return tileCenters; }



    // Main

    public GridRenderBuilder(int automataRadius) {

        this.gridRadius = automataRadius;
        this.square = new Square(gridCenter, automataRadius * 2 - 1);

    }

    public void updateGridSize(int size){

        gridRadius = size;

        buildGrid(gridCenter, gridRadius);
        buildTiles(gridCenter, gridRadius);
        bindAttributesData();

    }

    public void build(){

        buildGrid(gridCenter, gridRadius);
        square.build();
        bindAttributesData();

    }

    private void buildGrid(CubeCenter center, int gridSize){

        int linesInRow = gridSize * 2;

        vertexPositionData = new float[(linesInRow) * 4 * POSITION_COMPONENT_COUNT];
        vertexColorData = new float[(linesInRow) * 4 * COLOR_COORDINATES_COMPONENT_COUNT];

        resetOffsets();

        CellColor lineColor = new CellColor(Settings.gridColor);

        grid = new ArrayList<>();

        // Along X-axis
        CubeCenter defaultXLineStartPoint = new CubeCenter(center.x - gridSize + Settings.renderCubeSize / 2, center.y, center.z - gridSize + Settings.renderCubeSize/2);
        CubeCenter defaultXLineEndPoint = new CubeCenter(center.x - gridSize + Settings.renderCubeSize / 2, center.y , center.z + gridSize - Settings.renderCubeSize/2);

        // Along Z-axis
        CubeCenter defaultZLineStartPoint = new CubeCenter(center.x - gridSize + Settings.renderCubeSize / 2, center.y , center.z - gridSize + Settings.renderCubeSize/2);
        CubeCenter defaultZLineEndPoint = new CubeCenter(center.x + gridSize - Settings.renderCubeSize / 2, center.y , center.z - gridSize + Settings.renderCubeSize/2);

        // Create lines along X-axis
        for (int i = 0; i< linesInRow; i++){

            appendLine(new Line(defaultXLineStartPoint, defaultXLineEndPoint, lineColor));

            defaultXLineStartPoint.translateX(1f);
            defaultXLineEndPoint.translateX(1f);

        }

        // Create lines along Z-axis
        for (int i = 0; i< linesInRow; i++){

            appendLine(new Line(defaultZLineStartPoint, defaultZLineEndPoint, lineColor));

            defaultZLineStartPoint.translateZ(1f);
            defaultZLineEndPoint.translateZ(1f);

        }

        vertexNumber = vertexPositionData.length / POSITION_COMPONENT_COUNT;

        gridVertexPosArray = new VertexArray(vertexPositionData);
        vertexColorArray = new VertexArray(vertexColorData);

        vertexColorData = null;
        vertexPositionData = null;


    }

    private void buildTiles(CubeCenter gridCenter, float gridRadius) {

        tileCenters = new ArrayList<>();

        // in the corner
        CubeCenter defaultTilePosition = new CubeCenter( gridCenter.x - gridRadius - 1, gridCenter.y - Settings.renderCubeSize / 2, gridCenter.z - gridRadius - 1);

        for (int i = 0; i < gridRadius * 2 - 1; i++){

            for (int j = 0; j < gridRadius * 2 - 1; j++){

                CubeCenter center = defaultTilePosition.clone();
                center.translateX(cubeSize * j);

            }

            defaultTilePosition.translateZ(cubeSize);

        }


    }

    public void bindAttributesData(){

        if(gridVertexPosArray == null || vertexColorArray == null) return;

        glDeleteBuffers(2, new int[]{vertexBufferPositionIdx, vertexBufferColorIdx}, 0);

        final int buffers[] = new int[2];
        glGenBuffers(2, buffers, 0);

        vertexBufferPositionIdx = buffers[0];
        vertexBufferColorIdx = buffers[1];

        gridVertexPosArray.bindBufferToVBO(vertexBufferPositionIdx);
        vertexColorArray.bindBufferToVBO(vertexBufferColorIdx);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        gridVertexPosArray = null;
        vertexColorArray = null;

        square.bindAttributesData();

    }

    public void draw(){

        square.draw();

        GraphicsRenderer renderer = GRFX.renderer;


        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferPositionIdx);
        glEnableVertexAttribArray(renderer.getGridShader().getPositionAttributeLocation());
        glVertexAttribPointer(renderer.getGridShader().getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, 0);


        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferColorIdx);
        glEnableVertexAttribArray(renderer.getGridShader().getColorAttributeLocation());
        glVertexAttribPointer(renderer.getGridShader().getColorAttributeLocation(), COLOR_COORDINATES_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, 0);

        glEnable(GL_LINE_SMOOTH);
        glDrawArrays(GL_LINES, 0, vertexNumber);


    }

    private void appendLine(Line line){

        grid.add(line);

        for (float f: line.linePositionData){
            vertexPositionData[vertexDataOffset++] = f;
        }

        for (float f: line.lineColorData) {
            vertexColorData[vertexColorDataOffset++] = f;
        }

    }

    private void resetOffsets(){

        vertexColorDataOffset = 0;
        vertexDataOffset = 0;

    }

    private class Line{

        private String TAG = "Line";

        int NUMBER_OF_POINTS = 2;

        int POSITION_ARRAY_SIZE = NUMBER_OF_POINTS * POSITION_COMPONENT_COUNT;
        int COLOR_ARRAY_SIZE = NUMBER_OF_POINTS * COLOR_COORDINATES_COMPONENT_COUNT;

        public float[] linePositionData;
        public float[] lineColorData;

        private CubeCenter startPoint;
        private CubeCenter endPoint;

        public Line(CubeCenter start, CubeCenter end, CellColor color){
            this.startPoint = start;
            this.endPoint = end;


            linePositionData = new float[POSITION_ARRAY_SIZE];

            linePositionData[0] = start.x;
            linePositionData[1] = start.y;
            linePositionData[2] = start.z;

            linePositionData[3] = end.x;
            linePositionData[4] = end.y;
            linePositionData[5] = end.z;

            lineColorData = new float[COLOR_ARRAY_SIZE];

            lineColorData[0] = color.RED;
            lineColorData[1] = color.GREEN;
            lineColorData[2] = color.BLUE;
            lineColorData[3] = 1f;

            lineColorData[4] = color.RED;
            lineColorData[5] = color.GREEN;
            lineColorData[6] = color.BLUE;
            lineColorData[7] = 1f;

            checkIfEverythingAllRight();

        }

        private void checkIfEverythingAllRight(){

            if (
                    lineColorData[0] == 0 ||
                            lineColorData[COLOR_ARRAY_SIZE-1] == 0 ||
                            linePositionData[0] == 0 ||
                            linePositionData[POSITION_ARRAY_SIZE-1] == 0
                    ){

                //Log.w(TAG, "position or color data is missing");

            }

        }

    }



}

