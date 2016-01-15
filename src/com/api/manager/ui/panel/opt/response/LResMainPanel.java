package com.api.manager.ui.panel.opt.response;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.api.manager.Config;
import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.support.UIEvent;
import com.api.manager.ui.util.CompLogger;
import com.api.manager.ui.util.Icons;

/**
 * 响应主体信息面板
 *
 * @author ASLai
 */
public class LResMainPanel extends JPanel {

	private static final long serialVersionUID = 3924732046674260101L;

	private JScrollPane body_scroll;

	/**
	 * 构成方法
	 */
	public LResMainPanel() {

		CompLogger.create(this.getClass(), 3);
		initComp();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		Insets left = new Insets(10, 15, 0, 10);
		Insets right = new Insets(10, 10, 0, 22);

		// 路径
		c.weightx = 0;
		c.insets = new Insets(20, 15, 0, 10);
		this.add(new JLabel("路径："), c);

		JTextField url = new JTextField();
		c.gridx = 1;
		c.weightx = 100;
		c.insets = new Insets(20, 10, 0, 22);
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(url, c);

		// 状态
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		c.insets = left;
		c.fill = GridBagConstraints.NONE;
		this.add(new JLabel("状态："), c);

		JTextField status = new JTextField();
		c.gridx = 1;
		c.weightx = 100;
		c.insets = right;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(status, c);

		// 调用时间
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		c.insets = left;
		c.fill = GridBagConstraints.NONE;
		this.add(new JLabel("耗时："), c);

		JTextField times = new JTextField();
		c.gridx = 1;
		c.weightx = 100;
		c.insets = right;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(times, c);

		// 耗时
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0;
		c.insets = left;
		c.fill = GridBagConstraints.NONE;
		this.add(new JLabel("时间："), c);

		JTextField date = new JTextField();
		c.gridx = 1;
		c.weightx = 100;
		c.insets = right;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(date, c);

		// 响应类型
		c.gridx = 0;
		c.gridy = 4;
		c.weightx = 0;
		c.insets = left;
		c.fill = GridBagConstraints.NONE;
		this.add(new JLabel("类型："), c);

		JTextField contentType = new JTextField();
		c.gridx = 1;
		c.weightx = 100;
		c.insets = right;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(contentType, c);

		// 响应体面板
		JPanel body_panel = new JPanel(new BorderLayout());
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.weighty = 100;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(20, 10, 0, 15);
		this.add(body_panel, c);

		// 响应内容
		JTextArea body = new JTextArea();
		body_scroll = new JScrollPane(body);
		body.setBorder(null);
		body_panel.add(body_scroll, BorderLayout.CENTER);

		// 格式化按钮
		JPanel btns = new JPanel(new GridBagLayout());
		body_panel.add(btns, BorderLayout.LINE_START);

		// 添加按钮
		GridBagConstraints btns_c = new GridBagConstraints();
		btns_c.fill = GridBagConstraints.HORIZONTAL;

		// 格式化html按钮
		JButton html = new JButton("html");
		html.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				UIEvent.formatBody(UIEvent.FORMAT_HTML);
			}
		});
		btns.add(html, btns_c);

		// 格式化xml按钮
		JButton json = new JButton("Json");
		json.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				UIEvent.formatBody(UIEvent.FORMAT_JSON);
			}
		});
		btns_c.gridy = 1;
		btns.add(json, btns_c);

		// 格式化xml按钮
		JButton xml = new JButton("xml");
		xml.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				UIEvent.formatBody(UIEvent.FORMAT_XML);
			}
		});
		btns_c.gridy = 2;
		btns.add(xml, btns_c);

		// 重置按钮
		JButton restore = new JButton("还原");
		restore.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				UIEvent.restoreBody();
			}
		});
		btns_c.gridy = 3;
		btns.add(restore, btns_c);

		// 占位
		btns_c.gridy = 4;
		btns_c.weighty = 100;
		btns.add(new JLabel(), btns_c);

		// 置顶按钮
		JButton up = new JButton(Icons.getIcon(Config.Icon.up));
		up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				body_scroll.getHorizontalScrollBar().setValue(0);
				body_scroll.getVerticalScrollBar().setValue(0);
			}
		});
		btns_c.gridy = 5;
		btns_c.weighty = 0;
		btns_c.insets = new Insets(0, 0, 0, 0);
		btns_c.fill = GridBagConstraints.HORIZONTAL;
		btns.add(up, btns_c);

		// 注册组件
		UIComps.res_url = url;
		UIComps.res_body = body;
		UIComps.res_status = status;
		UIComps.res_times = times;
		UIComps.res_date = date;
		UIComps.res_content_type = contentType;
	}
}
