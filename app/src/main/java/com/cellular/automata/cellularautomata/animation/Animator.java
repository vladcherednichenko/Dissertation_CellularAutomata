package com.cellular.automata.cellularautomata.animation;

import android.opengl.GLES20;

import com.cellular.automata.cellularautomata.Settings;
import com.cellular.automata.cellularautomata.data.CubeDataHolder;
import com.cellular.automata.cellularautomata.objects.RenderCube;
import com.cellular.automata.cellularautomata.shaders.FigureShader;

import java.util.ArrayList;

import static android.opengl.GLES20.glDrawArrays;

public class Animator {

    private float fullSstretchDuration = 500;
    private int animationFramesAmount = 60;
    private float framesPerMS = (float)animationFramesAmount / fullSstretchDuration;
    private long animationStartTime;
    private int framesDrawn = -1;


    private boolean animationStretchIsRunning = false;
    private boolean animationSqueezeIsRunning = false;

    private boolean animationIsRunning = false;
    private boolean figureIsOpened = false;

    private float[] scatterVector = {0.0f, 0.0f, 0.0f};
    private float[] resetScatterVector = {0.0f, 0.0f, 0.0f};
    private float[] sliceVector = {0.0f, 0.0f, 0.0f};
    private float[] resetSliceVector = {0.0f, 0.0f, 0.0f};

    private final int cubeNumber;
    private final int objectHeight;
    private final ArrayList<RenderCube> renderCubes;

    public boolean animationIsRunning(){return animationSqueezeIsRunning || animationStretchIsRunning;}

    public Animator(int cubeNumber, int objectHeight, ArrayList<RenderCube> renderCubes){
        this.objectHeight = objectHeight;
        this.renderCubes = renderCubes;
        this.cubeNumber = cubeNumber;
    }


