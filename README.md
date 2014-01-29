simpleimageviewer4j - a simple image viewer
===========================================

The purpose of this project is to provide a simple way to display images
in Java (using Swing) to aid in debugging applications which manipulate
images.

A simple usage would be to specify images to display like the following:

```
// Images that we're working on.
BufferedImage img1 = ...
BufferedImage img2 = ...

// A window to view `img1` and `img2` will be shown.
new Viewer(img1, img2).show();
```

Using a `Collection` (such as a `List`) to specify images to display is also supported:

```
// A `List` containing images that we're working on.
List<BufferedImage> images = ...

// A window to view images contained in `images` will be shown.
new Viewer(images).show();
```


Features
--------

* Simple API to easily specify images to view.
* Swing UI to cycle through images with mouse and keyboard navigation.


Planned features
----------------

* Swing UI which allows zooming into and out of images.
