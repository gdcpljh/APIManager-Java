package com.api.manager.ui.bean;

import com.api.manager.worker.http.bean.RequestBean;
import com.api.manager.worker.http.bean.ResponseBean;

/**
 * 请求响应对象
 *
 * @author ASLai
 */
public class ReBean {

	private RequestBean req;
	private ResponseBean res;

	/**
	 * @return 请求对象
	 */
	public RequestBean getReq() {

		return req;
	}

	/**
	 * @param req 请求对象
	 */
	public void setReq(RequestBean req) {

		this.req = req;
	}

	/**
	 * @return 响应对象
	 */
	public ResponseBean getRes() {

		return res;
	}

	/**
	 * @param res 响应对象
	 */
	public void setRes(ResponseBean res) {

		this.res = res;
	}
}
