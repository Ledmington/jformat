package com.ledmington.javaparser.parser;

import java.util.Objects;

import com.ledmington.javaparser.parser.expr.ExpressionNode;

public final class FieldAssignment implements StatementNode {

	private final FieldAccessExpression fae;
	private final ExpressionNode value;

	public FieldAssignment(final FieldAccessExpression fae, final ExpressionNode value) {
		this.fae = Objects.requireNonNull(fae);
		this.value = Objects.requireNonNull(value);
	}

	@Override
	public String toJava() {
		return fae.toJava() + "=" + value.toJava();
	}
}
