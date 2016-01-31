package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.Matrix;
import com.dopenkov.tinyrenderer.vectormath.VectorF;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 26.01.16.
 */
public class RenderingContext {
    private Matrix viewportMatrix;
    private Matrix projection;
    private Matrix modelView;
    private Matrix transform;
    private VectorF lightingDir;
    private Model model;

    public RenderingContext(Matrix viewportMatrix, Matrix projection, Matrix modelView) {
        this.viewportMatrix = viewportMatrix;
        this.projection = projection;
        this.modelView = modelView;
        transform = viewportMatrix.mul(projection).mul(modelView);
    }

    public Matrix getViewportMatrix() {
        return viewportMatrix;
    }

    public Matrix getProjection() {
        return projection;
    }

    public Matrix getModelView() {
        return modelView;
    }

    public Matrix getTransform() {
        return transform;
    }

    public VectorF getLightingDir() {
        return lightingDir;
    }

    public void setLightingDir(VectorF lightingDir) {
        this.lightingDir = lightingDir;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
