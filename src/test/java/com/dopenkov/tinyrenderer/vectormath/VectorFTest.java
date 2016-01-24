package com.dopenkov.tinyrenderer.vectormath;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 22.01.16.
 */
public class VectorFTest {

    @Test
    public void testScale() throws Exception {
        assertEquals(new VectorF(2, 0, 0), VectorF.i.scale(2));
        assertEquals(new VectorF(-28, 21, -49), new VectorF(4, -3, 7).scale(-7));
    }

    @Test
    public void testAdd() throws Exception {
        assertEquals(new VectorF(4, -2, 7), new VectorF(4, -3, 7).add(VectorF.j));
        assertEquals(new VectorF(-33, -4, 28), new VectorF(-40, 3, 17).add(new VectorF(7, -7, 11)));
    }

    @Test
    public void testCross() throws Exception {
        assertEquals(VectorF.k, VectorF.i.cross(VectorF.j));
        assertEquals(new VectorF(26, -17, -11), new VectorF(5, 7, 1).cross(new VectorF(3, 2, 4)));
    }

    @Test
    public void testToInt() throws Exception {
        assertEquals(new Vector3i(26, -16, -11), new VectorF(25.5f, -16.5f, -11.49f).toInt());
    }

    @Test
    public void testRound() throws Exception {
        assertEquals(new VectorF(26, -16, -11), new VectorF(25.5f, -16.5f, -11.49f).round());
    }

    @Test
    public void testSwapXY() throws Exception {
        assertEquals(new VectorF(-16.5f, 25.5f, -11.49f), new VectorF(25.5f, -16.5f, -11.49f).swapXY());
    }
}