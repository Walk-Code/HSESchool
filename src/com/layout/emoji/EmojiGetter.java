package com.layout.emoji;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.Context;

import com.zhuochuang.hsej.R;

public class EmojiGetter {
	public static List<EmojiBean> readXML(Context ctx) {
		try {
			InputStream in = ctx.getResources().getAssets().open("emojis.xml");
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser saxParser = spf.newSAXParser();
			XMLContentHandler handler = new XMLContentHandler(ctx);
			saxParser.parse(in, handler);
			in.close();
			LinkedList<EmojiBean> emojis = handler.getEmojis();
			emojis.add(emojis.size() - 1, new EmojiBean(R.drawable.emoji_delete, "emoji_delete"));
			return emojis;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
