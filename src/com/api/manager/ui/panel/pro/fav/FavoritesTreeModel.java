package com.api.manager.ui.panel.pro.fav;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.api.manager.ui.bean.FavoritesBean;

/**
 * 文件夹树显示模型
 *
 * @author ASLai
 */
public class FavoritesTreeModel extends DefaultTreeModel {

	private static final long serialVersionUID = -2526685432826389542L;

	private boolean showLeaf;

	/**
	 * 构造方法
	 * 
	 * @param root 树根节点
	 * @param showLeaf 是否显示叶子节点
	 */
	public FavoritesTreeModel(TreeNode root, boolean showLeaf) {

		super(root);
		this.showLeaf = showLeaf;
	}

	@Override
	public Object getChild(Object parent, int index) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent;
		// 判断是否显示叶子节点
		if (showLeaf) {
			return node.getChildAt(index);
		}
		// 判断索引
		int i = 0;
		// 实际索引
		int rel_i = 0;
		Enumeration<?> children = node.children();
		// 遍历获取节点
		while (children.hasMoreElements()) {
			boolean isLeaf = nodeIsLeaf(children.nextElement());
			if (!isLeaf) {
				if (i == index) {
					return node.getChildAt(rel_i);
				}
				i++;
			}
			rel_i++;
		}
		return node.getChildAt(i);
	}

	@Override
	public int getChildCount(Object parent) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent;
		// 判断是否显示叶子节点
		if (showLeaf) {
			return node.getChildCount();
		}
		int count = 0;
		Enumeration<?> children = node.children();
		// 遍历节点
		while (children.hasMoreElements()) {
			boolean isLeaf = nodeIsLeaf(children.nextElement());
			if (!isLeaf) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 判断是否为叶子节点
	 * 
	 * @param node 节点
	 * @return 是否为叶子节点
	 */
	public boolean nodeIsLeaf(Object node) {

		Object bean = ((DefaultMutableTreeNode) node).getUserObject();
		if (bean instanceof FavoritesBean) {
			if (((FavoritesBean) bean).isLeaf()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isLeaf(Object node) {

		return getChildCount(node) == 0;
	}
}
