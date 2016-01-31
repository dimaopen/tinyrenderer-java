package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.Matrix;
import com.dopenkov.tinyrenderer.vectormath.VectorF;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static java.lang.Math.abs;
import static java.lang.Math.round;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 19.01.16.
 */
public class Renderer {
    public int outWidth, outHeight;
    public VectorF lookAt, cameraLocation, cameraUp;
    public VectorF lightDirection;
    private Model model;
    private BufferedImage renderedImage;

    public Renderer(Model model) {
        this.model = model;
        outWidth = 800;
        outHeight = 800;
        lookAt = new VectorF(0, 0, 0);
        cameraLocation = new VectorF(1, 1, 3);
        cameraUp = new VectorF(0, 1, 0);
        lightDirection = new VectorF(-1, -1, -1);
    }

    public BufferedImage getRenderedImage() {
        return renderedImage;
    }

    public void render() {
        if (renderedImage == null || renderedImage.getWidth() != outWidth || renderedImage.getHeight() != outHeight) {
            renderedImage = new BufferedImage(outWidth, outHeight, BufferedImage.TYPE_INT_RGB);
        }

        Matrix lookAt = lookAt(cameraLocation, this.lookAt, cameraUp);
        Matrix viewport = viewport(outWidth / 8, outHeight / 8, outWidth * 3 / 4, outHeight * 3 / 4);
        Matrix projection = projection(-1.f / cameraLocation.sub(this.lookAt).length());
        RenderingContext ctx = new RenderingContext(viewport, projection, lookAt);
        ctx.setLightingDir(lightDirection.normalize());
        ctx.setModel(model);

        Shader shader = new GouraudShader();
        float[] zbuffer = createZBuffer();
        for (Model.Vertex[] face : model.getFaces()) {
            VectorF[] screenCoord = new VectorF[3];
            for (int i = 0; i < 3; i++) {
                screenCoord[i] = shader.vertex(ctx, face[i], i);
            }
            triangle(screenCoord, ctx, shader, zbuffer);
        }
    }

    private float[] createZBuffer() {
        float[] zbuffer = new float[outWidth * outHeight];
        Arrays.fill(zbuffer, Float.NEGATIVE_INFINITY);
        return zbuffer;
    }

    private Matrix viewport(int x, int y, int w, int h) {
        Matrix viewportMatrix = Matrix.identity(4);
        viewportMatrix.set(0, 3, x + w / 2.f);
        viewportMatrix.set(1, 3, y + h / 2.f);
        viewportMatrix.set(2, 3, 255.f / 2.f);
        viewportMatrix.set(0, 0, w / 2.f);
        viewportMatrix.set(1, 1, h / 2.f);
        viewportMatrix.set(2, 2, 255.f / 2.f);
        return viewportMatrix;
    }

    private Matrix projection(float coeff) {
        Matrix projection = Matrix.identity(4);
        projection.set(3, 2, coeff);
        return projection;
    }

    private Matrix lookAt(VectorF cameraLocation, VectorF lookAt, VectorF up) {
        VectorF z = cameraLocation.sub(lookAt).normalize();
        VectorF x = up.cross(z).normalize();
        VectorF y = z.cross(x).normalize();
        Matrix modelView = Matrix.identity(4);
        for (int i = 0; i < 3; i++) {
            modelView.set(0, i, x.getComponent(i));
            modelView.set(1, i, y.getComponent(i));
            modelView.set(2, i, z.getComponent(i));
            modelView.set(i, 3, -lookAt.getComponent(i));
        }
        return modelView;
    }

    VectorF barycentric(VectorF A, VectorF B, VectorF C, VectorF P) {
        VectorF s0 = new VectorF(C.getX() - A.getX(), B.getX() - A.getX(), A.getX() - P.getX());
        VectorF s1 = new VectorF(C.getY() - A.getY(), B.getY() - A.getY(), A.getY() - P.getY());

        VectorF u = s0.cross(s1);
        if (abs(u.getZ()) > .00001f) {
            return new VectorF(1.f - (u.getX() + u.getY()) / u.getZ(), u.getY() / u.getZ(), u.getX() / u.getZ());
        }
        // If z component is zero then triangle ABC is degenerate
        // in this case generate negative coordinates, it will be thrown away by the rasterizer
        return new VectorF(-1, 1, 1);
    }

    private void triangle(VectorF points[], RenderingContext ctx, Shader fragmentShader, float[] zbuffer) {
        VectorF[] rounded = Arrays.stream(points).map(VectorF::round).toArray(VectorF[]::new);
        Arrays.sort(rounded, (v1, v2) -> v1.getX() < v2.getX() ? -1 : v1.getX() == v2.getX() ? 0 : 1);
        int minX = (int) Math.max(rounded[0].getX(), 0);
        int maxX = (int) Math.min(rounded[2].getX(), outWidth - 1);
        Arrays.sort(rounded, (v1, v2) -> v1.getY() < v2.getY() ? -1 : v1.getY() == v2.getY() ? 0 : 1);
        int minY = (int) Math.max(rounded[0].getY(), 0);
        int maxY = (int) Math.min(rounded[2].getY(), outHeight - 1);
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                VectorF P = new VectorF(x, y);
                VectorF c = barycentric(points[0], points[1], points[2], P);
                //find z of our point P
                //we need just weighted sum of z component of each point of our triangle
                float z = points[0].getZ() * c.getComponent(0) + points[1].getZ() * c.getComponent(1) + points[2].getZ() * c.getComponent(2);
                int idx = x + y * outWidth;
                if (c.getX() < 0 || c.getY() < 0 || c.getZ() < 0 || zbuffer[idx] >= z) continue;
                zbuffer[idx] = z;
                Color color = fragmentShader.fragment(ctx, c);
                if (color != null) {
                    setPixel(x, y, color);
                }
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

class GouraudShader implements Shader {
    private Matrix varying_uv = new Matrix(2, 3);
    private float[] varying_intencity = new float[3];

    @Override
    public VectorF vertex(RenderingContext ctx, Model.Vertex vertex, int vertexNum) {
        varying_intencity[vertexNum] = vertex.normal.dot(ctx.getLightingDir());
        varying_uv.setCol(vertexNum, vertex.uv);
        VectorF gl_Vertex = vertex.location.embedded();
        gl_Vertex = ctx.getTransform().mul(gl_Vertex);     // transform it to screen coordinates
        float lastComp = gl_Vertex.getComponent(gl_Vertex.getNumberOfComponents() - 1);
        return gl_Vertex.proj().scale(1.0f / lastComp);                  // project homogeneous coordinates to 3d
    }

    @Override
    public Color fragment(RenderingContext ctx, VectorF bc) {
        float intensity = new VectorF(varying_intencity).dot(bc);
        if (intensity < 0) {
            return Color.BLACK;
        }
        VectorF uv = varying_uv.mul(bc);
        Color c = ctx.getModel().getDiffuse(uv);
        return new Color(round(c.getRed() * intensity), round(c.getGreen() * intensity), round(c.getBlue() * intensity));
    }
}