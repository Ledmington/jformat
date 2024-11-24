package com.ledmington.javaparser.lexer;

import java.io.Serial;

public final class InvalidLiteralException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 4007465801267470648L;

	public InvalidLiteralException(final String invalidLiteral) {
		super(String.format("'%s' is an invalid literal.",invalidLiteral));
	}
}
