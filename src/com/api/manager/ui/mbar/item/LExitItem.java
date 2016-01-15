package com.api.manager.ui.mbar.item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import com.api.manager.ui.support.UIEvent;
import com.api.manager.ui.util.CompLogger;

/**
 * 退出菜单项
 *
 * @author ASLai
 */
public class LExitItem extends JMenuItem implements ActionListener {

	private static final long serialVersionUID = -818907924367656975L;

	/**
	 * 构造方法
	 */
	public LExitItem() {

		super("退出(X)");
		CompLogger.create(this.getClass(), 2);
		initEvent();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {

		// 设置快捷键
		this.setMnemonic('x');
		// 添加点击事件
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		UIEvent.exit();
	}
}
