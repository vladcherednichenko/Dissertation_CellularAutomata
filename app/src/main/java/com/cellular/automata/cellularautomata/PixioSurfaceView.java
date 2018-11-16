package com.cellular.automata.cellularautomata;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class PixioSurfaceView extends GLSurfaceView{


    public PixioSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
    }

    public PixioSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
    }




}
