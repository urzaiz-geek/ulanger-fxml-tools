package com.urzaizcoding.ulangerproxy.log;

public enum NotificationType {
	INFO("[INFO]","Log that relay program evolution"),
	WARNING("[WARNING]","A minor error occured or may occur due to something"),
	DEBUG("[DEBUG]","Used for debugging"),
	ERROR("[ERROR]","An error occured but the programm can anyway continue"),
	FATAL("[FATAL]","The error occured is fatal an the program will cease to fonction");
	NotificationType(String name,String description) {
		m_description = description;
		m_name = name;
	}
	private String m_description;
	private String m_name;
	public String getDescription() {
		return m_description;
	}
	public String toString() {
		return m_name;
	}
}
