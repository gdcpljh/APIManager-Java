package com.api.manager.worker.tool.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件读取器
 *
 */
public class PropertiesReader {

	/**
	 * 获取资源配置
	 * 
	 * @param path 路径
	 * @return 配置
	 */
	public static Properties getResourceProp(String path) {

		InputStream is = getResourceIS(path);
		if (is == null) {
			throw new ReaderException(path);
		}
		try {
			return getProp(is);
		} catch (IOException e) {
			throw new ReaderException(e);
		}
	}

	/**
	 * 获取文件配置
	 * 
	 * @param path 路径
	 * @return 配置
	 */
	public static Properties getFileProp(String path) {

		try {
			return getProp(getFileIS(path));
		} catch (IOException e) {
			throw new ReaderException(e);
		}
	}

	/**
	 * 获取资源流
	 * 
	 * @param path 资源路径
	 * @return 资源流
	 */
	public static InputStream getResourceIS(String path) {

		return Object.class.getResourceAsStream(path);
	}

	/**
	 * 获取文件流
	 * 
	 * @param path 文件路径
	 * @return 文件流
	 * @throws FileNotFoundException
	 */
	public static InputStream getFileIS(String path) throws FileNotFoundException {

		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException(path);
		}
		InputStream in = new FileInputStream(file);
		return in;
	}

	/**
	 * 获取配置对象
	 * 
	 * @param is 文件流
	 * @return 配置对象
	 * @throws IOException 异常
	 */
	public static Properties getProp(InputStream is) throws IOException {

		Properties prop = new Properties();
		try {
			prop.load(is);
		} catch (IOException e) {
			throw e;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return prop;
	}
}
