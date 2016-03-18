package com.layout.photoalbum;

import java.io.Serializable;

/**
 * һ��ͼƬ����
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("serial")
public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;
}
