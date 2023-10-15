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

package net.coobird.gui.simpleimageviewer4j;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
	
	@Test(expected=NullPointerException.class)
	public void constructorGivenNullSingleBufferedImage() {
		// given
		BufferedImage img = null;
		
		// when, then
		new Viewer(img);
	}
	
	@Test(expected=NullPointerException.class)
	public void constructorBufferedImagesFirstIsNull() {
		// given
		BufferedImage img = null;
		BufferedImage img2 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		
		// when, then
		new Viewer(img, img2);
	}
	
	@Test(expected=NullPointerException.class)
	public void constructorBufferedImagesLastIsNull() {
		// given
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage img2 = null;
		
		// when, then
		new Viewer(img, img2);
	}
	
	@Test(expected=NullPointerException.class)
	public void constructorGivenNullSingleBufferedImageAsCollection() {
		// given
		BufferedImage img = null;
		Collection<BufferedImage> images = Arrays.asList(img);
		
		// when, then
		new Viewer(images);
	}
	
	@Test(expected=NullPointerException.class)
	public void constructorBufferedImagesAsCollectionFirstIsNull() {
		// given
		BufferedImage img = null;
		BufferedImage img2 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Collection<BufferedImage> images = Arrays.asList(img, img2);
		
		// when, then
		new Viewer(images);
	}
	
	@Test(expected=NullPointerException.class)
	public void constructorBufferedImagesAsCollectionLastIsNull() {
		// given
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		BufferedImage img2 = null;
		Collection<BufferedImage> images = Arrays.asList(img, img2);
		
		// when, then
		new Viewer(images);
	}

	@Test(expected=NullPointerException.class)
	public void constructorWithTitleIsNullForBufferedImages() {
		// given
		String title = null;
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

		// when, then
		new Viewer(title, img);
	}

	@Test(expected=NullPointerException.class)
	public void constructorWithTitleIsNullForCollection() {
		// given
		String title = null;
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

		// when, then
		new Viewer(title, Collections.singletonList(img));
	}

	@Test
	public void constructorWithTitleIsSetForBufferedImages() throws AWTException {
		// given
		String title = "My title";
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

		// when
		Viewer viewer = new Viewer(title, img);

		// then
		// TODO Find a way to verify title programmatically.
	}

	@Test
	public void constructorWithTitleIsSetForCollection() {
		// given
		String title = "My title";
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

		// when
		Viewer viewer = new Viewer(title, Collections.singletonList(img));

		// then
		// TODO Find a way to verify title programmatically.
	}
}
