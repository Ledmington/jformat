package com.ledmington.javaparser.lexer;

import java.util.Objects;

public record JavaID(String id) implements JavaToken {

	public JavaID(final String id) {
		this.id = Objects.requireNonNull(id);
	}

	@Override
	public String toString() {
		return "ID(" + id + ")";
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
		return this.id.equals(((JavaID) other).id);
	}
}
