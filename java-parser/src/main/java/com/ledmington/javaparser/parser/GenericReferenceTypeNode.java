package com.ledmington.javaparser.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class GenericReferenceTypeNode extends ReferenceTypeNode {

	private final List<ReferenceTypeNode> innerTypes;

	public GenericReferenceTypeNode(final String className, final ReferenceTypeNode... innerTypes) {
		super(className);
		this.innerTypes = Arrays.stream(Objects.requireNonNull(innerTypes)).toList();
		for (final ReferenceTypeNode rtn : innerTypes) {
			Objects.requireNonNull(rtn);
		}
	}

	public GenericReferenceTypeNode(final String className, final List<ReferenceTypeNode> innerTypes) {
		super(className);
		this.innerTypes = Objects.requireNonNull(innerTypes);
		for (final ReferenceTypeNode rtn : innerTypes) {
			Objects.requireNonNull(rtn);
		}
	}

	@Override
	public String toJava() {
		return this.className + "<" + innerTypes.stream().map(JavaNode::toJava).collect(Collectors.joining(",")) + ">";
	}
}
