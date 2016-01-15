package com.api.manager.ui;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.api.manager.ui.support.SystemLocker;
import com.api.manager.ui.support.Theme;
import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.support.UIConfig;
import com.api.manager.ui.support.UIEvent;
import com.api.manager.ui.support.UIConfig.ConfigBean;
import com.api.manager.ui.util.CompLogger;

/**
 * UI构建器
 *
 * @author ASLai
 */
public class UIBuilder {

	private static Logger logger = Logger.getLogger(UIBuilder.class);

	/**
	 * 初始化全局样式
	 * 
	 * @param lf 样式
	 */
	private static void initTheme(String lf) {

		long start = System.currentTimeMillis();
		logger.info("初始化：主题样式");

		// 初始化字体
		Theme.setFont(UIConfig.sysConfig.font);
		// 初始化Look&Feel
		if (lf == null) {
			lf = UIConfig.sysConfig.themes.get(UIConfig.sysConfig.default_themes);
		}
		Theme.lookAndFeel(lf);
		UIEvent.current_lf = lf;

		logger.info("初始化：主题样式 -完毕- " + (System.currentTimeMillis() - start) + "ms");
	}

	/**
	 * 构建
	 */
	public static void build() {

		long start = System.currentTimeMillis();
		logger.info("系统启动");

		// 软件加载页面
		Loading l = new Loading();
		l.setVisible(true);

		// 读取系统配置
		l.setProgress(0, "初始化系统配置");
		try {
			UIConfig.init();
		} catch (RuntimeException e) {
			UIEvent.alert(e, "初始化系统配置失败");
			throw e;
		}

		// 读取用户配置
		l.setProgress(10, "读取用户配置文件");
		ConfigBean config = null;
		try {
			config = UIConfig.readConfig();
		} catch (RuntimeException e) {
			UIEvent.alert(e, "读取用户配置失败");
		}
		if (config != null) {
			// 设置基础配置信息
			UIEvent.setBaseConfig(config.getBase());
		}

		// 单例模式处理
		if (UIConfig.sysConfig.singleton) {
			// 检查系统启动情况
			l.setProgress(20, "检查系统启动情况");
			boolean lockSucc;
			try {
				lockSucc = SystemLocker.lock();
			} catch (RuntimeException e) {
				UIEvent.alert(e, "系统启动检查失败");
				throw e;
			}
			// 判断是否锁定
			if (!lockSucc) {
				JOptionPane.showMessageDialog(UIComps.ui, "系统正在运行", "提示", JOptionPane.ERROR_MESSAGE);
				throw new RuntimeException("系统正在运行");
			}
		}

		// 初始化全局样式
		l.setProgress(30, "加载主题样式");
		try {
			initTheme(config == null ? null : config.getLf());
		} catch (RuntimeException e) {
			UIEvent.alert(e, "加载主题样式失败");
			try {
				initTheme(null);
			} catch (RuntimeException ex) {
			}
		}

		// 创建主窗口
		l.setProgress(50, "初始化主窗口");
		UI ui;
		try {
			ui = new UI();
		} catch (RuntimeException e) {
			UIEvent.alert(e, "初始化主窗口失败");
			throw e;
		}

		// 设置窗口配置信息
		l.setProgress(80, "初始化主窗口配置");
		try {
			UIEvent.loadUserUI(config);
		} catch (RuntimeException e) {
			UIEvent.alert(e, "初始化主窗口配置失败");
		}

		// 显示主框架
		l.setProgress(100, "加载完毕");
		ui.setVisible(true);
		l.dispose();

		// 启动配置文件自动保存线程
		try {
			UIEvent.startConfigSaveThread();
		} catch (RuntimeException e) {
			UIEvent.alert(e, "启动配置文件保存线程失败");
		}
		// 清空日志
		CompLogger.clear();
		logger.info("系统启动 -完毕- " + (System.currentTimeMillis() - start) + "ms");
	}
}