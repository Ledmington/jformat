package com.ledmington.javaparser.parser;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ledmington.javaparser.parser.expr.ExpressionNode;

public final class FieldDeclarationNode implements JavaNode {

	private final List<AccessModifier> modifiers;
	private final TypeNode type;
	private final String name;
	private final ExpressionNode initializer;

	public FieldDeclarationNode(
			final List<AccessModifier> modifiers,
			final TypeNode type,
			final String name,
			final ExpressionNode initializer) {
		this.modifiers = Objects.requireNonNull(modifiers);
		for (final AccessModifier am : modifiers) {
			// TODO: check if the access modifiers are valid for a field declaration
			Objects.requireNonNull(am);
		}
		this.type = Objects.requireNonNull(type);
		this.name = Objects.requireNonNull(name);
		if (name.isBlank()) {
			throw new IllegalArgumentException(String.format("Invalid field name '%s'", name));
		}
		this.initializer = initializer;
	}

	public FieldDeclarationNode(final List<AccessModifier> modifiers, final TypeNode type, final String name) {
		this(modifiers, type, name, null);
	}

	@Override
	public String toJava() {
		return modifiers.stream().map(JavaNode::toJava).collect(Collectors.joining(" ")) + " " + type.toJava() + " "
				+ name + (initializer == null ? "" : ("=" + initializer.toJava())) + ";";
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("FieldDeclarationNode(");
		if (!modifiers.isEmpty()) {
			sb.append("modifiers=").append(modifiers).append(';');
		}
		sb.append("type=").append(type).append(";name=").append(name);
		if (initializer != null) {
			sb.append(";initializer=").append(initializer);
		}
		sb.append(')');
		return sb.toString();
	}

	@Override
	public int hashCode() {
		int h = 17;
		h = 31 * h + modifiers.hashCode();
		h = 31 * h + type.hashCode();
		h = 31 * h + name.hashCode();
		h = 31 * h + (initializer == null ? 0 : initializer.hashCode());
		return h;
	}

	@Override
	public boolean equals(final Object other) {
		if (other == null) {
			return false;
		}
		if (this == other) {
			return true;
		}
		if (!this.getClass().equals(other.getClass())) {
			return false;
		}
		final FieldDeclarationNode fdn = (FieldDeclarationNode) other;
		return this.modifiers.equals(fdn.modifiers)
				&& this.type.equals(fdn.type)
				&& this.name.equals(fdn.name)
				&& Objects.equals(this.initializer, fdn.initializer);
	}
}
