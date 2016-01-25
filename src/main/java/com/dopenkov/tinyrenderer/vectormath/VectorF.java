package com.dopenkov.tinyrenderer.vectormath;

import java.util.Arrays;
import java.util.function.BiFunction;

import static java.lang.Math.sqrt;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 19.01.16.
 */
public class VectorF {
    public static final VectorF i = new VectorF(1, 0, 0);
    public static final VectorF j = new VectorF(0, 1, 0);
    public static final VectorF k = new VectorF(0, 0, 1);

    private final float[] components;

    public VectorF(float x, float y, float z) {
        this.components = new float[3];
        this.components[0] = x;
        this.components[1] = y;
        this.components[2] = z;
    }

    public VectorF(float[] components) {
        this.components = components;
    }

    public VectorF(float x, float y) {
        this.components = new float[2];
        this.components[0] = x;
        this.components[1] = y;
    }

    public VectorF(String vString) {
        String[] strs = vString.trim().split("\\s+");
        this.components = new float[strs.length];
        for (int i = 0; i < strs.length; i++) {
            String comp = strs[i];
            try {
                this.components[i] = Float.parseFloat(comp);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Cannot parse float: " + comp + " in " + vString, e);
            }
        }
    }

    public int getNumberOfComponents() {
        return components.length;
    }

    public float getComponent(int i) {
        return components[i];
    }

    public float getX() {
        return components[0];
    }

    public float getY() {
        return components[1];
    }

    public float getZ() {
        return components.length >= 3 ? components[2] : 0;
    }

    public VectorF scale(float scalar) {
        return map((component, num) -> component * scalar);
    }

    public float length() {
        return (float) sqrt(lengthSQ());
    }

    public float lengthSQ() {
        VectorF mul = map((component, num) -> component * component);
        return sumComponents(mul);
    }

    public VectorF normalize() {
        final float lenSQ = this.lengthSQ();
        if (lenSQ == 0f || lenSQ == 1f) return this;
        return this.scale(1f / (float) sqrt(lenSQ));
    }

    public VectorF add(VectorF v) {
        return map((component, num) -> component + v.getComponent(num));
    }

    public VectorF sub(VectorF v) {
        return map((component, num) -> component - v.getComponent(num));
    }

    public float dot(VectorF v) {
        VectorF mul = map((component, num) -> component * v.getComponent(num));
        return sumComponents(mul);
    }

    private float sumComponents(VectorF mul) {
        float sum = 0;
        for (int i = 0; i < mul.components.length; i++) {
            sum += mul.components[i];
        }
        return sum;
    }

    public VectorF cross(VectorF v) {
        //works only for 3 dimension vectors
        return new VectorF(this.getY() * v.getZ() - this.getZ() * v.getY(),
                this.getZ() * v.getX() - this.getX() * v.getZ(),
                this.getX() * v.getY() - this.getY() * v.getX());
    }

    public Vector3i toInt() {
        return new Vector3i(this);
    }

    public VectorF round() {
        return map((component, num) -> (float) Math.round(component));
    }

    public VectorF swapXY() {
        float[] modified = Arrays.copyOf(components, components.length);
        float tmp = modified[0];
        modified[0] = modified[1];
        modified[1] = tmp;
        return new VectorF(modified);
    }

    private VectorF map(BiFunction<Float, Integer, Float> func) {
        float[] modified = new float[components.length];
        for (int i = 0; i < components.length; i++) {
            float component = components[i];
            modified[i] = func.apply(component, i);
        }
        return new VectorF(modified);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VectorF v = (VectorF) o;
        return Arrays.equals(components, v.components);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(components);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(50);
        builder.append('(');
        for (float component : components) {
            if (builder.length() > 1) {
                builder.append(", ");
            }
            builder.append(component);
        }
        builder.append(')');
        return builder.toString();
    }
}
