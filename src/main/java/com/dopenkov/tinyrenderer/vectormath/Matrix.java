package com.dopenkov.tinyrenderer.vectormath;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 25.01.16.
 */
public class Matrix {
    private float[] elements;
    private int width;

    public static Matrix identity(int dim) {
        float[] elements = new float[dim * dim];
        for (int i = 0; i < dim; i++) {
            elements[i * dim + i] = 1;
        }
        return new Matrix(elements, dim);
    }

    public Matrix(int height, int width) {
        elements = new float[width * height];
        this.width = width;
    }

    public Matrix(int dim) {
        this(dim, dim);
    }

    public Matrix(float[] elements, int width) {
        this.width = width;
        if (elements.length % width != 0) {
            throw new IllegalArgumentException("Width must be a divisor of length of elements");
        }
        this.elements = elements;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return elements.length / width;
    }

    public void set(int row, int col, float value) {
        elements[row * width + col] = value;
    }

    public float get(int row, int col) {
        return elements[row * width + col];
    }

    public void setCol(int colNum, VectorF col) {
        int m = this.getHeight();
        if (m != col.getNumberOfComponents()) {
            throw new IllegalArgumentException("Number of rows of matrix must be equal" +
                    " to number of components of vector");
        }
        for (int i = 0; i < m; i++) {
            set(i, colNum, col.getComponent(i));
        }
    }

    public VectorF row(int idx) {
        int from = idx * width;
        float[] components = Arrays.copyOfRange(elements, from, from + width);
        return new VectorF(components);
    }

    public VectorF col(int idx) {
        float[] components = new float[this.getHeight()];
        for (int i = 0; i < components.length; i++) {
            components[i] = get(i, idx);
        }
        return new VectorF(components);
    }

    public Matrix mul(Matrix b) {
        if (this.getWidth() != b.getHeight()) {
            throw new IllegalArgumentException("Number of columns of first matrix must be equal to" +
                    " number of rows in second matrix");
        }
        int m = this.getHeight();
        Matrix result = new Matrix(m, b.getWidth());
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < b.getWidth(); j++) {
                result.set(i, j, this.row(i).dot(b.col(j)));
            }
        }
        return result;
    }

    public VectorF mul(VectorF v) {
        if (this.getWidth() != v.getNumberOfComponents()) {
            throw new IllegalArgumentException("Number of columns of matrix must be equal to" +
                    " number of components of vector");
        }
        int m = this.getHeight();
        float[] components = new float[m];
        for (int i = 0; i < m; i++) {
                components[i] = this.row(i).dot(v);
        }
        return new VectorF(components);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix m = (Matrix) o;
        return width == m.width &&
                Arrays.equals(elements, m.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements, width);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(width * this.getHeight() * 4);
        for (int i = 0; i < this.getHeight(); i++) {
            if (i > 0) {
                sb.append('\n');
            }
            for (int j = 0; j < width; j++) {
                if (j > 0) {
                    sb.append('\t');
                }
                sb.append(this.get(i, j));
            }
        }
        return sb.toString();
    }
}
