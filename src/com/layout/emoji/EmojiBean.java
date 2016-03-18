package com.layout.emoji;


public class EmojiBean {
	private int resId;
	private String name;

	public EmojiBean() {
		super();
	}

	public EmojiBean(int resId, String name) {
		super();
		this.resId = resId;
		this.name = name;
	}

	public int getResId() {
		return resId;
	}

	public void setId(int id) {
		this.resId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
