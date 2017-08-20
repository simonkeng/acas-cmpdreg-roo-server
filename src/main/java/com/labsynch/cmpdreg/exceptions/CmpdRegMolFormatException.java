package com.labsynch.cmpdreg.exceptions;

public class CmpdRegMolFormatException extends Exception {

	public CmpdRegMolFormatException(Exception e) {
		this.setStackTrace(e.getStackTrace());
	}

	public CmpdRegMolFormatException() {
		// TODO Auto-generated constructor stub
	}
	
	public CmpdRegMolFormatException(String message) {
		// TODO Auto-generated constructor stub
	}

}
