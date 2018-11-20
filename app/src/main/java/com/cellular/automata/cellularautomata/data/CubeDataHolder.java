package com.cellular.automata.cellularautomata.data;

import java.util.ArrayList;

import static com.cellular.automata.cellularautomata.Constants.FACET_COMPONENT_COUNT;
import static com.cellular.automata.cellularautomata.Constants.POINT_COMPONENT_COUNT;

public class CubeDataHolder {

    public int sizeInVertex;

    public static int QUALITY_LOW = 0;
    public static int QUALITY_MEDIUM = 1;
    public static int QUALITY_HIGH = 2;

    public ArrayList<Facet> facetListLow;
    public ArrayList<Facet> facetListMedium;
    public ArrayList<Facet> facetListHigh;
    public int modelToLoad = -1;

    private float[] vertices;
    private float[] normals;


    private CubeDataHolder(){}

    private ArrayList<Facet> facetList;

    private static CubeDataHolder instance;

    public static CubeDataHolder getInstance(){

        if (instance == null){
            instance = new CubeDataHolder();


        }

        return instance;

    }


    public void setGraphicsQuality(int quality){

        switch (quality){

            case 0:{
                setFacetList(facetListLow);
                break;
            }
            case 1:{
                setFacetList(facetListMedium);
                break;
            }
            case 2:{
                setFacetList(facetListHigh);
                break;
            }

        }


    }

    public void setFacetList(ArrayList<Facet> facetList){


        this.facetList = facetList;
        vertices = new float[facetList.size() * FACET_COMPONENT_COUNT * POINT_COMPONENT_COUNT];
        normals = new float[facetList.size() * FACET_COMPONENT_COUNT * POINT_COMPONENT_COUNT];

        int verticesPointer = 0;
        int normalsPointer = 0;

        for (Facet facet: facetList){

            normals[normalsPointer++] = facet.normal.x;
            normals[normalsPointer++] = facet.normal.y;
            normals[normalsPointer++] = facet.normal.z;

            normals[normalsPointer++] = facet.normal.x;
            normals[normalsPointer++] = facet.normal.y;
            normals[normalsPointer++] = facet.normal.z;

            normals[normalsPointer++] = facet.normal.x;
            normals[normalsPointer++] = facet.normal.y;
            normals[normalsPointer++] = facet.normal.z;

            vertices[verticesPointer++] = facet.A.x;
            vertices[verticesPointer++] = facet.A.y;
            vertices[verticesPointer++] = facet.A.z;

            vertices[verticesPointer++] = facet.B.x;
            vertices[verticesPointer++] = facet.B.y;
            vertices[verticesPointer++] = facet.B.z;

            vertices[verticesPointer++] = facet.C.x;
            vertices[verticesPointer++] = facet.C.y;
            vertices[verticesPointer++] = facet.C.z;


        }

        sizeInVertex = vertices.length / 3;


    }

    public ArrayList<Facet> getFacetList() {

        return facetList;
    }

    public float[] getVertices(){
        return vertices;
    }
    public float[] getNormals(){
        return normals;
    }

}
