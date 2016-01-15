package com.api.manager.ui.panel.opt.request;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.api.manager.Config;
import com.api.manager.ui.util.Icons;

/**
 * 属性添加窗口
 *
 * @author ASLai
 */
public class LAttrDialog extends JDialog {

	private static final long serialVersionUID = 9134443776388650219L;

	private DefaultTableModel tableModel;
	private JTextField key;
	private JTextField value;

	/**
	 * 构造方法
	 * 
	 * @param tableModel 列表模型
	 */
	public LAttrDialog(DefaultTableModel tableModel) {

		this.tableModel = tableModel;
		initStyle();
		initComp();
		initEvent();
	}

	/**
	 * 初始化样式
	 */
	private void initStyle() {

		// 设置图标
		this.setIconImage(Icons.getImage(Config.Icon.logo));
		// 设置窗口大小
		this.setSize(250, 150);
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
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		this.setLayout(new GridBagLayout());

		// 设置布局
		GridBagConstraints c = new GridBagConstraints();

		Insets left_insets = new Insets(10, 15, 0, 5);
		Insets right_insets = new Insets(10, 5, 0, 15);

		// 添加组件
		c.insets = left_insets;
		this.add(new JLabel("键："), c);

		key = new JTextField();
		// 添加事件
		key.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					value.requestFocus();
				}
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.weightx = 100;
		c.insets = right_insets;
		this.add(key, c);

		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		c.insets = left_insets;
		this.add(new JLabel("值："), c);

		value = new JTextField();
		// 添加事件
		value.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					addAndContinue();
				}
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.weightx = 100;
		c.insets = right_insets;
		this.add(value, c);

		JPanel btns = new JPanel();
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.insets = new Insets(15, 10, 5, 10);
		this.add(btns, c);

		// 按钮布局
		btns.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 33;
		c.insets = new Insets(0, 5, 0, 5);

		// 添加按钮
		JButton add = new JButton("添加");
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				addAndClose();
			}
		});
		c.gridx = 0;
		btns.add(add, c);

		// 添加并继续按钮
		JButton next = new JButton("继续");
		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				addAndContinue();
			}
		});
		c.gridx = 1;
		btns.add(next, c);

		// 取消按钮
		JButton cancel = new JButton("取消");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 清空
				clear();
				// 关闭
				close();
			}
		});
		c.gridx = 2;
		btns.add(cancel, c);
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {

		// 设置退出事件
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {

				// 清空
				clear();
			}
		});
	}

	/**
	 * 添加
	 */
	private void addAndClose() {

		String k = key.getText();
		if (k != null && !k.isEmpty()) {
			// 获取键值
			String[] row = new String[] { k, value.getText() };
			// 添加到列表中
			tableModel.addRow(row);
			// 清空
			clear();
			// 关闭窗口
			close();
		} else {
			// 设置焦点
			key.requestFocus();
		}
	}

	/**
	 * 添加并继续
	 */
	private void addAndContinue() {

		String k = key.getText();
		if (k != null && !k.isEmpty()) {
			// 获取键值
			String[] row = new String[] { k, value.getText() };
			// 添加到列表中
			tableModel.addRow(row);
			// 清空
			clear();
		}
		// 设置焦点
		key.requestFocus();
	}

	/**
	 * 隐藏窗体
	 */
	private void clear() {

		key.setText(null);
		value.setText(null);
	}

	/**
	 * 关闭
	 */
	private void close() {

		this.setVisible(false);
	}
}
