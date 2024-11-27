package com.ledmington.parser;

import java.util.Objects;
import java.util.function.Predicate;

public final class Parser {

	public static ParserBuilder builder() {
		return new ParserBuilder();
	}

	final ParserStep step;
	private final Predicate<String> p;

	Parser(final ParserStep step, final Predicate<String> p) {
		this.step = Objects.requireNonNull(step);
		this.p = Objects.requireNonNull(p);
	}

	public boolean matches(final String s) {
		return p.test(s);
	}

	@Override
	public String toString() {
		return step.toRegularExpression();
	}

	@Override
	public int hashCode() {
		int h = 17;
		h = 31 * h + step.hashCode();
		h = 31 * h + p.hashCode();
		return h;
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other) {
			return true;
		}
		if (other == null) {
			return false;
		}
		if (!(other instanceof Parser par)) {
			return false;
		}
		return this.step.equals(par.step) && this.p.equals(par.p);
	}
}
