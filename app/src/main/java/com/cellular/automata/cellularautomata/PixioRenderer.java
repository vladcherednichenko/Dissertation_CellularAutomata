package com.cellular.automata.cellularautomata;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.cellular.automata.cellularautomata.shaders.CubeShader;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PixioRenderer implements GLSurfaceView.Renderer {

    private Context context;
    private CubeShader cubeShader;

    PixioRenderer(Context context){

        this.context = context;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        cubeShader = new CubeShader(context);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
