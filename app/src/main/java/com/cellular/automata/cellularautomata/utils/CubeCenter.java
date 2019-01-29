package com.cellular.automata.cellularautomata.utils;


public class CubeCenter {

    public float x;
    public float y;
    public float z;

    public CubeCenter(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void translateX(float dx){this.x+=dx; }
    public void translateY(float dy){this.y+=dy; }
    public void translateZ(float dz){this.z+=dz; }

    public CubeCenter translateX_(float dx){this.x+=dx; return this;}
    public CubeCenter translateY_(float dy){this.y+=dy; return this;}
    public CubeCenter translateZ_(float dz){this.z+=dz; return this;}

    public void translate(Geometry.Vector vector){
        x += vector.x;
        y += vector.y;
        z += vector.z;
    }


    public CubeCenter translateAndCopy(Geometry.Vector vector) {
        return new CubeCenter(
                x + vector.x,
                y + vector.y,
                z + vector.z);
    }

    public CubeCenter clone(){

        return new CubeCenter(x, y, z);

    }

    public boolean equals(CubeCenter secondPoint){

        return this.x == secondPoint.x &&
                this.y == secondPoint.y &&
                this.z == secondPoint.z;


    }

}
