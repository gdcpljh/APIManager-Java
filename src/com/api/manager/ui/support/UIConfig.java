package com.api.manager.ui.support;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import com.api.manager.Config;
import com.api.manager.ui.bean.FavoritesBean;
import com.api.manager.ui.bean.HistoryBean;
import com.api.manager.worker.tool.compress.Compressor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 用户配置
 *
 * @author ASLai
 */
public class UIConfig {

	private static Logger logger = Logger.getLogger(UIConfig.class);

	private static Gson gson = new Gson();
	private static FileSystemView fileSys = FileSystemView.getFileSystemView();

	/** 配置信息 */
	public static Config sysConfig;

	/**
	 * 初始化
	 */
	public static void init() {

		sysConfig = new Config();
		sysConfig.init();
	}

	/**
	 * 获取系统配置路径
	 * 
	 * @return 路径
	 */
	public static File getConfigDir() {

		return new File(fileSys.getDefaultDirectory(), sysConfig.config_dir);
	}

	/**
	 * 保存配置文件
	 * 
	 * @param config 配置对象
	 */
	public static void saveConfig(ConfigBean config) {

		long start = System.currentTimeMillis();
		logger.info("保存用户配置");

		File dir = getConfigDir();
		// 判断文件夹是否存在
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			save(new File(dir, sysConfig.config_file_style), config.lf);
			saveSettingConfig(new File(dir, sysConfig.config_file_base), config.getBase());
			saveFavoritesConfig(new File(dir, sysConfig.config_file_favorites), config.getFavorites());
			saveHistoryConfig(new File(dir, sysConfig.config_file_history), config.getHistory());
			logger.info("保存用户配置 -完毕- " + (System.currentTimeMillis() - start) + "ms");
		} catch (IOException e) {
			logger.error("保存配置文件失败", e);
		}
	}

	/**
	 * 保存基础配置文件
	 * 
	 * @param base 基础配置
	 */
	public static void saveSettingConfig(BaseConfigBean base) {

		long start = System.currentTimeMillis();
		logger.info("保存基础用户配置");

		File dir = getConfigDir();
		// 判断文件夹是否存在
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			saveSettingConfig(new File(dir, sysConfig.config_file_base), base);
			logger.info("保存基础用户配置 -完毕- " + (System.currentTimeMillis() - start) + "ms");
		} catch (IOException e) {
			logger.error("保存基础配置文件失败", e);
		}
	}

	/**
	 * 保存基础配置
	 * 
	 * @param file 文件路径
	 * @param base 基础配置
	 * @throws IOException IO异常
	 */
	public static void saveSettingConfig(File file, BaseConfigBean base) throws IOException {

		save(file, base);
	}

	/**
	 * 保存收藏夹
	 * 
	 * @param file 文件路径
	 * @param favorites 收藏夹
	 * @throws IOException IO异常
	 */
	public static void saveFavoritesConfig(File file, List<FavoritesTreeBean> favorites) throws IOException {

		save(file, favorites);
	}

	/**
	 * 保存历史记录
	 * 
	 * @param file 文件路径
	 * @param history 历史记录
	 * @throws IOException IO异常
	 */
	public static void saveHistoryConfig(File file, List<HistoryBean> history) throws IOException {

		save(file, history);
	}

	/**
	 * 保存配置文件
	 * 
	 * @param file 文件
	 * @param bean 保存对象
	 * @throws IOException IO异常
	 */
	private static void save(File file, Object bean) throws IOException {

		Compressor.zip(file, file.getName(), gson.toJson(bean).getBytes(sysConfig.charset), Compressor.C_MAX);
	}

	/**
	 * 读取配置信息
	 * 
	 * @return 配置对象
	 */
	public static ConfigBean readConfig() {

		long start = System.currentTimeMillis();
		logger.info("读取用户配置");

		ConfigBean config = new ConfigBean();
		File dir = getConfigDir();
		if (!dir.exists()) {
			// 文件不存在
			return config;
		}
		try {
			config.setLf((String) read(new File(dir, sysConfig.config_file_style), (Type) String.class));
			config.setBase(readSettingConfig(new File(dir, sysConfig.config_file_base)));
			config.setFavorites(readFavoritesConfig(new File(dir, sysConfig.config_file_favorites)));
			config.setHistory(readHistoryConfig(new File(dir, sysConfig.config_file_history)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		logger.info("读取用户配置 -完毕- " + (System.currentTimeMillis() - start) + "ms");
		return config;
	}

	/**
	 * 读取基础配置
	 * 
	 * @param file 文件路径
	 * @return 基础配置
	 * @throws IOException IO异常
	 */
	public static BaseConfigBean readSettingConfig(File file) throws IOException {

		return (BaseConfigBean) read(file, (Type) BaseConfigBean.class);
	}

	/**
	 * 读取收藏夹
	 * 
	 * @param file 文件路径
	 * @return 收藏夹
	 * @throws IOException IO异常
	 */
	@SuppressWarnings("unchecked")
	public static List<FavoritesTreeBean> readFavoritesConfig(File file) throws IOException {

		return (List<FavoritesTreeBean>) read(file, new TypeToken<List<FavoritesTreeBean>>() {
		}.getType());
	}

	/**
	 * 读取历史记录
	 * 
	 * @param file 文件路径
	 * @return 历史记录
	 * @throws IOException IO异常
	 */
	@SuppressWarnings("unchecked")
	public static List<HistoryBean> readHistoryConfig(File file) throws IOException {

		return (List<HistoryBean>) read(file, new TypeToken<List<HistoryBean>>() {
		}.getType());
	}

	/**
	 * 读取配置文件
	 * 
	 * @param file 文件
	 * @param type 类型
	 * @return 对象
	 * @throws IOException IO异常
	 */
	private static Object read(File file, Type type) throws IOException {

		if (!file.exists()) {
			return null;
		}
		byte[] bytes = Compressor.unzip(file, file.getName());
		return gson.fromJson(new String(bytes, sysConfig.charset), type);
	}

	/**
	 * 封装收藏夹树对象
	 * 
	 * @param root 树根节点
	 * @param beans 树对象
	 */
	public static void convert(DefaultMutableTreeNode root, List<FavoritesTreeBean> beans) {

		Enumeration<?> e = root.children();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) e.nextElement();
			FavoritesTreeBean bean = build(cNode);
			beans.add(bean);
		}
	}

	/**
	 * @param node 树节点
	 * @return 树对象
	 */
	private static FavoritesTreeBean build(DefaultMutableTreeNode node) {

		Enumeration<?> e = node.children();
		List<FavoritesTreeBean> children = new LinkedList<FavoritesTreeBean>();
		// 遍历子节点
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) e.nextElement();
			// 构建子节点
			FavoritesTreeBean bean = build(cNode);
			children.add(bean);
		}
		// 节点数据
		FavoritesBean item = (FavoritesBean) node.getUserObject();
		// 封装树对象
		if (children.isEmpty()) {
			return new FavoritesTreeBean(item);
		} else {
			return new FavoritesTreeBean(item, children);
		}
	}

	/**
	 * 封装收藏夹树节点
	 * 
	 * @param beans 树对象
	 * @param root 树根节点
	 */
	public static void convert(List<FavoritesTreeBean> beans, DefaultMutableTreeNode root) {

		if (beans == null) {
			return;
		}
		for (FavoritesTreeBean bean : beans) {

			DefaultMutableTreeNode node = build(bean);
			root.add(node);
		}
	}

	/**
	 * @param bean 树对象
	 * @return 树节点
	 */
	private static DefaultMutableTreeNode build(FavoritesTreeBean bean) {

		DefaultMutableTreeNode node = new DefaultMutableTreeNode(bean.getItem());
		if (bean.getChildren() != null) {
			for (FavoritesTreeBean cBean : bean.getChildren()) {
				node.add(build(cBean));
			}
		}
		return node;
	}

	/**
	 * 配置信息
	 *
	 * @author ASLai
	 */
	public static class ConfigBean {

		private String lf;
		private BaseConfigBean base;
		private List<HistoryBean> history;
		private List<FavoritesTreeBean> favorites;

		/**
		 * @return 主题样式
		 */
		public String getLf() {

			return lf;
		}

		/**
		 * @return 基础配置
		 */
		public BaseConfigBean getBase() {

			return base;
		}

		/**
		 * @return 收藏夹
		 */
		public List<FavoritesTreeBean> getFavorites() {

			return favorites;
		}

		/**
		 * @return 历史
		 */
		public List<HistoryBean> getHistory() {

			return history;
		}

		/**
		 * @param lf 主题样式
		 */
		public void setLf(String lf) {

			this.lf = lf;
		}

		/**
		 * @param base 基础信息
		 */
		public void setBase(BaseConfigBean base) {

			this.base = base;
		}

		/**
		 * @param history 历史记录
		 */
		public void setHistory(List<HistoryBean> history) {

			this.history = history;
		}

		/**
		 * @param favorites 收藏夹
		 */
		public void setFavorites(List<FavoritesTreeBean> favorites) {

			this.favorites = favorites;
		}
	}

	/**
	 * 基础配置信息
	 *
	 * @author ASLai
	 */
	public static class BaseConfigBean {

		private Boolean singleton;
		private Integer max_history;
		private Boolean save_history;
		private Integer time_out;

		/**
		 * @param config 配置信息
		 */
		public BaseConfigBean(Config config) {

			this.singleton = config.singleton;
			this.max_history = config.max_history;
			this.save_history = config.save_history;
			this.time_out = config.time_out;
		}

		/**
		 * @return 单例
		 */
		public Boolean getSingleton() {

			return singleton;
		}

		/**
		 * @return 最大历史记录条数
		 */
		public Integer getMax_history() {

			return max_history;
		}

		/**
		 * @return 是否保存历史记录
		 */
		public Boolean getSave_history() {

			return save_history;
		}

		/**
		 * @return 访问超时时间
		 */
		public Integer getTime_out() {

			return time_out;
		}
	}

	/**
	 * 收藏夹树
	 *
	 * @author ASLai
	 */
	public static class FavoritesTreeBean {

		private FavoritesBean item;
		private List<FavoritesTreeBean> children;

		/**
		 * 构造方法
		 * 
		 * @param item 数据
		 */
		public FavoritesTreeBean(FavoritesBean item) {

			this.item = item;
		}

		/**
		 * 构造方法
		 * 
		 * @param item 数据
		 * @param children 子节点
		 */
		public FavoritesTreeBean(FavoritesBean item, List<FavoritesTreeBean> children) {

			this.item = item;
			this.children = children;
		}

		/**
		 * @return 数据
		 */
		public FavoritesBean getItem() {

			return item;
		}

		/**
		 * @return 子节点
		 */
		public List<FavoritesTreeBean> getChildren() {

			return children;
		}
	}
}
