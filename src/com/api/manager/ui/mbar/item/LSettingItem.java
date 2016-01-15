package com.api.manager.ui.mbar.item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import com.api.manager.ui.mbar.dialog.LSettingDialog;
import com.api.manager.ui.util.CompLogger;

/**
 * 设置项
 *
 * @author ASLai
 */
public class LSettingItem extends JMenuItem implements ActionListener {

	private static final long serialVersionUID = -2226581106596798995L;

	private static LSettingDialog dialog;

	/**
	 * 构造方法
	 */
	public LSettingItem() {

		super("系统设置");
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
			dialog = new LSettingDialog();
		}
		SwingUtilities.updateComponentTreeUI(dialog);
		dialog.initValue();
		dialog.setVisible(true);
	}
}
