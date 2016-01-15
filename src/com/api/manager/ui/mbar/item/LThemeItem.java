package com.api.manager.ui.mbar.item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.api.manager.ui.support.UIComps;
import com.api.manager.ui.support.UIConfig;
import com.api.manager.ui.support.UIEvent;
import com.api.manager.ui.util.CompLogger;

/**
 * 设置
 *
 * @author ASLai
 */
public class LThemeItem extends JMenu {

	private static final long serialVersionUID = 8698890748478310953L;

	/**
	 * 构造方法
	 */
	public LThemeItem() {

		super("主题");
		CompLogger.create(this.getClass(), 2);
		initComp();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		for (final Entry<String, String> lf : UIConfig.sysConfig.themes.entrySet()) {

			// 判断是否为默认主题
			boolean isDefault = lf.getKey().equals(UIConfig.sysConfig.default_themes);
			// 设置菜单项
			JMenuItem theme = new JMenuItem(isDefault ? lf.getKey() + "（默认）" : lf.getKey());
			theme.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					try {
						UIEvent.switchTheme(lf.getValue());
					} catch (RuntimeException ex) {
						JOptionPane.showMessageDialog(UIComps.ui, "切换主题失败：" + ex.getMessage(), "错误",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			this.add(theme);
		}
	}
}
