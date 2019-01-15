package com.cellular.automata.cellularautomata.data;

import com.cellular.automata.cellularautomata.utils.Geometry;
import com.cellular.automata.cellularautomata.utils.CubeCenter;

public class Facet {

    public Geometry.Vector normal;
    public CubeCenter A;
    public CubeCenter B;
    public CubeCenter C;

    public Facet(){};

    public Facet(Geometry.Vector normal, CubeCenter a, CubeCenter b, CubeCenter c) {
        this.normal = normal;
        A = a;
        B = b;
        C = c;
    }

}
