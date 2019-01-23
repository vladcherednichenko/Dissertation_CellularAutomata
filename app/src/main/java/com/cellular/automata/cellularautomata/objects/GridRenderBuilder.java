package com.cellular.automata.cellularautomata.objects;

import android.opengl.GLES20;

import com.cellular.automata.cellularautomata.GRFX;
import com.cellular.automata.cellularautomata.GraphicsRenderer;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.data.VertexArray;
import com.cellular.automata.cellularautomata.shaders.GridShader;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CubeCenter;

import java.util.ArrayList;

import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDeleteBuffers;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glVertexAttribPointer;

public class GridRenderBuilder {

    private int POSITION_COMPONENT_COUNT = 3;
    private int COLOR_COORDINATES_COMPONENT_COUNT = 4;
    private int STRIDE = 0;
    private float cubeSize = 1f;

    private int dimensionsNumber = 4;
    private float[] gridDimensions = new float[dimensionsNumber];

    private final int gridMinX = 0;
    private final int gridMaxX = 1;

    private final int gridMinZ = 2;
    private final int gridMaxZ = 3;

    public int maxGridSize = Settings.unlimitedGrid ? Settings.unlimitedGridSize : Settings.maximumGridSize;
    public int minGridSize = Settings.minimumGridSize;
    public int gridSize = minGridSize;

    private CubeCenter gridCenter = new CubeCenter(Settings.gridOffsetX, Settings.gridHeight, Settings.gridOffsetZ);
    private int vertexNumber;

    private VertexArray vertexPosArray;
    private VertexArray vertexColorArray;

    private float[] vertexPositionData;
    private float[] vertexColorData;

    private int vertexBufferPositionIdx = 0;
    private int vertexBufferColorIdx = 0;

    private int vertexDataOffset = 0;
    private int vertexColorDataOffset = 0;


    private ArrayList<CubeCenter> tileCenters;
    private ArrayList<Line> grid;


    public ArrayList<CubeCenter> getTileCenters(){return tileCenters; }
    public boolean isGridMinimumSize(){return gridSize == minGridSize;}
    public boolean isGridMaximumSize(){return gridSize == maxGridSize;}


    public GridRenderBuilder(){

        gridDimensions[gridMinX] = -minGridSize/2;
        gridDimensions[gridMaxX] = minGridSize/2;
        gridDimensions[gridMinZ] = -minGridSize/2;
        gridDimensions[gridMaxZ] = minGridSize/2;

    }

    public GridRenderBuilder(int automataRadius){

        this.minGridSize = automataRadius;
        gridDimensions[gridMinX] = -minGridSize/2;
        gridDimensions[gridMaxX] = minGridSize/2;
        gridDimensions[gridMinZ] = -minGridSize/2;
        gridDimensions[gridMaxZ] = minGridSize/2;
    }

    public float getMaxGridXYSize(){
        return Math.max(
                gridDimensions[gridMaxX] - gridDimensions[gridMinX],
                gridDimensions[gridMaxZ] - gridDimensions[gridMinZ]);
    }

    public void updateGrid(float minX, float maxX, float minZ, float maxZ){

        if(minX == gridDimensions[gridMinX] || maxX == gridDimensions[gridMaxX] || minZ == gridDimensions[gridMinZ] || maxZ == gridDimensions[gridMaxZ]){

            if(gridSize >=maxGridSize){

                gridSize = maxGridSize;


            }else{

                gridSize+=2;
                buildGrid(gridSize, gridCenter);
                buildTiles(gridSize, gridCenter);
                bindAttributesData();

            }

        }

    }


    public void setGridSize(int size){

        if(size > maxGridSize) size = maxGridSize;
        if(size < minGridSize) size = minGridSize;

        gridSize = size;

        buildGrid(gridSize, gridCenter);
        buildTiles(gridSize, gridCenter);
        bindAttributesData();

    }

