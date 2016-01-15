package com.api.manager.worker.tool.reader;

/**
 * 读取器异常
 *
 * @author ASLai
 */
public class ReaderException extends RuntimeException {

	private static final long serialVersionUID = 8375410418502734002L;

	/**
	 * @param message 信息
	 */
	public ReaderException(String message) {

		super(message);
	}

	/**
	 * @param message 信息
	 * @param cause 错误
	 */
	public ReaderException(String message, Throwable cause) {

		super(message, cause);
	}

	/**
	 * @param cause 错误
	 */
	public ReaderException(Throwable cause) {

		super(cause);
	}

}
