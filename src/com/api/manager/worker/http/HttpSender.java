package com.api.manager.worker.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.api.manager.worker.http.bean.RequestBean;
import com.api.manager.worker.http.bean.ResponseBean;

/**
 * HTTP访问器
 *
 * @author ASLai
 */
public class HttpSender {

	/**
	 * 访问方式
	 */
	public enum Method {
		/** get方式 */
		GET,
		/** post方式 */
		POST;
	}

	/**
	 * 协议类型
	 *
	 * @author ASLai
	 */
	public enum Protocol {
		/** http协议 */
		HTTP("http://"),
		/** https协议 */
		HTTPS("https://"),
		/** ftp协议 */
		FTP("ftp://");

		private String value;

		/**
		 * @param value 值
		 */
		private Protocol(String value) {

			this.value = value;
		}

		/**
		 * @return 获取协议值
		 */
		public String getValue() {

			return value;
		}
	}

	/** 默认协议 */
	public static final Protocol DEFAULT_PRO = Protocol.HTTP;

	/** 请求cookie参数名 */
	private static final String HEAD_REQ_COOKIE_NAME = "Cookie";
	/** 响应cookie参数名 */
	private static final String HEAD_RES_COOKIE_NAME = "Set-Cookie";
	/** 响应类型参数名 */
	private static final String HEAD_RES_CONTTYPE_NAME = "Content-Type";
	/** 时间参数名 */
	private static final String HEAD_RES_DATE_NAME = "Date";
	/** Cookie参数分隔符 */
	private static final String SPLIT_COOKIE_VALUE = ";";
	/** 键值分隔符 */
	private static final String SPLIT_KEY_VALUE = "=";
	/** 参数分隔符 */
	private static final String SPLIT_PARAM = "&";
	/** url参数分隔符 */
	private static final String SPLIT_URL_PARAMS = "?";

	/** HTTP访问构建器 */
	private static HttpClientBuilder httpBuilder;

	/**
	 * 发送请求
	 * 
	 * @param request 请求参数对象
	 * @param timeOut
	 * @return 响应参数对象
	 * @throws IOException io异常
	 */
	public static ResponseBean send(RequestBean request, int timeOut) throws IOException {

		boolean isPost = request.getMethod() == Method.POST;
		HttpRequestBase http;
		try {
			http = isPost ? buildPostHttp(request) : buildGetHttp(request);
		} catch (RuntimeException e) {
			throw new IOException(e);
		}
		if (timeOut != -1) {
			// 配置
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeOut).setSocketTimeout(timeOut)
					.build();
			// 设置超时
			http.setConfig(requestConfig);
		}

		// 添加头部信息
		setHeader(http, request);
		// 添加Session
		setCookie(http, request);

		if (isPost && http instanceof HttpPost) {
			// 添加Body
			setBody((HttpPost) http, request);
		}

