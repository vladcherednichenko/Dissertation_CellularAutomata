package com.cellular.automata.cellularautomata;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.TextView;

import com.cellular.automata.cellularautomata.data.CubeDataHolder;
import com.cellular.automata.cellularautomata.interfaces.MainView;
import com.cellular.automata.cellularautomata.utils.AntialiasingConfigurator;
import com.cellular.automata.cellularautomata.utils.TextResourceReader;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class SurfaceViewForAutomata extends GLSurfaceView{


    private float scaleFactor = 1.f;

    private final float maxXAngle = 90f;
    private final float minXAngle = -90f;

    private int firstPointerIndex = 0;
    private int secondPointerIndex = 1;

    private float previousTouchCenterX = 0f;
    private float previousTouchCenterY = 0f;

    private float touchCenterX = 0f;
    private float touchCenterY = 0f;

    private float figureStrideX = 0f;
    private float figureStrideY = 0f;

    private float startingDistanceBetweenPointers;
    private float movingSensitivity = 15f;

    private int screenWidth = 0;
    private int screenHeight = 0;

    private final int SCALE = 1;
    private final int MOVE = 2;

    private boolean twoPointersDetected = false;

    private int currentAction = MOVE;

    //the figure will start scale when distance between pointers increases by this parameter %
    //0.1f means distance between fingers should dbe increased ast least by 10% of the screen width
    private float twoPointersMovementScaleBound = 0.1f;
    private float twoPointersMovementDistance = getX() * twoPointersMovementScaleBound;


    private GraphicsRenderer renderer;

    int pointerIndex = -1;

    private final float TOUCH_SCALE_FACTOR = 180.0f / 900;
    private final float ROTATE_START_POINT = 10f;
    private float mPreviousX;
    private float mPreviousY;

    private float startX;
    private float startY;

    TextView txtFPSLog;

    final DisplayMetrics displayMetrics = new DisplayMetrics();

    private MainView activityListener;

    public void setActivityListener(MainView listener){
        this.activityListener = listener;
        renderer.setActicvityListener(listener);
    }

    ScaleGestureDetector mScaleDetector;


    public interface CubeSurfaceViewListener{

        void logFps(int fps);

    }

    public GraphicsRenderer getRenderer(){

        return this.renderer;

    }

    public SurfaceViewForAutomata(Context context) {
        super(context);

        setEGLContextClientVersion(2);
        loadDefaultModel();
        //setEGLConfigChooser(new MyConfigChooser());


        LINKER.gameInstance = new GameInstance();
        this.renderer = new GraphicsRenderer(LINKER.gameInstance);
        LINKER.renderer = renderer;

        setRenderer(this.renderer);

    }

    public SurfaceViewForAutomata(Context context, AttributeSet attrs) {

        super(context, attrs);

        setEGLContextClientVersion(2);
        loadDefaultModel();

        LINKER.gameInstance = new GameInstance();
        this.renderer = new GraphicsRenderer(LINKER.gameInstance);
        LINKER.renderer = renderer;


        mScaleDetector = new ScaleGestureDetector(getContext(), new PinchListener());

        screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;

        twoPointersMovementDistance = screenWidth * twoPointersMovementScaleBound;

        if(Settings.antialiasing){setEGLConfigChooser(new AntialiasingConfigurator()); }


        setRenderer(this.renderer);


    }

    private void loadDefaultModel(){

        CubeDataHolder.getInstance().facetListMedium = TextResourceReader.getFacetsFromFileObject(getContext(), "cube_medium.obj");
        CubeDataHolder.getInstance().setGraphicsQuality(CubeDataHolder.QUALITY_MEDIUM);

    };


    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (e.getPointerCount() > 1) {

            int xPointer1 = (int) e.getX(firstPointerIndex);
            int yPointer1 = (int) e.getY(firstPointerIndex);

            int xPointer2 = (int) e.getX(secondPointerIndex);
            int yPointer2 = (int) e.getY(secondPointerIndex);

            float distanceBetweenPointers = 0f;

            if(e.getActionMasked() == ACTION_MOVE){

                twoPointersDetected = true;

                touchCenterX = (xPointer2 + xPointer1) / 2f;
                touchCenterY = (yPointer2 + yPointer1) / 2f;

                if(startingDistanceBetweenPointers == 0f){
                    startingDistanceBetweenPointers = (float)Math.sqrt(Math.pow((xPointer2 - xPointer1), 2) + Math.pow((yPointer2 - yPointer1), 2));
                    previousTouchCenterX = touchCenterX;
                    previousTouchCenterY = touchCenterY;


                }
                distanceBetweenPointers = (float)Math.sqrt(Math.pow((xPointer2 - xPointer1), 2) + Math.pow((yPointer2 - yPointer1), 2));
            }

            if(twoPointersDetected){
                currentAction = Math.abs(distanceBetweenPointers - startingDistanceBetweenPointers) > twoPointersMovementDistance? SCALE : MOVE;

                //logGesture on the display
                if(activityListener != null && Settings.debugTextViewEnabled){
                    String text = "First: " + String.valueOf(xPointer1) + " : " + String.valueOf(yPointer1) + "\n"
                            +"Second: " + String.valueOf(xPointer2) + " : " + String.valueOf(yPointer2) + "\n"
                            +"Distance: " + String.valueOf(distanceBetweenPointers) + "\n"
                            +"Action: " + (currentAction == SCALE? "scale" : "move") + "\n"
                            +"Scale: " + String.valueOf(scaleFactor);
                    activityListener.logGesture(text);
                }


                float touchCenterDX = touchCenterX - previousTouchCenterX;
                float touchCenterDY = touchCenterY - previousTouchCenterY;



                renderer.setStride(
                        renderer.getStrideX() + touchCenterDX / screenWidth * movingSensitivity,
                        renderer.getStrideY() + touchCenterDY / screenHeight * movingSensitivity);

                previousTouchCenterX = touchCenterX;
                previousTouchCenterY = touchCenterY;

                mScaleDetector.onTouchEvent(e);

            }






        }else{

            switch (e.getActionMasked()) {
                case ACTION_DOWN :{

                    twoPointersDetected = false;
                    pointerIndex = pointerIndex == -1? e.getActionIndex(): pointerIndex;

                    final int pointerIndex = e.getActionIndex();
                    mPreviousX = e.getX(pointerIndex);
                    mPreviousY = e.getY(pointerIndex);

                    startX = mPreviousX;
                    startY = mPreviousY;


                    break;

                }


                case ACTION_MOVE:{


                    if(twoPointersDetected){
                        break;
                    }

                    float x = e.getX(pointerIndex);
                    float y = e.getY(pointerIndex);

                    float dx = x - mPreviousX;
                    float dy = y - mPreviousY;



                    renderer.setXAngle(renderer.getXAngle() + dx * TOUCH_SCALE_FACTOR);
                    renderer.setYAngle(renderer.getYAngle() + dy * TOUCH_SCALE_FACTOR);

//                    renderer.setXAngle(dx * TOUCH_SCALE_FACTOR);
//                    renderer.setYAngle(dy * TOUCH_SCALE_FACTOR);

                    mPreviousX = x;
                    mPreviousY = y;


                    requestRender();
                    break;
                }

                case ACTION_UP:{

                    resetPointersValues();

                    float x = e.getX(pointerIndex);
                    float y = e.getY(pointerIndex);

                    float dx = x - startX;
                    float dy = y - startY;

                    if (Math.abs(dx) < ROTATE_START_POINT && Math.abs(dy) < ROTATE_START_POINT){


                        float normalizedX = (e.getX() / (float) getWidth()) * 2 - 1;
                        float normalizedY = -((e.getY() / (float) getHeight()) * 2 - 1);

                        renderer.handleTouch(normalizedX, normalizedY);
                    }

                }
            }



        }

        return true;
    }



    private void resetPointersValues(){

        startingDistanceBetweenPointers = 0f;
        previousTouchCenterX = 0f;
        previousTouchCenterY = 0f;

    }


    private class PinchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            renderer.setFigureScaleFactor(
                    Math.max(
                            Settings.minimumFigureScale,
                            Math.min(
                                    renderer.getScaleFactor()* detector.getScaleFactor(),
                                    Settings.maximumFigureScale)));

            twoPointersDetected = true;

            return true;
        }

    }




}
