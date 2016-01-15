package com.api.manager.ui.panel.opt.request;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.api.manager.Config;
import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.util.CompLogger;
import com.api.manager.ui.util.IconButton;

/**
 * 参数面板
 *
 * @author ASLai
 */
public class LBodyPanel extends JPanel {

	private static final long serialVersionUID = -970179038413155905L;

	private JTextArea body;
	private JPopupMenu menu;

	/**
	 * 构成方法
	 */
	public LBodyPanel() {

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

		// 标题
		c.weightx = 100;
		c.gridwidth = 2;
		c.insets = new Insets(5, 5, 5, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(new JLabel("请求体"), c);

		// 操作按钮
		JPanel btns = new JPanel();
		btns.setLayout(new BoxLayout(btns, BoxLayout.Y_AXIS));
		c.gridy = 1;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 100;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.VERTICAL;
		this.add(btns, c);

		// 清空按钮
		IconButton clear = new IconButton(Config.Icon.clear);
		// 添加单击事件
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				clearBody();
			}
		});
		btns.add(clear);

		// 内容
		body = new JTextArea();
		body.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				if (e.isPopupTrigger()) {

					// 创建菜单
					if (menu == null) {
						buildMenu();
					}
					menu.show(body, e.getX(), e.getY());
				}
			}
		});
		c.gridx = 1;
		c.weightx = 100;
		c.fill = GridBagConstraints.BOTH;
		this.add(new JScrollPane(body), c);
	}

	/**
	 * 构建菜单
	 */
	private void buildMenu() {

		menu = new JPopupMenu();
		JMenuItem clear = new JMenuItem("清空");
		menu.add(clear);

		// 清空全部事件
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				clearBody();
			}
		});
	}

	/**
	 * 清空body
	 */
	private void clearBody() {

		int result = JOptionPane.showConfirmDialog(UIComps.ui, "清空请求体？", "确认", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			body.setText(null);
		}
	}

	/**
	 * @return 获取录入框组件
	 */
	public JTextArea getBody() {

		return body;
	}
}
