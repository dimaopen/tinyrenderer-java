package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.VectorF;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static java.lang.Math.abs;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 19.01.16.
 */
public class Renderer {
    public int outWidth, outHeight;
    public VectorF lookat, cameraLocation, cameraUp;
    public VectorF lightDirection;
    private Model model;
    private BufferedImage renderedImage;

    public Renderer(Model model) {
        this.model = model;
        outWidth = 600;
        outHeight = 600;
        lookat = new VectorF(0, 0, 0);
        cameraLocation = new VectorF(0, 0, 5);
        cameraUp = new VectorF(0, 1, 0);
        lightDirection = new VectorF(0, 0, -1);
    }

    public BufferedImage getRenderedImage() {
        return renderedImage;
    }

    public void render() {
        if (renderedImage == null || renderedImage.getWidth() != outWidth || renderedImage.getHeight() != outHeight) {
            renderedImage = new BufferedImage(outWidth, outHeight, BufferedImage.TYPE_INT_RGB);
        }
        lightDirection = lightDirection.normalize();
        final VectorF one = new VectorF(1, 1, 0);
        float[] zbuffer = new float[outWidth * outHeight];
        Arrays.fill(zbuffer, Float.NEGATIVE_INFINITY);
        for (Model.Vertex[] face : model.getFaces()) {
            VectorF faceNormal = face[2].location.sub(face[0].location).cross(face[1].location.sub(face[0].location)).normalize();
            float intensity = faceNormal.dot(lightDirection);
            if (intensity <= 0) {
                continue;
            }
            Color color = new Color(Math.round(255 * intensity), Math.round(255 * intensity), Math.round(255 * intensity));
            VectorF[] screenCoord = Arrays.stream(face)
                    .map(vertex -> vertex.location.add(one).scale(outWidth / 2f))
                    .toArray(VectorF[]::new);
            triangle(screenCoord, color, zbuffer);
        }
    }

    VectorF barycentric(VectorF A, VectorF B, VectorF C, VectorF P) {
        VectorF s0 = new VectorF(C.getX() - A.getX(), B.getX() - A.getX(), A.getX() - P.getX());
        VectorF s1 = new VectorF(C.getY() - A.getY(), B.getY() - A.getY(), A.getY() - P.getY());

        VectorF u = s0.cross(s1);
        if (abs(u.getZ()) > .01) {
            // If z component is zero then triangle ABC is degenerate
            return new VectorF(1.f - (u.getX() + u.getY()) / u.getZ(), u.getY() / u.getZ(), u.getX() / u.getZ());
        }
        // in this case generate negative coordinates, it will be thrown away by the rasterizator
        return new VectorF(-1, 1, 1);
    }

    void triangle(VectorF points[], Color color, float[] zbuffer) {
        VectorF[] pts = Arrays.stream(points).map(VectorF::round).toArray(VectorF[]::new);
        Arrays.sort(pts, (v1, v2) -> v1.getX() < v2.getX() ? -1 : v1.getX() == v2.getX() ? 0 : 1);
        int minX = (int) Math.max(pts[0].getX(), 0);
        int maxX = (int) Math.min(pts[2].getX(), outWidth - 1);
        Arrays.sort(pts, (v1, v2) -> v1.getY() < v2.getY() ? -1 : v1.getY() == v2.getY() ? 0 : 1);
        int minY = (int) Math.max(pts[0].getY(), 0);
        int maxY = (int) Math.min(pts[2].getY(), outHeight - 1);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                VectorF P = new VectorF(x, y);
                VectorF c = barycentric(pts[0], pts[1], pts[2], P);
                //find z of our point P
                //we need just weighted sum of z component of each point of our triangle
                float z = pts[0].getZ() * c.getComponent(0) + pts[1].getZ() * c.getComponent(1) + pts[2].getZ() * c.getComponent(2);
                int idx = x + y * outWidth;
                if (c.getX() < 0 || c.getY() < 0 || c.getZ() < 0 || zbuffer[idx] >= z) continue;
                zbuffer[idx] = z;
                setPixel(x, y, color);
            }
        }
    }

    private void setPixel(int x, int y, Color color) {
        //flip vertically
        y = outHeight - y;
        if (x < 0 || y < 0 || x >= outWidth || y >= outHeight) {
            return;
        }
        renderedImage.setRGB(x, y, color.getRGB());
    }

}
