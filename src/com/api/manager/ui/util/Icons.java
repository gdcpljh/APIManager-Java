package com.api.manager.ui.util;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.api.manager.Config.Icon;

/**
 * 图标处理类
 */
public class Icons {

	private static Logger logger = Logger.getLogger(Icons.class);

	/**
	 * 获取图标
	 * 
	 * @param icon 图标类型
	 * @return 图标对象
	 */
	public static ImageIcon getIcon(Icon icon) {

		Image img = getImage(icon);
		return new ImageIcon(img);
	}

	/**
	 * 获取图片
	 * 
	 * @param icon 图标对象
	 * @return 图片对象
	 */
	public static Image getImage(Icon icon) {

		return getImage(icon.path);
	}

	/**
	 * @param path 路径
	 * @return 图片对象
	 */
	private static Image getImage(String path) {

		InputStream is = Object.class.getResourceAsStream("/" + path);
		Image img;
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return img;
	}
}
