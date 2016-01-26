package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.Matrix;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 26.01.16.
 */
public class RenderContext {
    private Matrix viewportMatrix;
    private Matrix projection;
    private Matrix modelView;
    private Matrix transform;

    public RenderContext(Matrix viewportMatrix, Matrix projection, Matrix modelView) {
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
}