    public void setGridSize(float figureMinX, float figureMaxX, float figureMinZ, float figureMaxZ){

        gridDimensions[gridMinX] = figureMinX <= (-1 * minGridSize /2)? figureMinX - 1: - minGridSize /2;
        gridDimensions[gridMaxX] = figureMaxX >= minGridSize /2?  figureMaxX + 1: minGridSize /2 ;

        gridDimensions[gridMinZ] = figureMinZ <= (-1* minGridSize /2)? figureMinZ - 1 : - minGridSize /2;
        gridDimensions[gridMaxZ] = figureMaxZ >= minGridSize /2? figureMaxZ + 1 : minGridSize /2;


        buildGrid(
                gridCenter,
                Math.round(gridDimensions[gridMinX]),
                Math.round(gridDimensions[gridMaxX]),
                Math.round(gridDimensions[gridMinZ]),
                Math.round(gridDimensions[gridMaxZ])
        );

        buildTiles(
                gridCenter,
                Math.round(gridDimensions[gridMinX]),
                Math.round(gridDimensions[gridMaxX]),
                Math.round(gridDimensions[gridMinZ]),
                Math.round(gridDimensions[gridMaxZ])
        );

        bindAttributesData();


    }


    public void build(){

        buildGrid(
                gridCenter,
                Math.round(gridDimensions[gridMinX]),
                Math.round(gridDimensions[gridMaxX]),
                Math.round(gridDimensions[gridMinZ]),
                Math.round(gridDimensions[gridMaxZ])
        );

        buildTiles(
                gridCenter,
                Math.round(gridDimensions[gridMinX]),
                Math.round(gridDimensions[gridMaxX]),
                Math.round(gridDimensions[gridMinZ]),
                Math.round(gridDimensions[gridMaxZ])
        );

    }


    private void buildGrid(CubeCenter center, int minX, int maxX, int minZ, int maxZ){

        int size = (maxX - minX) + (maxZ - minZ) + 2;

        vertexPositionData = new float[(size+1) * 4 * POSITION_COMPONENT_COUNT];
        vertexColorData = new float[(size+1) * 4 * COLOR_COORDINATES_COMPONENT_COUNT];

        resetOffsets();

        CellColor color = new CellColor(Settings.gridColor);
        CellColor highLightColor = new CellColor(Settings.highlightedGridColor);

        CubeCenter defaultVerticalLineStartPoint = new CubeCenter(center.x + minX, center.y, center.z + minZ);
        CubeCenter defaultVerticalLineEndPoint = new CubeCenter(center.x + minX, center.y, center.z + maxZ);

        CubeCenter defaultHorizontalLineStartPoint = new CubeCenter(center.x + minX, center.y, center.z + minZ);
        CubeCenter defaultHorizontalLineEndPoint = new CubeCenter(center.x + maxX, center.y, center.z + minZ);

        grid = new ArrayList<>();
        int linesInOneRow = (size+1);

        for(int i = minX; i<= maxX; i++){

            //create vertical lines
            if(Settings.highLightedCentralLines && i == 0){
                appendLine(new Line(defaultVerticalLineStartPoint, defaultVerticalLineEndPoint, highLightColor));
            }else{
                appendLine(new Line(defaultVerticalLineStartPoint, defaultVerticalLineEndPoint, color));
            }
            defaultVerticalLineStartPoint.translateX(1f);
            defaultVerticalLineEndPoint.translateX(1f);

        }

        for(int i = minZ; i<= maxZ; i++){

            //create horizontal lines
            if(Settings.highLightedCentralLines && i == 0){
                appendLine(new Line(defaultHorizontalLineStartPoint, defaultHorizontalLineEndPoint, highLightColor));
            }else{
                appendLine(new Line(defaultHorizontalLineStartPoint, defaultHorizontalLineEndPoint, color));
            }
            defaultHorizontalLineStartPoint.translateZ(1f);
            defaultHorizontalLineEndPoint.translateZ(1f);

        }

        vertexNumber = vertexPositionData.length / POSITION_COMPONENT_COUNT;

        vertexPosArray = new VertexArray(vertexPositionData);
        vertexColorArray = new VertexArray(vertexColorData);

        vertexColorData = null;
        vertexPositionData = null;

    }

