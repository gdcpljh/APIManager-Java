package com.api.manager.ui.bean;

/**
 * 收藏夹记录
 *
 * @author ASLai
 */
public class FavoritesBean extends ReBean {

	private String name;
	private boolean isLeaf;

	/**
	 * 目录构造方法
	 * 
	 * @param name 名称
	 */
	public FavoritesBean(String name) {

		this.name = name;
		this.isLeaf = false;
	}

	/**
	 * 访问构造方法
	 * 
	 * @param name 名称
	 * @param reBean 访问对象
	 */
	public FavoritesBean(String name, ReBean reBean) {

		setReq(reBean.getReq());
		setRes(reBean.getRes());
		this.name = name;
		this.isLeaf = true;
	}

	/**
	 * @return 名称
	 */
	public String getName() {

		return name;
	}

	/**
	 * @param name 名称
	 */
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * @return 是否为叶子节点
	 */
	public boolean isLeaf() {

		return isLeaf;
	}

	@Override
	public String toString() {

		return name;
	}
}
