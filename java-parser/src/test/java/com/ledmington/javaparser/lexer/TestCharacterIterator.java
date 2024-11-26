package com.ledmington.javaparser.lexer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class TestCharacterIterator {

	@ParameterizedTest
	@ValueSource(strings = { "", "abc", "a\nb\nc" })
	public void sampleString(final String s) {
		final CharacterIterator it = new CharacterIterator(s);
		int line = 1;
		int column = 1;
		for (int i = 0; i < s.length(); i++) {
			assertTrue(it.hasNext());
			for (int j = 1; j + i < s.length(); j++) {
				assertTrue(it.hasNext(j));
			}
			assertEquals(s.charAt(i), it.current());
			final int finalLine = line;
			assertEquals(line, it.getLine(),
					() -> String.format("Expected char '%c' to be at line %,d but was at line %,d.", it.current(),
							finalLine,
							it.getLine()));
			final int finalColumn = column;
			assertEquals(column, it.getColumn(),
					() -> String.format("Expected char '%c' to be at column %,d but was at column %,d.", it.current(),
							finalColumn,
							it.getColumn()));
			if (it.current() == '\n') {
				line++;
				column = 1;
			} else {
				column++;
			}
			it.move();
		}
		assertFalse(it.hasNext());
		assertFalse(it.hasNext(1));
	}
}
