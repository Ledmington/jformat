package com.ledmington.javaparser.parser;

import java.util.Objects;

public enum PrimitiveTypes implements TypeNode {
	VOID("void"),
	BYTE("byte"),
	CHAR("char"),
	SHORT("short"),
	INT("int"),
	FLOAT("float"),
	LONG("long"),
	DOUBLE("double");

	private final String repr;

	PrimitiveTypes(final String repr) {
		this.repr = Objects.requireNonNull(repr);
	}

	@Override
	public String toJava() {
		return repr;
	}
}
