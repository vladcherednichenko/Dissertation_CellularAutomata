package com.cellular.automata.cellularautomata;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.cellular.automata.cellularautomata.interfaces.MainView;
import com.cellular.automata.cellularautomata.interfaces.ApplicationListener;
import com.cellular.automata.cellularautomata.interfaces.EnvironmentListener;
import com.cellular.automata.cellularautomata.interfaces.ScreenshotListener;
import com.cellular.automata.cellularautomata.shaders.FigureShader;
import com.cellular.automata.cellularautomata.shaders.GridShader;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CubeCenter;
import com.cellular.automata.cellularautomata.utils.ObjectSelectHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glReadPixels;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.translateM;

public class GraphicsRenderer implements GLSurfaceView.Renderer {

    private MainView activityListener;
    private ApplicationListener applicationListener;
    private EnvironmentListener environmentListener;
    private ScreenshotListener screenshotListener;

    //shaders
    private FigureShader figureShader;
    private GridShader gridShader;

    private volatile float xAngle = 0f;
    private volatile float yAngle = 10f;

    //moving figure left-right, up-down
    private float strideX = 0f;
    private float strideY = 0f;
    private float additionalLayerStrideY = 0f;

    //screen
    private int width;
    private int height;

    //initial scale
    private float scaleFactor = 1f;

    //other
    private boolean screenshot = false;
    private boolean figureVisible = true;
    private boolean gridVisible = false;

    //mvp matrices
    private float[] viewMatrix = new float[16];
    private float[] modelMatrix = new float[16];
    private float[] figureModelMatrix = new float[16];
    private float[] gridModelMatrix = new float[16];
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
    public GridShader getGridShader(){return gridShader;}

    public void setFigureScaleFactor(float scaleFactor) {this.scaleFactor = scaleFactor; }
    public float getScaleFactor() {return scaleFactor;}

    public void setStride(float strideX, float strideY){this.strideX = strideX; this.strideY = strideY; }
    public float getStrideX(){return strideX;}
    public float getStrideY(){return strideY;}
    public void setXAngle(float xAngle) { this.xAngle = xAngle; }
    public void setYAngle(float yAngle) { this.yAngle = yAngle > 360? yAngle - 360: yAngle; }
    public float getXAngle() {return this.xAngle; }
    public float getYAngle() { return this.yAngle; }
    public void translateFigureVertical(float distance){ additionalLayerStrideY += distance;}
    public void resetAdditionalStride(){additionalLayerStrideY = 0f;}
    public void resetCam(){

        xAngle = -45f;
        yAngle = 10f;

        strideX = 0f;
        strideY = 0f;
        resetAdditionalStride();

        scaleFactor = 1;

    }

    public void screenshot(ScreenshotListener listener){this.screenshotListener = listener; this.screenshot = true;}


    public void handleTouch(float x, float y){

        if(environmentListener!=null ){
            environmentListener.onScreenTouched(x, y);
        }
    }

    public void setEnvironmentListener(EnvironmentListener environmentListener) {
        this.environmentListener = environmentListener;
    }

    public void setActicvityListener(MainView activityListener){
        this.activityListener = activityListener;
    }


    GraphicsRenderer(ApplicationListener applicationListener){

        this.applicationListener = applicationListener;

    }

    public ObjectSelectHelper.TouchResult getTouchedResult(float normalizedX, float normalizedY, ArrayList<CubeCenter> cellCentersList){

        return ObjectSelectHelper.getTouchResult(cellCentersList, normalizedX, normalizedY, invertedViewProjectionMatrix, modelMatrix, scaleFactor, strideX, strideY, (float)height/width);

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
        gridShader = new GridShader(activityListener);


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

    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        calculateModelMatrix();

        calculateLightMatrices(xAngle, yAngle);

        calculateInvertedMVPMatrix();

        if ( applicationListener == null) return;

        applicationListener.render();

        if(this.screenshot){takeScreenshot(); screenshot = false;}

    }

    float previousStrideX = 0f;
    float dx =0f, dz = 0f;

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

        System.arraycopy(modelMatrix, 0, gridModelMatrix, 0, modelMatrix.length);

