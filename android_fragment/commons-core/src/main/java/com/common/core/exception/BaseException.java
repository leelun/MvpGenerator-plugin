package com.common.core.exception;

public class BaseException extends RuntimeException{

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -456218279911987911L;

	public BaseException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BaseException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	public BaseException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public BaseException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

}
