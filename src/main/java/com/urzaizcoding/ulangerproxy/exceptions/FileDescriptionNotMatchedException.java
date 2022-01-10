package com.urzaizcoding.ulangerproxy.exceptions;

public class FileDescriptionNotMatchedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2893310794632457795L;
	private final String targetAppName;
	private final String targetAppVersion;

	public FileDescriptionNotMatchedException(String targetAppName, String targetAppVersion) {
		super();
		this.targetAppName = targetAppName;
		this.targetAppVersion = targetAppVersion;
	}

	@Override
	public String getMessage() {
		return "The file description doesn't match the current application description. "
					+"The file must have the following description :\nTarget application name : "
				+ targetAppName + "\nTarget application version : " + targetAppVersion;
	}
	
	

}
