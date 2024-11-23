package com.ledmington.javaparser.parser;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class MethodDeclarationNode implements JavaNode {

	private final List<AccessModifier> modifiers;
	private final TypeNode returnType;
	private final String name;
	private final List<MethodParameterDeclaration> parameters;
	private final List<StatementNode> body;

	public MethodDeclarationNode(
			final List<AccessModifier> modifiers,
			final TypeNode returnType,
			final String name,
			final List<MethodParameterDeclaration> parameters,
			final List<StatementNode> body) {
		this.modifiers = Objects.requireNonNull(modifiers);
		for (final AccessModifier am : modifiers) {
			// TODO: check if the access modifiers are valid for a field declaration
			Objects.requireNonNull(am);
		}
		this.returnType = Objects.requireNonNull(returnType);
		this.name = Objects.requireNonNull(name);
		if (name.isBlank()) {
			throw new IllegalArgumentException(String.format("Invalid method name '%s'", name));
		}
		this.parameters = Objects.requireNonNull(parameters);
		for (final MethodParameterDeclaration mpd : parameters) {
			Objects.requireNonNull(mpd);
		}
		this.body = body;
		if (body != null) {
			for (final StatementNode sn : body) {
				Objects.requireNonNull(sn);
			}
		}
	}

	@Override
	public String toJava() {
		return modifiers.stream().map(JavaNode::toJava).collect(Collectors.joining(" ")) + " " + returnType.toJava()
				+ " " + name + "(" + parameters.stream().map(JavaNode::toJava).collect(Collectors.joining(",")) + ")"
				+ (body == null
						? ";"
						: "{" + body.stream().map(JavaNode::toJava).collect(Collectors.joining("\n")) + "}");
	}
}
