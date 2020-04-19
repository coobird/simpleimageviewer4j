package net.coobird.gui.simpleimageviewer4j.component;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class NavigationPanel extends JPanel implements ViewerChangeListener {

	private final JButton prevButton = new JButton("<");
	private final JButton nextButton = new JButton(">");
	private final JLabel indicator;
	private final DisplayPanel vp;

	public NavigationPanel(final DisplayPanel vp) {
		this.vp = vp;
		this.setLayout(new GridLayout(1, 0));

		indicator = new JLabel();
		indicator.setFont(new Font("Monospaced", Font.PLAIN, 14));
		indicator.setHorizontalAlignment(SwingConstants.CENTER);

		KeyNavigation kn = new KeyNavigation(vp);

		prevButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vp.showPrevious();
			}
		});
		prevButton.addKeyListener(kn);

		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vp.showNext();
			}
		});
		nextButton.addKeyListener(kn);

		this.add(prevButton);
		this.add(indicator);
		this.add(nextButton);

		updateButtonStates();
	}

	private void updateButtonStates() {
		prevButton.setEnabled(vp.hasPrevious());
		nextButton.setEnabled(vp.hasNext());

		// Prevents leaving focus on button which is disabled.
		if (!prevButton.isEnabled() && prevButton.hasFocus()) {
			nextButton.requestFocus();
		}
		if (!nextButton.isEnabled() && nextButton.hasFocus()) {
			prevButton.requestFocus();
		}

		indicator.setText("" + (vp.current() + 1) + " / " + vp.count());
	}

	public void imageChanged() {
		updateButtonStates();
	}
}
