# simpleimageviewer4j - a simple image viewer

*simpleimageviewer4j* provides a simple way to display images in Java (using Swing.)

It's a useful when:

* Debugging applications which manipulates images.
* A simple image viewer in necessary in an application.


## Usage

A simple usage would be to specify images to display like the following:

```java
// Images that we're working on.
BufferedImage img1 = ...
BufferedImage img2 = ...

// A window to view `img1` and `img2` will be shown.
new Viewer(img1, img2).show();
```

Using a `Collection` (such as a `List`) to specify images to display is also supported:

```java
// A `List` containing images that we're working on.
List<BufferedImage> images = ...

// A window to view images contained in `images` will be shown.
new Viewer(images).show();
```


## Maven

`simpleimageviewer4j` is available through Maven.

Simply add the following to the `<dependencies>` section of the POM:

```
<dependency>
	<groupId>net.coobird.gui.simpleimageviewer4j</groupId>
	<artifactId>simpleimageviewer4j</artifactId>
	<version>0.1.3</version>
	<scope>jar</scope>
</dependency>
```

## Features

* Simple API to easily specify images to view.
* Swing UI to cycle through and zoom in/out of images with mouse and keyboard navigation

## Documentation

* [Documentation](https://coobird.github.io/simpleimageviewer4j/javadoc/0.1.3/)
