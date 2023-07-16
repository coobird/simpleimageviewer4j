package net.coobird.gui.simpleimageviewer4j.model;

import java.util.Arrays;

public class Zoom {
	private int zoomIndex;

	private final double[] zoomLevels;

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

	public void zoomIn() {
		if (isZoomInPossible()) {
			zoomIndex++;
		}
	}

	public void zoomOut() {
		if (isZoomOutPossible()) {
			zoomIndex--;
		}
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
