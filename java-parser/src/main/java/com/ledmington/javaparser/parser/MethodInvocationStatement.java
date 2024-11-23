package com.ledmington.javaparser.parser;

import java.util.Objects;

public final class MethodInvocationStatement implements StatementNode {

	private final MethodInvocationExpression mie;

	public MethodInvocationStatement(final MethodInvocationExpression mie) {
		this.mie = Objects.requireNonNull(mie);
	}

	@Override
	public String toJava() {
		return mie.toJava() + ";";
	}
}
