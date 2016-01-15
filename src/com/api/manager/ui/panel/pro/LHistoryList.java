package com.api.manager.ui.panel.pro;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.api.manager.ui.bean.HistoryBean;
import com.api.manager.ui.panel.pro.fav.FavoritesDialog;
import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.support.UIEvent;
import com.api.manager.ui.util.CompLogger;

/**
 * 调用历史列表
 *
 * @author ASLai
 */
public class LHistoryList extends JPanel {

	private static final long serialVersionUID = 6918933918172875241L;

	private JList<HistoryBean> list;
	private DefaultListModel<HistoryBean> listModel;
	private JPopupMenu menu;
	private JMenuItem fav;
	private JMenuItem load;
	private JMenuItem clear_select;

	/**
	 * 构造方法
	 */
	public LHistoryList() {

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
		c.weightx = 100;

		// 标题
		JButton title = new JButton("调用历史");
		title.setFocusable(false);
		title.setMargin(new Insets(3, 3, 3, 3));
		title.setRolloverEnabled(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(title, c);

		// 列表
		listModel = new DefaultListModel<HistoryBean>();
		// 数据变动监听
		listModel.addListDataListener(new ListDataListener() {

			@Override
			public void intervalAdded(ListDataEvent e) {

				list.clearSelection();
			}

			@Override
			public void intervalRemoved(ListDataEvent e) {

				list.clearSelection();
			}

			@Override
			public void contentsChanged(ListDataEvent e) {

			}
		});
		list = new JList<HistoryBean>(listModel);
		// 设置单选
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// 添加事件
		list.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				if (e.isPopupTrigger()) {

					int index = list.locationToIndex(e.getPoint());
					if (index != -1) {
						// 选中
						list.setSelectedIndex(index);
					}
					// 创建菜单
					if (menu == null) {
						buildMenu();
					}
					itemMenu(index != -1);
					menu.show(list, e.getX(), e.getY());
				}
			}
		});
		// 滚动
		JScrollPane scroll = new JScrollPane(list);
		scroll.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 100;
		this.add(scroll, c);

		UIComps.history = listModel;
	}

	/**
	 * 构建菜单
	 */
	private void buildMenu() {

		menu = new JPopupMenu();
		fav = new JMenuItem("添加收藏");
		load = new JMenuItem("读取");
		clear_select = new JMenuItem("删除");
		JMenuItem clear_all = new JMenuItem("清空全部");
		menu.add(fav);
		menu.add(load);
		menu.add(clear_select);
		menu.add(clear_all);

		// 添加收藏事件
		fav.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int index = list.getSelectedIndex();
				if (index != -1) {
					HistoryBean history = listModel.get(index);
					FavoritesDialog dialog = new FavoritesDialog(UIComps.favorites, history);
					dialog.setVisible(true);
				}
			}
		});

		// 读取事件
		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int index = list.getSelectedIndex();
				if (index != -1) {
					HistoryBean history = listModel.get(index);
					UIEvent.setRePanel(history);
				}
			}
		});
		// 清空选中事件
		clear_select.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int index = list.getSelectedIndex();
				if (index != -1) {
					listModel.remove(index);
				}
			}
		});
		// 清空全部事件
		clear_all.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int result = JOptionPane.showConfirmDialog(UIComps.ui, "清空全部调用历史？", "确认", JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (result == JOptionPane.YES_OPTION) {
					listModel.clear();
				}
			}
		});
	}

	/**
	 * @param show 是否显示菜单项
	 */
	private void itemMenu(boolean show) {

		fav.setVisible(show);
		load.setVisible(show);
		clear_select.setVisible(show);
	}
}
