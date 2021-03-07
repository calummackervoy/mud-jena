package com.mackervoy.calum.mud.exception;

public class MissingRequiredArgumentException extends NullPointerException {
	private static final long serialVersionUID = 5627096569547521249L;
	 
    public MissingRequiredArgumentException(String s) {
        super(s);
    }
}
