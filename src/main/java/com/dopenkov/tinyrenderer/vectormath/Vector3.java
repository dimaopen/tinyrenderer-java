package com.dopenkov.tinyrenderer.vectormath;

import java.util.Objects;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 19.01.16.
 */
public class Vector3 {
    public static final Vector3 i = new Vector3(1, 0, 0);
    public static final Vector3 j = new Vector3(0, 1, 0);
    public static final Vector3 k = new Vector3(0, 0, 1);

    private final float x, y, z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(float[] components) {
        this(components[0], components[1], components[2]);
    }

    public Vector3(float x, float y) {
        this(x, y, 0);
    }

    public Vector3(String vString) {
        /*for (StringTokenizer tokenizer = new StringTokenizer(vString); tokenizer.hasMoreTokens(); ) {
            String comp = tokenizer.nextToken();

        }*/
        String[] components = vString.trim().split("\\s+");
        try {
            if (components.length == 2) {
                this.x = Float.parseFloat(components[0]);
                this.y = Float.parseFloat(components[1]);
                this.z = 0;
            } else if (components.length == 3) {
                this.x = Float.parseFloat(components[0]);
                this.y = Float.parseFloat(components[1]);
                this.z = Float.parseFloat(components[2]);
            } else {
                throw new IllegalArgumentException("Vector should contain 2 or 3 float numbers separated by spaces");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot parse float: " + vString, e);
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Vector3 scale(float scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    public Vector3 add(Vector3 v) {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    public Vector3i toInt() {
        return new Vector3i(this);
    }

    public Vector3 round() {
        return new Vector3(Math.round(this.x), Math.round(this.y), Math.round(this.z));
    }

    public Vector3 swapXY() {
        return new Vector3(this.y, this.x, this.z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3 vector3 = (Vector3) o;
        return Objects.equals(x, vector3.x) &&
                Objects.equals(y, vector3.y) &&
                Objects.equals(z, vector3.z);
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
