package com.yhx.manage.common.exception;

public class DuomanRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String code;
	private Object messages = "SUCCESS";
	private String typeName;

	public DuomanRuntimeException() {
		super();
	}

	public DuomanRuntimeException(String code, Object message) {
		super();
		this.code = code;
		this.messages = message;
	}
	
	public DuomanRuntimeException(String typeName,String code, Object message) {
		super();
		this.code = code;
		this.messages = message;
		this.typeName = typeName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getMessages() {
		return messages;
	}

	public void setMessages(Object message) {
		this.messages = message;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}



}
