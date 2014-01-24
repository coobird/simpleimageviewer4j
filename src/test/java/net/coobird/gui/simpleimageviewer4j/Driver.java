package net.coobird.gui.simpleimageviewer4j;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Driver {
	public static void main(String[] args) throws IOException {
		InputStream is = ClassLoader.getSystemResourceAsStream("grid.png");
		BufferedImage img = ImageIO.read(is);
		is.close();
		
		new Viewer(img).show();
	}
}
