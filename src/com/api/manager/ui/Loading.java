package com.api.manager.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.plaf.basic.BasicProgressBarUI;

import com.api.manager.ui.util.CompLogger;

/**
 * 软件加载页面
 */
public class Loading extends JWindow {

	private static final long serialVersionUID = -7256687493842580993L;

	private static JProgressBar progress;

	/**
	 * 构造方法
	 */
	public Loading() {

		CompLogger.create(this.getClass(), 0);
		initStyle();
		initComp();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化样式
	 */
	private void initStyle() {

		// 设置大小
		this.setSize(300, 30);
		// 设置窗口居中
		// 获取屏幕的尺寸
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = screen.width / 2 - this.getWidth() / 2;
		int y = screen.height / 2 - this.getHeight() / 2;
		// 设置显示位置
		this.setLocation(x, y);
		// 设置鼠标样式
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		progress = new JProgressBar();
		progress.setMinimum(0);
		progress.setMaximum(100);
		progress.setStringPainted(true);
		// 字体设置
		progress.setFont(new Font("微软雅黑", Font.PLAIN, 11));
		// 背景设置
		progress.setBackground(Color.WHITE);
		progress.setForeground(new Color(196, 196, 196));
		// 边框设置
		progress.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(170, 170, 170)));
		// 字体颜色设置
		progress.setUI(new BasicProgressBarUI() {

			protected Color getSelectionBackground() {

				return Color.BLACK;
			}

			protected Color getSelectionForeground() {

				return Color.BLACK;
			}
		});
		this.add(progress);
	}

	/**
	 * 设置进度信息
	 * 
	 * @param value 进度值
	 * @param info 信息
	 */
	public void setProgress(int value, String info) {

		progress.setValue(value);
		progress.setString(info);
	}
}
