package com.api.manager.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.util.CompLogger;

/**
 * 状态面板
 *
 * @author ASLai
 */
public class LStatusPanel extends JPanel {

	private static final long serialVersionUID = 678964291562744892L;

	/**
	 * 构造方法
	 */
	public LStatusPanel() {

		CompLogger.create(this.getClass(), 1);
		initStyle();
		initComp();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化样式
	 */
	private void initStyle() {

		// 设置样式
		this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		// 设置布局
		this.setLayout(new BorderLayout());

		JPanel panel = new JPanel();
		panel.add(new JLabel());

		JLabel info = new JLabel();
		info.setHorizontalAlignment(JLabel.RIGHT);
		this.add(info, BorderLayout.CENTER);

		// 进度条
		JProgressBar progress = new JProgressBar();
		progress.setMinimum(0);
		progress.setMaximum(100);
		progress.setValue(0);
		panel.add(progress);
		this.add(panel, BorderLayout.LINE_END);

		// 注册进度条
		UIComps.progress = progress;
		UIComps.info = info;
	}
}
