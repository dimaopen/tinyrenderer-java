package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.VectorF;

import java.awt.*;
import java.awt.image.BufferedImage;

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
        lightDirection = new VectorF(1, 3, 1);
    }

    public BufferedImage getRenderedImage() {
        return renderedImage;
    }

    public void render() {
        if (renderedImage == null || renderedImage.getWidth() != outWidth || renderedImage.getHeight() != outHeight) {
            renderedImage = new BufferedImage(outWidth, outHeight, BufferedImage.TYPE_INT_RGB);
        }
        final VectorF one = new VectorF(1, 1, 0);
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};
        for (Model.Vertex[] face : model.getFaces()) {
            for (int i = 0; i < face.length; i++) {
                Model.Vertex v = face[i];
                Model.Vertex next = face[(i + 1) % face.length];
                line(v.location.add(one).scale(outWidth / 2f), next.location.add(one).scale(outWidth / 2f), colors[i]);
            }
        }
    }

    public void line(VectorF p0, VectorF p1, Color color) {
        p0 = p0.round();
        p1 = p1.round();
        int deltaX = (int) abs(p1.getX() - p0.getX());
        int deltaY = (int) abs(p1.getY() - p0.getY());
        boolean steep = deltaX < deltaY;
        int x0, y0, x1, y1;
        if (steep) {
            p0 = p0.swapXY();
            p1 = p1.swapXY();
            int tmp = deltaX;
            deltaX = deltaY;
            deltaY = tmp;
        }
        if (p0.getX() > p1.getX()) {
            VectorF tmp = p0;
            p0 = p1;
            p1 = tmp;
        }
        x0 = (int) p0.getX();
        y0 = (int) p0.getY();
        x1 = (int) p1.getX();
        y1 = (int) p1.getY();
        int stepY = y1 > y0 ? 1 : -1;
        int error = 0;
        int y = y0;
        for (int x = x0; x <= x1; x++) {
            if (steep) {
                setPixel(y, x, color);
            } else {
                setPixel(x, y, color);
            }
            error += deltaY;
            if (2 * error >= deltaX) {
                y += stepY;
                error -= deltaX;
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
