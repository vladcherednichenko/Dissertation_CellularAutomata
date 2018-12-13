package com.cellular.automata.cellularautomata;

import android.content.Context;

import com.cellular.automata.cellularautomata.data.CubeMap;
import com.cellular.automata.cellularautomata.objects.Cube;
import com.cellular.automata.cellularautomata.utils.CellColor;
import com.cellular.automata.cellularautomata.utils.CellPoint;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void cube_map_test() {

        CubeMap map = new CubeMap(4);

        Cube cube = new Cube(new CellPoint(0, 0, 0), new CellColor("#222222"), false);
        Cube cube1 = new Cube(new CellPoint(0, 0, 0), new CellColor("#222222"), false);

        assertTrue(map.cubeInBounds(cube1));

    }
}