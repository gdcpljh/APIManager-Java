package com.api.manager.ui.panel.opt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import com.api.manager.ui.panel.opt.response.LResAttachPanel;
import com.api.manager.ui.panel.opt.response.LResMainPanel;
import com.api.manager.ui.util.CompLogger;

/**
 * 请求参数面板
 *
 * @author ASLai
 */
public class LResponsePanel extends JPanel {

	private static final long serialVersionUID = -4417297932288696655L;

	/**
	 * 构成方法
	 */
	public LResponsePanel() {

		CompLogger.create(this.getClass(), 2);
		initComp();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		// 设置布局
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 100;
		c.weightx = 50;

		c.insets = new Insets(5, 0, 10, 5);
		this.add(new LResMainPanel(), c);

		c.gridx = 1;
		c.insets = new Insets(5, 5, 10, 10);
		this.add(new LResAttachPanel(), c);
	}
}
