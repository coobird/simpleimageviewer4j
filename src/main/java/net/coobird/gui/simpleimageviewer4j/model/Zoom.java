package net.coobird.gui.simpleimageviewer4j.model;

public class Zoom {
    private int zoomIndex;

    private final double[] zoomLevels;

    public Zoom(double[] zoomLevels, int defaultZoomIndex) {
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