    private void buildGrid(int size, CubeCenter center){

        vertexPositionData = new float[(size+1) * 4 * POSITION_COMPONENT_COUNT];
        vertexColorData = new float[(size+1) * 4 * COLOR_COORDINATES_COMPONENT_COUNT];

        resetOffsets();

        CellColor color = new CellColor(Settings.gridColor);

        CubeCenter defaultHorizontalStartPoint = new CubeCenter(center.x - size/2, center.y, center.z - size / 2f);
        CubeCenter defaultHorizontalEndPoint = new CubeCenter(center.x - size/2, center.y, center.z + size / 2f);

        CubeCenter defaultVerticalStartPoint = new CubeCenter(center.x -size / 2f, center.y, center.z - size/2);
        CubeCenter defaultVerticalEndPoint = new CubeCenter(center.x + size / 2f, center.y, center.z - size/2);



        grid = new ArrayList<>();
        int linesInOneRow = (size+1);


        for (int i = 0; i< linesInOneRow; i++){

            //create horizontal lines
            appendLine(new Line(defaultHorizontalStartPoint.clone(), defaultHorizontalEndPoint.clone(), color));
            defaultHorizontalEndPoint.translateX(1f);
            defaultHorizontalStartPoint.translateX(1f);

            //create vertical lines
            appendLine(new Line(defaultVerticalStartPoint, defaultVerticalEndPoint, color));
            defaultVerticalEndPoint.translateZ(1f);
            defaultVerticalStartPoint.translateZ(1f);
        }


        vertexNumber = vertexPositionData.length / POSITION_COMPONENT_COUNT;

        vertexPosArray = new VertexArray(vertexPositionData);
        vertexColorArray = new VertexArray(vertexColorData);

        vertexColorData = null;
        vertexPositionData = null;

    }

    private void buildTiles(CubeCenter gridCenter, int minX, int maxX, int minZ, int maxZ){

        tileCenters = new ArrayList<>();

        CubeCenter defaultTilePosition = new CubeCenter( gridCenter.x + minX + cubeSize/2, gridCenter.y - cubeSize / 2, gridCenter.z + minZ + cubeSize/2);


        for (int i = 0; i<= maxZ - minZ; i++){
            for (int j = 0; j<= maxX - minX; j++){

                CubeCenter center = defaultTilePosition.clone();
                center.translateX(cubeSize * j);

                tileCenters.add(center);


            }

            defaultTilePosition.translateZ(cubeSize);

        }

    }

    private void buildTiles(float gridSize, CubeCenter gridCenter){

        tileCenters = new ArrayList<>();

        CubeCenter defaultTilePosition = new CubeCenter( gridCenter.x - gridSize/2 + cubeSize/2, gridCenter.y - cubeSize / 2, gridCenter.z-gridSize/2 + cubeSize/2);

        gridDimensions[gridMinX] = defaultTilePosition.x;
        gridDimensions[gridMinZ] = defaultTilePosition.z;

        for (int i = 0; i< gridSize; i++){


            for (int j = 0; j< gridSize; j++){

                CubeCenter center = defaultTilePosition.clone();
                center.translateX(cubeSize * j);

                tileCenters.add(center);

                gridDimensions[gridMaxX] = center.x;
                gridDimensions[gridMaxZ] = center.z;

            }

            defaultTilePosition.translateZ(cubeSize);

        }


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

        GraphicsRenderer renderer = GRFX.renderer;


        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferPositionIdx);
        glEnableVertexAttribArray(renderer.getGridShader().getPositionAttributeLocation());
        glVertexAttribPointer(renderer.getGridShader().getPositionAttributeLocation(), POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, 0);


        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferColorIdx);
        glEnableVertexAttribArray(renderer.getGridShader().getColorAttributeLocation());
        glVertexAttribPointer(renderer.getGridShader().getColorAttributeLocation(), COLOR_COORDINATES_COMPONENT_COUNT, GLES20.GL_FLOAT, false, 0, 0);

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

