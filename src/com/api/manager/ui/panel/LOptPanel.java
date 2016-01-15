package com.api.manager.ui.panel;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.api.manager.Config;
import com.api.manager.ui.panel.opt.LRequestPanel;
import com.api.manager.ui.panel.opt.LResponsePanel;
import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.support.UIConfig;
import com.api.manager.ui.support.UIEvent;
import com.api.manager.ui.util.CompLogger;
import com.api.manager.ui.util.IconButton;
import com.api.manager.worker.http.HttpSender.Method;

/**
 * 操作面板
 *
 * @author ASLai
 */
public class LOptPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -8201205034235429816L;

	private JTextField url;
	private JButton submit;

	/**
	 * 构造方法
	 */
	public LOptPanel() {

		CompLogger.create(this.getClass(), 1);
		initComp();
		initEvent();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		// 设置布局
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// 标签
		c.insets = new Insets(20, 15, 5, 0);
		this.add(new JLabel("接口URL:"), c);

		// 路径录入框
		url = new JTextField();
		// 添加事件
		url.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					submit();
				}
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.weightx = 100;
		c.insets = new Insets(20, 10, 5, 0);
		this.add(url, c);

		// 请求方式
		DefaultComboBoxModel<Method> method_model = new DefaultComboBoxModel<Method>();
		for (Method method : Method.values()) {
			method_model.addElement(method);
		}
		JComboBox<Method> method = new JComboBox<Method>(method_model);
		method.setSelectedItem(UIConfig.sysConfig.default_method);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 2;
		c.weightx = 0;
		this.add(method, c);

		// 访问按钮
		submit = new JButton("访问 (Ctrl+D)");
		submit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		submit.setFocusable(false);
		// 添加事件
		submit.addActionListener(this);
		c.gridx = 4;
		this.add(submit, c);

		// 清空按钮
		IconButton clear = new IconButton(Config.Icon.clear);
		// 添加事件
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				clear();
			}
		});
		c.insets = new Insets(20, 0, 5, 10);
		c.gridx = 5;
		this.add(clear, c);

		// 详细面板
		JTabbedPane tab = new JTabbedPane();
		tab.addTab("请求参数", new LRequestPanel());
		tab.addTab("响应报告", new LResponsePanel());
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(10, 10, 10, 10);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 100;
		c.weighty = 100;
		c.gridwidth = 6;
		this.add(tab, c);

		// 注册组件
		UIComps.url = url;
		UIComps.method = method;
		UIComps.optTab = tab;
		UIComps.submit = submit;
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {

		// 设置事件
		this.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		submit();
	}

	/**
	 * 清空
	 */
	private void clear() {

		int result = JOptionPane.showConfirmDialog(UIComps.ui, "是否清空访问信息？", "确认", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			// 清空请求
			UIEvent.clearReqPanel();
			UIEvent.clearResPanel();
			UIComps.optTab.setSelectedIndex(0);
		}
	}

	/**
	 * 提交
	 */
	private void submit() {

		if (submit.isEnabled()) {
			String u = url.getText();
			if (u != null && !u.trim().isEmpty()) {
				// 发送请求
				UIEvent.send();
			} else {
				url.requestFocus();
			}
		}
	}
}
