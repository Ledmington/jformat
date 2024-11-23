package com.ledmington.javaparser.parser;

import java.util.Objects;

import com.ledmington.javaparser.parser.expr.ExpressionNode;

public final class FieldAccessExpression implements ExpressionNode {

	private final ExpressionNode expr;
	private final String id;

	public FieldAccessExpression(final ExpressionNode expr, final String id) {
		this.expr = Objects.requireNonNull(expr);
		this.id = Objects.requireNonNull(id);
		if (id.isBlank()) {
			throw new IllegalArgumentException(String.format("Invalid field name '%s'", id));
		}
	}

	@Override
	public String toJava() {
		return expr.toJava() + "." + id;
	}
}
