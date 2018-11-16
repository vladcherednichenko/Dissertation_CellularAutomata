package com.cellular.automata.cellularautomata.data;

import com.cellular.automata.cellularautomata.utils.Geometry;
import com.cellular.automata.cellularautomata.utils.PixioPoint;

public class Facet {

    public Geometry.Vector normal;
    public PixioPoint A;
    public PixioPoint B;
    public PixioPoint C;

    public Facet(){};

    public Facet(Geometry.Vector normal, PixioPoint a, PixioPoint b, PixioPoint c) {
        this.normal = normal;
        A = a;
        B = b;
        C = c;
    }

}
