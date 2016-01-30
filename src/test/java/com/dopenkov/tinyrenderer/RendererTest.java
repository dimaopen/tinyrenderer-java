package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.VectorF;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 29.01.16.
 */
public class RendererTest {

    @Test
    public void testBarycentric() throws Exception {
        assertEquals(new VectorF(0, 1, 0),  bc(new VectorF(10, 0, 0)));
        assertEquals(new VectorF(0, 0, 1), bc(new VectorF(5, 10, 0)));
        assertEquals(new VectorF(.25f, .25f, .5f), bc(new VectorF(5, 5, 0)));
        assertEquals(new VectorF(.3f, .3f, .4f), bc(new VectorF(5, 4, 0)));
        assertEquals(new VectorF(.85f, .05f, .1f), bc(new VectorF(1, 1, 0)));
        Renderer rendererT = new Renderer(null);
        VectorF result = rendererT.barycentric(
                new VectorF(390.851501f, 298.794495f, 0),
                new VectorF(388.478668f, 302.37854f, 0),
                new VectorF(394.836975f, 301.919678f, 0), new VectorF(388, 298));
        assertEquals(new VectorF(1.29309618f, 0.264751941f, -0.557848155f), result);
    }

    private VectorF bc(VectorF P) {
        Renderer rendererT = new Renderer(null);
        return rendererT.barycentric(new VectorF(0, 0, 0), new VectorF(10, 0, 0), new VectorF(5, 10, 0), P);
    }
}