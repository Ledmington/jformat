package com.ledmington.javaparser.lexer;

import java.math.BigInteger;
import java.util.Objects;

public final class IntegerLiteral implements JavaToken {

	private final BigInteger value;
	private final boolean declaredAsLong;

	public IntegerLiteral(final BigInteger value, final boolean declaredAsLong) {
		this.value = Objects.requireNonNull(value);
		this.declaredAsLong = declaredAsLong;
	}

	public IntegerLiteral(final BigInteger value) {
		this(value, false);
	}

	public BigInteger value() {
		return value;
	}

	public boolean declaredAsLong() {
		return declaredAsLong;
	}

	@Override
	public String toString() {
		return "IntegerLiteral(" + value + (declaredAsLong ? "L" : "") + ")";
	}

	@Override
	public int hashCode() {
		int h = 17;
		h = 31 * h + value.hashCode();
		h = 31 * h + (declaredAsLong ? 1 : 0);
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
		final IntegerLiteral il = (IntegerLiteral) other;
		return this.value.equals(il.value) && this.declaredAsLong == il.declaredAsLong;
	}
}
