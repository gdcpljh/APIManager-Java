package com.api.manager.ui.tbar;

import javax.swing.JToolBar;

import com.api.manager.ui.util.CompLogger;

/**
 * 工具栏
 *
 * @author ASLai
 */
public class LToolBar extends JToolBar {

	private static final long serialVersionUID = -5273733734451362181L;

	/**
	 * 构造方法
	 */
	public LToolBar() {

		CompLogger.create(this.getClass(), 1);
		initStyle();
		initComp();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化样式
	 */
	private void initStyle() {

	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

	}
}
