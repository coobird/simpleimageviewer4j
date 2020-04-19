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
