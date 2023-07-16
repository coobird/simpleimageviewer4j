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

package net.coobird.gui.simpleimageviewer4j.component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class NavigationPanel extends JPanel implements DisplayChangeListener {

	private final JButton prevButton = new JButton("<");
	private final JButton nextButton = new JButton(">");
	private final JLabel indicator;
	private final DisplayPanel dp;

	public NavigationPanel(final DisplayPanel dp) {
		this.dp = dp;
		this.setLayout(new GridLayout(1, 0));

		indicator = new JLabel();
		indicator.setFont(new Font("Monospaced", Font.PLAIN, 14));
		indicator.setHorizontalAlignment(SwingConstants.CENTER);

		KeyNavigation kn = new KeyNavigation(dp);

		prevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dp.showPrevious();
			}
		});
		prevButton.addKeyListener(kn);

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dp.showNext();
			}
		});
		nextButton.addKeyListener(kn);

		this.add(prevButton);
		this.add(indicator);
		this.add(nextButton);

		updateButtonStates();
		updateIndicator();
	}

	private void updateButtonStates() {
		prevButton.setEnabled(dp.hasPrevious());
		nextButton.setEnabled(dp.hasNext());

		// Prevents leaving focus on button which is disabled.
		if (!prevButton.isEnabled() && prevButton.hasFocus()) {
			nextButton.requestFocus();
		}
		if (!nextButton.isEnabled() && nextButton.hasFocus()) {
			prevButton.requestFocus();
		}
	}

	private void updateIndicator() {
		indicator.setText(
				String.format(
						"%d / %d (%d%%)",
						dp.current() + 1,
						dp.count(),
						Math.round(dp.getMagnification() * 100.0)
				)
		);
	}

	public void imageChanged() {
		updateButtonStates();
		updateIndicator();
	}
}
