package com.ledmington.parser;

import java.util.Objects;
import java.util.function.Predicate;

public final class Parser {

	public static ParserBuilder builder() {
		return new ParserBuilder();
	}

	private final ParserStep step;
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
}
