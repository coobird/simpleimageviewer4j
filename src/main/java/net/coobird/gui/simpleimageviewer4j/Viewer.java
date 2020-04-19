package net.coobird.gui.simpleimageviewer4j;

import net.coobird.gui.simpleimageviewer4j.component.DisplayPanel;
import net.coobird.gui.simpleimageviewer4j.component.KeyNavigation;
import net.coobird.gui.simpleimageviewer4j.component.NavigationPanel;

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * <h1>simpleimageviewer4j - a simple image viewer</h1>
 * <p>
 * The purpose of this class is to provide a simple way to display images
 * in Java (using Swing) to aid in debugging applications which manipulate
 * images.
 * </p>
 * <p>
 * A simple usage would be to specify images to display like the following:
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

	/**
	 * Displays the graphical image viewer.
	 * <p><strong>
	 * Caution: This method should only be called from the AWT Event Dispatch
	 * Thread (EDT).
	 * </strong></p>
	 * <p>
	 * If there is a need to display the viewer from outside of the EDT,
	 * either use {@link SwingUtilities#invokeLater(Runnable)} or, use
	 * the {@link #run()} method which is a convenience method for running
	 * this method ({@link #show()} method) from the EDT.
	 */
	public void show() {
		final JFrame f = new JFrame("Simple Image Viewer");
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setLayout(new BorderLayout());

		final DisplayPanel dp = new DisplayPanel(this.images);
		f.addKeyListener(new KeyNavigation(dp));
		// This will allow focus on the Frame after clicking on one of the
		// navigation buttons. This will subsequently allow use of keyboard
		// to change the images.
		dp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				f.requestFocus();
			}
		});
		
		final NavigationPanel np = new NavigationPanel(dp);
		dp.addListener(np);
		
		f.add(new JScrollPane(dp), BorderLayout.CENTER);
		f.add(np, BorderLayout.SOUTH);
		f.pack();
		
		int frameWidth = f.getWidth();
		int frameHeight = f.getHeight();
		
		DisplayMode displayMode = f.getGraphicsConfiguration().getDevice().getDisplayMode();
		int screenWidth = displayMode.getWidth();
		int screenHeight = displayMode.getHeight();
		
		int x = (screenWidth / 2) - (frameWidth / 2);
		int y = (screenHeight / 2) - (frameHeight / 2);
		f.setLocation(x, y);
		
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
