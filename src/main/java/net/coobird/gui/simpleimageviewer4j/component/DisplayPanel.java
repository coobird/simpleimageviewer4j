package net.coobird.gui.simpleimageviewer4j.component;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public final class DisplayPanel extends JPanel {
	private int index = 0;
	private List<BufferedImage> images;
	private BufferedImage curImage;
	private List<DisplayChangeListener> listeners = new ArrayList<DisplayChangeListener>();

	public DisplayPanel(List<BufferedImage> images) {
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

		int x = -(curImage.getWidth() / 2) + (this.getWidth() / 2);
		int y = -(curImage.getHeight() / 2) + (this.getHeight() / 2);
		this.setLocation(new Point(x,y));

		// Forces layout of parent, so that the scrollbar will appear when
		// displaying a large image.
		// doLayout() forces a call to getPreferredSize and getLocation.
		this.getParent().doLayout();
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

	public void addListener(DisplayChangeListener listener) {
		listeners.add(listener);
	}

	private void notifyListeners() {
		for (DisplayChangeListener listener : listeners) {
			listener.imageChanged();
		}
	}

	private static final int CHECKER_WIDTH = 10;
	private static final int CHECKER_HEIGHT = 10;

	private void drawBackground(Graphics g) {
		Rectangle visibleRect = this.getVisibleRect();
		int width = visibleRect.x + visibleRect.width;
		int height = visibleRect.y + visibleRect.height;
		int startX = (visibleRect.x / (CHECKER_WIDTH * 2)) * (CHECKER_WIDTH * 2);
		int startY = (visibleRect.y / (CHECKER_HEIGHT * 2)) * (CHECKER_HEIGHT * 2);

		g.setColor(Color.gray);
		g.fillRect(visibleRect.x, visibleRect.y, visibleRect.width, visibleRect.height);

		g.setColor(Color.lightGray);
		for (int j = startY; j <= height; j += CHECKER_HEIGHT * 2) {
			for (int i = startX; i <= width; i += CHECKER_WIDTH * 2) {
				g.fillRect(i, j, CHECKER_WIDTH, CHECKER_HEIGHT);
				g.fillRect(i + CHECKER_WIDTH, j + CHECKER_HEIGHT, CHECKER_WIDTH, CHECKER_HEIGHT);
			}
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

		drawBackground(g);
		g.drawImage(curImage, x, y, null);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(curImage.getWidth(), curImage.getHeight());
	}
}
