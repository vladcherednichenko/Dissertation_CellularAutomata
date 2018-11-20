package com.cellular.automata.cellularautomata.data;

import com.cellular.automata.cellularautomata.utils.Geometry;
import com.cellular.automata.cellularautomata.utils.CellularPoint;

public class Facet {

    public Geometry.Vector normal;
    public CellularPoint A;
    public CellularPoint B;
    public CellularPoint C;

    public Facet(){};

    public Facet(Geometry.Vector normal, CellularPoint a, CellularPoint b, CellularPoint c) {
        this.normal = normal;
        A = a;
        B = b;
        C = c;
    }

}
