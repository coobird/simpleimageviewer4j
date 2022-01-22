/*
 * Copyright (c) 2014-2022 Chris Kroells
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

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.util.List;

public final class ViewerPanel extends JPanel {

	/**
	 * Instantiates a {@code ViewerPanel} instance which will be prepared to
	 * display the specified images.
	 * <p>
	 * Changes made to the original {@link List} will not be visible to
	 * the viewer instance.
	 *
	 * @param images The images to display.
	 */
	public ViewerPanel(List<BufferedImage> images) {
		this.setLayout(new BorderLayout());

		final DisplayPanel dp = new DisplayPanel(images);
		this.addKeyListener(new KeyNavigation(dp));

		final NavigationPanel np = new NavigationPanel(dp);
		dp.addListener(np);

		this.add(new JScrollPane(dp), BorderLayout.CENTER);
		this.add(np, BorderLayout.SOUTH);
	}
}
