package com.api.manager.ui.support;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.api.manager.ui.bean.HistoryBean;
import com.api.manager.ui.bean.ReBean;
import com.api.manager.ui.support.UIConfig.BaseConfigBean;
import com.api.manager.ui.support.UIConfig.ConfigBean;
import com.api.manager.ui.support.UIConfig.FavoritesTreeBean;
import com.api.manager.worker.http.HttpSender;
import com.api.manager.worker.http.HttpSender.Method;
import com.api.manager.worker.http.bean.RequestBean;
import com.api.manager.worker.http.bean.ResponseBean;
import com.api.manager.worker.tool.format.DataFormatException;
import com.api.manager.worker.tool.format.DataFormater;

/**
 * UI事件
 *
 * @author ASLai
 */
public class UIEvent {

	private static Logger logger = Logger.getLogger(UIEvent.class);

	/** 格式化类型-json */
	public static final short FORMAT_JSON = 0;
	/** 格式化类型-html */
	public static final short FORMAT_HTML = 1;
	/** 格式化类型-xml */
	public static final short FORMAT_XML = 2;

	/** 基础配置 */
	public static final short CONFIG_TYPE_BASE = 0;
	/** 收藏夹 */
	public static final short CONFIG_TYPE_FAVORITES = 1;
	/** 历史记录 */
	public static final short CONFIG_TYPE_HISTORY = 2;

	/** 当前响应信息 */
	private static ResponseBean currentRes;
	/** 当前主题 */
	public static String current_lf;

	/** 访问线程池 */
	private static ExecutorService httpPool = Executors.newSingleThreadExecutor();
	/** 配置文件线程池 */
	private static ExecutorService configPool = Executors.newSingleThreadExecutor();

	private static final String HIS_TIME_FORMAT = "hh:mm:ss";
	private static SimpleDateFormat df = new SimpleDateFormat(HIS_TIME_FORMAT);

	/** 文件选择器 */
	private static JFileChooser chooser;

	/** 配置保存现在是否在启动 */
	private static Boolean configThreadRunning = false;

	/** 强制退出时间 */
	private static long forceTime = 60000;

