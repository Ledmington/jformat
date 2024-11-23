package com.ledmington.javaparser.parser;

public record EmptyStatement() implements StatementNode {
	@Override
	public String toJava() {
		return ";";
	}
}
