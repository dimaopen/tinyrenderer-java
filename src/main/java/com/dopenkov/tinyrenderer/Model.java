package com.dopenkov.tinyrenderer;

import com.dopenkov.tinyrenderer.vectormath.VectorF;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Logger;
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

    Logger logger = Logger.getLogger(this.getClass().getName());
    private List<Vertex[]> faces;
    private BufferedImage diffuse;

    public Model() {
        faces = new ArrayList<>(1024);
    }

    public void loadData(Path objFile) throws FileNotFoundException {
        logger.info("Loading model data from " + objFile.toString());
        loadData(new BufferedInputStream(new FileInputStream(objFile.toFile())));
        File textureFile = getTextureFile(objFile);
        logger.info("Loading model texture from " + textureFile.getAbsolutePath());
        try {
            loadTexture(new BufferedInputStream(new FileInputStream(textureFile)));
        } catch (FileNotFoundException e) {
            logger.warning("No texture file found " + textureFile.getAbsolutePath());
        }
    }

    private File getTextureFile(Path objFile) {
        String fileName = objFile.getFileName().toString();
        String textureFileName = fileName.substring(0, fileName.lastIndexOf('.')) + "_diffuse.tga";
        return new File(objFile.getParent().toFile(), textureFileName);
    }

    public void loadTexture(InputStream inputStream) {
        try {
            diffuse = ImageIO.read(inputStream);
        } catch (IOException e) {
            logger.warning("Cannot load texture;");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                //do nothing
            }
        }
    }

    public void loadData(InputStream objStream) {
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
    }

    Color getDiffuse(VectorF uv) {
        if (diffuse == null) {
            return Color.WHITE;
        }
        int x = Math.round(uv.getX() * diffuse.getWidth());
        int y = Math.round(uv.getY() * diffuse.getHeight());
        return new Color(diffuse.getRGB(x, diffuse.getHeight() - 1 - y));
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
