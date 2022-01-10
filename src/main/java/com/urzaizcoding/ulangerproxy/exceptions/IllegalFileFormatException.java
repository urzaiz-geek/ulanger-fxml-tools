package com.urzaizcoding.ulangerproxy.exceptions;

public class IllegalFileFormatException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5057460593817437542L;
	private final Integer line;
	private final String sequence;
	private final String fileName;

	public IllegalFileFormatException(String fileN, Integer line, String sequence) {
		super();
		this.line = line;
		this.sequence = sequence;
		this.fileName = fileN;
	}

	@Override
	public String getMessage() {
		return "Unexpected syntax used in file " + getFileName() + " at line " + getLine() + " in the sequence : \""
				+ getSequence() + "\"";
	}

	public Integer getLine() {
		return line;
	}

	public String getSequence() {
		return sequence;
	}

	public String getFileName() {
		return fileName;
	}
	
	
	

}
