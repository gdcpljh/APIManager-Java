package com.api.manager.ui.mbar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.api.manager.ui.mbar.item.LAboutItem;
import com.api.manager.ui.mbar.item.LExitItem;
import com.api.manager.ui.mbar.item.LExportItem;
import com.api.manager.ui.mbar.item.LImportItem;
import com.api.manager.ui.mbar.item.LSettingItem;
import com.api.manager.ui.mbar.item.LThemeItem;
import com.api.manager.ui.mbar.item.LVersionItem;
import com.api.manager.ui.support.UIEvent;
import com.api.manager.ui.util.CompLogger;

/**
 * 菜单
 *
 * @author ASLai
 */
public class LMemuBar extends JMenuBar {

	private static final long serialVersionUID = -6826759950657417803L;

	/**
	 * 构造方法
	 */
	public LMemuBar() {

		CompLogger.create(this.getClass(), 1);
		initComp();
		CompLogger.finish(this.getClass());
	}

	/**
	 * 初始化组件
	 */
	private void initComp() {

		JMenu in = new JMenu("读取");
		in.add(new LImportItem(UIEvent.CONFIG_TYPE_BASE));
		in.add(new LImportItem(UIEvent.CONFIG_TYPE_FAVORITES));
		in.add(new LImportItem(UIEvent.CONFIG_TYPE_HISTORY));

		JMenu out = new JMenu("另存");
		out.add(new LExportItem(UIEvent.CONFIG_TYPE_BASE));
		out.add(new LExportItem(UIEvent.CONFIG_TYPE_FAVORITES));
		out.add(new LExportItem(UIEvent.CONFIG_TYPE_HISTORY));

		JMenu file = new JMenu("文件(F)");
		file.setMnemonic('f');
		file.add(in);
		file.add(out);
		file.addSeparator();
		file.add(new LExitItem());

		JMenu set = new JMenu("设置(S)");
		set.setMnemonic('s');
		set.add(new LThemeItem());
		set.add(new LSettingItem());

		JMenu help = new JMenu("帮助(H)");
		help.setMnemonic('h');
		help.add(new LVersionItem());
		help.add(new LAboutItem());

		this.add(file);
		this.add(set);
		this.add(help);
	}
}
