package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.VectorF;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 19.01.16.
 */
public class Model {
    public class Vertex {
        public VectorF location;
        public VectorF normal;
        public VectorF uv;
    }

    private List<Vertex[]> faces;

    public Model(Path objFile) throws FileNotFoundException {
        this(new FileInputStream(objFile.toFile()));
    }

    public Model(InputStream objStream) {
        faces = new ArrayList<>(1024);
        try (Scanner scanner = new Scanner(objStream)) {
            scanner.useDelimiter("\\s+|/");
            scanner.useLocale(Locale.US);
            List<VectorF> vertexes = new ArrayList<>(1024);
            List<VectorF> normals = new ArrayList<>(1024);
            List<VectorF> uvs = new ArrayList<>(1024);
            Pattern v = Pattern.compile("v");
            Pattern vn = Pattern.compile("vn");
            Pattern vt = Pattern.compile("vt");
            Pattern f = Pattern.compile("f");
            int lineNum = 1;
            while (scanner.hasNextLine()) {
                try {
                    if (scanner.hasNext(v)) {
                        vertexes.add(readVector(scanner));
                    } else if (scanner.hasNext(vn)) {
                        normals.add(readVector(scanner));
                    } else if (scanner.hasNext(vt)) {
                        uvs.add(readVector(scanner));
                    } else if (scanner.hasNext(f)) {
                        scanner.next();
                        final Vertex[] face = new Vertex[3];
                        for (int i = 0; i < 3; i++) {
                            Vertex vertex = new Vertex();
                            vertex.location = vertexes.get(scanner.nextInt() - 1);
                            vertex.uv = uvs.get(scanner.nextInt() - 1);
                            vertex.normal = normals.get(scanner.nextInt() - 1);
                            face[i] = vertex;
                        }
                        faces.add(face);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Cannot parse line " + lineNum);
                }
                scanner.nextLine();
                lineNum++;
            }
            System.out.println("vertexes = " + vertexes.size());
            System.out.println("normals = " + normals.size());
            System.out.println("uvs = " + uvs.size());
            System.out.println("faces = " + faces.size());
        }
        /*BufferedImage img = null;
        try {
            img = ImageIO.read(new File("test01.tga"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private VectorF readVector(Scanner scanner) {
        scanner.next();
        float x = scanner.hasNextFloat() ? scanner.nextFloat() : 0;
        float y = scanner.hasNextFloat() ? scanner.nextFloat() : 0;
        float z = scanner.hasNextFloat() ? scanner.nextFloat() : 0;
        return new VectorF(x, y, z);
    }


    public List<Vertex[]> getFaces() {
        return faces;
    }
}
