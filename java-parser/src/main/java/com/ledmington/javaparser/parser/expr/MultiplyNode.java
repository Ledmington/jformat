package com.ledmington.javaparser.parser.expr;

import java.util.Objects;

public final class MultiplyNode implements ExpressionNode {

	private final ExpressionNode lhs;
	private final ExpressionNode rhs;

	public MultiplyNode(final ExpressionNode lhs, final ExpressionNode rhs) {
		this.lhs = Objects.requireNonNull(lhs);
		this.rhs = Objects.requireNonNull(rhs);
	}

	@Override
	public String toJava() {
		return lhs.toJava() + "*" + rhs.toJava();
	}

	@Override
	public String toString() {
		return "MultiplyNode(" + lhs.toJava() + "," + rhs.toJava() + ")";
	}

	@Override
	public int hashCode() {
		int h = 17;
		h = 31 * h + lhs.hashCode();
		h = 31 * h + rhs.hashCode();
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
		final MultiplyNode mn = (MultiplyNode) other;
		return this.lhs.equals(mn.lhs) && this.rhs.equals(mn.rhs);
	}
}
