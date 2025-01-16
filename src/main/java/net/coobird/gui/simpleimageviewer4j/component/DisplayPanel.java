/*
 * Copyright (c) 2014-2025 Chris Kroells
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

package net.coobird.gui.simpleimageviewer4j.component;

import net.coobird.gui.simpleimageviewer4j.model.Zoom;
import net.coobird.gui.simpleimageviewer4j.model.ZoomChangeListener;
import net.coobird.gui.simpleimageviewer4j.util.Cache;
import net.coobird.gui.simpleimageviewer4j.util.Pair;
import net.coobird.thumbnailator.Thumbnails;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public final class DisplayPanel extends JPanel implements ZoomChangeListener {

	private static final double[] ZOOM_LEVELS = new double[] { 0.25, 0.5, 1.0, 2.0, 4.0 };
	private static final int DEFAULT_ZOOM = 2;
	private final Zoom zoom = new Zoom(ZOOM_LEVELS, DEFAULT_ZOOM);

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

		zoom.addListener(this);
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

		int x = -(getMagnifiedWidth() / 2) + (this.getWidth() / 2);
		int y = -(getMagnifiedHeight() / 2) + (this.getHeight() / 2);
		this.setLocation(new Point(x, y));

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

	public void zoomIn() {
		if (zoom.isZoomInPossible()) {
			zoom.zoomIn();
			this.repaint();
			notifyListeners();
		}
	}

	public void zoomOut() {
		if (zoom.isZoomOutPossible()) {
			zoom.zoomOut();
			this.repaint();
			notifyListeners();
		}
	}

	@Override
	public void zoomChanged(double magnification) {
		repaint();
	}

	public Zoom getZoomModel() {
		return zoom;
	}

	private double getMagnification() {
		return zoom.getMagnification();
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

	private final Cache<Pair<BufferedImage, Double>, BufferedImage> cache =
			new Cache<Pair<BufferedImage, Double>, BufferedImage>();

	private int getMagnifiedWidth() {
		return (int)Math.round(curImage.getWidth() * getMagnification());
	}

	private int getMagnifiedHeight() {
		return (int)Math.round(curImage.getHeight() * getMagnification());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		double magnification = getMagnification();
		final int width = getMagnifiedWidth();
		final int height = getMagnifiedHeight();

		// Center image, but show scrollbars when smaller than window.
		int x = (this.getWidth() / 2) - (width / 2);
		int y = (this.getHeight() / 2) - (height / 2);
		x = Math.max(0, x);
		y = Math.max(0, y);

		drawBackground(g);
		if (magnification < 1.0) {
			try {
				Pair<BufferedImage, Double> key = new Pair<BufferedImage, Double>(curImage, magnification);
				BufferedImage img = cache.computeIfAbsent(key, new Callable<BufferedImage>() {
					@Override
					public BufferedImage call() throws Exception {
						return Thumbnails.of(curImage)
								.size(width, height)
								.asBufferedImage();
					}
				});
				g.drawImage(img, x, y, width, height, null);

			} catch (Exception e) {
				throw new IllegalStateException("This should not happen.", e);
			}

		} else {
			g.drawImage(curImage, x, y, width, height, null);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(getMagnifiedWidth(), getMagnifiedHeight());
	}
}