	/**
	 * 弹出异常提示框
	 * 
	 * @param e 异常
	 * @param title 标题
	 */
	public static void alert(Exception e, String title) {

		logger.error(title);
		logger.error(e.getMessage(), e);
		JOptionPane.showMessageDialog(UIComps.ui, e.getMessage(), title, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * 退出系统
	 */
	public static void exit() {

		int result = JOptionPane.showConfirmDialog(UIComps.ui, "是否退出系统？", "确认", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			logger.info("--退出系统--");
			UIComps.ui.dispose();
			// 保存配置信息
			saveConfig();
			// 关闭连接池
			configPool.shutdown();
			// 判断配置信息是否保存完毕
			long start = System.currentTimeMillis();
			while (!configPool.isTerminated()) {
				// 判断是否过长时间没有保存完毕
				if (System.currentTimeMillis() - start > forceTime) {
					break;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
			}
			System.exit(0);
		}
	}

	/**
	 * 启动配置文件保存线程
	 */
	public static void startConfigSaveThread() {

		if (!UIConfig.sysConfig.config_auto) {
			return;
		}
		synchronized (configThreadRunning) {
			// 判断是否已经运行
			if (configThreadRunning) {
				return;
			}
			configThreadRunning = true;
		}
		// 启动配置文件保存
		new Thread(new Runnable() {

			@Override
			public void run() {

				while (UIConfig.sysConfig.config_auto) {
					try {
						Thread.sleep(UIConfig.sysConfig.config_auto_time);
					} catch (InterruptedException e) {
						break;
					}
					saveConfig();
				}
				configThreadRunning = false;
			}
		}).start();
	}

	/**
	 * 保存基础全局配置
	 */
	public static void saveBaseConfig() {

		configPool.execute(new Runnable() {

			@Override
			public void run() {

				// 保存基础配置
				UIConfig.saveSettingConfig(new BaseConfigBean(UIConfig.sysConfig));
			}
		});
	}

	/**
	 * 保存全局配置
	 */
	public static void saveConfig() {

		configPool.execute(new Runnable() {

			@Override
			public void run() {

				// 保存配置信息
				ConfigBean config = new ConfigBean();
				// 主题样式
				config.setLf(current_lf);
				// 基础配置信息
				config.setBase(new BaseConfigBean(UIConfig.sysConfig));
				// 封装收藏夹
				config.setFavorites(new LinkedList<FavoritesTreeBean>());
				UIConfig.convert(UIComps.favoritesNode, config.getFavorites());
				// 保存配置
				if (UIConfig.sysConfig.save_history) {
					// 历史信息
					config.setHistory(Collections.list(UIComps.history.elements()));
				}
				UIConfig.saveConfig(config);
			}
		});
	}

	/**
	 * 保存配置
	 * 
	 * @param configType 配置文件类型
	 */
	public static void saveAsConfig(short configType) {

		// 校验一下
		if (configType == UIEvent.CONFIG_TYPE_FAVORITES) {
			if (UIComps.favoritesNode.getChildCount() == 0) {
				JOptionPane.showMessageDialog(UIComps.ui, "收藏夹为空", "提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		} else if (configType == CONFIG_TYPE_HISTORY) {
			if (UIComps.history.isEmpty()) {
				JOptionPane.showMessageDialog(UIComps.ui, "历史记录为空", "提示", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
		if (chooser == null) {
			buildFileChooser();
		}

		// 更新避免主题修改
		chooser.updateUI();
		// 设置文件名
		chooser.setSelectedFile(new File(""));
		// 显示文件保存框
		int on = chooser.showSaveDialog(UIComps.ui);
		if (on == JFileChooser.APPROVE_OPTION) {
			// 获取选择文件
			File file = chooser.getSelectedFile();
			if (file != null) {
				String name = file.getName();
				if (name.lastIndexOf(".") == -1) {
					// 添加默认后缀名
					file = new File(file.getParentFile(), name + UIConfig.sysConfig.config_suffix);
				}
				// 保存收藏夹配置
				try {
					if (configType == UIEvent.CONFIG_TYPE_FAVORITES) {
						saveAsFavoritesConfig(file);
					} else if (configType == CONFIG_TYPE_HISTORY) {
						saveAsHistoryConfig(file);
					} else {
						saveAsBaseConfig(file);
					}
					JOptionPane.showMessageDialog(UIComps.ui, "保存成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(UIComps.ui, "配置保存失败！", "提示", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * 保存基础配置
	 * 
	 * @param file 路径
	 * @throws IOException IO异常
	 */
	public static void saveAsBaseConfig(File file) throws IOException {

		// 保存基础配置
		UIConfig.saveSettingConfig(file, new BaseConfigBean(UIConfig.sysConfig));
	}

	/**
	 * 保存收藏夹配置
	 * 
	 * @param file 路径
	 * @throws IOException IO异常
	 */
	public static void saveAsFavoritesConfig(File file) throws IOException {

		// 封装收藏夹
		List<FavoritesTreeBean> favorites = new LinkedList<FavoritesTreeBean>();
		UIConfig.convert(UIComps.favoritesNode, favorites);
		// 保存收藏夹
		UIConfig.saveFavoritesConfig(file, favorites);
	}

	/**
	 * 保存历史记录配置
	 * 
	 * @param file 路径
	 * @throws IOException IO异常
	 */
	public static void saveAsHistoryConfig(File file) throws IOException {

		// 封装历史记录
		List<HistoryBean> history = Collections.list(UIComps.history.elements());
		// 保存历史记录
		UIConfig.saveHistoryConfig(file, history);
	}

	/**
	 * 读取配置
	 * 
	 * @param configType 配置文件类型
	 */
	public static void readAsConfig(short configType) {

		if (chooser == null) {
			buildFileChooser();
		}
		// 更新避免主题修改
		chooser.updateUI();
		// 设置文件名
		chooser.setSelectedFile(new File(""));
		// 显示文件保存框
		int on = chooser.showOpenDialog(UIComps.ui);
		if (on == JFileChooser.APPROVE_OPTION) {
			// 获取选择文件
			File file = chooser.getSelectedFile();
			if (file != null) {
				try {
					if (configType == UIEvent.CONFIG_TYPE_FAVORITES) {
						readAsFavoritesConfig(file);
					} else if (configType == CONFIG_TYPE_HISTORY) {
						readAsHistoryConfig(file);
					} else {
						readAsBaseConfig(file);
						JOptionPane.showMessageDialog(UIComps.ui, "读取成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(UIComps.ui, "文件不存在或被锁定！", "提示", JOptionPane.ERROR_MESSAGE);
				} catch (RuntimeException e) {
					JOptionPane.showMessageDialog(UIComps.ui, "配置文件读取失败！", "提示", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	/**
	 * 读取基础配置
	 * 
	 * @param file 路径
	 * @throws IOException IO异常
	 */
	public static void readAsBaseConfig(File file) throws IOException {

		BaseConfigBean baseConfig = UIConfig.readSettingConfig(file);
		setBaseConfig(baseConfig);
	}

	/**
	 * 读取收藏夹配置
	 * 
	 * @param file 路径
	 * @throws IOException IO异常
	 */
	public static void readAsFavoritesConfig(File file) throws IOException {

		List<FavoritesTreeBean> favorites = UIConfig.readFavoritesConfig(file);
		if (!favorites.isEmpty() && favorites.get(0).getItem().getName() == null) {
			throw new RuntimeException("配置文件错误");
		}
		// 判断是否需要清空
		if (UIComps.favoritesNode.getChildCount() != 0) {
			// 弹出提示
			int result = JOptionPane.showConfirmDialog(UIComps.ui, "是否清空当前收藏夹？", "确认", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (result == JOptionPane.CLOSED_OPTION) {
				return;
			} else if (result == JOptionPane.YES_OPTION) {
				clearFavorites();
			}
		}
		// 加载收藏夹
		setFavorites(favorites);
	}

	/**
	 * 读取历史记录配置
	 * 
	 * @param file 路径
	 * @throws IOException IO异常
	 */
	public static void readAsHistoryConfig(File file) throws IOException {

		List<HistoryBean> history = UIConfig.readHistoryConfig(file);
		if (!history.isEmpty() && history.get(0).getReq() == null) {
			throw new RuntimeException("配置文件错误");
		}
		// 判断是否需要清空
		if (!UIComps.history.isEmpty()) {
			// 弹出提示
			int result = JOptionPane.showConfirmDialog(UIComps.ui, "是否清空当前历史记录？", "确认", JOptionPane.YES_NO_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (result == JOptionPane.CLOSED_OPTION) {
				return;
			} else if (result == JOptionPane.YES_OPTION) {
				clearHistory();
			}
		}
		setHistory(history);
	}

	/**
	 * 切换主题
	 * 
	 * @param lf
	 */
	public static void switchTheme(String lf) {

		Theme.lookAndFeel(lf);
		SwingUtilities.updateComponentTreeUI(UIComps.ui);
		current_lf = lf;
	}

	/**
	 * 初始化
	 * 
	 * @param config 配置
	 */
	public static void loadUserUI(ConfigBean config) {

		if (config == null) {
			return;
		}

		long start = System.currentTimeMillis();
		logger.info("初始化用户窗口配置");

		setFavorites(config.getFavorites());
		setHistory(config.getHistory());

		logger.info("初始化用户窗口配置 -完毕- " + (System.currentTimeMillis() - start) + "ms");
	}

	/**
	 * 初始化基础配置
	 * 
	 * @param baseConfig 基础配置
	 */
	public static void setBaseConfig(BaseConfigBean baseConfig) {

		if (baseConfig != null) {
			if (baseConfig.getMax_history() != null) {
				UIConfig.sysConfig.max_history = baseConfig.getMax_history();
			}
			if (baseConfig.getSave_history() != null) {
				UIConfig.sysConfig.save_history = baseConfig.getSave_history();
			}
			if (baseConfig.getTime_out() != null) {
				UIConfig.sysConfig.time_out = baseConfig.getTime_out();
			}
			if (baseConfig.getSingleton() != null) {
				UIConfig.sysConfig.singleton = baseConfig.getSingleton();
			}
		}
	}

	/**
	 * 初始化收藏夹
	 * 
	 * @param favorites 历史
	 */
	public static void setFavorites(List<FavoritesTreeBean> favorites) {

		if (favorites != null) {
			UIConfig.convert(favorites, UIComps.favoritesNode);
			UIComps.favoritesModel.reload();
		}
	}

	/**
	 * 清空收藏夹
	 */
	public static void clearFavorites() {

		UIComps.favoritesNode.removeAllChildren();
		UIComps.favoritesModel.reload();
	}

	/**
	 * 初始化历史记录
	 * 
	 * @param history 历史
	 */
	public static void setHistory(List<HistoryBean> history) {

		if (history != null) {
			for (HistoryBean his : history) {
				UIComps.history.addElement(his);
			}
		}
	}

	/**
	 * 清空历史记录
	 */
	public static void clearHistory() {

		UIComps.history.clear();
	}

	/**
	 * 加入历史记录
	 * 
	 * @param req 请求
	 * @param res 响应
	 */
	private static void addHistory(RequestBean req, ResponseBean res) {

		int size = UIComps.history.getSize();
		if (size >= UIConfig.sysConfig.max_history) {
			UIComps.history.remove(size - 1);
		}
		UIComps.history.add(0, new HistoryBean(df.format(new Date()), req, res));
	}

	/**
	 * 发送请求
	 */
	public static void send() {

		UIComps.submit.setEnabled(false);

		// 加入到线程池
		httpPool.execute(new Runnable() {

			/** 是否正在运行 */
			private boolean running = false;

			@Override
			public void run() {

				running = true;
				UIComps.progress.setValue(0);

				// 清空响应面板
				clearResPanel();
				// 封装请求对象
				final RequestBean request = buildRequest();

				UIComps.info.setText("访问：" + request.getUrl());
				UIComps.progress.setValue(20);

				new Thread(new Runnable() {

					// 进度条
					@Override
					public void run() {

						while (running) {
							// 判断进度条进度
							int value = UIComps.progress.getValue();
							if (value >= 20 && value < 50) {
								// 增长进度
								UIComps.progress.setValue(value += 5);
							} else if (value >= 50 && value < 80) {
								// 增长进度
								UIComps.progress.setValue(++value);
							} else {
								break;
							}
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								logger.debug(e.getMessage());
							}
						}
					}
				}).start();

				// 发送请求
				sendRequest(request);
				running = false;
			}
		});
	}

	/**
	 * 构建请求对象
	 * 
	 * @return 请求对象
	 */
	private static RequestBean buildRequest() {

		// 封装请求参数
		RequestBean request = new RequestBean();
		request.setUrl(UIComps.url.getText());
		request.setMethod((Method) UIComps.method.getSelectedItem());
		request.setBody(UIComps.body.getText());
		request.setParams(getMap(UIComps.params));
		request.setHeaders(getMap(UIComps.headers));
		request.setCookies(getMap(UIComps.cookies));
		return request;
	}

	/**
	 * 发送请求
	 * 
	 * @param request 请求对象
	 */
	private static void sendRequest(RequestBean request) {

		ResponseBean response;
		try {
			// 发送请求
			response = HttpSender.send(request, UIConfig.sysConfig.time_out);
		} catch (IOException e) {
			response = new ResponseBean();
			response.setUrl(request.getUrl());
			response.setStatus("连接失败:" + e.getMessage());
		}
		// 返回处理
		afterSend(response);
		addHistory(request, response);
	}

	/**
	 * 响应后处理
	 * 
	 * @param response 响应对象
	 */
	private static void afterSend(ResponseBean response) {

		// 设置响应面板
		setResPanel(response);
		// 进度条
		UIComps.progress.setValue(100);
		// 访问按钮
		UIComps.submit.setEnabled(true);
		// 切换标签页
		UIComps.optTab.setSelectedIndex(1);
	}

	/**
	 * 加载访问信息
	 * 
	 * @param reBean 访问信息
	 */
	public static void setRePanel(ReBean reBean) {

		if (!UIComps.submit.isEnabled()) {
			JOptionPane.showMessageDialog(UIComps.ui, "访问尚未完毕，请稍候进行读取！", "提示", JOptionPane.WARNING_MESSAGE);
			return;
		}
		// 清空面板
		clearReqPanel();
		clearResPanel();
		// 设置面板
		setReqPanel(reBean.getReq());
		setResPanel(reBean.getRes());
		// 设置当前响应
		currentRes = reBean.getRes();
		UIComps.optTab.setSelectedIndex(1);
	}

	/**
	 * 设置请求面板
	 * 
	 * @param request 请求对象
	 */
	public static void setReqPanel(RequestBean request) {

		// url
		UIComps.url.setText(request.getUrl());
		// 访问方式
		UIComps.method.setSelectedItem(request.getMethod());
		// 体
		UIComps.body.setText(request.getBody());
		// 设置访问参数
		for (Entry<String, String> param : request.getParams().entrySet()) {
			UIComps.params.addRow(new String[] { param.getKey(), param.getValue() });
		}
		// 设置头
		for (Entry<String, String> header : request.getHeaders().entrySet()) {
			UIComps.headers.addRow(new String[] { header.getKey(), header.getValue() });
		}
		// 设置Cookie
		for (Entry<String, String> cookie : request.getCookies().entrySet()) {
			UIComps.cookies.addRow(new String[] { cookie.getKey(), cookie.getValue() });
		}
	}

	/**
	 * 设置响应报告
	 * 
	 * @param response 响应对象
	 */
	public static void setResPanel(ResponseBean response) {

		// 信息
		UIComps.info.setText(response.getStatus());
		// url
		UIComps.res_url.setText(response.getUrl());
		// 体
		UIComps.res_body.setText(response.getBody());
		// 状态
		UIComps.res_status.setText(response.getStatus());
		// 调用时间
		UIComps.res_date.setText(response.getDate());
		// 耗时
		if (response.getTimes() != null) {
			UIComps.res_times.setText(response.getTimes() + " ms");
		}
		// 响应类型
		UIComps.res_content_type.setText(response.getContentType());
		// 设置头
		for (Entry<String, String> header : response.getHeaders().entrySet()) {
			UIComps.res_headers.addRow(new String[] { header.getKey(), header.getValue() });
		}
		// 设置Cookie
		for (Entry<String, String> cookie : response.getCookies().entrySet()) {
			UIComps.res_cookies.addRow(new String[] { cookie.getKey(), cookie.getValue() });
		}
		currentRes = response;
	}

	/**
	 * 清空请求面板
	 */
	public static void clearReqPanel() {

		UIComps.url.setText(null);
		UIComps.method.setSelectedItem(UIConfig.sysConfig.default_method);
		UIComps.body.setText(null);
		UIComps.params.getDataVector().clear();
		UIComps.params.fireTableDataChanged();
		UIComps.headers.getDataVector().clear();
		UIComps.headers.fireTableDataChanged();
		UIComps.cookies.getDataVector().clear();
		UIComps.cookies.fireTableDataChanged();
	}

	/**
	 * 清空响应报告
	 */
	public static void clearResPanel() {

		currentRes = null;
		UIComps.res_url.setText(null);
		UIComps.res_body.setText(null);
		UIComps.res_status.setText(null);
		UIComps.res_date.setText(null);
		UIComps.res_times.setText(null);
		UIComps.res_content_type.setText(null);
		UIComps.res_headers.getDataVector().clear();
		UIComps.res_headers.fireTableDataChanged();
		UIComps.res_cookies.getDataVector().clear();
		UIComps.res_cookies.fireTableDataChanged();
	}

	/**
	 * 还原响应体
	 */
	public static void restoreBody() {

		if (currentRes != null) {
			UIComps.res_body.setText(currentRes.getBody());
		}
	}

	/**
	 * 格式化响应体
	 * 
	 * @param type 格式化类型
	 */
	public static void formatBody(short type) {

		if (currentRes == null) {
			return;
		}
		String data = currentRes.getBody();
		if (data == null || data.trim().isEmpty()) {
			return;
		}
		String format = null;
		try {
			switch (type) {
			case FORMAT_HTML:
				format = DataFormater.html(data);
				break;
			case FORMAT_JSON:
				format = DataFormater.json(data);
				break;
			case FORMAT_XML:
				format = DataFormater.xml(data);
				break;
			}
		} catch (DataFormatException e) {
			logger.debug(e.getMessage());
			JOptionPane.showMessageDialog(UIComps.ui, "失败：" + e.getMessage(), "失败", JOptionPane.ERROR_MESSAGE);
		}
		if (format != null) {
			UIComps.res_body.setText(format);
		}
	}

	/**
	 * 构建文件选择器
	 */
	private static void buildFileChooser() {

		// 获取系统路径工具
		FileSystemView fileSys = FileSystemView.getFileSystemView();
		// 创建选择器
		JFileChooser fileChooser = new JFileChooser();
		// 默认路径桌面
		fileChooser.setCurrentDirectory(fileSys.getHomeDirectory());
		// 处理文件过滤
		fileChooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File file) {

				if (file.isDirectory()) {
					return true;
				}
				return file.getName().endsWith(UIConfig.sysConfig.config_suffix);
			}

			@Override
			public String getDescription() {

				return UIConfig.sysConfig.config_suffix;
			}
		});
		chooser = fileChooser;
	}

	/**
	 * 转换表单为map集合
	 * 
	 * @param model 模型
	 * @return map集合
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, String> getMap(DefaultTableModel model) {

		Map<String, String> map = new LinkedHashMap<String, String>();
		for (Object p : model.getDataVector()) {
			Vector<String> v = (Vector<String>) p;
			map.put(v.get(0), v.get(1));
		}
		return map;
	}
}
