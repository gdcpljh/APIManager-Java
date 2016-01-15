package com.api.manager.ui.panel.pro.fav;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.api.manager.Config;
import com.api.manager.ui.bean.FavoritesBean;
import com.api.manager.ui.util.Icons;

/**
 * 收藏夹渲染器
 *
 * @author ASLai
 */
public class FavoritesRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 587945400776816497L;

	private Icon icon_leaf = Icons.getIcon(Config.Icon.leaf);
	private Icon icon_folder_open = Icons.getIcon(Config.Icon.folder_open);
	private Icon icon_folder = Icons.getIcon(Config.Icon.folder);

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf,
			int row, boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		// 获取节点
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		// 获取节点数据
		Object bean = node.getUserObject();
		if (bean instanceof FavoritesBean) {
			if (((FavoritesBean) bean).isLeaf()) {
				// 叶子节点
				setIcon(icon_leaf);
			} else if (expanded) {
				// 打开状态文件夹
				setIcon(icon_folder_open);
			} else {
				// 关闭状态文件夹
				setIcon(icon_folder);
			}
		}
		return this;
	}
}
