package com.cellular.automata.cellularautomata;

import com.cellular.automata.cellularautomata.data.RenderCubeMap;
import com.cellular.automata.cellularautomata.objects.RenderCube;
import com.cellular.automata.cellularautomata.data.CellColor;
import com.cellular.automata.cellularautomata.utils.CubeCenter;

import org.junit.Test;

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

        RenderCubeMap map = new RenderCubeMap(4);

        RenderCube renderCube = new RenderCube(new CubeCenter(0, 0, 0), new CellColor("#222222"), false);
        RenderCube renderCube1 = new RenderCube(new CubeCenter(0, 0, 0), new CellColor("#222222"), false);

        assertTrue(map.cubeInBounds(renderCube1));

    }
}