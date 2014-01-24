package net.coobird.gui.simpleimageviewer4j;

import java.awt.Dimension;
import java.awt.Graphics;
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
		this.images = Arrays.asList(images);
	}
	
	private class ViewerPanel extends JPanel {
		private BufferedImage curImage = images.get(0);
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			// Center image
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
		f.setLocation(100, 100);
		f.add(new JScrollPane(new ViewerPanel()));
		f.pack();
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
