package com.api.manager.ui.panel;

import javax.swing.JSplitPane;

import com.api.manager.ui.panel.pro.LFavoritesList;
import com.api.manager.ui.panel.pro.LHistoryList;
import com.api.manager.ui.util.CompLogger;

/**
 * 项目面板
 *
 * @author ASLai
 */
public class LProPanel extends JSplitPane {

	private static final long serialVersionUID = -4498638275944937839L;

	/**
	 * 构造方法
	 */
	public LProPanel() {

		CompLogger.create(this.getClass(), 1);
		initStyle();
		initComp();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化样式
	 */
	private void initStyle() {

		// 设置收缩按钮
		this.setOneTouchExpandable(true);
		// 设置分割方式
		this.setOrientation(JSplitPane.VERTICAL_SPLIT);
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		// 添加收藏夹
		this.setLeftComponent(new LFavoritesList());
		// 添加历史记录列表
		this.setRightComponent(new LHistoryList());
	}
}
