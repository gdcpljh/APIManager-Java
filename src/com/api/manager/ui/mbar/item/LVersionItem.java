package com.api.manager.ui.mbar.item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import com.api.manager.ui.mbar.dialog.LVersionDialog;
import com.api.manager.ui.util.CompLogger;

/**
 * 版本信息项
 *
 * @author ASLai
 */
public class LVersionItem extends JMenuItem implements ActionListener {

	private static final long serialVersionUID = -5413955131221638742L;

	private static LVersionDialog dialog;

	/**
	 * 构造方法
	 */
	public LVersionItem() {

		super("版本信息");
		CompLogger.create(this.getClass(), 2);
		initEvent();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {

		// 添加点击事件
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (dialog == null) {
			dialog = new LVersionDialog();
		}
		SwingUtilities.updateComponentTreeUI(dialog);
		dialog.setVisible(true);
	}
}
