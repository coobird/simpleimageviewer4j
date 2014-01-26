package net.coobird.gui.simpleimageviewer4j;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Driver {
	private static BufferedImage getImage(String res) throws IOException {
		InputStream is = ClassLoader.getSystemResourceAsStream(res);
		BufferedImage img = ImageIO.read(is);
		is.close();
		return img;
	}
	
	public static void main(String[] args) throws IOException {
		new Viewer(getImage("grid.png"), getImage("igrid.png")).show();
	}
}