        //additional stride made when editing figure
        Matrix.translateM(gridModelMatrix, 0, 0, -additionalLayerStrideY, 0);


//        float dx, dz;
//        dx = (float)(strideX/scaleFactor * Math.cos(Math.toRadians((double)xAngle)));
//        dz = (float)(strideX/scaleFactor * Math.sin(Math.toRadians((double)xAngle)));
//
//
//        GRFX.activityListener.logText("angle: " + String.valueOf(xAngle) +  ", dx = " + String.valueOf(dx) + ", dz = " + String.valueOf(dz));
//
//        //set user made stride
//        Matrix.translateM(modelMatrix, 0, dz, -strideY/scaleFactor, dx);
//
//
//        // Position the eye in front of the origin.
//        float eyeX = 0.0f;
//        float eyeY = 0.0f;
//        float eyeZ = -0.5f;
//
//        // We are looking toward the distance
//        float lookX = 0.0f;
//        float lookY = 0.0f;
//        float lookZ = -5.0f;
//
//        // Set our up vector. This is where our head would be pointing were we holding the camera.
//        final float upX = 0.0f;
//        final float upY = 1.0f;
//        final float upZ = 0.0f;
//
//
//        // Set the view matrix. This matrix can be said to represent the camera position.
//        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
//        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
//        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
//
//        rotateM(viewMatrix, 0, yAngle, 1f, 0f, 0f);
//        rotateM(viewMatrix, 0, xAngle, 0f, 1f, 0f);
//
//
//        if(strideX != 0){
//
//            previousStrideX = strideX;
//
//            dx += (float)(strideX/scaleFactor * Math.cos(Math.toRadians((double)xAngle)));
//            dz += (float)(strideX/scaleFactor * Math.sin(Math.toRadians((double)xAngle)));
//
//            strideX = 0;
//
//        }
//
//        //if(cos)
//
//        GRFX.activityListener.logText("angle: " + String.valueOf(xAngle) +  ", dx = " + String.valueOf(dx) + ", dz = " + String.valueOf(dz));
//
//        Matrix.translateM(viewMatrix, 0, dx, -strideY/scaleFactor, dz);



    }


    private void calculateLightMatrices(float xAngle, float yAngle){

        Matrix.setIdentityM(frontLightModelMatrix, 0);
        Matrix.translateM(frontLightModelMatrix, 0, 0.0f, 0.0f, -7.0f);

        //count all the light source position
        float lightDistance = Settings.lightDistance;

//        rotateM(frontLightModelMatrix, 0, yAngle, 1f, 0f, 0f);
//        rotateM(frontLightModelMatrix, 0, xAngle, 0f, 1f, 0f);

        System.arraycopy(frontLightModelMatrix ,0, backLightModelMatrix, 0, frontLightModelMatrix.length);
        System.arraycopy(frontLightModelMatrix ,0, rightLightModelMatrix, 0, frontLightModelMatrix.length);
        System.arraycopy(frontLightModelMatrix ,0, topLightModelMatrix, 0, frontLightModelMatrix.length);

        Matrix.translateM(backLightModelMatrix, 0, 0.0f, 0.0f, -lightDistance);
        Matrix.translateM(rightLightModelMatrix, 0, lightDistance, 0.0f, 0.0f);
        Matrix.translateM(topLightModelMatrix,  0, 0.0f, lightDistance, 0.0f);



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

    public void setFigureUniforms(){

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

    public void setGridUniforms(){

        gridShader.useProgram();
        gridShader.setUniforms(gridModelMatrix, viewMatrix, projectionMatrix);

    }

    public void setFigureScaleFactor(){

        figureShader.setScaleFactor(scaleFactor);

    }

    public void setGridScaleFactor(){

        gridShader.setScaleFactor(scaleFactor);

    }

    //UTILS
    private void takeScreenshot(){

        int screenshotSize = width * height;
        ByteBuffer bb = ByteBuffer.allocateDirect(screenshotSize * 4);
        bb.order(ByteOrder.nativeOrder());
        glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, bb);
        int pixelsBuffer[] = new int[screenshotSize];
        bb.asIntBuffer().get(pixelsBuffer);
        bb = null;

        for (int i = 0; i < screenshotSize; ++i) {
            // The alpha and green channels' positions are preserved while the      red and blue are swapped
            pixelsBuffer[i] = ((pixelsBuffer[i] & 0xff00ff00)) |    ((pixelsBuffer[i] & 0x000000ff) << 16) | ((pixelsBuffer[i] & 0x00ff0000) >> 16);
        }

        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        image.setPixels(pixelsBuffer, screenshotSize-width, -width, 0, 0, width, height);

        if(screenshotListener != null){

            screenshotListener.onScreenShot(image);

        }

    }



}






















