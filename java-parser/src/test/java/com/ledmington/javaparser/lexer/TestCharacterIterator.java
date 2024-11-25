package com.ledmington.javaparser.lexer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class TestCharacterIterator {

	@ParameterizedTest
	@ValueSource(strings = {"", "abc"})
	public void sampleString(final String s) {
		final CharacterIterator it = new CharacterIterator(s);
		for(int i=0; i<s.length(); i++) {
			assertTrue(it.hasNext());
			for(int j=1; j+i<s.length(); j++) {
				assertTrue(it.hasNext(j));
			}
			assertEquals(s.charAt(i), it.current());
			it.move();
		}
		assertFalse(it.hasNext());
		assertFalse(it.hasNext(1));
	}
}
