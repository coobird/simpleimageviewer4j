package net.coobird.gui.simpleimageviewer4j;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;

import org.junit.Test;

public class ViewerTest {
	
	@Test
	public void constructorArgumentNull() {
		// given
		BufferedImage[] images = null;
		
		// when
		try {
			new Viewer(images);
			fail();
			
		} catch (NullPointerException e) {
			// then
		}
	}
	
	@Test
	public void constructorArgumentIsBlank() {
		// given, when
		try {
			new Viewer();
			fail();
			
		} catch (NullPointerException e) {
			// then
		}
	}
	
	@Test
	public void constructorArgumentSingleImageNonArray() {
		// given
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		
		// when, then
		new Viewer(img);
	}
	
	@Test
	public void constructorArgumentMultipleImagesNonArray() {
		// given
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		
		// when, then
		new Viewer(img, img);
	}
	
	@Test
	public void constructorArgumentSingleImageAsArray() {
		// given
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage[] images = new BufferedImage[] { img }; 
		
		// when, then
		new Viewer(images);
	}
	
	@Test
	public void constructorArgumentMultipleImagesAsArray() {
		// given
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage[] images = new BufferedImage[] { img, img }; 
		
		// when, then
		new Viewer(images);
	}
}
