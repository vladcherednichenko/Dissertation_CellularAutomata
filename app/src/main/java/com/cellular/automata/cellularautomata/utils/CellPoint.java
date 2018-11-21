package com.cellular.automata.cellularautomata.utils;


public class CellPoint {

    public float x;
    public float y;
    public float z;

    public CellPoint(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void translateX(float dx){this.x+=dx; }
    public void translateY(float dy){this.y+=dy; }
    public void translateZ(float dz){this.z+=dz; }

    public void translate(Geometry.Vector vector){
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }

    public CellPoint translateAndCopy(Geometry.Vector vector) {
        return new CellPoint(
                x + vector.x,
                y + vector.y,
                z + vector.z);
    }

    public CellPoint clone(){

        return new CellPoint(x, y, z);

    }

    public boolean equals(CellPoint secondPoint){

        return this.x == secondPoint.x &&
                this.y == secondPoint.y &&
                this.z == secondPoint.z;


    }

}
