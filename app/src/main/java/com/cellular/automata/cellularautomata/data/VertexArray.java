package com.cellular.automata.cellularautomata.data;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.cellular.automata.cellularautomata.Constants.BYTES_PER_FLOAT;

public class VertexArray {

    private FloatBuffer floatBuffer;

    public VertexArray(float [] vertexData){

        floatBuffer = ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);

        floatBuffer.position(0);

    }

    public void setVertexAttribPointer(int dataOffset,int attributeLocation, int componentCount, int stride){

        floatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT, false, stride, floatBuffer);
        glEnableVertexAttribArray(attributeLocation);
        floatBuffer.position(0);

    }

    public void bindBufferToVBO(){

    }




}
