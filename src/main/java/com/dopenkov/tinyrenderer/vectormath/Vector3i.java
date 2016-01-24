package com.dopenkov.tinyrenderer.vectormath;

import java.util.Objects;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 20.01.16.
 */
public class Vector3i {
    private final int x, y, z;

    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3i(int x, int y) {
        this(x, y, 0);
    }
    
    public Vector3i(VectorF v) {
        v = v.round();
        this.x = (int) v.getX();
        this.y = (int) v.getY();
        this.z = (int) v.getZ();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3i Vector3i = (Vector3i) o;
        return Objects.equals(x, Vector3i.x) &&
                Objects.equals(y, Vector3i.y) &&
                Objects.equals(z, Vector3i.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", x, y, z);
    }
}
