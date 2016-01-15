package com.api.manager.worker.tool.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jsoup.Jsoup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * 格式化工具
 *
 * @author ASLai
 */
public class DataFormater {

	/** gson处理工具 */
	private static Gson gson;
	/** dom4j处理工具 */
	private static SAXBuilder xBuilder;
	private static Format xFormat;

	/**
	 * 格式化json
	 * 
	 * @param json 待格式化json字符串
	 * @return json字符串
	 * @throws DataFormatException 格式化异常
	 */
	public static String json(String json) throws DataFormatException {

		if (gson == null) {
			gson = new GsonBuilder().setPrettyPrinting().create();
		}
		JsonParser jp = new JsonParser();
		JsonElement je;
		try {
			je = jp.parse(json);
		} catch (JsonSyntaxException e) {
			throw new DataFormatException("格式化Json字符串失败", e);
		}
		return gson.toJson(je);
	}

	/**
	 * 格式化xml
	 * 
	 * @param xml 待格式化xml字符串
	 * @return xml字符串
	 * @throws DataFormatException 格式化异常
	 */
	public static String xml(String xml) throws DataFormatException {

		if (xFormat == null) {
			xFormat = Format.getCompactFormat().setIndent("  ");
		}
		if (xBuilder == null) {
			xBuilder = new SAXBuilder();
		}

		XMLOutputter xmlout = new XMLOutputter(xFormat);

		StringReader xmlReader = new StringReader(xml);
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		try {
			org.jdom2.Document doc = xBuilder.build(xmlReader);
			xmlout.output(doc, os);
		} catch (JDOMException e) {
			throw new DataFormatException("格式化XML字符串失败", e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return os.toString();
	}

	/**
	 * 格式化html
	 * 
	 * @param html 待格式化html字符串
	 * @return html字符串
	 */
	public static String html(String html) {

		org.jsoup.nodes.Document doc = Jsoup.parseBodyFragment(html);
		return doc.html();
	}
}
