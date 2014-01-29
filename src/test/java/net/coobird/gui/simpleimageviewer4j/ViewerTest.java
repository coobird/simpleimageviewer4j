package net.coobird.gui.simpleimageviewer4j;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public void constructorArgumentNullList() {
		// given
		List<BufferedImage> images = null;
		
		// when
		try {
			new Viewer(images);
			fail();
			
		} catch (NullPointerException e) {
			// then
		}
	}
	
	@Test
	public void constructorArgumentNullSet() {
		// given
		Set<BufferedImage> images = null;
		
		// when
		try {
			new Viewer(images);
			fail();
			
		} catch (NullPointerException e) {
			// then
		}
	}
	
	@Test
	public void constructorArgumentEmptyList() {
		// given
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		
		// when
		try {
			new Viewer(images);
			fail();
			
		} catch (NullPointerException e) {
			// then
		}
	}
	
	@Test
	public void constructorArgumentEmptySet() {
		// given
		Set<BufferedImage> images = new HashSet<BufferedImage>();
		
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
	
	@Test
	public void constructorArgumentSingleImageAsList() {
		// given
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		List<BufferedImage> images = Arrays.asList(img); 
		
		// when, then
		new Viewer(images);
	}
	
	@Test
	public void constructorArgumentMultipleImagesAsList() {
		// given
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		List<BufferedImage> images = Arrays.asList(img, img); 
		
		// when, then
		new Viewer(images);
	}
	
	@Test
	public void constructorArgumentSingleImageAsSet() {
		// given
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Set<BufferedImage> images = new HashSet<BufferedImage>(Arrays.asList(img)); 
		
		// when, then
		new Viewer(images);
	}
	
	@Test
	public void constructorArgumentMultipleImagesAsSet() {
		// given
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage img2 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Set<BufferedImage> images = new HashSet<BufferedImage>(Arrays.asList(img, img2));
		
		// when, then
		new Viewer(images);
	}
}
