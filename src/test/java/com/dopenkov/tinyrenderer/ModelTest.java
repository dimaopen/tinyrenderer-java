package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.VectorF;
import org.junit.Test;

import java.awt.*;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 19.01.16.
 */
public class ModelTest {

    @Test
    public void testReadingObj() {
        Model modelT = new Model();
        modelT.loadData(this.getClass().getResourceAsStream("/model/test_model.obj"));
        List<Model.Vertex[]> faces = modelT.getFaces();
        assertEquals(50054, faces.size());
        Model.Vertex[] face0 = faces.get(0);
        assertEquals(3, face0.length);
        assertEquals(new VectorF(0.028911f,  0.137885f, 0.150147f), face0[0].location);
        assertEquals(new VectorF(0.122546f, 0.262678f), face0[0].uv);
        assertEquals(new VectorF(0.124554f, 0.265355f), face0[1].uv);
        assertEquals(new VectorF(0.589900f, -0.453900f, -0.667800f), face0[2].normal);

        Model.Vertex[] faceLast = faces.get(faces.size() - 1);
        assertEquals(3, faceLast.length);
        assertEquals(new VectorF(0.205570f, -0.159949f, 0.068638f), faceLast[2].location);
    }

    @Test
    public void testDiffuse() {
        Model modelT = new Model();
        modelT.loadTexture(this.getClass().getResourceAsStream("/model/test_model_diffuse.tga"));
        assertEquals(new Color(193, 197, 234), modelT.getDiffuse(new VectorF(.259f, .3525f)));
    }
}