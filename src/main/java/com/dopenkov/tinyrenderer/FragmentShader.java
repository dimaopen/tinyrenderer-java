package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.VectorF;

import java.awt.*;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 26.01.16.
 */
public interface FragmentShader {
    Color fragment(RenderingContext ctx, VectorF barycentric);
}
