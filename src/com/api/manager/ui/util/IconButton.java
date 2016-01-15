package com.api.manager.ui.util;

import java.awt.Cursor;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.JButton;

import com.api.manager.Config;

/**
 * 标题按钮
 *
 * @author ASLai
 */
public class IconButton extends JButton {

	private static final long serialVersionUID = 4770859219829249302L;

	/** 默认边距 */
	private static final Insets DEFAUTL_INS = new Insets(3, 3, 3, 3);

	/**
	 * 构造方法
	 * 
	 * @param icon 图标
	 */
	public IconButton(Config.Icon icon) {

		setIcon(Icons.getIcon(icon));
		init();
	}

	/**
	 * 构造方法
	 * 
	 * @param icon 图标
	 */
	public IconButton(Icon icon) {

		super(icon);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {

		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setFocusable(false);
		this.setCursor(new Cursor(Cursor.HAND_CURSOR));
		this.setMargin(DEFAUTL_INS);
	}
}
