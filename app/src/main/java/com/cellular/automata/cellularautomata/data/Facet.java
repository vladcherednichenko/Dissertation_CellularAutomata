package com.cellular.automata.cellularautomata.data;

import com.cellular.automata.cellularautomata.utils.Geometry;
import com.cellular.automata.cellularautomata.utils.CellPoint;

public class Facet {

    public Geometry.Vector normal;
    public CellPoint A;
    public CellPoint B;
    public CellPoint C;

    public Facet(){};

    public Facet(Geometry.Vector normal, CellPoint a, CellPoint b, CellPoint c) {
        this.normal = normal;
        A = a;
        B = b;
        C = c;
    }

}
