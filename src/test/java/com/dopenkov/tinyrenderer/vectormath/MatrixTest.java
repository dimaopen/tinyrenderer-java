package com.dopenkov.tinyrenderer.vectormath;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 26.01.16.
 */
@RunWith(Theories.class)
public class MatrixTest {

    @DataPoints
    public static int[] getDimensions() {
        return IntStream.range(1, 10).toArray();
    }

    @Theory
    public void testIdentity(int dim) throws Exception {
        Matrix matrixT = Matrix.identity(dim);
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                assertEquals(i == j ? 1 : 0, matrixT.get(i, j), Double.MIN_NORMAL);
            }
        }
    }

    @Theory
    public void testSetGet(int width) throws Exception {
        int height = 10;
        Matrix matrixT = createMatrix(height, width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                assertEquals(1 + i * width + j, matrixT.get(i, j), Double.MIN_NORMAL);
            }
        }
    }

    @Theory
    public void testRow(int width) throws Exception {
        int height = 10;
        Matrix matrixT = createMatrix(height, width);
        for (int i = 0; i < height; i++) {
            float[] components = new float[width];
            for (int j = 0; j < width; j++) {
                components[j] = 1 + i * width + j;
            }
            assertEquals(new VectorF(components), matrixT.row(i));
        }
    }

    private Matrix createMatrix(int height, int width) {
        Matrix matrixT = new Matrix(height, width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrixT.set(i, j, 1 + i * width + j);
            }
        }
        return matrixT;
    }

    @Theory
    public void testCol(int width) throws Exception {
        int height = 10;
        Matrix matrixT = createMatrix(height, width);
        for (int i = 0; i < width; i++) {
            float[] components = new float[height];
            for (int j = 0; j < height; j++) {
                components[j] = 1 + i + j * width;
            }
            assertEquals(new VectorF(components), matrixT.col(i));
        }
    }

    @Test
    public void testMulMatrix() {
        Matrix m1 = createMatrix(3, 2);
        Matrix m2 = createMatrix(2, 4);
        Matrix expected = new Matrix(new float[]{11.0f, 14.0f, 17.0f, 20.0f,
                23.0f, 30.0f, 37.0f, 44.0f,
                35.0f, 46.0f, 57.0f, 68.0f}, 4);
        assertEquals(expected, m1.mul(m2));
    }

    @Test
    public void testMulVector() {
        Matrix m = createMatrix(3, 2);
        VectorF v = new VectorF(new float[]{2, 4});
        assertEquals(new VectorF(new float[]{10, 22, 34}), m.mul(v));
    }
}