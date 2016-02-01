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
    private BufferedImage specular;
    public Model() {
        faces = new ArrayList<>(1024);
    }

    public void loadData(Path objFile) throws FileNotFoundException {
        logger.info("Loading model data from " + objFile.toString());
        loadData(new BufferedInputStream(new FileInputStream(objFile.toFile())));
        diffuse = loadImg(getTextureFile(objFile, "_diffuse.tga"), "texture");
        specular = loadImg(getTextureFile(objFile, "_spec.tga"), "specular map");
    }

    public void loadTexture(InputStream ios) {
        diffuse = loadImg(ios);
    }

    private BufferedImage loadImg(File imgFile, String imgName) {
        logger.info(String.format("Loading model %s from %s", imgName, imgFile.getAbsolutePath()));
        try {
            return loadImg(new BufferedInputStream(new FileInputStream(imgFile)));
        } catch (FileNotFoundException e) {
            logger.warning(String.format("No %s file found %s", imgName, imgFile.getAbsolutePath()));
        }
        return null;
    }

    private File getTextureFile(Path objFile, String suffix) {
        String fileName = objFile.getFileName().toString();
        String textureFileName = fileName.substring(0, fileName.lastIndexOf('.')) + suffix;
        return new File(objFile.getParent().toFile(), textureFileName);
    }

    private BufferedImage loadImg(InputStream inputStream) {
        try {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            logger.warning("Cannot load texture;");
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                //do nothing
            }
        }
        return null;
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
                        vertexes.add(readVector3(scanner));
                    } else if (scanner.hasNext(vn)) {
                        VectorF n = readVector3(scanner);
                        //because in .obj components of normal is reversed
                        //we need to reverse it back
                        normals.add(n.scale(-1));
                    } else if (scanner.hasNext(vt)) {
                        uvs.add(readVector2(scanner));
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
            logger.info("vertexes = " + vertexes.size());
            logger.info("normals = " + normals.size());
            logger.info("uvs = " + uvs.size());
            logger.info("faces = " + faces.size());
        }
    }

    public Color getDiffuse(VectorF uv) {
        if (diffuse == null) {
            return Color.WHITE;
        }
        return getColorFromUV(uv, diffuse);
    }

    public float getSecular(VectorF uv) {
        if (specular == null) {
            return 0;
        }
        Color c = getColorFromUV(uv, specular);
        return c.getRed();
    }

    private Color getColorFromUV(VectorF uv, BufferedImage img) {
        int x = Math.round(uv.getX() * img.getWidth());
        int y = Math.round(uv.getY() * img.getHeight());
        return new Color(img.getRGB(x, img.getHeight() - 1 - y));
    }

    private VectorF readVector3(Scanner scanner) {
        scanner.next();
        float x = scanner.nextFloat();
        float y = scanner.nextFloat();
        float z = scanner.nextFloat();
        return new VectorF(x, y, z);
    }

    private VectorF readVector2(Scanner scanner) {
        scanner.next();
        float x = scanner.nextFloat();
        float y = scanner.nextFloat();
        return new VectorF(x, y);
    }


    public List<Vertex[]> getFaces() {
        return faces;
    }
}
