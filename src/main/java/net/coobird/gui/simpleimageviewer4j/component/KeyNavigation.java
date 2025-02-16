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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class KeyNavigation extends KeyAdapter {
	private final DisplayPanel dp;

	public KeyNavigation(DisplayPanel dp) {
		this.dp = dp;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		char c = e.getKeyChar();

		if (key == KeyEvent.VK_LEFT) {
			dp.showPrevious();

		} else if (key == KeyEvent.VK_RIGHT) {
			dp.showNext();

		} else if (c == '+') {
			dp.getZoomModel().zoomIn();

		} else if (c == '-') {
			dp.getZoomModel().zoomOut();
		}
	}
}
