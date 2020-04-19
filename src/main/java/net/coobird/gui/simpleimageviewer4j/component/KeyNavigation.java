package net.coobird.gui.simpleimageviewer4j.component;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class KeyNavigation extends KeyAdapter {
	private final DisplayPanel vp;

	public KeyNavigation(DisplayPanel vp) {
		this.vp = vp;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_LEFT) {
			vp.showPrevious();

		} else if (key == KeyEvent.VK_RIGHT) {
			vp.showNext();
		}
	}
}
