package com.model.data;

/**
 * 商品信息实体类
 * @author chenjunsen
 * 2015年8月17日下午11:23:04
 */
public class ProductInfo extends BaseInfo{
	private String imageUrl;
	private String desc;
	private double price;
	private double markPrice;
	private String color;
	private String size;
	private int count;
	private String shopCarId;
	private int position;//绝对位置，只在ListView构造的购物车中，在删除时有效
	
	private int inventory;
	
	public ProductInfo() {
		super();
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getInventory() {
		return inventory;
	}
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getShopCarId() {
		return shopCarId;
	}
	public void setShopCarId(String shopCarId) {
		this.shopCarId = shopCarId;
	}
	public double getMarkPrice() {
		return markPrice;
	}
	public void setMarkPrice(double markPrice) {
		this.markPrice = markPrice;
	}

}
