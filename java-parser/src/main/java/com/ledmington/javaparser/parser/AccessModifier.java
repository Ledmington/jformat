package com.ledmington.javaparser.parser;

import java.util.Objects;

public enum AccessModifier implements JavaNode {
	PUBLIC("public"),
	PRIVATE("private"),
	PROTECTED("protected"),
	FINAL("final"),
	ABSTRACT("abstract"),
	VOLATILE("volatile"),
	SYNCHRONIZED("synchronized"),
	STRICTFP("strictfp"),
	TRANSIENT("transient"),
	STATIC("static"),
	NATIVE("native");

	private final String repr;

	AccessModifier(final String repr) {
		this.repr = Objects.requireNonNull(repr);
	}

	@Override
	public String toJava() {
		return repr;
	}
}
