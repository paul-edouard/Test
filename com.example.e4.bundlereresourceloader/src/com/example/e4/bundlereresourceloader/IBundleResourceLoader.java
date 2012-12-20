package com.example.e4.bundlereresourceloader;

import org.eclipse.swt.graphics.Image;

public interface IBundleResourceLoader {

	public Image loadImage(Class<?> clazz, String path);

}
