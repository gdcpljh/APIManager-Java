package com.api.manager.worker.tool.format;

/**
 * 格式化异常
 *
 * @author ASLai
 */
public class DataFormatException extends RuntimeException {

	private static final long serialVersionUID = 6967611822035866643L;

	/**
	 * @param message 信息
	 */
	public DataFormatException(String message) {

		super(message);
	}

	/**
	 * @param message 信息
	 * @param cause 错误
	 */
	public DataFormatException(String message, Throwable cause) {

		super(message, cause);
	}

	/**
	 * @param cause 错误
	 */
	public DataFormatException(Throwable cause) {

		super(cause);
	}
}
