package com.urzaizcoding.ulangerproxy.exceptions;

@SuppressWarnings("serial")
public class IncompletePathException extends Exception {

	private final String causeMessage;

	public IncompletePathException(String m) {
		super(m);
		this.causeMessage = null;
	}

	public IncompletePathException(Throwable cause) {
		super(cause);
		this.causeMessage = null;
	}

	public IncompletePathException(Throwable cause, String message) {
		super(cause);
		this.causeMessage = message;
	}

	public String getCauseMessage() {
		return causeMessage;
	}
}