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

package net.coobird.gui.simpleimageviewer4j.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Zoom {
	private int zoomIndex;

	private final double[] zoomLevels;
	private final List<ZoomChangeListener> listeners = new ArrayList<ZoomChangeListener>();

	private static void checkArrayInAscendingOrder(double[] zoomLevels) {
		double[] tmpLevels = new double[zoomLevels.length];
		System.arraycopy(zoomLevels, 0, tmpLevels, 0, zoomLevels.length);
		Arrays.sort(tmpLevels);

		if (!Arrays.equals(tmpLevels, zoomLevels)) {
			throw new IllegalArgumentException("Zoom levels array not in ascending order");
		}
	}

	public Zoom(double[] zoomLevels, int defaultZoomIndex) {
		if (zoomLevels == null) {
			throw new NullPointerException("Zoom levels array cannot be null.");
		}
		if (zoomLevels.length == 0) {
			throw new IllegalArgumentException("Zoom levels array cannot be empty.");
		}

		checkArrayInAscendingOrder(zoomLevels);

		for (Double value : zoomLevels) {
			if (value <= 0.0) {
				throw new IllegalArgumentException("Zoom magnigication must be a positive, non-zero value.");
			}
		}

		if (defaultZoomIndex > zoomLevels.length - 1 || defaultZoomIndex < 0) {
			throw new IllegalArgumentException("Default zoom level out of range.");
		}
		this.zoomLevels = zoomLevels;
		this.zoomIndex = defaultZoomIndex;
	}

	public void addListener(ZoomChangeListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ZoomChangeListener listener) {
		listeners.remove(listener);
	}

	public void notifyListeners() {
		for (ZoomChangeListener listener : listeners) {
			listener.zoomChanged(zoomLevels[zoomIndex]);
		}
	}

	public void zoomIn() {
		if (isZoomInPossible()) {
			zoomIndex++;
			notifyListeners();
		}
	}

	public void zoomOut() {
		if (isZoomOutPossible()) {
			zoomIndex--;
			notifyListeners();
		}
	}

	public double[] getZoomLevels() {
		double[] tmp = new double[zoomLevels.length];
		System.arraycopy(zoomLevels, 0, tmp, 0, zoomLevels.length);
		return tmp;
	}
	
	public void setMagnification(double zoom) {
		for (int i = 0; i < zoomLevels.length; i++) {
			if (zoom == zoomLevels[i]) {
				zoomIndex = i;
				notifyListeners();
				return;
			}
		}
		throw new IllegalArgumentException(
				String.format("%s is not a valid magnification level.", zoom)
		);
	}

	public boolean isZoomInPossible() {
		return zoomIndex < zoomLevels.length - 1;
	}

	public boolean isZoomOutPossible() {
		return zoomIndex > 0;
	}

	public double getMagnification() {
		return zoomLevels[zoomIndex];
	}
}
