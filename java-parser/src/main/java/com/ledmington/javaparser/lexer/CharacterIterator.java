package com.ledmington.javaparser.lexer;

import java.util.Objects;

public final class CharacterIterator {

	private final String str;
	private int i = 0;

	public CharacterIterator(final String str) {
		this.str = Objects.requireNonNull(str);
	}

	public char current(final int step) {
		return str.charAt(i + step);
	}

	public char current() {
		return current(0);
	}

	public char next() {
		return current(1);
	}

	public boolean hasNext(final int step) {
		if(step < 1) {
			throw new AssertionError();
		}
		return i < (str.length() - (step - 1));
	}

	public boolean hasNext() {
		return hasNext(1);
	}

	public void move() {
		i++;
	}
}
