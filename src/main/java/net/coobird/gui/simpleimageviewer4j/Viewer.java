package net.coobird.gui.simpleimageviewer4j;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
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
	}
	
	private interface ViewerChangeListener {
		public void imageChanged();
	}
	
	private final class ViewerPanel extends JPanel {
		private int index = 0;
		private List<BufferedImage> images;
		private BufferedImage curImage;
		private List<ViewerChangeListener> listeners = new ArrayList<ViewerChangeListener>();
		
		public ViewerPanel(List<BufferedImage> images) {
			// We'll keep a separate instance of the list, so that changes to
			// the original list will not immediately apply to the images
			// that this class knows of.
			//
			// If adding/removing images are required, a separate interface
			// should be provided, which should keep track of the internal
			// state correctly. (Such as calling all listeners.)
			this.images = new ArrayList<BufferedImage>(images);
			
			curImage = this.images.get(index);
		}
		
		/**
		 * Returns number of images.
		 * @return Number of images.
		 */
		public int count() {
			return images.size();
		}
		
		/**
		 * Returns the index of the current image as a zero-based index.
		 * @return Index of current image.
		 */
		public int current() {
			return index;
		}
		
		public boolean hasPrevious() {
			return index > 0;
		}
		
		public boolean hasNext() {
			return index < images.size() - 1;
		}
		
		private void updateImage(int index) {
			curImage = images.get(index);
			notifyListeners();
			repaint();
		}
		
		public void showPrevious() {
			if (hasPrevious()) {
				updateImage(--index);
			}
		}
		
		public void showNext() {
			if (hasNext()) {
				updateImage(++index);
			}
		}
		
		private void addListener(ViewerChangeListener listener) {
			listeners.add(listener);
		}
		
		private void notifyListeners() {
			for (ViewerChangeListener listener : listeners) {
				listener.imageChanged();
			}
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			// Center image, but show scrollbars when smaller than window.
			int x = (this.getWidth() / 2) - (curImage.getWidth() / 2);
			int y = (this.getHeight() / 2) - (curImage.getHeight() / 2);
			x = Math.max(0, x);
			y = Math.max(0, y);
			
			g.drawImage(curImage, x, y, null);
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(curImage.getWidth(), curImage.getHeight());
		}
	}
	
	private final class NavigationPanel extends JPanel implements ViewerChangeListener {

		private final JButton prevButton = new JButton("<");
		private final JButton nextButton = new JButton(">");
		private final JLabel indicator;
		private final ViewerPanel vp;
		
		public NavigationPanel(final ViewerPanel vp) {
			this.vp = vp;
			this.setLayout(new GridLayout(1, 0));
			
			indicator = new JLabel();
			indicator.setFont(new Font("Monospaced", Font.PLAIN, 14));
			indicator.setHorizontalAlignment(SwingConstants.CENTER);
			
			KeyNavigation kn = new KeyNavigation(vp);
			
			prevButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					vp.showPrevious();
				}
			});
			prevButton.addKeyListener(kn);
			
			nextButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					vp.showNext();
				}
			});
			nextButton.addKeyListener(kn);
			
			this.add(prevButton);
			this.add(indicator);
			this.add(nextButton);
			
			updateButtonStates();
		}
		
		private void updateButtonStates() {
			prevButton.setEnabled(vp.hasPrevious());
			nextButton.setEnabled(vp.hasNext());
			
			// Prevents leaving focus on button which is disabled.
			if (!prevButton.isEnabled() && prevButton.hasFocus()) {
				nextButton.requestFocus();
			}
			if (!nextButton.isEnabled() && nextButton.hasFocus()) {
				prevButton.requestFocus();
			}
			
			indicator.setText("" + (vp.current() + 1) + " / " + vp.count());
		}
		
		public void imageChanged() {
			updateButtonStates();
		}
	}
	
	private final class KeyNavigation extends KeyAdapter {
		private final ViewerPanel vp;

		private KeyNavigation(ViewerPanel vp) {
			this.vp = vp;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_LEFT) {
				vp.showPrevious();
				
			} else if (key == KeyEvent.VK_RIGHT) {
				vp.showNext();
			}
		}
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
		
		final ViewerPanel vp = new ViewerPanel(this.images);
		f.addKeyListener(new KeyNavigation(vp));
		// This will allow focus on the Frame after clicking on one of the
		// navigation buttons. This will subsequently allow use of keyboard
		// to change the images.
		vp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				super.mouseReleased(e);
				f.requestFocus();
			}
		});
		
		final NavigationPanel np = new NavigationPanel(vp);
		vp.addListener(np);
		
		f.add(new JScrollPane(vp), BorderLayout.CENTER);
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
