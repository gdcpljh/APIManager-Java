package com.api.manager.ui.bean;

import com.api.manager.worker.http.HttpSender;
import com.api.manager.worker.http.bean.RequestBean;
import com.api.manager.worker.http.bean.ResponseBean;

/**
 * 历史记录
 *
 * @author ASLai
 */
public class HistoryBean extends ReBean {

	private String time;

	/**
	 * 构造方法
	 * 
	 * @param time 调用时间
	 * @param req 请求
	 * @param res 响应
	 */
	public HistoryBean(String time, RequestBean req, ResponseBean res) {

		setReq(req);
		setRes(res);
		this.time = time;
	}

	@Override
	public String toString() {

		RequestBean req = getReq();
		if (req != null) {
			String url = req.getUrl();
			String def = HttpSender.DEFAULT_PRO.getValue();
			StringBuilder str = new StringBuilder();
			str.append(time).append(" - ").append(url.startsWith(def) ? url.substring(def.length()) : url);
			return str.toString();
		}
		return null;
	}
}
