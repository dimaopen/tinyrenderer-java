package com.dopenkov.tinyrenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 *         Created 19.01.16.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar tinyrenderer.jar path/to/model.obj");
            System.exit(3);
            return;
        }
        Model model = new Model();
        try {
            model.loadData(Paths.get(args[0]));
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find file " + args[0]);
            System.exit(1);
            return;
        }
        Renderer renderer = new Renderer(model);
        renderer.render();
        BufferedImage img = renderer.getRenderedImage();
        File outputfile = new File("result.png");
        System.out.println("Writing result to " + outputfile.getAbsolutePath());

        try {
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {
            System.err.println("Cannot write file " + outputfile.getAbsolutePath());
            System.err.println(e.getMessage());
            System.exit(2);
        }
    }
}
