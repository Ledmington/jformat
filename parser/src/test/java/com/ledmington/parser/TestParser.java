package com.ledmington.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class TestParser {

	private record Pair<X, Y>(X first, Y second) {}

	private static final Parser empty = Parser.builder().build();
	private static final Parser a = Parser.builder().one('a').build();
	private static final Parser abcSingle = Parser.builder().one('a').one('b').one('c').build();
	private static final Parser abc = Parser.builder().one("abc").build();
	private static final Parser optionalA = Parser.builder().zeroOrOne('a').build();
	private static final Parser optionalA_optionalB_optionalC = Parser.builder().zeroOrOne('a').zeroOrOne('b').zeroOrOne('c').build();
	private static final Parser optionalABC = Parser.builder().zeroOrOne("abc").build();
	private static final Parser anyA = Parser.builder().zeroOrMore('a').build();
	private static final Parser anyA_anyB = Parser.builder().zeroOrMore('a').zeroOrMore('b').build();
	private static final Map<Parser, Pair<List<String>, List<String>>> tests = Map.of(
		empty, new Pair<>(List.of(""), List.of("a")),
		a, new Pair<>(List.of("a"), List.of("", "b", "aa")),
		abcSingle, new Pair<>(List.of("abc"), List.of("", "a", "b", "c", "abcabc", "cba")),
		abc, new Pair<>(List.of("abc"), List.of("", "a", "b", "c", "abcabc", "cba")),
		optionalA, new Pair<>(List.of("", "a"), List.of("b", "aa")),
		optionalA_optionalB_optionalC, new Pair<>(List.of("", "a", "b", "c", "ab", "ac", "bc", "abc"), List.of("aa", "bb", "cc", "cba")),
		optionalABC, new Pair<>(List.of("", "abc"), List.of("a", "b", "c", "ab", "ac", "bc", "aa", "bb", "cc", "cba")),
		anyA, new Pair<>(List.of("", "a", "aa", "aaa", "aaaa", "aaaaa"), List.of("b", "ab", "ba")),
		anyA_anyB, new Pair<>(List.of("", "a", "aa", "aaa", "aaaa", "aaaaa", "b", "ab", "aab", "aaab", "aaaab", "aaaaab", "bb", "abb", "aabb", "aaabb", "aaaabb", "aaaaabb", "bbb", "abbb", "aabbb", "aaabbb", "aaaabbb", "aaaaabbb"), List.of("ba", "aba", "bab"))
	);

	private static Stream<Arguments> correctMatches() {
		return tests.entrySet().stream().flatMap(e -> e.getValue().first().stream().map(v -> Arguments.of(e.getKey(), v)));
	}

	private static Stream<Arguments> wrongMatches() {
		return tests.entrySet().stream().flatMap(e -> e.getValue().second().stream().map(v -> Arguments.of(e.getKey(), v)));
	}

	@ParameterizedTest
	@MethodSource("correctMatches")
	void correctParsing(final Parser p, final String input) {
		assertTrue(p.matches(input), () -> String.format("Expected '%s' to match '%s' but didn't.", p, input));
	}

	@ParameterizedTest
	@MethodSource("wrongMatches")
	void wrongParsing(final Parser p, final String input) {
		assertFalse(p.matches(input), () -> String.format("Expected '%s' to NOT match '%s' but did.", p, input));
	}
}
