package com.ledmington.javaparser.lexer;

import java.util.Objects;

public record StringLiteral(String content) implements JavaToken{
	public StringLiteral {
		Objects.requireNonNull(content);
	}
}
