package com.ledmington.javaparser.parser;

import java.util.List;
import java.util.Objects;

public final class PackageDeclarationNode implements JavaNode {

	public static final PackageDeclarationNode DEFAULT_PACKAGE = new PackageDeclarationNode("<default_package>");

	private final String[] packages;

	public PackageDeclarationNode(final String basePackage, final String... subPackages) {
		this.packages = new String[Objects.requireNonNull(subPackages).length + 1];
		this.packages[0] = Objects.requireNonNull(basePackage);
		System.arraycopy(subPackages, 0, this.packages, 1, subPackages.length);
	}

	public PackageDeclarationNode(final List<String> packageNames) {
		Objects.requireNonNull(packageNames);
		if (packageNames.isEmpty()) {
			throw new IllegalArgumentException("Cannot have a package declaration with no package names");
		}
		this.packages = new String[packageNames.size()];
		for (int i = 0; i < packageNames.size(); i++) {
			this.packages[i] = Objects.requireNonNull(packageNames.get(i));
		}
	}

	@Override
	public String toJava() {
		return "package " + String.join(".", packages) + ";";
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("PackageDeclarationNode(").append(packages[0]);
		for (int i = 1; i < packages.length; i++) {
			sb.append('.').append(packages[i]);
		}
		sb.append(')');
		return sb.toString();
	}

	@Override
	public int hashCode() {
		int h = 17;
		for (final String p : packages) {
			h = 31 * h + p.hashCode();
		}
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
		final PackageDeclarationNode n = (PackageDeclarationNode) other;
		if (this.packages.length != n.packages.length) {
			return false;
		}
		for (int i = 0; i < this.packages.length; i++) {
			if (!this.packages[i].equals(n.packages[i])) {
				return false;
			}
		}
		return true;
	}
}
