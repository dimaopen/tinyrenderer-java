package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.VectorF;

import java.awt.Color;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 31.01.16.
 */
public interface Shader {
    VectorF vertex(RenderingContext ctx, Model.Vertex vertex, int vertexNum);

    Color fragment(RenderingContext ctx, VectorF barycentric);

}
