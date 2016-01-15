package com.api.manager.ui.support;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.api.manager.ui.bean.HistoryBean;
import com.api.manager.ui.panel.pro.fav.FavoritesTreeModel;
import com.api.manager.worker.http.HttpSender.Method;

/**
 * UI组件
 *
 * @author ASLai
 */
public class UIComps {

	/** 框架 */
	public static JFrame ui;

	/** 标签页 */
	public static JTabbedPane optTab;
	/** 进度条 */
	public static JProgressBar progress;
	/** 想问信息 */
	public static JLabel info;

	/** 访问按钮 */
	public static JButton submit;
	/** 访问url */
	public static JTextField url;
	/** 请求方式 */
	public static JComboBox<Method> method;
	/** 参数列表 */
	public static DefaultTableModel params;
	/** 头列表 */
	public static DefaultTableModel headers;
	/** Cookie列表 */
	public static DefaultTableModel cookies;
	/** 体 */
	public static JTextArea body;

	/** 访问url */
	public static JTextField res_url;
	/** 体 */
	public static JTextArea res_body;
	/** 状态 */
	public static JTextField res_status;
	/** 调用时间 */
	public static JTextField res_date;
	/** 耗时 */
	public static JTextField res_times;
	/** 响应类型 */
	public static JTextField res_content_type;
	/** Header列表 */
	public static DefaultTableModel res_headers;
	/** Cookie列表 */
	public static DefaultTableModel res_cookies;

	/** 调用历史 */
	public static DefaultListModel<HistoryBean> history;
	/** 收藏夹 */
	public static JTree favorites;
	/** 收藏夹模型 */
	public static FavoritesTreeModel favoritesModel;
	/** 收藏夹节点 */
	public static DefaultMutableTreeNode favoritesNode;
}
