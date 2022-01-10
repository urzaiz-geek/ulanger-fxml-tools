package com.urzaizcoding.ulangerproxy.exceptions;

public class IdNotFoundInClassException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8480554516099014175L;
	private final String erronedIdName;
	private final String className;
	

	public IdNotFoundInClassException(String erronedIdName, String className) {
		super();
		this.erronedIdName = erronedIdName;
		this.className = className;
	}

	public String getErronedIdName() {
		return erronedIdName;
	}

	@Override
	public String getMessage() {
		return "The requiered id" + erronedIdName+ " is not found in the class "+className;
	}
	
	
}
