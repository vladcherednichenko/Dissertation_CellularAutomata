package com.cellular.automata.cellularautomata;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.cellular.automata.cellularautomata.interfaces.ActivityListener;
import com.cellular.automata.cellularautomata.interfaces.ApplicationListener;
import com.cellular.automata.cellularautomata.interfaces.EnvironmentListener;
import com.cellular.automata.cellularautomata.shaders.FigureShader;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CellPoint;
import com.cellular.automata.cellularautomata.utils.ObjectSelectHelper;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.translateM;

public class GraphicsRenderer implements GLSurfaceView.Renderer {

    private ActivityListener activityListener;
    private ApplicationListener applicationListener;
    private EnvironmentListener environmentListener;

    //shaders
    private FigureShader figureShader;

    private volatile float xAngle = -45f;
    private volatile float yAngle = 10f;

    private float strideX = 0f;
    private float strideY = 0f;

    private float width;
    private float height;

    private float scaleFactor = 1f;

    //mvp matrices
    private float[] viewMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] viewProjectionMatrix = new float[16];
    private float[] MVPMatrix = new float[16];
    //matrix that undo the effects of view and projection matrix
    private final float[] invertedViewProjectionMatrix = new float[16];

    //light matrices
    private final float[] lightPosInModelSpace = new float[]{0.0f, 0.0f, 0.0f, 1.0f};

    private float[] frontLightModelMatrix = new float[16];
    private final float[] frontLightPosInWorldSpace = new float[4];
    private final float[] frontLightPosInEyeSpace = new float[4];

    private float[] backLightModelMatrix = new float[16];
    private final float[] backLightPosInWorldSpace = new float[4];
    private final float[] backLightPosInEyeSpace = new float[4];

    private float[] rightLightModelMatrix = new float[16];
    private final float[] rightLightPosInWorldSpace = new float[4];
    private final float[] rightLightPosInEyeSpace = new float[4];

    private float[] topLightModelMatrix = new float[16];
    private final float[] topLightPosInWorldSpace = new float[4];
    private final float[] topLightPosInEyeSpace = new float[4];

    private float[] leftLightModelMatrix = new float[16];
    private final float[] leftLightPosInWorldSpace = new float[4];
    private final float[] leftLightPosInEyeSpace = new float[4];

    private float[] bottomLightModelMatrix = new float[16];
    private final float[] bottomLightPosInWorldSpace = new float[4];
    private final float[] bottomLightPosInEyeSpace = new float[4];

    public FigureShader getShader(){return figureShader;}

    public void setScaleFactor(float scaleFactor) {this.scaleFactor = scaleFactor; }
    public float getScaleFactor() {return scaleFactor;}

    public void setStride(float strideX, float strideY){this.strideX = strideX; this.strideY = strideY; }
    public float getStrideX(){return strideX;}
    public float getStrideY(){return strideY;}

    public void setXAngle(float xAngle) { this.xAngle = xAngle; }
    public void setYAngle(float yAngle) { this.yAngle = yAngle > 360? yAngle - 360: yAngle; }
    public float getXAngle() {return this.xAngle; }
    public float getYAngle() { return this.yAngle; }

    public void handleTouch(float x, float y){

        if(environmentListener!=null ){
            environmentListener.onScreenTouched(x, y);
        }
    }

    public void setEnvironmentListener(EnvironmentListener environmentListener) {
        this.environmentListener = environmentListener;
    }

    public void setActicvityListener(ActivityListener activityListener){
        this.activityListener = activityListener;
    }


    GraphicsRenderer(ApplicationListener applicationListener){

        this.applicationListener = applicationListener;

    }

    public ObjectSelectHelper.TouchResult getTouchedResult(float normalizedX, float normalizedY, ArrayList<CellPoint> cellCentersList){

        return ObjectSelectHelper.getTouchResult(cellCentersList, normalizedX, normalizedY, invertedViewProjectionMatrix, modelMatrix, scaleFactor, strideX, strideY, (float)height/width);

    }

    public void resetCam(){

        xAngle = -45f;
        yAngle = 10f;

        strideX = 0f;
        strideY = 0f;

        scaleFactor = 1;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        CellColor bgColor =  new CellColor(Settings.backgroundColor);
        GLES20.glClearColor(bgColor.RED, bgColor.GREEN, bgColor.BLUE, 0.0f);

        // Remove faces that are the back side to the screen
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Enable depth testing to remove drawing objects that are behind other objects
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);


        // Position the eye in front of the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -0.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        figureShader = new FigureShader(activityListener);


        if ( applicationListener == null) return;

        applicationListener.create();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 3.0f;
        final float far = 100.0f;

        this.width = width;
        this.height = height;

        Matrix.orthoM(projectionMatrix, 0, left,right, bottom, top, near, far);

        if ( applicationListener == null) return;

        applicationListener.resize();

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);


