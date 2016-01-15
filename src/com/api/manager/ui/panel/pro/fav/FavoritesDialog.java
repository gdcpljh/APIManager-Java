package com.api.manager.ui.panel.pro.fav;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.api.manager.Config;
import com.api.manager.ui.bean.FavoritesBean;
import com.api.manager.ui.bean.ReBean;
import com.api.manager.ui.util.Icons;

/**
 * 收藏编辑框
 *
 * @author ASLai
 */
public class FavoritesDialog extends JDialog {

	private static final long serialVersionUID = 8119489070421539002L;

	private boolean newFav;
	private ReBean reBean;
	private DefaultMutableTreeNode updateNode;
	private JTree tree;
	private JTree favTree;
	private FavoritesTreeModel favModel;
	private TreePath favPath;
	private JTextField name;

	/**
	 * 新增收藏构造方法
	 * 
	 * @param favTree 树
	 * @param reBean 对象
	 */
	public FavoritesDialog(JTree favTree, ReBean reBean) {

		this.newFav = true;
		this.reBean = reBean;
		this.favTree = favTree;
		this.favModel = (FavoritesTreeModel) favTree.getModel();
		initStyle();
		initComp();
	}

	/**
	 * 收藏构造方法
	 * 
	 * @param favTree 树
	 */
	public FavoritesDialog(JTree favTree) {

		this.newFav = false;
		this.favTree = favTree;
		this.favModel = (FavoritesTreeModel) favTree.getModel();
		this.updateNode = (DefaultMutableTreeNode) favTree.getLastSelectedPathComponent();
		this.reBean = (ReBean) updateNode.getUserObject();
		this.favPath = favTree.getSelectionPath().getParentPath();
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
		this.setSize(300, 300);
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
		// 标题
		if (newFav) {
			this.setTitle("添加到收藏");
		} else {
			this.setTitle("编辑访问信息");
		}
		// 关闭销毁
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		this.setLayout(new GridBagLayout());

		Insets label_insets = new Insets(10, 10, 0, 10);
		Insets body_insets = new Insets(5, 10, 0, 10);

		// 设置布局
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 100;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = label_insets;
		c.gridwidth = 2;

		this.add(new JLabel("名称："), c);

		name = new JTextField();
		if (!newFav) {
			name.setText(updateNode.toString());
		}
		// 添加事件
		name.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					addFavorites();
				}
			}
		});
		c.gridy = 1;
		c.insets = body_insets;
		this.add(name, c);

		c.gridy = 2;
		c.insets = label_insets;
		this.add(new JLabel("文件夹："), c);

		// 树模型
		FavoritesTreeModel model = new FavoritesTreeModel((TreeNode) favModel.getRoot(), false);
		// 收藏夹树
		tree = new JTree(model);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setCellRenderer(new FavoritesRenderer());
		// 默认选中根节点
		if (newFav) {
			tree.setSelectionRow(0);
		} else {
			tree.setSelectionPath(favPath);
		}
		// 添加选中事件
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {

				TreePath path = tree.getSelectionPath();
				if (path != null) {
					tree.expandPath(path);
				}
			}
		});
		c.gridy = 3;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 100;
		c.insets = body_insets;
		this.add(new JScrollPane(tree), c);

		JButton add = new JButton("确定");
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				addFavorites();
			}
		});
		c.gridy = 4;
		c.gridwidth = 1;
		c.weighty = 0;
		c.insets = new Insets(15, 10, 15, 5);
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(add, c);

		JButton cancel = new JButton("取消");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				dispose();
			}
		});
		c.gridx = 1;
		c.insets = new Insets(15, 5, 15, 10);
		this.add(cancel, c);
	}

	/**
	 * 添加收藏
	 */
	private void addFavorites() {

		String n = name.getText();
		if (n == null || n.isEmpty()) {
			name.requestFocus();
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		if (node == null) {
			return;
		}
		// 选择文件夹路径
		TreePath path = tree.getSelectionPath();
		// 判断是否为修改
		if (newFav) {
			// 添加节点
			node.add(new DefaultMutableTreeNode(new FavoritesBean(n, reBean)));
		} else {
			// 判断是否只是修改名称
			if (favPath.equals(path)) {
				FavoritesBean bean = (FavoritesBean) reBean;
				bean.setName(n);
			} else {
				// 添加并移除自身
				node.add(new DefaultMutableTreeNode(new FavoritesBean(n, reBean)));
				((DefaultMutableTreeNode) updateNode.getParent()).remove(updateNode);
			}
		}
		// 更新收藏夹组件
		favModel.reload();
		// 展开文件夹
		favTree.expandPath(path);
		dispose();
	}
}
