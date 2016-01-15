package com.api.manager.ui.mbar.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.api.manager.Config;
import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.support.UIConfig;
import com.api.manager.ui.support.UIEvent;
import com.api.manager.ui.util.Icons;

/**
 * 用户设置信息框
 *
 * @author ASLai
 */
public class LSettingDialog extends JDialog {

	private static final long serialVersionUID = 4292653218395486050L;

	private static JTextField maxHistory;
	private static JComboBox<String> saveHistory;
	private static JComboBox<String> single;
	private static JTextField timeOut;

	private static final String HISTORY_SAVE = "退出保存";
	private static final String HISTORY_UNSAVE = "退出不保存";

	private static final String SINGLE_TRUE = "仅允许一个系统运行";
	private static final String SINGLE_FALSE = "同时允许多个系统运行";

	/**
	 * 构造方法
	 */
	public LSettingDialog() {

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
		this.setSize(300, 210);
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
		this.setTitle("系统设置");
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		this.setLayout(new GridBagLayout());

		// 回车事件
		KeyAdapter ka = new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// 保存配置
					setConfig();
				}
			}
		};

		// 设置布局
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 0, 10);

		// 单例模式
		this.add(new JLabel("系统启动模式："), c);

		DefaultComboBoxModel<String> single_model = new DefaultComboBoxModel<String>();
		single_model.addElement(SINGLE_TRUE);
		single_model.addElement(SINGLE_FALSE);
		single = new JComboBox<String>(single_model);
		single.addKeyListener(ka);
		c.gridx = 1;
		c.weightx = 100;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(single, c);

		// 保存历史记录
		c.gridy = 1;
		c.gridx = 0;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		this.add(new JLabel("保存历史记录："), c);

		DefaultComboBoxModel<String> method_model = new DefaultComboBoxModel<String>();
		method_model.addElement(HISTORY_SAVE);
		method_model.addElement(HISTORY_UNSAVE);
		saveHistory = new JComboBox<String>(method_model);
		saveHistory.addKeyListener(ka);
		c.gridx = 1;
		c.weightx = 100;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(saveHistory, c);

		// 历史记录条数
		c.gridy = 2;
		c.gridx = 0;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		this.add(new JLabel("历史记录条数："), c);

		maxHistory = new JTextField();
		// 添加事件
		maxHistory.addKeyListener(ka);
		c.gridx = 1;
		c.weightx = 100;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(maxHistory, c);

		// 超时时间
		c.gridy = 3;
		c.gridx = 0;
		c.weightx = 0;
		c.fill = GridBagConstraints.NONE;
		this.add(new JLabel("访问超时 (ms)："), c);

		timeOut = new JTextField();
		// 添加事件
		timeOut.addKeyListener(ka);
		c.gridx = 1;
		c.weightx = 100;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(timeOut, c);

		// 按钮面板
		JPanel btns = new JPanel();
		c.gridy = 4;
		c.gridx = 0;
		c.gridwidth = 2;
		c.weightx = 100;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(15, 10, 5, 10);
		this.add(btns, c);

		btns.setLayout(new GridBagLayout());
		c = new GridBagConstraints();

		JButton backDefault = new JButton("还原默认");
		backDefault.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 还原默认
				backDefault();
			}
		});
		btns.add(backDefault, c);

		// 占位
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 100;
		c.gridx = 1;
		btns.add(new JLabel(), c);

		JButton ok = new JButton("确定");
		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 保存配置
				setConfig();
			}
		});
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		c.gridx = 2;
		btns.add(ok, c);

		// 取消按钮
		JButton cancel = new JButton("取消");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 关闭
				close();
			}
		});
		c.gridx = 3;
		c.insets = new Insets(0, 5, 0, 0);
		btns.add(cancel, c);
	}

	/**
	 * 关闭
	 */
	private void close() {

		this.setVisible(false);
	}

	private void setConfig() {

		// 校验最大条数
		int max;
		try {
			max = Integer.valueOf(maxHistory.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(UIComps.ui, "历史记录条数为大于零整数", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (max <= 0) {
			JOptionPane.showMessageDialog(UIComps.ui, "历史记录条数为大于零整数", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// 校验超时时间
		int time;
		String timeStr = timeOut.getText();
		if (timeStr == null || timeStr.equals("")) {
			time = -1;
		} else {
			try {
				time = Integer.valueOf(timeStr);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(UIComps.ui, "访问超时时间为大于零整数，不设置超时请置空", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (time < 0) {
				JOptionPane.showMessageDialog(UIComps.ui, "访问超时时间为大于零整数，不设置超时请置空", "错误", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (time == 0) {
				time = -1;
			}
		}

		boolean save = saveHistory.getSelectedItem().equals(HISTORY_SAVE);
		boolean singleton = single.getSelectedItem().equals(SINGLE_TRUE);

		close();

		// 保存配置
		UIConfig.sysConfig.max_history = max;
		UIConfig.sysConfig.time_out = time;
		UIConfig.sysConfig.save_history = save;
		UIConfig.sysConfig.singleton = singleton;
		UIEvent.saveBaseConfig();
	}

	/**
	 * 还原默认
	 */
	private void backDefault() {

		close();
		// 初始化配置
		UIConfig.sysConfig.init();
		// 保存配置
		UIEvent.saveBaseConfig();
	}

	/**
	 * 初始化值
	 */
	public void initValue() {

		// 历史记录条数
		maxHistory.setText(UIConfig.sysConfig.max_history + "");
		// 是否保存历史记录
		if (UIConfig.sysConfig.save_history) {
			saveHistory.setSelectedItem(HISTORY_SAVE);
		} else {
			saveHistory.setSelectedItem(HISTORY_UNSAVE);
		}
		// 超时时间
		if (UIConfig.sysConfig.time_out != -1) {
			timeOut.setText(UIConfig.sysConfig.time_out + "");
		} else {
			timeOut.setText("");
		}
		// 单例模式
		if (UIConfig.sysConfig.singleton) {
			single.setSelectedItem(SINGLE_TRUE);
		} else {
			single.setSelectedItem(SINGLE_FALSE);
		}
	}
}
