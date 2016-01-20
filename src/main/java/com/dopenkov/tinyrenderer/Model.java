package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.Vector3;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 19.01.16.
 */
public class Model {
    public class Vertex {
        public Vector3 location;
        public Vector3 normal;
        public Vector3 uv;
    }

    private List<Vertex[]> faces;

    public Model(Path objFile) throws FileNotFoundException {
        this(new FileInputStream(objFile.toFile()));
    }

    public Model(InputStream objStream) {
        faces = new ArrayList<>(1024);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(objStream))) {
            List<Vector3> vertexes = new ArrayList<>(1024);
            List<Vector3> normals = new ArrayList<>(1024);
            List<Vector3> uvs = new ArrayList<>(1024);
            Pattern vectorPattern = Pattern.compile("^(v|(vn)|(vt))\\s+(.+)");
            Pattern facePattern = Pattern.compile("^f\\s+(.+)");
            for (String line; (line = br.readLine()) != null; ) {
                Matcher m = vectorPattern.matcher(line);
                if (m.find()) {
                    //do vector parsing
                    Vector3 vec = new Vector3(m.group(4));

                    final String letter = m.group(1);
                    switch (letter) {
                        case "v":
                            vertexes.add(vec);
                            break;
                        case "vn":
                            normals.add(vec);
                            break;
                        case "vt":
                            uvs.add(vec);
                            break;
                    }
                    continue;
                }
                Matcher fm = facePattern.matcher(line);
                if (fm.find()) {
                    final String faceData = fm.group(1);
                    int i = 0;
                    final Vertex[] face = new Vertex[3];
                    for (StringTokenizer tokenizer = new StringTokenizer(faceData); tokenizer.hasMoreTokens(); i++) {
                        String s = tokenizer.nextToken();
                        String[] numbers = s.split("/");
                        Vertex v = new Vertex();
                        try {
                            v.location = vertexes.get(Integer.parseInt(numbers[0]) - 1);
                            v.uv = uvs.get(Integer.parseInt(numbers[1]) - 1);
                            v.normal = normals.get(Integer.parseInt(numbers[2]) - 1);
                            face[i] = v;
                        } catch (Exception e) {
                            throw new RuntimeException("Cannot parse face from string " + faceData, e);
                        }
                    }
                    faces.add(face);
                }
            }
            System.out.println("vertexes = " + vertexes.size());
            System.out.println("normals = " + normals.size());
            System.out.println("uvs = " + uvs.size());
            System.out.println("faces = " + faces.size());
        } catch (IOException e) {
            throw new RuntimeException("Cannot read from stream", e);
        }
        /*BufferedImage img = null;
        try {
            img = ImageIO.read(new File("/Users/Dmitry/Projects/samples/tinyrenderer-tutorial/obj/african_head_diffuse.tga"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    public List<Vertex[]> getFaces() {
        return faces;
    }
}
