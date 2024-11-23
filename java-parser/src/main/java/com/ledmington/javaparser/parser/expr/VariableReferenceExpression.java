package com.ledmington.javaparser.parser.expr;

import java.util.Objects;

public final class VariableReferenceExpression implements ExpressionNode {

	private final String variableName;

	public VariableReferenceExpression(final String variableName) {
		this.variableName = Objects.requireNonNull(variableName);
		if (variableName.isBlank() || variableName.isEmpty()) {
			throw new IllegalArgumentException(String.format("Invalid variable name '%s'", variableName));
		}
	}

	@Override
	public String toJava() {
		return variableName;
	}

	@Override
	public String toString() {
		return "VariableReference(" + variableName + ")";
	}
}
