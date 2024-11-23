package com.ledmington.javaparser.parser;

import java.util.Objects;

public class ReferenceTypeNode implements TypeNode {

	protected final String className;

	public ReferenceTypeNode(final String className) {
		this.className = Objects.requireNonNull(className);
		if (className.isBlank()) {
			throw new IllegalArgumentException(String.format("Invalid type name '%s'", className));
		}
	}

	@Override
	public String toJava() {
		return className;
	}
}
