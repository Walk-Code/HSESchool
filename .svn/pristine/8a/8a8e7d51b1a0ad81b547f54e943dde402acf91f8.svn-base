package com.layout.emoji;

import java.util.LinkedList;

import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import android.content.Context;

public class XMLContentHandler extends DefaultHandler2 {
	private LinkedList<EmojiBean> persons = null;
	private EmojiBean currentBean;
	private String tagName = null;
	private Context ctx;

	public XMLContentHandler() {
		super();
	}

	public XMLContentHandler(Context ctx) {
		super();
		this.ctx = ctx;
	}

	public LinkedList<EmojiBean> getEmojis() {
		return persons;
	}

	@Override
	public void startDocument() throws SAXException {
		persons = new LinkedList<EmojiBean>();
	}

	@Override
	public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (localName.equals("id")) {
			currentBean = new EmojiBean();
		}
		this.tagName = localName;
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if (localName.equals("id")) {
			persons.add(currentBean);
			currentBean = null;
		}
		this.tagName = null;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if (tagName != null) {
			String data = new String(ch, start, length);
			if (tagName.equals("id")) {
				this.currentBean.setName(data);
				this.currentBean.setId(ctx.getResources().getIdentifier(data, "drawable", ctx.getPackageName()));
			}
		}
	}
}
