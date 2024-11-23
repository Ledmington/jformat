package com.ledmington.javaparser.parser;

import java.util.Objects;

import com.ledmington.javaparser.parser.expr.ExpressionNode;

public final class ReturnStatement implements StatementNode {

	private final ExpressionNode expr;

	public ReturnStatement(final ExpressionNode expr) {
		this.expr = Objects.requireNonNull(expr);
	}

	@Override
	public String toJava() {
		return "return " + expr.toJava() + ";";
	}
}
