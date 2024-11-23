package com.ledmington.javaparser.lexer;

import java.io.Serial;

public final class UnknownTokenException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 2103339894077764034L;

	public UnknownTokenException(final char token) {
		super(String.format("Unknown token '%c'", token));
	}
}