//        calculateModelMatrix();
//
//        calculateLightMatrices(xAngle, yAngle);
//
//        calculateInvertedMVPMatrix();
//
//        setUniforms();
//
//        setScaleFactor();


        calculateModelMatrix();

        calculateLightMatrices(xAngle, yAngle);

        calculateInvertedMVPMatrix();

        setUniforms();

        setScaleFactor();


        if ( applicationListener == null) return;

        applicationListener.render();

    }

    private void calculateModelMatrix(){

        //manipulations with the cubes model matrix
        //push figure to the distance
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -7.0f);

        //set user made stride
        Matrix.translateM(modelMatrix, 0, strideX/scaleFactor, -strideY/scaleFactor, 0.0f);

        //set user made rotation
        rotateM(modelMatrix, 0, yAngle, 1f, 0f, 0f);
        rotateM(modelMatrix, 0, xAngle, 0f, 1f, 0f);


    }


    private void calculateLightMatrices(float xAngle, float yAngle){

        Matrix.setIdentityM(frontLightModelMatrix, 0);
        Matrix.translateM(frontLightModelMatrix, 0, 0.0f, 0.0f, -7.0f);

        //count all the light source position
        float lightDistance = Settings.lightDistance;

        rotateM(frontLightModelMatrix, 0, yAngle, 1f, 0f, 0f);
        rotateM(frontLightModelMatrix, 0, xAngle, 0f, 1f, 0f);

        System.arraycopy(frontLightModelMatrix ,0, backLightModelMatrix, 0, frontLightModelMatrix.length);
        System.arraycopy(frontLightModelMatrix ,0, rightLightModelMatrix, 0, frontLightModelMatrix.length);
        System.arraycopy(frontLightModelMatrix ,0, topLightModelMatrix, 0, frontLightModelMatrix.length);
        //System.arraycopy(frontLightModelMatrix ,0, leftLightModelMatrix, 0, frontLightModelMatrix.length);
        //System.arraycopy(frontLightModelMatrix ,0, bottomLightModelMatrix, 0, frontLightModelMatrix.length);

        Matrix.translateM(backLightModelMatrix, 0, 0.0f, 0.0f, -lightDistance);
        Matrix.translateM(rightLightModelMatrix, 0, lightDistance, 0.0f, 0.0f);
        Matrix.translateM(topLightModelMatrix,  0, 0.0f, lightDistance, 0.0f);
        //Matrix.translateM(leftLightModelMatrix, 0, -lightDistance, 0.0f, 0.0f);
        //Matrix.translateM(bottomLightModelMatrix, 0, 0.0f, -lightDistance, 0.0f);



        multiplyMV(frontLightPosInWorldSpace, 0, frontLightModelMatrix, 0, lightPosInModelSpace, 0);
        multiplyMV(frontLightPosInEyeSpace, 0, viewMatrix, 0, frontLightPosInWorldSpace, 0);

        multiplyMV(backLightPosInWorldSpace, 0, backLightModelMatrix, 0, lightPosInModelSpace, 0);
        multiplyMV(backLightPosInEyeSpace, 0, viewMatrix, 0, backLightPosInWorldSpace, 0);

        multiplyMV(rightLightPosInWorldSpace, 0, rightLightModelMatrix, 0, lightPosInModelSpace, 0);
        multiplyMV(rightLightPosInEyeSpace, 0, viewMatrix, 0, rightLightPosInWorldSpace, 0);

        multiplyMV(topLightPosInWorldSpace, 0, topLightModelMatrix, 0, lightPosInModelSpace, 0);
        multiplyMV(topLightPosInEyeSpace, 0, viewMatrix, 0, topLightPosInWorldSpace, 0);

    }

    private void calculateInvertedMVPMatrix(){

        multiplyMM(viewProjectionMatrix, 0, viewMatrix, 0, projectionMatrix, 0);
        invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0);
        translateM(invertedViewProjectionMatrix, 0, 0f, 0f, -10f);

    }

    private void setUniforms(){
        // Set our per-vertex lighting gridShader.
        figureShader.useProgram();
        figureShader.setUniforms(modelMatrix, viewMatrix, projectionMatrix,
                    frontLightPosInEyeSpace,
                    backLightPosInEyeSpace,
                    leftLightPosInEyeSpace,
                    rightLightPosInEyeSpace,
                    topLightPosInEyeSpace,
                    bottomLightPosInEyeSpace);
    }

    private void setScaleFactor(){
        figureShader.setScaleFactor(scaleFactor);
    }




}






















