package com.ledmington.javaparser.parser;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ledmington.javaparser.parser.expr.ExpressionNode;

public final class MethodInvocationExpression implements ExpressionNode {

	private final ExpressionNode expr;
	private final String id;
	private final List<ExpressionNode> callParameters;

	public MethodInvocationExpression(
			final ExpressionNode expr, final String id, final List<ExpressionNode> callParameters) {
		this.expr = Objects.requireNonNull(expr);
		this.id = Objects.requireNonNull(id);
		if (id.isBlank()) {
			throw new IllegalArgumentException(String.format("Invalid method name %s", id));
		}
		this.callParameters = Objects.requireNonNull(callParameters);
		for (final ExpressionNode en : callParameters) {
			Objects.requireNonNull(en);
		}
	}

	@Override
	public String toJava() {
		return expr.toJava() + "." + id + "("
				+ callParameters.stream().map(JavaNode::toJava).collect(Collectors.joining(",")) + ")";
	}
}
