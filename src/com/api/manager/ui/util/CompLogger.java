package com.api.manager.ui.util;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 组件日志处理器
 *
 * @author ASLai
 */
public class CompLogger {

	private static Logger logger = Logger.getLogger(CompLogger.class);

	private static Map<String, C> cs = new HashMap<String, C>();

	/**
	 * @return 获取日志打印集合
	 */
	private static Map<String, C> getCs() {

		if (cs == null) {
			cs = new HashMap<String, C>();
		}
		return cs;
	}

	/**
	 * 创建
	 * 
	 * @param clas 组件类型
	 * @param level 层级
	 */
	public static void create(Class<? extends Component> clas, int level) {

		create(clas.getSimpleName(), level);
	}

	/**
	 * 创建
	 * 
	 * @param key 组件名称
	 * @param level 层级
	 */
	public static void create(String key, int level) {

		// 获取当前时间
		long current = System.currentTimeMillis();
		// 封装组件对象
		C c = new C(level, current);

		// 添加到集合中
		Map<String, C> cs = getCs();
		cs.put(key, c);

		// 打印日志
		StringBuilder log = new StringBuilder();
		for (int i = 0; i < level - 1; i++) {
			log.append("        ");
		}
		if (c.level != 0) {
			log.append("  |--");
		}
		log.append(key);
		logger.info(log);
	}

	/**
	 * 创建完毕
	 * 
	 * @param clas 组件类型
	 */
	public static void finish(Class<? extends Component> clas) {

		finish(clas.getSimpleName());
	}

	/**
	 * 创建完毕
	 * 
	 * @param key 组件名称
	 */
	public static void finish(String key) {

		// 获取当前时间
		long current = System.currentTimeMillis();

		// 获取集合对象
		Map<String, C> cs = getCs();
		C c = cs.get(key);

		if (c == null) {
			logger.info(key + " -完毕- ");
		} else {
			// 打印日志
			StringBuilder log = new StringBuilder();
			for (int i = 0; i < c.level - 1; i++) {
				log.append("        ");
			}
			if (c.level != 0) {
				log.append("  |--");
			}
			log.append(key).append(" -完毕- ").append(current - c.start).append("ms");
			logger.info(log);
		}
		cs.remove(key);
	}

	/**
	 * 清空
	 */
	public static void clear() {

		cs.clear();
		cs = null;
	}

	/**
	 * 组件级别
	 *
	 * @author ASLai
	 */
	private static class C {

		private int level;
		private long start;

		/**
		 * @param level 级别
		 * @param start 起始日期
		 */
		private C(int level, long start) {

			this.level = level;
			this.start = start;
		}
	}
}