		if (httpBuilder == null) {
			httpBuilder = HttpClients.custom();
		}
		CloseableHttpClient httpClient = httpBuilder.build();
		CloseableHttpResponse response = null;
		try {
			// 调用
			long start = System.currentTimeMillis();
			response = httpClient.execute(http);
			long times = System.currentTimeMillis() - start;

			// 构建响应对象
			ResponseBean res = buildResponse(response);
			// 添加耗时
			res.setTimes(times);
			res.setUrl(request.getUrl());
			return res;

		} catch (IOException e) {
			throw e;
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
				}
			}
			httpClient.close();
		}
	}

	/**
	 * 构建响应对象
	 * 
	 * @param response 响应
	 * @return 响应对象
	 * @throws IOException io异常
	 */
	private static ResponseBean buildResponse(CloseableHttpResponse response) throws IOException {

		ResponseBean bean = new ResponseBean();

		// 响应状态
		bean.setStatus(response.getStatusLine().toString());
		// 头
		Header[] headers = response.getAllHeaders();
		for (Header header : headers) {

			String key = header.getName();
			if (key.equals(HEAD_RES_COOKIE_NAME)) {
				// Cookie处理
				String[] ns = header.getValue().split(SPLIT_COOKIE_VALUE)[0].split(SPLIT_KEY_VALUE);
				bean.getCookies().put(ns[0], ns[1]);
			} else if (key.equals(HEAD_RES_CONTTYPE_NAME)) {
				// 响应类型
				bean.setContentType(header.getValue());
			} else if (key.equals(HEAD_RES_DATE_NAME)) {
				// 响应类型
				bean.setDate(header.getValue());
			} else {
				// Header处理
				bean.getHeaders().put(key, header.getValue());
			}
		}
		// 响应体
		HttpEntity entity = response.getEntity();
		bean.setBody(EntityUtils.toString(entity));
		// 关闭体
		EntityUtils.consume(entity);
		return bean;
	}

	/**
	 * 构建POST请求
	 * 
	 * @param request 请求参数对象
	 * @return POST请求
	 */
	private static HttpPost buildPostHttp(RequestBean request) {

		String url = request.getUrl();
		// 查看是否已包含协议
		for (Protocol p : Protocol.values()) {
			if (url.startsWith(p.value)) {
				return new HttpPost(url);
			}
		}
		// 不包含则使用默认协议
		url = DEFAULT_PRO.value + url;
		request.setUrl(url);
		return new HttpPost(url);
	}

	/**
	 * 构建GET请求
	 * 
	 * @param request 请求参数对象
	 * @return GET请求
	 */
	private static HttpGet buildGetHttp(RequestBean request) {

		String u = request.getUrl();
		// 查看是否已包含协议
		boolean exists = false;
		for (Protocol p : Protocol.values()) {
			if (u.startsWith(p.value)) {
				exists = true;
				break;
			}
		}
		// 拼装基础url
		StringBuilder url = new StringBuilder();
		if (exists) {
			url.append(u);
		} else {
			url.append(DEFAULT_PRO.value).append(u);
			request.setUrl(url.toString());
		}
		// 检查否包含参数
		if (u.indexOf(SPLIT_URL_PARAMS) == -1) {
			url.append(SPLIT_URL_PARAMS);
		} else {
			url.append(SPLIT_PARAM);
		}
		boolean first = true;
		for (Entry<String, String> param : request.getParams().entrySet()) {
			// 不是第一个参数添加分隔符
			if (first) {
				first = false;
			} else {
				url.append(SPLIT_PARAM);
			}
			// 添加参数
			url.append(param.getKey()).append(SPLIT_KEY_VALUE).append(param.getValue());
		}
		return new HttpGet(url.toString());
	}

	/**
	 * 添加头部信息
	 * 
	 * @param http 请求
	 * @param request 请求参数对象
	 */
	private static void setHeader(HttpRequestBase http, RequestBean request) {

		// 循环添加头信息
		for (Entry<String, String> header : request.getHeaders().entrySet()) {
			http.addHeader(header.getKey(), header.getValue());
		}
	}

	/**
	 * 添加Cookie信息
	 * 
	 * @param http 请求
	 * @param request 请求参数对象
	 */
	private static void setCookie(HttpRequestBase http, RequestBean request) {

		// 循环添加Cookie
		for (Entry<String, String> cookie : request.getCookies().entrySet()) {
			StringBuilder value = new StringBuilder().append(cookie.getKey()).append(SPLIT_KEY_VALUE)
					.append(cookie.getValue());
			http.addHeader(HEAD_REQ_COOKIE_NAME, value.toString());
		}
	}

	/**
	 * 添加Body信息
	 * 
	 * @param http 请求
	 * @param request 请求参数对象
	 * @throws UnsupportedEncodingException
	 */
	private static void setBody(HttpPost http, RequestBean request) throws UnsupportedEncodingException {

		String body = request.getBody();
		Map<String, String> params = request.getParams();

		if (body != null && !body.trim().isEmpty()) {

			// 优先处理body
			http.setEntity(new StringEntity(body));

		} else if (params != null && !params.isEmpty()) {

			// 处理参数列表
			List<NameValuePair> ps = new ArrayList<NameValuePair>();
			for (Entry<String, String> param : params.entrySet()) {
				ps.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			}
			http.setEntity(new UrlEncodedFormEntity(ps));
		}
	}
}
