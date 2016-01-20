package com.dopenkov.tinyrenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @author <a href="mailto:dimaopen@gmail.com">Dmitry Openkov</a>
 * Created 19.01.16.
 */
public class Main {

    public static void main(String[] args) {
        Model model;
        try {
            model = new Model(Paths.get(args[0]));
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file " + args[0]);
            System.exit(1);
            return;
        }
        Renderer renderer = new Renderer(model);
        renderer.render();
        BufferedImage img = renderer.getRenderedImage();
        File outputfile = new File("result.png");
        try {
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {
            System.out.println("Cannot write file " + outputfile.getAbsolutePath());
            System.out.println(e.getMessage());
            System.exit(2);
        }
    }
}
