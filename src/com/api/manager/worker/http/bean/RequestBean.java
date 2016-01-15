package com.api.manager.worker.http.bean;

import java.util.Map;

import com.api.manager.worker.http.HttpSender.Method;

/**
 * 请求参数对象
 *
 * @author ASLai
 */
public class RequestBean {

	private String url;
	private Method method;
	private String body;
	private Map<String, String> params;
	private Map<String, String> headers;
	private Map<String, String> cookies;

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
	 * @return 请求类型
	 */
	public Method getMethod() {

		return method;
	}

	/**
	 * @param method 请求类型
	 */
	public void setMethod(Method method) {

		this.method = method;
	}

	/**
	 * @return 请求体
	 */
	public String getBody() {

		return body;
	}

	/**
	 * @param body 请求体
	 */
	public void setBody(String body) {

		this.body = body;
	}

	/**
	 * @return 请求参数
	 */
	public Map<String, String> getParams() {

		return params;
	}

	/**
	 * @param params 请求参数
	 */
	public void setParams(Map<String, String> params) {

		this.params = params;
	}

	/**
	 * @return 请求头
	 */
	public Map<String, String> getHeaders() {

		return headers;
	}

	/**
	 * @param headers 请求头
	 */
	public void setHeaders(Map<String, String> headers) {

		this.headers = headers;
	}

	/**
	 * @return cookie
	 */
	public Map<String, String> getCookies() {

		return cookies;
	}

	/**
	 * @param cookies cookie
	 */
	public void setCookies(Map<String, String> cookies) {

		this.cookies = cookies;
	}
}
