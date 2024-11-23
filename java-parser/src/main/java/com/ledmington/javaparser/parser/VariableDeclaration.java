package com.ledmington.javaparser.parser;

import java.util.Objects;

import com.ledmington.javaparser.parser.expr.ExpressionNode;

public final class VariableDeclaration implements StatementNode {

	private final boolean isFinal;
	private final TypeNode type;
	private final String name;
	private final ExpressionNode expr;

	public VariableDeclaration(
			final boolean isFinal, final TypeNode type, final String name, final ExpressionNode expr) {
		this.isFinal = isFinal;
		this.type = Objects.requireNonNull(type);
		if (name.isBlank()) {
			throw new IllegalArgumentException(String.format("Invalid variable name '%s'", name));
		}
		this.name = Objects.requireNonNull(name);
		this.expr = Objects.requireNonNull(expr);
	}

	@Override
	public String toJava() {
		return (isFinal ? "final " : "") + type + toJava() + " " + name + "=" + expr.toJava() + ";";
	}
}
