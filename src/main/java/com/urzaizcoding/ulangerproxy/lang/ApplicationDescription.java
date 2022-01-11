package com.urzaizcoding.ulangerproxy.lang;

public class ApplicationDescription {
	private final String applicationName;
	private final String version;
	
	
	public ApplicationDescription(String applicationName, String version) {
		this.applicationName = applicationName;
		this.version = version;
	}


	public String getApplicationName() {
		return applicationName;
	}


	public String getVersion() {
		return version;
	}

	

}
