package com.api.manager.ui.panel.opt.request;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import com.api.manager.Config;
import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.util.CompLogger;
import com.api.manager.ui.util.IconButton;

/**
 * 参数面板
 *
 * @author ASLai
 */
public class LTablePanel extends JPanel {

	private static final long serialVersionUID = 5772392114416978285L;

	private String name;
	private LAttrDialog attrDialog;
	private DefaultTableModel tableModel;
	private JTable table;
	private JPopupMenu menu;

	/**
	 * 构成方法
	 * 
	 * @param name 列表名称
	 */
	public LTablePanel(String name) {

		CompLogger.create(this.getClass(), 3);
		this.name = name;
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
		this.add(new JLabel(name), c);

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

		// 添加按钮
		IconButton add = new IconButton(Config.Icon.add);
		// 添加单击事件
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				showDialog();
			}
		});
		btns.add(add);

		// 删除按钮
		IconButton delete = new IconButton(Config.Icon.delete);
		// 添加单击事件
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				deleteSelected();
			}
		});
		btns.add(delete);

		// 清空按钮
		IconButton clear = new IconButton(Config.Icon.clear);
		// 添加单击事件
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				clearTabel();
			}
		});
		btns.add(clear);

		// 参数列表
		tableModel = new DefaultTableModel(null, new String[] { "键", "值" });
		table = new JTable(tableModel);
		table.addMouseListener(getMouseListener(table));
		JScrollPane scroll = new JScrollPane(table);
		// 添加事件
		scroll.addMouseListener(getMouseListener(scroll));
		c.gridx = 1;
		c.weightx = 100;
		c.fill = GridBagConstraints.BOTH;
		this.add(scroll, c);

		// 注册组件
		UIComps.params = tableModel;
	}

	/**
	 * 获取鼠标右键菜单事件
	 * 
	 * @param c 组件
	 * @return 事件
	 */
	private MouseListener getMouseListener(final Component c) {

		return new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				if (e.isPopupTrigger()) {

					// 创建菜单
					if (menu == null) {
						buildMenu();
					}
					menu.show(c, e.getX(), e.getY());
				}
			}
		};
	}

	/**
	 * 显示参数录入框
	 */
	private void showDialog() {

		if (attrDialog == null) {
			buildDialog();
		}
		SwingUtilities.updateComponentTreeUI(attrDialog);
		attrDialog.setVisible(true);
	}

	/**
	 * 删除选中项
	 */
	private void deleteSelected() {

		int[] rows = table.getSelectedRows();
		for (int i = rows.length - 1; i >= 0; i--) {
			tableModel.removeRow(rows[i]);
		}
	}

	/**
	 * 清空表格
	 */
	private void clearTabel() {

		int result = JOptionPane.showConfirmDialog(UIComps.ui, "清空全部" + name + "？", "确认", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			tableModel.getDataVector().clear();
			tableModel.fireTableDataChanged();
		}
	}

	/**
	 * 创建参数输入框
	 */
	private void buildDialog() {

		attrDialog = new LAttrDialog(tableModel);
		attrDialog.setTitle("添加" + name);
	}

	/**
	 * 构建菜单
	 */
	private void buildMenu() {

		menu = new JPopupMenu();
		JMenuItem add = new JMenuItem("添加");
		JMenuItem delete = new JMenuItem("删除");
		JMenuItem clear = new JMenuItem("清空全部");
		menu.add(add);
		menu.add(delete);
		menu.add(clear);

		// 添加事件
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				showDialog();
			}
		});
		// 删除选中事件
		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				deleteSelected();
			}
		});
		// 清空全部事件
		clear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				clearTabel();
			}
		});
	}

	/**
	 * @return 表格模型
	 */
	public DefaultTableModel getModel() {

		return tableModel;
	}
}
