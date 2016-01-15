package com.api.manager.ui.mbar.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.api.manager.Config;
import com.api.manager.ui.support.UIConfig;
import com.api.manager.ui.util.Icons;

/**
 * 版本信息框
 *
 * @author ASLai
 */
public class LVersionDialog extends JDialog {

	private static final long serialVersionUID = 2642875937564217448L;

	/**
	 * 构造方法
	 */
	public LVersionDialog() {

		initStyle();
		initComp();
	}

	/**
	 * 初始化样式
	 */
	private void initStyle() {

		// 设置图标
		this.setIconImage(Icons.getImage(Config.Icon.logo));
		// 设置窗口大小
		this.setSize(300, 400);
		// 设置窗口居中
		// 获取屏幕的尺寸
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screen.width / 2 - this.getWidth() / 2;
		int y = screen.height / 2 - this.getHeight() / 2;
		// 设置显示位置
		this.setLocation(x, y);
		// 设置不能改变大小
		this.setResizable(false);
		// 设置modal模式
		this.setModal(true);
		// 表头
		this.setTitle("版本信息");
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		this.setLayout(new GridBagLayout());

		// 设置布局
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 100;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 10, 0, 10);
		this.add(new JLabel("版本信息："), c);

		JTextArea versions = new JTextArea(UIConfig.sysConfig.readVersion());
		versions.setEditable(false);
		// 自动换行
		versions.setLineWrap(true);
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 100;
		c.gridy = 1;
		this.add(new JScrollPane(versions), c);

		JPanel panel = new JPanel();
		c.insets = new Insets(5, 10, 5, 10);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0;
		c.gridy = 2;
		this.add(panel, c);

		// 确定按钮
		JButton ok = new JButton("确定");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 关闭
				close();
			}
		});
		panel.add(ok);
	}

	/**
	 * 关闭
	 */
	private void close() {

		this.setVisible(false);
	}
}
