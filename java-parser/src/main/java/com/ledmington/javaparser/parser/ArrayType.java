package com.ledmington.javaparser.parser;

import java.util.Objects;

public final class ArrayType implements TypeNode {

	private final TypeNode baseType;

	public ArrayType(final TypeNode baseType) {
		this.baseType = Objects.requireNonNull(baseType);
	}

	@Override
	public String toJava() {
		return baseType.toJava() + "[]";
	}

	@Override
	public String toString() {
		return "ArrayType(" + baseType + ")";
	}

	@Override
	public int hashCode() {
		int h = 17;
		h = 31 * h + baseType.hashCode();
		return h;
	}

	@Override
	public boolean equals(final Object other) {
		if (other == null) {
			return false;
		}
		if (this == other) {
			return true;
		}
		if (!this.getClass().equals(other.getClass())) {
			return false;
		}
		return this.baseType.equals(((ArrayType) other).baseType);
	}
}
