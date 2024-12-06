package com.ledmington.parser;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class TestParser {

	private record Pair<X, Y>(X first, Y second) {
	}

	private static final Supplier<Parser> empty = () -> Parser.builder().build();
	private static final Supplier<Parser> a = () -> Parser.builder().one('a').build();
	private static final Supplier<Parser> abcSingle = () -> Parser.builder().one('a').one('b').one('c').build();
	private static final Supplier<Parser> abc = () -> Parser.builder().one("abc").build();
	private static final Supplier<Parser> optionalA = () -> Parser.builder().zeroOrOne('a').build();
	private static final Supplier<Parser> optionalA_optionalB_optionalC = () -> Parser.builder().zeroOrOne('a').zeroOrOne('b')
			.zeroOrOne('c').build();
	private static final Supplier<Parser> optionalABC = () -> Parser.builder().zeroOrOne("abc").build();
	private static final Supplier<Parser> anyA = () -> Parser.builder().zeroOrMore('a').build();
	private static final Supplier<Parser> anyA_anyB = () -> Parser.builder().zeroOrMore('a').zeroOrMore('b').build();
	private static final Supplier<Parser> anyA_B_anyC = () -> Parser.builder().zeroOrMore('a').one('b').zeroOrMore('c').build();
	private static final Supplier<Parser> anyAB = () -> Parser.builder().zeroOrMore("ab").build();
	private static final Supplier<Parser> nestedA = () -> Parser.builder().one(Parser.builder().one('a').build()).build();
	private static final Supplier<Parser> nestedABC = () -> Parser.builder().one(Parser.builder().one('a').build())
			.one(Parser.builder().one('b').build()).one(Parser.builder().one('c').build()).build();
	private static final Supplier<Parser> B_optionalA_C = () -> Parser.builder().one('b')
			.zeroOrOne(Parser.builder().one('a').build()).one('c').build();
	private static final Supplier<Parser> B_anyA_C = () -> Parser.builder().one('b').zeroOrMore(Parser.builder().one('a').build()).one('c').build();
	private static final Map<Supplier<Parser>, Pair<List<String>, List<String>>> tests = Map.ofEntries(
			Map.entry(empty, new Pair<>(List.of(""), List.of("a", "b", "aa", "ab"))),
			Map.entry(a, new Pair<>(List.of("a"), List.of("", "b", "aa"))),
			Map.entry(abcSingle, new Pair<>(List.of("abc"), List.of("", "a", "b", "c", "ab", "bc", "abcabc", "cba"))),
			Map.entry(abc, new Pair<>(List.of("abc"), List.of("", "a", "b", "c", "ab", "bc", "abcabc", "cba"))),
			Map.entry(optionalA, new Pair<>(List.of("", "a"), List.of("b", "aa"))),
			Map.entry(optionalA_optionalB_optionalC,
					new Pair<>(List.of("", "a", "b", "c", "ab", "ac", "bc", "abc"), List.of("aa", "bb", "cc", "cba"))),
			Map.entry(optionalABC,
					new Pair<>(List.of("", "abc"), List.of("a", "b", "c", "ab", "ac", "bc", "aa", "bb", "cc", "cba"))),
			Map.entry(anyA, new Pair<>(List.of("", "a", "aa", "aaa", "aaaa", "aaaaa"), List.of("b", "ab", "ba"))),
			Map.entry(anyA_anyB,
					new Pair<>(List.of("", "a", "aa", "aaa", "aaaa", "aaaaa", "b", "ab", "aab", "aaab", "aaaab",
							"aaaaab", "bb",
							"abb", "aabb", "aaabb", "aaaabb", "aaaaabb", "bbb", "abbb", "aabbb", "aaabbb", "aaaabbb",
							"aaaaabbb"), List.of("ba", "aba", "bab"))),
			Map.entry(anyA_B_anyC,
					new Pair<>(List.of("b", "ab", "bc", "abc", "aab", "bcc", "aabc", "abcc", "aabcc"),
							List.of("", "a", "c", "aba", "cbc", "cba", "bac"))),
			Map.entry(anyAB, new Pair<>(List.of("", "ab", "abab", "ababab", "abababab"),
					List.of("a", "b", "aba", "abcab", "c"))),
			Map.entry(nestedA, new Pair<>(List.of("a"), List.of("", "b", "aa"))),
			Map.entry(nestedABC, new Pair<>(List.of("abc"), List.of("", "a", "b", "c", "ab", "bc", "abcabc", "cba"))),
			Map.entry(B_optionalA_C, new Pair<>(List.of("bc", "bac"), List.of("", "a", "b", "c", "ab", "abc", "abcabc", "cba"))),
			Map.entry(B_anyA_C, new Pair<>(List.of("bc", "bac", "baac", "baaac", "baaaac"), List.of("", "a", "b", "c", "ab", "ba", "abc", "ac", "abcabc", "cba"))));

	private static Stream<Arguments> correctMatches() {
		return tests.entrySet().stream()
				.flatMap(e -> e.getValue().first().stream().map(v -> Arguments.of(e.getKey().get(), v)));
	}

	private static Stream<Arguments> wrongMatches() {
		return tests.entrySet().stream()
				.flatMap(e -> e.getValue().second().stream().map(v -> Arguments.of(e.getKey().get(), v)));
	}

	@ParameterizedTest
	@MethodSource("correctMatches")
	void correctParsing(final Parser parser, final String input) {
		assertTrue(parser.matches(input), () -> String.format("Expected '%s' to match '%s' but didn't.", parser, input));
	}

	@ParameterizedTest
	@MethodSource("wrongMatches")
	void wrongParsing(final Parser parser, final String input) {
		assertFalse(parser.matches(input), () -> String.format("Expected '%s' to NOT match '%s' but did.", parser, input));
	}
}
