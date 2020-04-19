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

		if (key == KeyEvent.VK_LEFT) {
			dp.showPrevious();

		} else if (key == KeyEvent.VK_RIGHT) {
			dp.showNext();
		}
	}
}
