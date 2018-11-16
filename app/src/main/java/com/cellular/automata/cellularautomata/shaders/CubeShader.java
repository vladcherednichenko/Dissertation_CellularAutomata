package com.cellular.automata.cellularautomata.shaders;

import android.content.Context;
import android.opengl.Matrix;

import com.cellular.automata.cellularautomata.R;
import com.cellular.automata.cellularautomata.utils.ShaderHelper;
import com.cellular.automata.cellularautomata.utils.TextResourceReader;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;


public class CubeShader {

    private final int program;

    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aNormalLocation;

    private final int uMVPMatrixLocation;

    // Attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_NORMAL = "a_Normal";

    //Uniform location
    protected static final String U_MVPMatrix = "u_MVPMatrix";


    public int getPositionAttributeLocation() {return aPositionLocation; }
    public int getColorAttributeLocation() {
        return aColorLocation;
    }
    public int getNormalAttributeLocation() {
        return aNormalLocation;
    }

    public CubeShader(Context context){

        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context, R.raw.vertex_shader),
                TextResourceReader.readTextFileFromResource(context, R.raw.fragment_shader));

        aPositionLocation = glGetAttribLocation (program, A_POSITION);
        aColorLocation = glGetAttribLocation (program, A_POSITION);
        aNormalLocation = glGetAttribLocation (program, A_POSITION);

        uMVPMatrixLocation = glGetUniformLocation(program, U_MVPMatrix);

    }


    public void setUniforms (float [] modelMatrix, float [] viewMatrix, float [] projectionMatrix){

        float[] MVPMatrix = new float[16];

        Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, MVPMatrix, 0);

        // Pass in the combined matrix.
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, MVPMatrix, 0);

    }


    public void useProgram(){

        glUseProgram(program);

    }


}
