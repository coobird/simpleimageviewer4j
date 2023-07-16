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
        if (zoomIndex < zoomLevels.length - 1) {
            zoomIndex++;
        }
    }

    public void zoomOut() {
        if (zoomIndex > 0) {
            zoomIndex--;
        }
    }

    public double getMagnification() {
        return zoomLevels[zoomIndex];
    }
}