    public void drawFullStretchedFigure(FigureShader shader){

        if(animationSqueezeIsRunning){
            drawClosedFigure(shader);
            return;
        }

        if (!figureIsOpened ){
            animateStretch(shader);
        }else{
            float currentY = renderCubes.get(renderCubes.size()-1).center.y;
            scatterVector = new float[]{0.0f, objectHeight/2, 0.0f};

            int cubesToDraw = 0;

            for (int i = 0; i< renderCubes.size(); i++){


                RenderCube renderCube = renderCubes.get(renderCubes.size()-1 - i);
                if (renderCube.center.y != currentY || i == renderCubes.size()-1){

                    if (i == (renderCubes.size()-1)){ cubesToDraw++; i++;};

                    currentY = renderCube.center.y;
                    shader.setScatter(scatterVector);

                    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, CubeDataHolder.getInstance().sizeInVertex * (cubeNumber-i), CubeDataHolder.getInstance().sizeInVertex * cubesToDraw);

                    scatterVector[1] = scatterVector[1]-1;
                    cubesToDraw = 0;
                }
                cubesToDraw++;
            }
        }

    };
    public void drawClosedFigure(FigureShader shader){

        if (animationStretchIsRunning){
            drawFullStretchedFigure(shader);
            return;
        }

        if (figureIsOpened){
            animateSqueeze(shader);
        }else{
            shader.setScatter(resetScatterVector);
            glDrawArrays(GLES20.GL_TRIANGLES, 0, CubeDataHolder.getInstance().sizeInVertex * cubeNumber);
        }


    }

    public void animateSlice(int layerToSlice, FigureShader shader){



    }

    public void animateAssemble(){



    }

    public void drawSlicedFigure(int layerToSlice, FigureShader shader){

        sliceVector = new float[]{0.0f, Settings.sliceHeight, 0.0f};

        int cubesDrawDown = cubeNumber;

        for (int i = 0; i< renderCubes.size(); i++){

            RenderCube renderCube = renderCubes.get(i);

            if (renderCube.center.y >layerToSlice ){

                cubesDrawDown = i;
                break;

            }
        }

        shader.setScatter(resetSliceVector);
        glDrawArrays(GLES20.GL_TRIANGLES, 0, CubeDataHolder.getInstance().sizeInVertex * cubesDrawDown);
        shader.setScatter(sliceVector);
        glDrawArrays(GLES20.GL_TRIANGLES, CubeDataHolder.getInstance().sizeInVertex * cubesDrawDown , CubeDataHolder.getInstance().sizeInVertex * (cubeNumber - cubesDrawDown));

    }

    public void animateStretch(FigureShader shader){

        //don!t run the animation if the figure is stretched
        if (figureIsOpened) return;

        //set animation start time
        if (!animationStretchIsRunning) {
            animationStretchIsRunning = true;
            animationStartTime = System.currentTimeMillis();
        }

        long currentTime = System.currentTimeMillis();
        long timePast = (currentTime - animationStartTime);

        //define the current frame
        float frame = Math.round(timePast * framesPerMS);

        //turn of the animation if all the frames are drawn
        if (frame > animationFramesAmount) {
            animationStretchIsRunning = false;
            figureIsOpened = true;
            framesDrawn = -1;
            drawFullStretchedFigure(shader);
            return;
        }

        //update drawn frames amount
        if (frame > framesDrawn)  framesDrawn++; else return;

        float animationPercentage = frame / animationFramesAmount;

        float currentY = renderCubes.get(renderCubes.size()-1).center.y;
        scatterVector = new float[]{0.0f, objectHeight/2, 0.0f};


        int cubesToDraw = 0;

        for (int i = 0; i< renderCubes.size(); i++){


            RenderCube renderCube = renderCubes.get(renderCubes.size()-1 - i);
            if (renderCube.center.y != currentY || i == renderCubes.size()-1){

                if (i == (renderCubes.size()-1)){ cubesToDraw++; i++;};

                currentY = renderCube.center.y;
                shader.setScatter(new float[]{scatterVector[0], scatterVector[1] * animationPercentage, scatterVector[2]});

                glDrawArrays(GLES20.GL_TRIANGLES, CubeDataHolder.getInstance().sizeInVertex * (cubeNumber-i), CubeDataHolder.getInstance().sizeInVertex * cubesToDraw);

                scatterVector[1] = scatterVector[1]-1;
                cubesToDraw = 0;
            }
            cubesToDraw++;
        }

    }

    public void animateSqueeze(FigureShader shader){

        //don!t run the animation if the figure is stretched
        if (!figureIsOpened) return;

        //set animation start time
        if (!animationSqueezeIsRunning) {
            animationSqueezeIsRunning = true;
            animationStartTime = System.currentTimeMillis();
        }

        long currentTime = System.currentTimeMillis();
        long timePast = (currentTime - animationStartTime);

        //define the current frame
        float frame = Math.round(timePast * framesPerMS);

        //turn of the animation if all the frames are drawn
        if (frame > animationFramesAmount) {
            animationSqueezeIsRunning = false;
            figureIsOpened = false;
            framesDrawn = -1;
            drawClosedFigure(shader);
            return;
        }

        //update drawn frames amount
        if (frame > framesDrawn)  framesDrawn++; else return;

        float animationPercentage = (animationFramesAmount - frame) / animationFramesAmount;

        float currentY = renderCubes.get(renderCubes.size()-1).center.y;
        scatterVector = new float[]{0.0f, objectHeight/2, 0.0f};


        int cubesToDraw = 0;

        for (int i = 0; i< renderCubes.size(); i++){


            RenderCube renderCube = renderCubes.get(renderCubes.size()-1 - i);
            if (renderCube.center.y != currentY || i == renderCubes.size()-1){

                if (i == (renderCubes.size()-1)){ cubesToDraw++; i++;};

                currentY = renderCube.center.y;
                shader.setScatter(new float[]{scatterVector[0], scatterVector[1] * animationPercentage, scatterVector[2]});

                glDrawArrays(GLES20.GL_TRIANGLES, CubeDataHolder.getInstance().sizeInVertex * (cubeNumber-i), CubeDataHolder.getInstance().sizeInVertex * cubesToDraw);

                scatterVector[1] = scatterVector[1]-1;
                cubesToDraw = 0;
            }
            cubesToDraw++;
        }
    }


}
