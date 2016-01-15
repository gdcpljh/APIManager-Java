package com.api.manager.ui.panel.opt.response;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.util.CompLogger;

/**
 * 响应附加信息面板
 *
 * @author ASLai
 */
public class LResAttachPanel extends JPanel {

	private static final long serialVersionUID = 8190409045685504471L;

	private DefaultTableModel cookieTableModel;
	private DefaultTableModel headerTableModel;

	/**
	 * 构成方法
	 */
	public LResAttachPanel() {

		CompLogger.create(this.getClass(), 3);
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
		c.weightx = 100;

		Insets title_insets = new Insets(5, 5, 5, 0);
		Insets table_insets = new Insets(0, 0, 0, 0);

		// Header
		// 标题
		c.insets = title_insets;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(new JLabel("Header"), c);
		// 参数列表
		String[] title = { "键", "值" };
		headerTableModel = new DefaultTableModel(null, title);
		c.gridy = 1;
		c.weighty = 50;
		c.insets = table_insets;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JScrollPane(new JTable(headerTableModel)), c);

		// Cookie
		// 标题
		c.gridy = 2;
		c.weighty = 0;
		c.insets = title_insets;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(new JLabel("Cookie"), c);
		// 参数列表
		c.gridy = 3;
		c.weighty = 50;
		c.insets = table_insets;
		c.fill = GridBagConstraints.BOTH;
		cookieTableModel = new DefaultTableModel(null, title);
		this.add(new JScrollPane(new JTable(cookieTableModel)), c);

		// 注册组件
		UIComps.res_headers = headerTableModel;
		UIComps.res_cookies = cookieTableModel;
	}
}
