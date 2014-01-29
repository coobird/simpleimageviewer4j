package net.coobird.gui.simpleimageviewer4j;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class Viewer {
	private final List<BufferedImage> images;
	
	public Viewer(BufferedImage... images) {
		if (images == null || images.length == 0) {
			throw new NullPointerException("Must specify images.");
		}
		
		this.images = Arrays.asList(images);
	}
	
	private class ViewerPanel extends JPanel {
		private int index = 0;
		private BufferedImage curImage;
		
		public ViewerPanel() {
			curImage = images.get(index);
		}
		
		public void showNext() {
			index = Math.max(0, --index);
			curImage = images.get(index);
			repaint();
		}
		
		public void showPrevious() {
			index = Math.min(++index, images.size() - 1);
			curImage = images.get(index);
			repaint();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			// Center image, but show scrollbars when smaller than window.
			int x = (this.getWidth() / 2) - (curImage.getWidth() / 2);
			int y = (this.getHeight() / 2) - (curImage.getHeight() / 2);
			x = Math.max(0, x);
			y = Math.max(0, y);
			
			g.drawImage(curImage, x, y, null);
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(curImage.getWidth(), curImage.getHeight());
		}
	}
	
	public void show() {
		JFrame f = new JFrame("Simple Image Viewer");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final ViewerPanel vp = new ViewerPanel();
		f.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				int key = e.getKeyCode();
				
				if (key == KeyEvent.VK_LEFT) {
					vp.showNext();
					
				} else if (key == KeyEvent.VK_RIGHT) {
					vp.showPrevious();
				}
			}
		});
		
		f.add(new JScrollPane(vp));
		f.pack();
		
		int frameWidth = f.getWidth();
		int frameHeight = f.getHeight();
		
		DisplayMode displayMode = f.getGraphicsConfiguration().getDevice().getDisplayMode();
		int screenWidth = displayMode.getWidth();
		int screenHeight = displayMode.getHeight();
		
		int x = (screenWidth / 2) - (frameWidth / 2);
		int y = (screenHeight / 2) - (frameHeight / 2);
		f.setLocation(x, y);
		
		f.setVisible(true);
	}
	
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				show();
			}
		});
	}
}
