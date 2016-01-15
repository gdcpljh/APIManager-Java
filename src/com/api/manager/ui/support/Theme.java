package com.api.manager.ui.support;

import java.awt.Font;
import java.util.Map.Entry;

import javax.swing.UIManager;

/**
 * 主题
 *
 * @author ASLai
 */
public class Theme {

	/**
	 * 初始化LookAndFeel
	 * 
	 * @param lf LookAndFeel类型
	 */
	public static void lookAndFeel(String lf) {

		try {
			UIManager.setLookAndFeel(lf);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 初始化字体
	 * 
	 * @param font 字体
	 */
	public static void setFont(Font font) {

		// 遍历设置字体
		for (Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
			String key = entry.getKey().toString();
			// 判断是否为字体
			if (key.endsWith(".font")) {
				UIManager.put(key, font);
			}
		}
	}
}
