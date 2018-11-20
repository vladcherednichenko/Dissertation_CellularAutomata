package com.cellular.automata.cellularautomata.utils;


public class CellularPoint {

    public float x;
    public float y;
    public float z;

    public CellularPoint(float x, float y, float z) {
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

    public CellularPoint translateAndCopy(Geometry.Vector vector) {
        return new CellularPoint(
                x + vector.x,
                y + vector.y,
                z + vector.z);
    }

    public CellularPoint clone(){

        return new CellularPoint(x, y, z);

    }

    public boolean equals(CellularPoint secondPoint){

        return this.x == secondPoint.x &&
                this.y == secondPoint.y &&
                this.z == secondPoint.z;


    }

}
