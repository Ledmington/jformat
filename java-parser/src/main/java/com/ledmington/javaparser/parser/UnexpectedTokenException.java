package com.ledmington.javaparser.parser;

import java.io.Serial;

import com.ledmington.javaparser.lexer.JavaToken;

public final class UnexpectedTokenException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 3505223255523865323L;

	public UnexpectedTokenException(final JavaToken t) {
		super(String.format("Unexpected token '%s'", t));
	}

	public UnexpectedTokenException(final JavaToken expected, final JavaToken actual) {
		super(String.format("Expected token '%s' but '%s' was found", expected, actual));
	}
}
