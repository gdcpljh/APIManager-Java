package com.api.manager.worker.http.bean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 响应参数对象
 *
 * @author ASLai
 */
public class ResponseBean {

	private String url;
	private String status;
	private String contentType;
	private String date;
	private String body;
	private Map<String, String> cookies;
	private Map<String, String> headers;
	private Long times;

	/**
	 * @return 访问路径
	 */
	public String getUrl() {

		return url;
	}

	/**
	 * @param url 访问路径
	 */
	public void setUrl(String url) {

		this.url = url;
	}

	/**
	 * @return 状态
	 */
	public String getStatus() {

		return status;
	}

	/**
	 * @param status 状态
	 */
	public void setStatus(String status) {

		this.status = status;
	}

	/**
	 * @return 内容类型
	 */
	public String getContentType() {

		return contentType;
	}

	/**
	 * @param contentType 内容类型
	 */
	public void setContentType(String contentType) {

		this.contentType = contentType;
	}

	/**
	 * @return 调用时间
	 */
	public String getDate() {

		return date;
	}

	/**
	 * @param date 调用时间
	 */
	public void setDate(String date) {

		this.date = date;
	}

	/**
	 * @return 响应体
	 */
	public String getBody() {

		return body;
	}

	/**
	 * @param body 响应体
	 */
	public void setBody(String body) {

		this.body = body;
	}

	/**
	 * @return Cookie
	 */
	public Map<String, String> getCookies() {

		if (cookies == null) {
			cookies = new LinkedHashMap<String, String>();
		}
		return cookies;
	}

	/**
	 * @param cookies Cookie
	 */
	public void setCookies(Map<String, String> cookies) {

		this.cookies = cookies;
	}

	/**
	 * @return 头
	 */
	public Map<String, String> getHeaders() {

		if (headers == null) {
			headers = new LinkedHashMap<String, String>();
		}
		return headers;
	}

	/**
	 * @param headers 头
	 */
	public void setHeaders(Map<String, String> headers) {

		this.headers = headers;
	}

	/**
	 * @return 耗时
	 */
	public Long getTimes() {

		return times;
	}

	/**
	 * @param times 耗时
	 */
	public void setTimes(Long times) {

		this.times = times;
	}
}
