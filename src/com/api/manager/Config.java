package com.api.manager;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.api.manager.worker.http.HttpSender.Method;
import com.api.manager.worker.tool.reader.PropertiesReader;

/**
 * 配置
 *
 * @author ASLai
 *
 */
public class Config {

	/**
	 * 图标资源
	 */
	public enum Icon {
		/** logo */
		logo("image/logo.png"),
		/** 新增按钮 */
		add("image/add.png"),
		/** 删除按钮 */
		delete("image/delete.png"),
		/** 清空按钮 */
		clear("image/clear.png"),
		/** 清空按钮 */
		up("image/up.png"),
		/** 文件夹 */
		folder("image/folder.png"),
		/** 文件夹-打开 */
		folder_open("image/folder_open.png"),
		/** 叶子节点 */
		leaf("image/leaf.png");

		/** 图片路径 */
		public final String path;

		private Icon(String path) {

			this.path = path;
		}
	}

	private static Logger logger = Logger.getLogger(Config.class);

	private static final String CONFIG_PATH = "/properties/config.properties";
	private static final String VERSION_PATH = "/version/version.properties";
	private static final String VERSION_TEXT_PATH = "/version/version.txt";

	private static final String ATTR_NAME = "manager.name";
	private static final String ATTR_SINGLETON = "manager.singleton";
	private static final String ATTR_CHARTSET = "manager.charset";
	private static final String ATTR_CONFIG_DIR = "manager.config.dir";
	private static final String ATTR_CONFIG_FILE_BASE = "manager.config.file.base";
	private static final String ATTR_CONFIG_FILE_STYLE = "manager.config.file.style";
	private static final String ATTR_CONFIG_FILE_HISTORY = "manager.config.file.history";
	private static final String ATTR_CONFIG_FILE_FAVORITES = "manager.config.file.favorites";
	private static final String ATTR_CONFIG_SUFFIX = "manager.config.suffix";
	private static final String ATTR_CONFIG_AUTO = "manager.config.auto";
	private static final String ATTR_CONFIG_AUTO_TIME = "manager.config.auto.time";
	private static final String ATTR_FONT_NAME = "manager.font.name";
	private static final String ATTR_FONT_SIZE = "manager.font.size";
	private static final String ATTR_THEME = "manager.theme";
	private static final String ATTR_THEME_DEFAULT = "manager.theme.default";
	private static final String ATTR_METHOD_DEFAULT = "manager.method.default";
	private static final String ATTR_HISTORY_MAX = "manager.history.max";
	private static final String ATTR_HISTORY_SAVE = "manager.history.save";
	private static final String ATTR_HTTP_TIMEOUT = "manager.http.time.out";
	private static final String ATTR_VERSION = "manager.version";
	private static final String ATTR_VERSION_NUMBER = "manager.version.number";
	private static final String ATTR_VERSION_DATE = "manager.version.date";

	/** 名称 */
	public String name;
	/** 是否为单例 */
	public Boolean singleton;
	/** 编码 */
	public String charset;
	/** 用户配置路径 */
	public String config_dir;
	/** 基础配置文件 */
	public String config_file_base;
	/** 主题样式配置文件 */
	public String config_file_style;
	/** 历史记录配置文件 */
	public String config_file_history;
	/** 收藏夹配置文件 */
	public String config_file_favorites;
	/** 配置默认后缀 */
	public String config_suffix;
	/** 是否开启配置自动保存 */
	public Boolean config_auto;
	/** 自动保存配置间隔时间 */
	public Integer config_auto_time;
	/** 默认字体 */
	public Font font;
	/** 图标 */
	public Map<String, String> icons;
	/** 主题 */
	public Map<String, String> themes;
	/** 默认主题 */
	public String default_themes;
	/** 默认调用方式 */
	public Method default_method;
	/** 历史记录最大条数 */
	public Integer max_history;
	/** 是否保存历史记录 */
	public Boolean save_history;
	/** 连接超时时间 */
	public Integer time_out;
	/** 版本 */
	public String version;
	/** 版本序号 */
	public Integer version_number;
	/** 版本构建事件 */
	public String version_date;

