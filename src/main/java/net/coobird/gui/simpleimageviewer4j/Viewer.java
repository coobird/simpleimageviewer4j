/*
 * Copyright (c) 2014-2023 Chris Kroells
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

import net.coobird.gui.simpleimageviewer4j.component.ViewerPanel;

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * <h1>simpleimageviewer4j - a simple image viewer</h1>
 * <p>
 * This class provides a simple way to display images in Java using Swing.
 * It's intended to aid in debugging applications which manipulate images
 * while developing on a desktop environment.
 * </p>
 * <p>
 * A simple usage is to specify images to display like this:
 * </p>
 * <pre>
// Images that we're working on.
BufferedImage img1 = ...
BufferedImage img2 = ...

// A window to view `img1` and `img2` will be shown.
new Viewer(img1, img2).show();
</pre>
 * <p>
 * Using a Collection (such as a List) to specify images to display is
 * also supported:
 * </p>
 * <pre>
// A `List` containing images that we're working on.
List&lt;BufferedImage&gt; images = ...

// A window to view images contained in `images` will be shown.
new Viewer(images).show();
</pre>
 * 
 * @author coobird
 *
 */
public final class Viewer {
	private final List<BufferedImage> images;
	
	private void checkImagesForNull() {
		for (BufferedImage image : images) {
			if (image == null) {
				throw new NullPointerException("A null image was provided.");
			}
		}
	}
	
	/**
	 * Instantiates a {@code Viewer} instance which will be prepared to
	 * display the specified images. To display the images, call the
	 * {@link #show()} or {@link #run()} method on the {@code Viewer}
	 * instance.
	 * <p>
	 * If the argument is passed as an array, changes made to the original
	 * array will not be visible to the viewer instance.
	 * 
	 * @param images The images to display.
	 */
	public Viewer(BufferedImage... images) {
		if (images == null || images.length == 0) {
			throw new NullPointerException("Must specify images.");
		}
		
		this.images = new ArrayList<BufferedImage>(Arrays.asList(images));
		checkImagesForNull();
	}
	
	/**
	 * Instantiates a {@code Viewer} instance which will be prepared to
	 * display the specified images. To display the images, call the
	 * {@link #show()} or {@link #run()} method on the {@code Viewer}
	 * instance.
	 * <p>
	 * Changes made to the original {@link Collection} will not be visible to
	 * the viewer instance.
	 * 
	 * @param images The images to display.
	 */
	public Viewer(Collection<BufferedImage> images) {
		if (images == null || images.size() == 0) {
			throw new NullPointerException("Must specify images.");
		}
		
		this.images = new ArrayList<BufferedImage>(images);
		checkImagesForNull();
	}

	private Point getCenterOfScreen(Frame f) {
		int frameWidth = f.getWidth();
		int frameHeight = f.getHeight();

		DisplayMode displayMode = f.getGraphicsConfiguration().getDevice().getDisplayMode();

		int screenWidth = displayMode.getWidth();
		int screenHeight = displayMode.getHeight();

		int x = (screenWidth / 2) - (frameWidth / 2);
		int y = (screenHeight / 2) - (frameHeight / 2);

		return new Point(x, y);
	}

	/**
	 * Displays the graphical image viewer.
	 * <p><strong>
	 * Caution: This method should only be called from the AWT Event Dispatch
	 * Thread (EDT).
	 * </strong></p>
	 * <p>
	 * If it is necessary to display the viewer from outside the EDT,
	 * either use {@link SwingUtilities#invokeLater(Runnable)} or, use
	 * the {@link #run()} method which is a convenience method for running
	 * this method from the EDT.
	 */
	public void show() {
		final JFrame f = new JFrame("Simple Image Viewer");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setLayout(new BorderLayout());

		f.add(new ViewerPanel(images), BorderLayout.CENTER);
		f.pack();

		f.setLocation(getCenterOfScreen(f));
		f.setVisible(true);
	}

	/**
	 * Displays the graphical image viewer.
	 * <p>
	 * This method will ensure that the viewer is created from the AWT Event
	 * Dispatch Thread (EDT).
	 */
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				show();
			}
		});
	}
}
