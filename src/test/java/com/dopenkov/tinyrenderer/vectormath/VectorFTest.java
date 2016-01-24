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

    @Test
    public void testSub() throws Exception {
        assertEquals(new VectorF(4, -4, 7), new VectorF(4, -3, 7).sub(VectorF.j));
        assertEquals(new VectorF(-47, 10, 6), new VectorF(-40, 3, 17).sub(new VectorF(7, -7, 11)));
    }

    @Test
    public void testLength() throws Exception {
        assertEquals(1f, VectorF.i.length(), Float.MIN_VALUE);
        assertEquals(24.269321f, new VectorF(12, 11, -18).length(), Float.MIN_VALUE);
    }

    @Test
    public void testLengthSQ() throws Exception {
        assertEquals(1, VectorF.i.length(), Float.MIN_VALUE);
        assertEquals(589, new VectorF(12, 11, 18).lengthSQ(), Float.MIN_VALUE);
    }

    @Test
    public void testNormalize() throws Exception {
        assertEquals(VectorF.i, VectorF.i.normalize());
        assertEquals(new VectorF(0.4944514f, 0.45324713f, -0.7416771f), new VectorF(12, 11, -18).normalize());
    }

    @Test
    public void testDot() throws Exception {
        assertEquals(1, VectorF.i.dot(VectorF.i), Float.MIN_VALUE);
        assertEquals(0, VectorF.i.dot(VectorF.j), Float.MIN_VALUE);
        assertEquals(0, VectorF.j.dot(VectorF.k), Float.MIN_VALUE);
        assertEquals(0, VectorF.k.dot(VectorF.i), Float.MIN_VALUE);
        assertEquals(-135, new VectorF(12, 11, -18).dot(new VectorF (2, -3, 7)), Float.MIN_VALUE);
    }
}