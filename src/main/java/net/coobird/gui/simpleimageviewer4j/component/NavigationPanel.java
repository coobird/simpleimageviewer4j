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

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;

public final class NavigationPanel extends JPanel implements DisplayChangeListener {

	private final JButton prevButton = new JButton("<");
	private final JButton nextButton = new JButton(">");
	private final ZoomButton zoomInButton;
	private final ZoomButton zoomOutButton;
	private final ZoomLevelComboBox zoomLevelList;

	private final JLabel indicator;
	private final DisplayPanel dp;

	private abstract static class ZoomButton extends JButton implements ZoomChangeListener {
		protected final Zoom zoomModel;
		private ZoomButton(Zoom zoomModel, String title) {
			super(title);
			this.zoomModel = zoomModel;
		}
	}
	
	private static class ZoomLevelComboBox extends JComboBox implements ZoomChangeListener {
		private static ComboBoxModel createModel(Zoom zoomModel) {
			double[] zoomLevels = zoomModel.getZoomLevels();
			Double[] tmp = new Double[zoomLevels.length];
			for (int i = 0; i < zoomLevels.length; i++) {
				tmp[i] = zoomLevels[i];
			}
			return new DefaultComboBoxModel(tmp);
		}

		private ZoomLevelComboBox(final Zoom zoomModel) {
			super(createModel(zoomModel));
			setSelectedItem(zoomModel.getMagnification());

			this.setRenderer(new DefaultListCellRenderer() {
				@Override
				public Component getListCellRendererComponent(JList list, Object value, int i, boolean isSelected, boolean hasFocus) {
					JLabel component = (JLabel) super.getListCellRendererComponent(list, value, i, isSelected, hasFocus);
					double zoom = (Double) value;
					component.setText(String.format("%d%%", Math.round(zoom * 100.0)));
					return component;
				}
			});

			this.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent itemEvent) {
					double selectedMagnification = (Double) itemEvent.getItem();
					zoomModel.setMagnification(selectedMagnification);
				}
			});
		}

		@Override
		public void zoomChanged(double magnification) {
			// Find out if component had focus. If not, unfocus after selecting item.
			boolean hasFocus = hasFocus();
			setSelectedItem(magnification);
			if (!hasFocus) {
				transferFocus();
			}
		}
	}

	public NavigationPanel(final DisplayPanel dp) {
		this.dp = dp;
		this.setLayout(new GridLayout(1, 0));

		indicator = new JLabel();
		indicator.setFont(new Font("Monospaced", Font.PLAIN, 14));
		indicator.setHorizontalAlignment(SwingConstants.CENTER);

		final Zoom zoomModel = dp.getZoomModel();

		zoomLevelList = new ZoomLevelComboBox(zoomModel);
		zoomModel.addListener(zoomLevelList);

		KeyNavigation kn = new KeyNavigation(dp);

		ResourceBundle messages = ResourceBundle.getBundle("i18n.messages");

		prevButton.setToolTipText(messages.getString("PreviousImage"));
		prevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dp.showPrevious();
			}
		});
		prevButton.addKeyListener(kn);

		nextButton.setToolTipText(messages.getString("NextImage"));
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dp.showNext();
			}
		});
		nextButton.addKeyListener(kn);

		zoomInButton = new ZoomButton(zoomModel, "+") {
			@Override
			public void zoomChanged(double magnification) {
				setEnabled(zoomModel.isZoomInPossible());
			}
		};
		zoomInButton.setToolTipText(messages.getString("ZoomIn"));
		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomModel.zoomIn();
			}
		});
		zoomInButton.addKeyListener(kn);
		zoomModel.addListener(zoomInButton);

		zoomOutButton = new ZoomButton(zoomModel, "-") {
			@Override
			public void zoomChanged(double magnification) {
				setEnabled(zoomModel.isZoomOutPossible());
			}
		};
		zoomOutButton.setToolTipText(messages.getString("ZoomOut"));
		zoomOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomModel.zoomOut();
			}
		});
		zoomOutButton.addKeyListener(kn);
		zoomModel.addListener(zoomOutButton);

		JPanel leftPanel = new JPanel(new GridLayout());
		leftPanel.add(prevButton);
		leftPanel.add(zoomInButton);

		JPanel rightPanel = new JPanel(new GridLayout());
		rightPanel.add(zoomOutButton);
		rightPanel.add(nextButton);

		JPanel centerPanel = new JPanel(new GridLayout());
		centerPanel.add(indicator);
		centerPanel.add(zoomLevelList);

		this.add(leftPanel);
		this.add(centerPanel);
		this.add(rightPanel);

		updateButtonStates();
		updateIndicator();
	}

	private void updateButtonStates() {
		prevButton.setEnabled(dp.hasPrevious());
		nextButton.setEnabled(dp.hasNext());

		// Prevents leaving focus on button which is disabled.
		if (!zoomInButton.isEnabled() && zoomInButton.hasFocus()) {
			zoomOutButton.requestFocus();
		}
		if (!zoomOutButton.isEnabled() && zoomOutButton.hasFocus()) {
			zoomInButton.requestFocus();
		}
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
						"%d / %d",
						dp.current() + 1,
						dp.count()
				)
		);
	}

	public void imageChanged() {
		updateButtonStates();
		updateIndicator();
	}
}
