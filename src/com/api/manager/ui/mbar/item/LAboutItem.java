package com.api.manager.ui.mbar.item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.support.UIConfig;
import com.api.manager.ui.util.CompLogger;

/**
 * 关于菜单项
 *
 * @author ASLai
 */
public class LAboutItem extends JMenuItem implements ActionListener {

	private static final long serialVersionUID = 6319281369019802547L;

	/**
	 * 构造方法
	 */
	public LAboutItem() {

		super("关于");
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

		StringBuilder sb = new StringBuilder();
		sb.append(UIConfig.sysConfig.name);
		sb.append("\r\n").append("\r\n");
		sb.append("\t").append("版本号：").append(UIConfig.sysConfig.version);
		sb.append("\r\n");
		sb.append("构建时间：").append(UIConfig.sysConfig.version_date);
		sb.append("\r\n").append("\r\n");
		sb.append("作者：ASLai");
		sb.append("\r\n");
//		sb.append("反馈：loongzcx@163.com");
		sb.append("注：本程序基于开源项目重新编译");
		sb.append("\r\n");
		JOptionPane.showMessageDialog(UIComps.ui, sb.toString(), "关于", JOptionPane.INFORMATION_MESSAGE);
	}
}
