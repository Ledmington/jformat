package com.ledmington.javaparser.parser.expr;

import java.math.BigInteger;
import java.util.Objects;

public final class ConstantNode implements ExpressionNode {

	private final BigInteger value;
	private final boolean declaredAsLong;

	public ConstantNode(final BigInteger value, final boolean declaredAsLong) {
		this.value = Objects.requireNonNull(value);
		this.declaredAsLong = declaredAsLong;
	}

	@Override
	public String toJava() {
		return value.toString(10) + (declaredAsLong ? "L" : "");
	}

	@Override
	public String toString() {
		return "ConstantNode(" + value + (declaredAsLong ? "L" : "") + ")";
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
		final ConstantNode cn = (ConstantNode) other;
		return this.value.equals(cn.value) && this.declaredAsLong == cn.declaredAsLong;
	}
}
