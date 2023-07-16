package net.coobird.gui.simpleimageviewer4j.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ZoomTest {

    @Test
    public void normal() {
        // given
        Zoom zoom = new Zoom(new double[] { 0.5, 1.0, 2.0 }, 1);

        // when, then
        assertEquals(1.0, zoom.getMagnification(), 0.001);
    }

    @Test
    public void defaultZoomLeftBoundary() {
        // given
        Zoom zoom = new Zoom(new double[] { 0.5, 1.0, 2.0 }, 0);

        // when, then
        assertEquals(0.5, zoom.getMagnification(), 0.001);
    }

    @Test
    public void defaultZoomRightBoundary() {
        // given
        Zoom zoom = new Zoom(new double[] { 0.5, 1.0, 2.0 }, 2);

        // when, then
        assertEquals(2.0, zoom.getMagnification(), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void defaultZoomIndexOutOfRangePositive() {
        // given, when, then
        new Zoom(new double[] { 0.5, 1.0, 2.0 }, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void defaultZoomIndexOutOfRangeNegative() {
        // given, when, then
        new Zoom(new double[] { 0.5, 1.0, 2.0 }, -1);
    }

}