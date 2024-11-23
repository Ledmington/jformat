package com.ledmington.javaparser.parser.expr;

import java.util.Objects;

public final class DivideNode implements ExpressionNode {

	private final ExpressionNode lhs;
	private final ExpressionNode rhs;

	public DivideNode(final ExpressionNode lhs, final ExpressionNode rhs) {
		this.lhs = Objects.requireNonNull(lhs);
		this.rhs = Objects.requireNonNull(rhs);
	}

	@Override
	public String toJava() {
		return lhs.toJava() + "/" + rhs.toJava();
	}

	@Override
	public String toString() {
		return "DivideNode(" + lhs.toJava() + "," + rhs.toJava() + ")";
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
		final DivideNode dn = (DivideNode) other;
		return this.lhs.equals(dn.lhs) && this.rhs.equals(dn.rhs);
	}
}
