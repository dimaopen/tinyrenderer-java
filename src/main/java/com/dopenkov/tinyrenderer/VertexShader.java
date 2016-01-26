package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.VectorF;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 26.01.16.
 */
public interface VertexShader {
    VectorF vertex(RenderingContext ctx, Model.Vertex vertex, int vertexNum);
}
