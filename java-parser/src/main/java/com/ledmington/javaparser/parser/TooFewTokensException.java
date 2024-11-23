package com.ledmington.javaparser.parser;

import java.io.Serial;
import java.util.List;

import com.ledmington.javaparser.lexer.JavaToken;

public final class TooFewTokensException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -7535379936846688209L;

	public TooFewTokensException(final List<JavaToken> lastTokens) {
		super(String.format(
				"Stream of tokens finished too early. Last %,d tokens were %s", lastTokens.size(), lastTokens));
	}
}
