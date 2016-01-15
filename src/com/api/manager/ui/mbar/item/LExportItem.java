package com.api.manager.ui.mbar.item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.api.manager.ui.support.UIEvent;
import com.api.manager.ui.util.CompLogger;

/**
 * 导出收藏夹菜单项
 *
 * @author ASLai
 */
public class LExportItem extends JMenuItem implements ActionListener {

	private static final long serialVersionUID = 3416612156208194743L;

	private short configType;

	/**
	 * 构造方法
	 * 
	 * @param configType 配置类型
	 */
	public LExportItem(short configType) {

		CompLogger.create(this.getClass(), 2);
		this.configType = configType;
		initStyle();
		initEvent();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化样式
	 */
	private void initStyle() {

		switch (configType) {
		case UIEvent.CONFIG_TYPE_BASE:
			this.setText("基础配置");
			break;
		case UIEvent.CONFIG_TYPE_FAVORITES:
			this.setText("收藏夹");
			break;
		case UIEvent.CONFIG_TYPE_HISTORY:
			this.setText("历史记录");
			break;
		}
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {

		// 添加点击事件
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		UIEvent.saveAsConfig(configType);
	}
}
