package com.api.manager;

import org.apache.log4j.PropertyConfigurator;

import com.api.manager.ui.UIBuilder;

/**
 * 主函数
 */
public class APIManager {

	static {
		// 读取log4j配置
		PropertyConfigurator.configure(Object.class.getResource("/properties/log4j.properties"));
	}

	/**
	 * 入口函数
	 * 
	 * @param arr 参数
	 */
	public static void main(String... arr) {

		try {
			// 构建图形
			UIBuilder.build();
		} catch (RuntimeException e) {
			// 异常则退出系统
			System.exit(0);
		}
	}
}