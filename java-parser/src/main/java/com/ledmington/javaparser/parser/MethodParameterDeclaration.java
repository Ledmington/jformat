package com.ledmington.javaparser.parser;

import java.util.Objects;

public final class MethodParameterDeclaration implements JavaNode {

	private final boolean isFinal;
	private final TypeNode type;
	private final String name;

	public MethodParameterDeclaration(final boolean isFinal, final TypeNode type, final String name) {
		this.isFinal = isFinal;
		this.type = Objects.requireNonNull(type);
		this.name = Objects.requireNonNull(name);
		if (name.isBlank()) {
			throw new IllegalArgumentException(String.format("Invalid method parameter name '%s'", name));
		}
	}

	@Override
	public String toJava() {
		return (isFinal ? "final " : "") + type.toJava() + " " + name;
	}

	@Override
	public String toString() {
		return "MethodParameterDeclaration(isFinal=" + isFinal + ";type=" + type.toString() + ";name=" + name + ")";
	}

	@Override
	public int hashCode() {
		int h = 17;
		h = 31 * h + (isFinal ? 1 : 0);
		h = 31 * h + type.hashCode();
		h = 31 * h + name.hashCode();
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
		final MethodParameterDeclaration mpd = (MethodParameterDeclaration) other;
		return this.isFinal == mpd.isFinal && this.type.equals(mpd.type) && this.name.equals(mpd.name);
	}
}
