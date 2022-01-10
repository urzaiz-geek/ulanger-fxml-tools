package com.urzaizcoding.ulangerproxy.log;

public enum LogOutPut {
	STD_OUTPUT("Std output"),
	FILE_OUTPUT("File"),
	BOTH("File and Std output");
	private String m_name;
	private LogOutPut(String name) {
		m_name = name;
	}
	public String toString() {
		return m_name;
	}
}