	/**
	 * 初始化配置
	 */
	public void init() {

		long start = System.currentTimeMillis();
		logger.info("读取系统配置");

		Properties prop = PropertiesReader.getResourceProp(CONFIG_PATH);
		initBase(prop);
		initTheme(prop);
		initFont(prop);
		initMethod(prop);

		prop = PropertiesReader.getResourceProp(VERSION_PATH);
		initVersion(prop);

		logger.info("读取系统配置 -完毕- " + (System.currentTimeMillis() - start) + "ms");
	}

	/**
	 * 初始化基础配置
	 * 
	 * @param prop 配置
	 */
	private void initBase(Properties prop) {

		name = (String) prop.get(ATTR_NAME);
		singleton = Boolean.valueOf((String) prop.get(ATTR_SINGLETON));
		max_history = Integer.valueOf((String) prop.get(ATTR_HISTORY_MAX));
		save_history = Boolean.valueOf((String) prop.get(ATTR_HISTORY_SAVE));
		time_out = Integer.valueOf((String) prop.get(ATTR_HTTP_TIMEOUT));
		config_dir = (String) prop.get(ATTR_CONFIG_DIR);
		config_file_base = (String) prop.get(ATTR_CONFIG_FILE_BASE);
		config_file_style = (String) prop.get(ATTR_CONFIG_FILE_STYLE);
		config_file_history = (String) prop.get(ATTR_CONFIG_FILE_HISTORY);
		config_file_favorites = (String) prop.get(ATTR_CONFIG_FILE_FAVORITES);
		config_suffix = (String) prop.get(ATTR_CONFIG_SUFFIX);
		config_auto = Boolean.valueOf((String) prop.get(ATTR_CONFIG_AUTO));
		config_auto_time = Integer.valueOf((String) prop.get(ATTR_CONFIG_AUTO_TIME));
		charset = (String) prop.get(ATTR_CHARTSET);
	}

	/**
	 * 初始化主体配置
	 * 
	 * @param prop 配置
	 */
	private void initTheme(Properties prop) {

		themes = new LinkedHashMap<String, String>();
		// 主题类型
		String[] types = ((String) prop.get(ATTR_THEME)).split("\\|");
		for (String type : types) {
			// 主题类型信息
			String[] themeTypeInfo = type.split("_");
			// 主题类型名称
			String name = themeTypeInfo[0];
			// 获取主题
			for (String theme : themeTypeInfo[1].split(",")) {
				StringBuilder attr = new StringBuilder();
				// 配置参数名称
				attr.append(ATTR_THEME).append(".").append(name).append(".").append(theme);
				// 添加主题信息
				String[] themeInfo = prop.get(attr.toString()).toString().split(",");
				themes.put(themeInfo[0], themeInfo[1]);
			}
		}
		default_themes = (String) prop.get(ATTR_THEME_DEFAULT);
	}

	/**
	 * 初始化字体
	 * 
	 * @param prop 配置
	 */
	private void initFont(Properties prop) {

		String font_name = (String) prop.get(ATTR_FONT_NAME);
		Integer font_size = Integer.valueOf((String) prop.get(ATTR_FONT_SIZE));
		font = new Font(font_name, Font.PLAIN, font_size);
	}

	/**
	 * 初始化默认调用方式
	 * 
	 * @param prop 配置
	 */
	private void initMethod(Properties prop) {

		String method = (String) prop.get(ATTR_METHOD_DEFAULT);
		default_method = Method.valueOf(method);
	}

	private void initVersion(Properties prop) {

		version = (String) prop.get(ATTR_VERSION);
		version_number = Integer.valueOf((String) prop.get(ATTR_VERSION_NUMBER));
		version_date = (String) prop.get(ATTR_VERSION_DATE);
	}

	/**
	 * 读取版本信息
	 * 
	 * @return 版本信息
	 */
	public String readVersion() {

		// 读取版本信息文件流
		InputStream is = Object.class.getResourceAsStream(VERSION_TEXT_PATH);
		// 读取器
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			// 版本信息
			StringBuilder version = new StringBuilder();
			// 行字符串
			String str = null;
			// 是否为起始行
			boolean start = true;
			// 读取文件
			while ((str = reader.readLine()) != null) {
				if (start) {
					start = false;
				} else {
					version.append("\r\n");
				}
				version.append(new String(str.getBytes(), charset));
			}
			return version.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			// 关闭流
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
			try {
				is.close();
			} catch (IOException e) {
			}
		}
	}
}
