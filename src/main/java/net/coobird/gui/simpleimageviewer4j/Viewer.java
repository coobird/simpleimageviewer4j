package net.coobird.gui.simpleimageviewer4j;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Viewer {
	private final List<BufferedImage> images;
	
	public Viewer(BufferedImage... images) {
		if (images == null || images.length == 0) {
			throw new NullPointerException("Must specify images.");
		}
		
		this.images = Arrays.asList(images);
	}
	
	public Viewer(Collection<BufferedImage> images) {
		if (images == null || images.size() == 0) {
			throw new NullPointerException("Must specify images.");
		}
		
		this.images = new ArrayList<BufferedImage>(images);
	}
	
	private interface ViewerChangeListener {
		public void imageChanged();
	}
	
	private class ViewerPanel extends JPanel {
		private int index = 0;
		private BufferedImage curImage;
		private List<ViewerChangeListener> listeners = new ArrayList<ViewerChangeListener>();
		
		public ViewerPanel() {
			curImage = images.get(index);
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
		
		public void showPrevious() {
			index = Math.max(0, --index);
			curImage = images.get(index);
			notifyListeners();
			repaint();
		}
		
		public void showNext() {
			index = Math.min(++index, images.size() - 1);
			curImage = images.get(index);
			notifyListeners();
			repaint();
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
	
	private class NavigationPanel extends JPanel implements ViewerChangeListener {

		private final JButton prevButton = new JButton("Previous");
		private final JButton nextButton = new JButton("Next");
		private final ViewerPanel vp;
		
		public NavigationPanel(final ViewerPanel vp) {
			this.vp = vp;
			
			this.setLayout(new GridLayout(1, 0));
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
	
	public void show() {
		final JFrame f = new JFrame("Simple Image Viewer");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLayout(new BorderLayout());
		
		final ViewerPanel vp = new ViewerPanel();
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
	
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				show();
			}
		});
	}
}
