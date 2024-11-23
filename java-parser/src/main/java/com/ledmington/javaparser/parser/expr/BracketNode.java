package com.ledmington.javaparser.parser.expr;

import java.util.Objects;

public final class BracketNode implements ExpressionNode {

	private final ExpressionNode expr;

	public BracketNode(final ExpressionNode expr) {
		this.expr = Objects.requireNonNull(expr);
	}

	@Override
	public String toJava() {
		return "(" + expr.toJava() + ")";
	}
}
