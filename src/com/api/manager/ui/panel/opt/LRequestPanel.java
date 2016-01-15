package com.api.manager.ui.panel.opt;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import com.api.manager.ui.panel.opt.request.LBodyPanel;
import com.api.manager.ui.panel.opt.request.LTablePanel;
import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.util.CompLogger;

/**
 * 请求参数面板
 *
 * @author ASLai
 */
public class LRequestPanel extends JPanel {

	private static final long serialVersionUID = -4417297932288696655L;

	/**
	 * 构成方法
	 */
	public LRequestPanel() {

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
		c.weightx = 50;
		c.weighty = 50;

		c.insets = new Insets(5, 0, 0, 5);
		LTablePanel param = new LTablePanel("请求参数");
		this.add(param, c);

		c.gridx = 1;
		c.insets = new Insets(5, 5, 0, 10);
		LBodyPanel body = new LBodyPanel();
		this.add(body, c);

		c.gridy = 1;
		c.gridx = 0;
		c.insets = new Insets(5, 0, 10, 5);
		LTablePanel header = new LTablePanel("请求头");
		this.add(header, c);

		c.gridx = 1;
		c.insets = new Insets(5, 5, 10, 10);
		LTablePanel cookie = new LTablePanel("Cookie");
		this.add(cookie, c);

		// 注册组件
		UIComps.params = param.getModel();
		UIComps.body = body.getBody();
		UIComps.headers = header.getModel();
		UIComps.cookies = cookie.getModel();
	}
}
