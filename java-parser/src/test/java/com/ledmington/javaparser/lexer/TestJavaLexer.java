package com.ledmington.javaparser.lexer;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class TestJavaLexer {

	private static Stream<Arguments> correctJavaSourceCode() {
		return Stream.of(
				Arguments.of(" ", List.of()),
				Arguments.of("/*ignored*/", List.of()),
				Arguments.of("//ignored\n", List.of()),
				// keywords
				Arguments.of("public", List.of(JavaKeywords.PUBLIC)),
				Arguments.of("private", List.of(JavaKeywords.PRIVATE)),
				Arguments.of("protected", List.of(JavaKeywords.PROTECTED)),
				Arguments.of("static", List.of(JavaKeywords.STATIC)),
				Arguments.of("final", List.of(JavaKeywords.FINAL)),
				Arguments.of("volatile", List.of(JavaKeywords.VOLATILE)),
				Arguments.of("synchronized", List.of(JavaKeywords.SYNCHRONIZED)),
				Arguments.of("strictfp", List.of(JavaKeywords.STRICTFP)),
				Arguments.of("transient", List.of(JavaKeywords.TRANSIENT)),
				Arguments.of("abstract", List.of(JavaKeywords.ABSTRACT)),
				Arguments.of("native", List.of(JavaKeywords.NATIVE)),
				Arguments.of("sealed", List.of(JavaKeywords.SEALED)),
				Arguments.of("class", List.of(JavaKeywords.CLASS)),
				Arguments.of("enum", List.of(JavaKeywords.ENUM)),
				Arguments.of("interface", List.of(JavaKeywords.INTERFACE)),
				Arguments.of("record", List.of(JavaKeywords.RECORD)),
				Arguments.of("permits", List.of(JavaKeywords.PERMITS)),
				Arguments.of("extends", List.of(JavaKeywords.EXTENDS)),
				Arguments.of("implements", List.of(JavaKeywords.IMPLEMENTS)),
				Arguments.of("package", List.of(JavaKeywords.PACKAGE)),
				Arguments.of("import", List.of(JavaKeywords.IMPORT)),
				Arguments.of("throws", List.of(JavaKeywords.THROWS)),
				// types
				Arguments.of("boolean", List.of(JavaKeywords.BOOLEAN)),
				Arguments.of("byte", List.of(JavaKeywords.BYTE)),
				Arguments.of("char", List.of(JavaKeywords.CHAR)),
				Arguments.of("short", List.of(JavaKeywords.SHORT)),
				Arguments.of("int", List.of(JavaKeywords.INT)),
				Arguments.of("float", List.of(JavaKeywords.FLOAT)),
				Arguments.of("long", List.of(JavaKeywords.LONG)),
				Arguments.of("double", List.of(JavaKeywords.DOUBLE)),
				Arguments.of("void", List.of(JavaKeywords.VOID)),
				Arguments.of("null", List.of(JavaKeywords.NULL)),
				Arguments.of("true", List.of(JavaKeywords.TRUE)),
				Arguments.of("false", List.of(JavaKeywords.FALSE)),
				// control flow
				Arguments.of("if", List.of(JavaKeywords.IF)),
				Arguments.of("else", List.of(JavaKeywords.ELSE)),
				Arguments.of("do", List.of(JavaKeywords.DO)),
				Arguments.of("while", List.of(JavaKeywords.WHILE)),
				Arguments.of("for", List.of(JavaKeywords.FOR)),
				Arguments.of("try", List.of(JavaKeywords.TRY)),
				Arguments.of("catch", List.of(JavaKeywords.CATCH)),
				Arguments.of("finally", List.of(JavaKeywords.FINALLY)),
				Arguments.of("throw", List.of(JavaKeywords.THROW)),
				Arguments.of("goto", List.of(JavaKeywords.GOTO)),
				Arguments.of("return", List.of(JavaKeywords.RETURN)),
				Arguments.of("switch", List.of(JavaKeywords.SWITCH)),
				Arguments.of("case", List.of(JavaKeywords.CASE)),
				Arguments.of("break", List.of(JavaKeywords.BREAK)),
				Arguments.of("default", List.of(JavaKeywords.DEFAULT)),
				Arguments.of("continue", List.of(JavaKeywords.CONTINUE)),
				Arguments.of("assert", List.of(JavaKeywords.ASSERT)),
				Arguments.of("new", List.of(JavaKeywords.NEW)),
				Arguments.of("instanceof", List.of(JavaKeywords.INSTANCEOF)),
				Arguments.of("this", List.of(JavaKeywords.THIS)),
				Arguments.of("super", List.of(JavaKeywords.SUPER)),
				Arguments.of("const", List.of(JavaKeywords.CONST)),
				// symbols
				Arguments.of("{", List.of(JavaSymbols.LEFT_CURLY_BRACKET)),
				Arguments.of("}", List.of(JavaSymbols.RIGHT_CURLY_BRACKET)),
				Arguments.of(".", List.of(JavaSymbols.DOT)),
				Arguments.of(",", List.of(JavaSymbols.COMMA)),
				Arguments.of(";", List.of(JavaSymbols.SEMICOLON)),
				Arguments.of(":", List.of(JavaSymbols.COLON)),
				Arguments.of("[", List.of(JavaSymbols.LEFT_SQUARE_BRACKET)),
				Arguments.of("]", List.of(JavaSymbols.RIGHT_SQUARE_BRACKET)),
				Arguments.of("(", List.of(JavaSymbols.LEFT_BRACKET)),
				Arguments.of(")", List.of(JavaSymbols.RIGHT_BRACKET)),
				Arguments.of("<", List.of(JavaSymbols.LEFT_ANGLE_BRACKET)),
				Arguments.of(">", List.of(JavaSymbols.RIGHT_ANGLE_BRACKET)),
				Arguments.of("+", List.of(JavaSymbols.PLUS)),
				Arguments.of("-", List.of(JavaSymbols.MINUS)),
				Arguments.of("*", List.of(JavaSymbols.ASTERISK)),
				Arguments.of("/", List.of(JavaSymbols.FORWARD_SLASH)),
				Arguments.of("%", List.of(JavaSymbols.PERCENT)),
				Arguments.of("^", List.of(JavaSymbols.HAT)),
				Arguments.of("|", List.of(JavaSymbols.PIPE)),
				Arguments.of("&", List.of(JavaSymbols.AMPERSAND)),
				Arguments.of("~", List.of(JavaSymbols.TILDE)),
				Arguments.of("!", List.of(JavaSymbols.EXCLAMATION_MARK)),
				Arguments.of("?", List.of(JavaSymbols.QUESTION_MARK)),
				Arguments.of("=", List.of(JavaSymbols.EQUAL)),
				Arguments.of("@", List.of(JavaSymbols.AT_SIGN)),
				Arguments.of("==", List.of(JavaSymbols.DOUBLE_EQUAL)),
				Arguments.of("!=", List.of(JavaSymbols.NOT_EQUAL)),
				Arguments.of("<=", List.of(JavaSymbols.LESS_OR_EQUAL)),
				Arguments.of(">=", List.of(JavaSymbols.GREATER_OR_EQUAL)),
				Arguments.of("&&", List.of(JavaSymbols.DOUBLE_AMPERSAND)),
				Arguments.of("||", List.of(JavaSymbols.DOUBLE_PIPE)),
				Arguments.of("->", List.of(JavaSymbols.ARROW)),
				Arguments.of("++", List.of(JavaSymbols.PLUS_PLUS)),
				Arguments.of("--", List.of(JavaSymbols.MINUS_MINUS)),
				Arguments.of("+=", List.of(JavaSymbols.PLUS_EQUAL)),
				Arguments.of("-=", List.of(JavaSymbols.MINUS_EQUAL)),
				Arguments.of("*=", List.of(JavaSymbols.ASTERISK_EQUAL)),
				Arguments.of("/=", List.of(JavaSymbols.FORWARD_SLASH_EQUAL)),
				Arguments.of("%=", List.of(JavaSymbols.PERCENT_EQUAL)),
				Arguments.of("|=", List.of(JavaSymbols.PIPE_EQUAL)),
				Arguments.of("&=", List.of(JavaSymbols.AMPERSAND_EQUAL)),
				Arguments.of("^=", List.of(JavaSymbols.HAT_EQUAL)),
				// integer/long literals
				Arguments.of("-1_2_3_4", List.of(JavaSymbols.MINUS, new IntegerLiteral(BigInteger.valueOf(1234)))),
				Arguments.of("-12", List.of(JavaSymbols.MINUS, new IntegerLiteral(BigInteger.valueOf(12)))),
				Arguments.of("0", List.of(new IntegerLiteral(BigInteger.valueOf(0)))),
				Arguments.of("12", List.of(new IntegerLiteral(BigInteger.valueOf(12)))),
				Arguments.of("1_2_3_4", List.of(new IntegerLiteral(BigInteger.valueOf(1234)))),
				Arguments.of(
						"-1_2_3_4L", List.of(JavaSymbols.MINUS, new IntegerLiteral(BigInteger.valueOf(1234), true))),
				Arguments.of("-12L", List.of(JavaSymbols.MINUS, new IntegerLiteral(BigInteger.valueOf(12), true))),
				Arguments.of("0L", List.of(new IntegerLiteral(BigInteger.valueOf(0), true))),
				Arguments.of("12L", List.of(new IntegerLiteral(BigInteger.valueOf(12), true))),
				Arguments.of("1_2_3_4L", List.of(new IntegerLiteral(BigInteger.valueOf(1234), true))),
				// hexadecimal literals
				Arguments.of("0x0", List.of(new IntegerLiteral(BigInteger.valueOf(0)))),
				Arguments.of("0x0L", List.of(new IntegerLiteral(BigInteger.valueOf(0), true))),
				Arguments.of("0x0123456789abcdef", List.of(new IntegerLiteral(BigInteger.valueOf(81985529216486895L)))),
				Arguments.of(
						"0x0123456789abcdefL",
						List.of(new IntegerLiteral(BigInteger.valueOf(81985529216486895L), true))),
				// binary literals
				Arguments.of("0b0", List.of(new IntegerLiteral(BigInteger.valueOf(0)))),
				Arguments.of("0b0L", List.of(new IntegerLiteral(BigInteger.valueOf(0), true))),
				Arguments.of("0b11010101", List.of(new IntegerLiteral(BigInteger.valueOf(213)))),
				Arguments.of("0b11010101L", List.of(new IntegerLiteral(BigInteger.valueOf(213), true))),
				// char literals
				Arguments.of("'a'", List.of(new CharLiteral("a"))),
				Arguments.of("'\\n'", List.of(new CharLiteral("\\n"))),
				Arguments.of("'\\u1234'", List.of(new CharLiteral("\\u1234"))),
				// string literals
				Arguments.of("\"\"", List.of(new StringLiteral(""))),
				Arguments.of("\"abc\"", List.of(new StringLiteral("abc"))),
				Arguments.of("\"\n\"", List.of(new StringLiteral("\n"))),
				// IDs
				Arguments.of("x", List.of(new JavaID("x"))),
				Arguments.of("x1", List.of(new JavaID("x1"))),
				Arguments.of("x1y", List.of(new JavaID("x1y"))),
				Arguments.of("ID", List.of(new JavaID("ID"))),
				Arguments.of("_x", List.of(new JavaID("_x"))),
				Arguments.of("Another_ID", List.of(new JavaID("Another_ID"))));
	}

	@ParameterizedTest
	@MethodSource("correctJavaSourceCode")
	void correctParsing(final String sourceCode, final List<JavaToken> tokens) {
		final JavaLexer it = new JavaLexer(sourceCode);

		for (int i = 0; i < tokens.size(); i++) {
			final Optional<JavaToken> t = it.next();
			Assertions.assertTrue(t.isPresent());
			final int finalI = i;
			Assertions.assertEquals(
					tokens.get(i),
					t.orElseThrow(),
					() -> String.format(
							"%,d-th token expected to be %s but was %s", finalI, tokens.get(finalI), t.orElseThrow()));
		}
		final Optional<JavaToken> t = it.next();
		Assertions.assertTrue(
				t.isEmpty(), () -> String.format("Expected no more tokens but there was '%s'", t.orElseThrow()));
	}

	private static Stream<Arguments> wrongJavaSourceCode() {
		return Stream.of("$", "£", "#", "€","\"a","\"","'a","'").map(Arguments::of);
	}

	@ParameterizedTest
	@MethodSource("wrongJavaSourceCode")
	void invalidParsing(final String sourceCode) {
		final JavaLexer it = new JavaLexer(sourceCode);

		try {
			while (it.next().isPresent()) {
				// nothing to do
			}
			// if we reach the end without exceptions we fail
			Assertions.fail();
		} catch (final UnknownTokenException | InvalidLiteralException e) {
			// ignored because it is what we expect
		}
	}
}
