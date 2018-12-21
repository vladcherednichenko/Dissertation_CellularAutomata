package com.cellular.automata.cellularautomata.objects;

import android.opengl.GLES20;
import android.util.Log;

import com.cellular.automata.cellularautomata.GRFX;
import com.cellular.automata.cellularautomata.GraphicsRenderer;
import com.cellular.automata.cellularautomata.animation.Animator;
import com.cellular.automata.cellularautomata.core.Rule;
import com.cellular.automata.cellularautomata.data.CubeDataHolder;
import com.cellular.automata.cellularautomata.data.CubeMap;
import com.cellular.automata.cellularautomata.data.VertexArray;
import com.cellular.automata.cellularautomata.interfaces.CellSelectListener;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CellPoint;
import com.cellular.automata.cellularautomata.utils.ObjectSelectHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

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

    //the automata radius (from center to the bound)
    private int automataRadius = 30;
    private CubeMap map;

    private CellSelectListener selectListener;
    private boolean isTouched = false;
    private boolean isStretched = false;
    private boolean viewMode = false;
    private boolean generating = false;
    private ObjectSelectHelper.TouchResult touchResult;

    private Rule rule;
    private OnTouchListener onTouchListener;
    private Animator animator;

    //temporary
    private Random random = new Random();
    private long time;
    private long timePast;
    private float delay = 100;

    public interface OnTouchListener{

        void onTouch();

    }

    public AutomataBuilder(){

        map = new CubeMap(automataRadius);
        animator = new Animator(map.size(), map.height(), map.getCubeList());

    }

    //GETTERS

    public ObjectSelectHelper.TouchResult getTouchResult() {
        return touchResult;
    }

    public ArrayList<CellPoint> getCellCentersList(){

        return map.getCubeCenters();

    }

    public int getAutomataRadius(){

        return automataRadius;

    }

    public CubeMap getMap() {

        return map;

    }

    //SETTERS

    //this method can be called from the Application Manager
    public void setSelectListener(CellSelectListener listener){

        if(listener != null) this.selectListener = listener;

    }

    public void setRule(Rule rule){

        this.rule = rule;

    }

    public void setOnTouchListener(OnTouchListener listener){

        this.onTouchListener = listener;

    }

    //sets the cellList
    public void setModel(Model modelToLoad){

        map.clear();

        float [] rawCoords = modelToLoad.getAutomataCoords();
        CellColor[] cellColors = modelToLoad.getAutomataColors();

        for(int i = 0; i< modelToLoad.getCellsNumber(); i++){

            CellPoint cellCenter = new CellPoint(rawCoords[i*3], rawCoords[i*3 + 1], rawCoords[i*3 + 2]);
            map.add(new Cube(cellCenter, cellColors[i]));

        }

    }

    //MAIN METHODS
    public boolean isTouched(){

        if(isTouched){
            isTouched = false;
            return true;
        }
        return false;
    }

    public void handleTouch(ObjectSelectHelper.TouchResult touchResult){

        this.touchResult = touchResult;
        isTouched = true;
        if(onTouchListener != null) onTouchListener.onTouch();

    }

    public void stretch(){

        if(isStretched) return;

        animator = new Animator(map.size(), map.height(), map.sort());
        isStretched = true;
        viewMode = true;

    }

    public void squeeze(){

        if(!isStretched) return;
        isStretched = false;

    }

    //general function, is called first when want to add a cubed
    public void addNewCube(CellPoint center, CellColor color){

        map.add(new Cube(center, color));

        //rebuild figure
        build();
        bindAttributesData();

    }

    //general function is called first when we want to paint the cube
    public void paintCube(CellPoint center, CellColor color){

        Cube cubeToRecolor = map.getCubeByCenter(center);
        if(cubeToRecolor == null) return;

        cubeToRecolor.paintCube(color);
        build();
        bindAttributesData();

    }

    //general function is called first when we want to delete the cube
    public void deleteCube(CellPoint center){



    }

    public void start(){

        generating = true;
        delay = 100;

    }

    public void pause(){

        generating = false;

    }

    public void speedUp(){

        generating = true;
        delay = delay * 0.5f;

    }

    public void execute(){

        if(generating){

            if(map.size() >= automataRadius*2 * automataRadius*2 * automataRadius*2 -1) return;

            //generating random stuff
            timePast = System.currentTimeMillis() - time;
            if(timePast > delay){
                time = System.currentTimeMillis();

                addNewCube(generateCellPoint(),  new CellColor(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                //Log.d("Timer", "half a second");

                GRFX.activityListener.loxTextTop("cubes: " + String.valueOf(map.size()));
            }

        }

    }

    CellPoint generateCellPoint(){

        CellPoint point =  new CellPoint(random.nextInt(20)-9,random.nextInt(20)-9, random.nextInt(20)-9);
        if(cubeExists(point)){
            generateCellPoint();
        }else{
            return point;
        }

        return point;

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

        for (Cube cube : map.getCubeList()){

            appendCell(cube);

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




        if(viewMode){

            if(isStretched){

                animator.drawOpenedFigure(renderer.getShader());

            }else{

                animator.drawClosedFigure(renderer.getShader());
                if(!animator.animationIsRunning())
                    viewMode = false;

            }

        }else{

            float[] resetScatterVector = {0.0f, 0.0f, 0.0f};
            renderer.getShader().setScatter(resetScatterVector);
            glDrawArrays(GLES20.GL_TRIANGLES, 0, CubeDataHolder.getInstance().sizeInVertex * howManyCells());

        }


    }

    //additional stuff

    private int howManyCells(){

        return map.size();

    }

    private boolean cubeExists(CellPoint cubeCenter){

        return map.cubeExists(cubeCenter);

    }

}
