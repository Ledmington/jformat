package com.ledmington.javaparser.parser;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class JavaClassDeclaration implements JavaNode {

	private final PackageDeclarationNode classPackage;
	private final List<ImportDeclarationNode> imports;
	private final List<AccessModifier> modifiers;
	private final String name;
	private final String superClass;
	private final List<String> interfaces;
	private final List<FieldDeclarationNode> fields;
	private final List<MethodDeclarationNode> methods;

	public JavaClassDeclaration(
			final PackageDeclarationNode classPackage,
			final List<ImportDeclarationNode> imports,
			final List<AccessModifier> modifiers,
			final String name,
			final String superClass,
			final List<String> interfaces,
			final List<FieldDeclarationNode> fields,
			final List<MethodDeclarationNode> methods) {
		this.classPackage = Objects.requireNonNull(classPackage);
		this.imports = Objects.requireNonNull(imports);
		for (final ImportDeclarationNode idn : imports) {
			Objects.requireNonNull(idn);
		}
		this.modifiers = Objects.requireNonNull(modifiers);
		for (final AccessModifier am : modifiers) {
			// TODO: check if the access modifiers are valid for a class declaration
			Objects.requireNonNull(am);
		}
		this.name = Objects.requireNonNull(name);
		if (name.isBlank()) {
			throw new IllegalArgumentException(String.format("Invalid class name '%s'", name));
		}
		if (superClass != null && superClass.isBlank()) {
			throw new IllegalArgumentException(String.format("Invalid super class name '%s'", superClass));
		}
		this.superClass = superClass;
		this.interfaces = Objects.requireNonNull(interfaces);
		for (final String s : interfaces) {
			Objects.requireNonNull(s);
			if (s.isBlank()) {
				throw new IllegalArgumentException(String.format("Invalid interface name '%s'", s));
			}
		}
		this.fields = Objects.requireNonNull(fields);
		for (final FieldDeclarationNode fdn : fields) {
			Objects.requireNonNull(fdn);
		}
		this.methods = Objects.requireNonNull(methods);
		for (final MethodDeclarationNode mdn : methods) {
			Objects.requireNonNull(mdn);
		}
	}

	@Override
	public String toJava() {
		return classPackage.toJava() + "\n"
				+ imports.stream().map(JavaNode::toJava).collect(Collectors.joining("\n"))
				+ "\n" + modifiers.stream().map(JavaNode::toJava).collect(Collectors.joining(" ")) + " class " + name
				+ (superClass == null ? "" : " extends " + superClass)
				+ (interfaces.isEmpty() ? "" : " implements " + String.join(",", interfaces))
				+ "{" + fields.stream().map(JavaNode::toJava).collect(Collectors.joining("\n")) + "\n"
				+ methods.stream().map(JavaNode::toJava).collect(Collectors.joining("\n")) + "}";
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(200);
		sb.append("JavaClassDeclaration(package=").append(classPackage.toString());
		if (!imports.isEmpty()) {
			sb.append(";imports=").append(imports.getFirst().toString());
			for (int i = 1; i < imports.size(); i++) {
				sb.append(',').append(imports.get(i).toString());
			}
		}
		if (!modifiers.isEmpty()) {
			sb.append(";modifiers=").append(modifiers.getFirst().toString());
			for (int i = 1; i < modifiers.size(); i++) {
				sb.append(',').append(modifiers.get(i).toString());
			}
		}
		sb.append(";name=").append(name);
		if (superClass != null) {
			sb.append(";superClass=").append(superClass);
		}
		if (!interfaces.isEmpty()) {
			sb.append(";interfaces=").append(interfaces.getFirst());
			for (int i = 1; i < interfaces.size(); i++) {
				sb.append(',').append(interfaces.get(i));
			}
		}
		if (!fields.isEmpty()) {
			sb.append(";fields=").append(fields.getFirst().toString());
			for (int i = 1; i < fields.size(); i++) {
				sb.append(',').append(fields.get(i).toString());
			}
		}
		if (!methods.isEmpty()) {
			sb.append(";methods=").append(methods.getFirst().toString());
			for (int i = 1; i < methods.size(); i++) {
				sb.append(',').append(methods.get(i).toString());
			}
		}
		sb.append(')');
		return sb.toString();
	}

	@Override
	public int hashCode() {
		int h = 17;
		h = 31 * h + classPackage.hashCode();
		h = 31 * h + imports.hashCode();
		h = 31 * h + name.hashCode();
		h = 31 * h + (superClass == null ? 0 : superClass.hashCode());
		h = 31 * h + interfaces.hashCode();
		h = 31 * h + fields.hashCode();
		h = 31 * h + methods.hashCode();
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
		final JavaClassDeclaration jcd = (JavaClassDeclaration) other;
		return this.classPackage.equals(jcd.classPackage)
				&& this.imports.equals(jcd.imports)
				&& this.name.equals(jcd.name)
				&& Objects.equals(this.superClass, jcd.superClass)
				&& this.interfaces.equals(jcd.interfaces)
				&& this.fields.equals(jcd.fields)
				&& this.methods.equals(jcd.methods);
	}
}
