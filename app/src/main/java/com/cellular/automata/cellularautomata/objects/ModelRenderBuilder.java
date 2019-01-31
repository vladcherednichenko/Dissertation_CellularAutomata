package com.cellular.automata.cellularautomata.objects;

import android.opengl.GLES20;
import android.util.Log;

import com.cellular.automata.cellularautomata.GRFX;
import com.cellular.automata.cellularautomata.GraphicsRenderer;
import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.animation.Animator;
import com.cellular.automata.cellularautomata.data.CubeDataHolder;
import com.cellular.automata.cellularautomata.data.RenderCubeMap;
import com.cellular.automata.cellularautomata.data.VertexArray;
import com.cellular.automata.cellularautomata.interfaces.CellSelectListener;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CubeCenter;
import com.cellular.automata.cellularautomata.utils.ObjectSelectHelper;

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

public class ModelRenderBuilder {

    private String TAG = "RENDER_BUILDER";

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
    private int automataRadius = 0;
    private RenderCubeMap renderMap;

    private CellSelectListener selectListener;
    private boolean isTouched = false;
    private boolean isStretched = false;
    private boolean isSliced = false;
    private boolean viewMode = true;
    private int currentLayerOpened = 0;

    private ObjectSelectHelper.TouchResult touchResult;

    private OnTouchListener onTouchListener;
    private Animator animator;


    public interface OnTouchListener{

        void onTouch();

    }

    public ModelRenderBuilder(int automataRadius){

        this.renderMap = new RenderCubeMap(automataRadius);

    }


    //GETTERS

    public ObjectSelectHelper.TouchResult getTouchResult() {
        return touchResult;
    }

    public ArrayList<CubeCenter> getCellCentersList(){

        if(isSliced){

            return renderMap.getLayer(currentLayerOpened);

        }else{

            return renderMap.getCubeCenters();

        }

    }

    public RenderCubeMap getRenderMap() {

        return renderMap;

    }

    //SETTERS

    //this method can be called from the Application Manager
    public void setSelectListener(CellSelectListener listener){

        if(listener != null) this.selectListener = listener;

    }

    public void setAutomataRadius(int radius){

        this.automataRadius = radius;

    }


    public void setOnTouchListener(OnTouchListener listener){

        this.onTouchListener = listener;

    }

    public void setRenderMap(RenderCubeMap map){

        this.renderMap = map;
        animator = new Animator(renderMap.size(), renderMap.height(), renderMap.getRenderCubeList());

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

    public void setViewMode(boolean isViewMode){

        this.viewMode = isViewMode;
        isSliced = false;
        resetCurrentLayer();

    }

    public void setSliced(boolean isSliced){

        this.isSliced = isSliced;

    }

    public void layerUp(){

        this.currentLayerOpened += 1;

    }

    public void layerDown(){

        this.currentLayerOpened -= 1;

    }



    private void resetCurrentLayer(){
        this.currentLayerOpened = 0;
    }

    public void stretch(){

        if(isStretched) return;

        animator = new Animator(renderMap.size(), renderMap.height(), renderMap.sort());
        isStretched = true;

    }

    public void squeeze(){

        if(!isStretched) return;
        isStretched = false;

    }

    //general function, is called first when want to add a cubed
    public void addNewCube(CubeCenter center, CellColor color){

        addNewCube(new RenderCube(center, color));

    }

    public void addNewCube(RenderCube renderCube){

        renderMap.add(renderCube);
        //rebuild figure
        build();
        bindAttributesData();

    }

    public void addAllCubes(ArrayList<RenderCube> renderCubes){

        renderMap.addAll(renderCubes);
        //rebuild figure
        build();
        bindAttributesData();

    }

    //general function is called first when we want to paint the cube
    public void paintCube(CubeCenter center, CellColor color){

        RenderCube renderCubeToRecolor = renderMap.getCubeByCenter(center);
        if(renderCubeToRecolor == null) return;

        renderCubeToRecolor.paintCube(color);
        build();
        bindAttributesData();

    }

    //general function is called first when we want to delete the cube
    public void deleteCube(CubeCenter center){

        renderMap.remove(new RenderCube(center, new CellColor ("#ffffff")));


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

        for (RenderCube renderCube : renderMap.getRenderCubeList()){

            appendCell(renderCube);

        }

        vertexPosArray = new VertexArray(vertexPositionData);
        vertexColorArray = new VertexArray(vertexColorData);
        vertexNormalArray = new VertexArray(vertexNormalData);

        vertexPositionData = null;
        vertexNormalData = null;
        vertexColorData = null;

    }

    private void appendCell(RenderCube renderCube){

        for (float f: renderCube.getCubePositionData()){
            vertexPositionData[vertexDataOffset++] = f;
        }

        for (float f: renderCube.getCubeNormalData()){
            vertexNormalData[vertexNormalDataOffset++] = f;
        }

        for (float f: renderCube.getCubeColorData()){
            vertexColorData[vertexColorDataOffset++] = f;
        }

        renderCube.releaseCubeData();

    }

    //This method can be called only inside the GL context
    //binds data to the VBOs
    public void bindAttributesData(){

        if(howManyCells() <= 0){
            Log.d(TAG, "No data to bind");
            return;
        }

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

                animator.drawFullStretchedFigure(renderer.getShader());

            }else{

                animator.drawClosedFigure(renderer.getShader());

            }

        }else{


            if(isSliced){

                animator.drawSlicedFigure(currentLayerOpened, renderer.getShader());

            }else{

                float[] resetScatterVector = {0.0f, 0.0f, 0.0f};
                renderer.getShader().setScatter(resetScatterVector);
                glDrawArrays(GLES20.GL_TRIANGLES, 0, CubeDataHolder.getInstance().sizeInVertex * howManyCells());

            }



        }


    }

    //additional stuff

    private int howManyCells(){

        return renderMap.size();

    }

    public boolean cubeExists(CubeCenter cubeCenter){

        return renderMap.cubeExists(cubeCenter);

    }

}
