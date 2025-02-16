/*
 * Copyright (c) 2014-2024 Chris Kroells
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.coobird.gui.simpleimageviewer4j;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * This is driver code for using simpleimageviewer4j.
 * It will initialize a viewer loaded with some test images.
 */
public class Driver {
	private static BufferedImage getImage(String res) throws IOException {
		InputStream is = ClassLoader.getSystemResourceAsStream(res);
		BufferedImage img = ImageIO.read(is);
		is.close();
		return img;
	}

	private static BufferedImage makeImage(Color c) {
		int size = 500;
		int halfSize = size / 2;

		BufferedImage img = new BufferedImage(size,size, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setColor(c);
		g.fillRect(0, 0, halfSize, size);
		g.setColor(Color.black);
		g.fillRect(halfSize, 0, halfSize, size);
		g.dispose();

		return img;
	}
	
	public static void main(String[] args) throws IOException {
		new Viewer(
				getImage("grid.png"),
				makeImage(Color.blue),
				getImage("igrid.png"),
				makeImage(Color.green),
				makeImage(Color.red)
		).show();
	}
}
