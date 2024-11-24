package com.ledmington.javaparser.lexer;

import java.util.Objects;

public record CharLiteral(String content)implements JavaToken {
	public CharLiteral {
		Objects.requireNonNull(content);
		if (content.isEmpty() || content.length() > 6) {
			throw new IllegalArgumentException(String.format("'%s' is not a char literal.", content));
		}
	}
}
