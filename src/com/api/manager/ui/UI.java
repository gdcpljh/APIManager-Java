package com.api.manager.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import com.api.manager.Config;
import com.api.manager.ui.mbar.LMemuBar;
import com.api.manager.ui.panel.LOptPanel;
import com.api.manager.ui.panel.LProPanel;
import com.api.manager.ui.panel.LStatusPanel;
import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.support.UIConfig;
import com.api.manager.ui.support.UIEvent;
import com.api.manager.ui.util.CompLogger;
import com.api.manager.ui.util.Icons;

/**
 * 主框架窗口
 *
 * @author ASLai
 */
public class UI extends JFrame {

	private static final long serialVersionUID = -5296337500660500659L;

	/**
	 * 构造方法
	 */
	public UI() {

		CompLogger.create(this.getClass(), 0);
		initStyle();
		initComp();
		initEvent();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化样式
	 */
	private void initStyle() {

		// 设置图标
		this.setIconImage(Icons.getImage(Config.Icon.logo));
		// 设置窗口名称
		this.setTitle(UIConfig.sysConfig.name);
		// 设置窗口大小
		this.setSize(900, 650);
		// 设置窗口居中
		// 获取屏幕的尺寸
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screen.width / 2 - this.getWidth() / 2;
		int y = screen.height / 2 - this.getHeight() / 2;
		// 设置显示位置
		this.setLocation(x, y);
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		// 添加菜单
		this.setJMenuBar(new LMemuBar());

		// 设置布局
		this.setLayout(new BorderLayout());

		// 分割容器
		JSplitPane sp = new JSplitPane();
		// 设置样式
		sp.setBorder(null);
		sp.setOneTouchExpandable(true);
		sp.setDividerLocation(150);
		// 添加组件
		sp.setLeftComponent(new LProPanel());
		sp.setRightComponent(new LOptPanel());
		this.add(sp, BorderLayout.CENTER);

		// 添加状态栏
		this.add(new LStatusPanel(), BorderLayout.PAGE_END);

		// 注册组件
		UIComps.ui = this;
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {

		// 设置退出事件
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {

				UIEvent.exit();
			}
		});
	}
}