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
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.api.manager.ui.bean.FavoritesBean;
import com.api.manager.ui.panel.pro.fav.FavoritesDialog;
import com.api.manager.ui.panel.pro.fav.FavoritesRenderer;
import com.api.manager.ui.panel.pro.fav.FavoritesTreeModel;
import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.support.UIEvent;
import com.api.manager.ui.util.CompLogger;

/**
 * 项目列表
 *
 * @author ASLai
 */
public class LFavoritesList extends JPanel {

	private static final long serialVersionUID = 9061149064517144473L;

	private static final int ROOT = 0;
	private static final int DIR = 1;
	private static final int LEAF = 2;

	private DefaultMutableTreeNode favNode;
	private FavoritesTreeModel favModel;
	private JTree favTree;
	private JPopupMenu menu;
	private JMenuItem dir;
	private JMenuItem load;
	private JMenuItem edit;
	private JMenuItem remove;

	/**
	 * 构造方法
	 */
	public LFavoritesList() {

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
		JButton title = new JButton("收藏夹");
		title.setFocusable(false);
		title.setMargin(new Insets(3, 3, 3, 3));
		title.setRolloverEnabled(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(title, c);

		// 收藏夹
		favNode = new DefaultMutableTreeNode(new FavoritesBean("收藏夹"));
		// 树模型
		favModel = new FavoritesTreeModel(favNode, true);
		// 收藏夹树
		favTree = new JTree(favModel);
		favTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		favTree.setRootVisible(false);
		favTree.setShowsRootHandles(true);
		favTree.setCellRenderer(new FavoritesRenderer());
		// 添加事件
		favTree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {

				// 判断是否为菜单事件
				if (e.isPopupTrigger()) {

					DefaultMutableTreeNode node;
					TreePath path = favTree.getPathForLocation(e.getX(), e.getY());
					if (path == null) {
						favTree.clearSelection();
						node = favNode;
					} else {
						favTree.setSelectionPath(path);
						node = (DefaultMutableTreeNode) favTree.getLastSelectedPathComponent();
					}
					// 创建菜单
					if (menu == null) {
						buildMenu();
					}
					if (node == favNode) {
						// 根节点
						itemMenu(ROOT);
					} else if (favModel.nodeIsLeaf(node)) {
						// 叶子节点
						itemMenu(LEAF);
					} else {
						// 文件夹
						itemMenu(DIR);
					}
					menu.show(favTree, e.getX(), e.getY());
				}
			}
		});
		favTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {

				TreePath path = favTree.getSelectionPath();
				if (path != null) {
					favTree.expandPath(path);
				}
			}
		});
		// 滚动
		JScrollPane scroll = new JScrollPane(favTree);
		scroll.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 100;
		this.add(scroll, c);

		// 注册组件
		UIComps.favorites = favTree;
		UIComps.favoritesModel = favModel;
		UIComps.favoritesNode = favNode;
	}

	/**
	 * 构建菜单
	 */
	private void buildMenu() {

		menu = new JPopupMenu();
		dir = new JMenuItem("新建文件夹");
		load = new JMenuItem("读取");
		edit = new JMenuItem("编辑");
		remove = new JMenuItem("删除");
		menu.add(dir);
		menu.add(load);
		menu.add(edit);
		menu.add(remove);

		// 新建文件夹
		dir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 新建名称输入框
				String name = inputDialog("新建文件夹", "名称：", null);
				// 不为取消
				if (name != null) {
					// 获取选中节点
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) favTree.getLastSelectedPathComponent();
					// 获取选中路径
					TreePath path = favTree.getSelectionPath();
					if (node == null) {
						// 未选中默认根目录
						node = favNode;
					}
					// 添加文件夹
					node.add(new DefaultMutableTreeNode(new FavoritesBean(name.toString())));
					// 重载树
					favModel.reload();
					// 展开文件夹
					if (path != null) {
						favTree.expandPath(path);
					}
				}
			}
		});

		// 读取
		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) favTree.getLastSelectedPathComponent();
				if (node != null) {
					Object obj = node.getUserObject();
					if (obj instanceof FavoritesBean) {
						FavoritesBean bean = (FavoritesBean) obj;
						if (bean.isLeaf()) {
							// 加载访问信息
							UIEvent.setRePanel(bean);
						}
					}
				}
			}
		});

		// 编辑
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 获取选中节点
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) favTree.getLastSelectedPathComponent();
				// 获取选中路径
				TreePath path = favTree.getSelectionPath();
				if (node != null && node != favNode) {
					Object obj = node.getUserObject();
					if (obj instanceof FavoritesBean) {
						FavoritesBean bean = (FavoritesBean) obj;
						if (bean.isLeaf()) {
							// 访问允许修改名称及位置
							FavoritesDialog dialog = new FavoritesDialog(favTree);
							dialog.setVisible(true);
						} else {
							// 文件夹只修改名称
							String name = inputDialog("修改文件夹", "名称：", bean.getName());
							if (name != null) {
								bean.setName(name.trim());
								// 重载树
								favModel.reload();
								// 展开文件夹
								if (path != null) {
									favTree.expandPath(path.getParentPath());
								}
							}
						}
					}
				}
			}
		});

		// 删除
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 获取选中节点
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) favTree.getLastSelectedPathComponent();
				TreePath path = favTree.getSelectionPath();
				if (node != null && node != favNode) {
					// 提示
					int result = JOptionPane.showConfirmDialog(UIComps.ui, "是否删除？", "确认", JOptionPane.YES_NO_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					if (result != JOptionPane.YES_OPTION) {
						return;
					}
					// 父节点
					DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
					// 删除自己
					parent.remove(node);
					// 重载树
					favModel.reload();
					// 展开文件夹
					if (path != null) {
						if (parent.getChildCount() == 0) {
							favTree.expandPath(path.getParentPath().getParentPath());
						} else {
							favTree.expandPath(path.getParentPath());
						}
					}
				}
			}
		});
	}

	/**
	 * @param show 是否显示菜单项
	 */
	private void itemMenu(int type) {

		switch (type) {
		case ROOT:
			dir.setVisible(true);
			load.setVisible(false);
			edit.setVisible(false);
			remove.setVisible(false);
			break;
		case DIR:
			dir.setVisible(true);
			load.setVisible(false);
			edit.setVisible(true);
			remove.setVisible(true);
			break;
		case LEAF:
			dir.setVisible(false);
			load.setVisible(true);
			edit.setVisible(true);
			remove.setVisible(true);
			break;
		}
	}

	/**
	 * 名称弹出框
	 * 
	 * @param title 标题
	 * @param label 标签名称
	 * @param value 默认值
	 * @return 返回
	 */
	private String inputDialog(String title, String label, String value) {

		String name;
		while (true) {
			name = (String) JOptionPane.showInputDialog(UIComps.ui, label, title, JOptionPane.PLAIN_MESSAGE, null, null,
					value);
			if (name == null) {
				break;
			}
			if (!name.trim().isEmpty()) {
				break;
			}
		}
		return name;
	}
}
