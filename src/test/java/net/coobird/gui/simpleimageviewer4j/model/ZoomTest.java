/*
 * Copyright (c) 2014-2023 Chris Kroells
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

	@Test(expected = IllegalArgumentException.class)
	public void emptyZoomArrayInConstructor() {
		// given, when, then
		new Zoom(new double[] {}, 1);
	}

	@Test(expected = NullPointerException.class)
	public void nullZoomArrayInConstructor() {
		// given, when, then
		new Zoom(null, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void notAscendingZoomArrayInConstructor() {
		// given, when, then
		new Zoom(new double[] { 1.0, 0.25, 2.5 }, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void zoomArrayContainsZeroInConstructor() {
		// given, when, then
		new Zoom(new double[] { 0.0, 1.0, 2.0 }, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void zoomArrayContainsNegativeInConstructor() {
		// given, when, then
		new Zoom(new double[] { -1.0, 1.0, 2.0 }, 1);
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

	@Test
	public void zoomInWithinBoundary() {
		// given
		Zoom zoom = new Zoom(new double[] { 0.5, 1.0, 2.0 }, 1);

		// when
		zoom.zoomIn();

		// then
		assertEquals(2.0, zoom.getMagnification(), 0.001);
	}

	@Test
	public void zoomOutWithinBoundary() {
		// given
		Zoom zoom = new Zoom(new double[] { 0.5, 1.0, 2.0 }, 1);

		// when
		zoom.zoomOut();

		// then
		assertEquals(0.5, zoom.getMagnification(), 0.001);
	}

	@Test
	public void zoomInPastBoundary() {
		// given
		Zoom zoom = new Zoom(new double[] { 0.5, 1.0, 2.0 }, 1);

		// when
		zoom.zoomIn();
		zoom.zoomIn();

		// then
		assertEquals(2.0, zoom.getMagnification(), 0.001);
	}

	@Test
	public void zoomOutPastBoundary() {
		// given
		Zoom zoom = new Zoom(new double[] { 0.5, 1.0, 2.0 }, 1);

		// when
		zoom.zoomOut();
		zoom.zoomOut();

		// then
		assertEquals(0.5, zoom.getMagnification(), 0.001);
	}

	@Test
	public void zoomInAndOutSamePlace() {
		// given
		Zoom zoom = new Zoom(new double[] { 0.5, 1.0, 2.0 }, 1);

		// when
		zoom.zoomIn();
		zoom.zoomOut();

		// then
		assertEquals(1.0, zoom.getMagnification(), 0.001);
	}
}
