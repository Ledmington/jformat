package com.ledmington.javaparser.parser;

import java.io.Serial;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ledmington.javaparser.lexer.JavaToken;

public final class TooFewTokensException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -7535379936846688209L;

	public TooFewTokensException(final JavaToken expectedToken, final JavaToken... others) {
		super(String.format(
				"Expected %s %s but stream of tokens finished too early.",
				others.length == 0 ? "token" : "any of",
				Stream.concat(Stream.of(expectedToken), Arrays.stream(others)).map(s -> '\'' + s.toString() + '\'')
						.collect(Collectors.joining(", "))));
	}
}
