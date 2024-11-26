package com.ledmington.javaparser.lexer;

import java.util.Objects;

public final class CharacterIterator {

	private final String str;
	private int index = 0;
	private int line = 1;
	private int column = 1;

	public CharacterIterator(final String str) {
		this.str = Objects.requireNonNull(str);
	}

	public char current(final int step) {
		return str.charAt(index + step);
	}

	public char current() {
		return current(0);
	}

	public char next() {
		return current(1);
	}

	public boolean hasNext(final int step) {
		if (step < 1) {
			throw new AssertionError();
		}
		return index < (str.length() - (step - 1));
	}

	public boolean hasNext() {
		return hasNext(1);
	}

	private void moveOne() {
		if (hasNext() && current() == '\n') {
			line++;
			column = 1;
		} else {
			column++;
		}
		index++;
	}

	public void move(final int step) {
		if (step < 1) {
			throw new AssertionError();
		}
		for (int i = 0; i < step; i++) {
			moveOne();
		}
	}

	public void move() {
		move(1);
	}

	public int getLine() {
		return line;
	}

	public int getColumn() {
		return column;
	}
}
