package com.ledmington.javaparser.lexer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class JavaLexer {

	private JavaLexer() {
	}

	public static List<JavaToken> tokenize(final String code) {
		final CharacterIterator it = new CharacterIterator(Objects.requireNonNull(code));
		final List<JavaToken> tokens = new ArrayList<>();
		while (it.hasNext()) {
			final Optional<JavaToken> t = tokenize(it);
			if (t.isPresent()) {
				tokens.add(t.orElseThrow());
				// it.move();
			} else {
				break;
			}
		}
		return tokens;
	}

	private static Optional<JavaToken> tokenize(final CharacterIterator it) {
		if (!it.hasNext()) {
			return Optional.empty();
		}

		skipBlanksAndComments(it);

		if (!it.hasNext()) {
			return Optional.empty();
		}

		return Optional.of(switch (it.current()) {
			case '{' -> {
				it.move();
				yield JavaSymbols.LEFT_CURLY_BRACKET;
			}
			case '}' -> {
				it.move();
				yield JavaSymbols.RIGHT_CURLY_BRACKET;
			}
			case '[' -> {
				it.move();
				yield JavaSymbols.LEFT_SQUARE_BRACKET;
			}
			case ']' -> {
				it.move();
				yield JavaSymbols.RIGHT_SQUARE_BRACKET;
			}
			case '(' -> {
				it.move();
				yield JavaSymbols.LEFT_BRACKET;
			}
			case ')' -> {
				it.move();
				yield JavaSymbols.RIGHT_BRACKET;
			}
			case '.' -> {
				it.move();
				yield JavaSymbols.DOT;
			}
			case ',' -> {
				it.move();
				yield JavaSymbols.COMMA;
			}
			case ':' -> {
				it.move();
				yield JavaSymbols.COLON;
			}
			case ';' -> {
				it.move();
				yield JavaSymbols.SEMICOLON;
			}
			case '~' -> {
				it.move();
				yield JavaSymbols.TILDE;
			}
			case '!' -> {
				it.move();
				yield JavaSymbols.EXCLAMATION_MARK;
			}
			case '?' -> {
				it.move();
				yield JavaSymbols.QUESTION_MARK;
			}
			case '@' -> {
				it.move();
				yield JavaSymbols.AT_SIGN;
			}
			case '#' -> throw new UnknownTokenException('#');
			case '<' -> {
				if (it.hasNext(3) && it.next() == '<' && it.current(2) == '=') {
					it.move(3);
					yield JavaSymbols.LEFT_SHIFT_EQUAL;
				} else if (it.hasNext(2)) {
					if (it.next() == '=') {
						it.move(2);
						yield JavaSymbols.LESS_OR_EQUAL;
					} else if (it.next() == '<') {
						it.move(2);
						yield JavaSymbols.LEFT_SHIFT;
					}
				}
				it.move();
				yield JavaSymbols.LEFT_ANGLE_BRACKET;
			}
			case '>' -> {
				if (it.hasNext(4) && it.next() == '>' && it.current(2) == '>' && it.current(3) == '=') {
					it.move(4);
					yield JavaSymbols.UNSIGNED_RIGHT_SHIFT_EQUAL;
				} else if (it.hasNext(3) && it.next() == '>' && it.current(2) == '=') {
					it.move(3);
					yield JavaSymbols.RIGHT_SHIFT_EQUAL;
				} else if (it.hasNext(3) && it.next() == '>' && it.current(2) == '>') {
					it.move(3);
					yield JavaSymbols.UNSIGNED_RIGHT_SHIFT;
				} else if (it.hasNext(2) && it.next() == '=') {
					it.move(2);
					yield JavaSymbols.GREATER_OR_EQUAL;
				} else if (it.hasNext(2) && it.next() == '>') {
					it.move(2);
					yield JavaSymbols.RIGHT_SHIFT;
				}
				it.move();
				yield JavaSymbols.RIGHT_ANGLE_BRACKET;
			}
			case '%' -> {
				if (it.hasNext(2) && it.next() == '=') {
					it.move(2);
					yield JavaSymbols.PERCENT_EQUAL;
				}
				it.move();
				yield JavaSymbols.PERCENT;
			}
			case '+' -> {
				if (it.hasNext(2)) {
					if (it.next() == '=') {
						it.move(2);
						yield JavaSymbols.PLUS_EQUAL;
					} else if (it.next() == '+') {
						it.move(2);
						yield JavaSymbols.PLUS_PLUS;
					}
				}
				it.move();
				yield JavaSymbols.PLUS;
			}
			case '-' -> {
				if (it.hasNext(2)) {
					if (it.next() == '=') {
						it.move(2);
						yield JavaSymbols.MINUS_EQUAL;
					} else if (it.next() == '-') {
						it.move(2);
						yield JavaSymbols.MINUS_MINUS;
					} else if (it.next() == '>') {
						it.move(2);
						yield JavaSymbols.ARROW;
					}
				}
				it.move();
				yield JavaSymbols.MINUS;
			}
			case '*' -> {
				if (it.hasNext(2) && it.next() == '=') {
					it.move(2);
					yield JavaSymbols.ASTERISK_EQUAL;
				}
				it.move();
				yield JavaSymbols.ASTERISK;
			}
			case '/' -> {
				if (it.hasNext(2) && it.next() == '=') {
					it.move(2);
					yield JavaSymbols.FORWARD_SLASH_EQUAL;
				}
				it.move();
				yield JavaSymbols.FORWARD_SLASH;
			}
			case '^' -> {
				if (it.hasNext(2) && it.next() == '=') {
					it.move(2);
					yield JavaSymbols.HAT_EQUAL;
				}
				it.move();
				yield JavaSymbols.HAT;
			}
			case '|' -> {
				if (it.hasNext(2) && it.next() == '=') {
					it.move(2);
					yield JavaSymbols.PIPE_EQUAL;
				} else if (it.hasNext(2) && it.next() == '|') {
					it.move(2);
					yield JavaSymbols.DOUBLE_PIPE;
				}
				it.move();
				yield JavaSymbols.PIPE;
			}
			case '&' -> {
				if (it.hasNext(2) && it.next() == '=') {
					it.move(2);
					yield JavaSymbols.AMPERSAND_EQUAL;
				} else if (it.hasNext(2) && it.next() == '&') {
					it.move(2);
					yield JavaSymbols.DOUBLE_AMPERSAND;
				}
				it.move();
				yield JavaSymbols.AMPERSAND;
			}
			case '=' -> {
				if (it.hasNext(2) && it.next() == '=') {
					it.move(2);
					yield JavaSymbols.DOUBLE_EQUAL;
				}
				it.move();
				yield JavaSymbols.EQUAL;
			}
			case '\'' -> {
				it.move();
				final StringBuilder sb = new StringBuilder();
				while (true) {
					if (!it.hasNext()) {
						throw new InvalidLiteralException('\'' + sb.toString());
					}
					if (it.current() == '\'') {
						break;
					}
					sb.append(it.current());
					it.move();
				}
				it.move();
				yield new CharLiteral(sb.toString());
			}
			case '"' -> {
				it.move();
				final StringBuilder sb = new StringBuilder();
				while (true) {
					if (!it.hasNext()) {
						throw new InvalidLiteralException('"' + sb.toString());
					}
					if (it.current() == '"') {
						break;
					}
					sb.append(it.current());
					it.move();
				}
				it.move();
				yield new StringLiteral(sb.toString());
			}
			default -> {
				if (Character.isDigit(it.current())) {
					yield parseIntegerLiteral(it);
				}

				final StringBuilder sb = new StringBuilder();
				while (it.hasNext() && (Character.isAlphabetic(it.current()) || Character.isDigit(it.current())
						|| it.current() == '_' || it.current() == '$' || it.current() == '£' || it.current() == '€')) {
					sb.append(it.current());
					it.move();
				}
				final String str = sb.toString();
				yield switch (str) {
					case "public" -> JavaKeywords.PUBLIC;
					case "private" -> JavaKeywords.PRIVATE;
					case "protected" -> JavaKeywords.PROTECTED;
					case "static" -> JavaKeywords.STATIC;
					case "final" -> JavaKeywords.FINAL;
					case "volatile" -> JavaKeywords.VOLATILE;
					case "synchronized" -> JavaKeywords.SYNCHRONIZED;
					case "strictfp" -> JavaKeywords.STRICTFP;
					case "transient" -> JavaKeywords.TRANSIENT;
					case "abstract" -> JavaKeywords.ABSTRACT;
					case "native" -> JavaKeywords.NATIVE;
					case "sealed" -> JavaKeywords.SEALED;
					case "class" -> JavaKeywords.CLASS;
					case "enum" -> JavaKeywords.ENUM;
					case "interface" -> JavaKeywords.INTERFACE;
					case "record" -> JavaKeywords.RECORD;
					case "permits" -> JavaKeywords.PERMITS;
					case "extends" -> JavaKeywords.EXTENDS;
					case "implements" -> JavaKeywords.IMPLEMENTS;
					case "package" -> JavaKeywords.PACKAGE;
					case "import" -> JavaKeywords.IMPORT;
					case "throws" -> JavaKeywords.THROWS;
					case "boolean" -> JavaKeywords.BOOLEAN;
					case "byte" -> JavaKeywords.BYTE;
					case "char" -> JavaKeywords.CHAR;
					case "short" -> JavaKeywords.SHORT;
					case "int" -> JavaKeywords.INT;
					case "float" -> JavaKeywords.FLOAT;
					case "long" -> JavaKeywords.LONG;
					case "double" -> JavaKeywords.DOUBLE;
					case "void" -> JavaKeywords.VOID;
					case "null" -> JavaKeywords.NULL;
					case "true" -> JavaKeywords.TRUE;
					case "false" -> JavaKeywords.FALSE;
					case "if" -> JavaKeywords.IF;
					case "else" -> JavaKeywords.ELSE;
					case "do" -> JavaKeywords.DO;
					case "while" -> JavaKeywords.WHILE;
					case "for" -> JavaKeywords.FOR;
					case "try" -> JavaKeywords.TRY;
					case "catch" -> JavaKeywords.CATCH;
					case "finally" -> JavaKeywords.FINALLY;
					case "throw" -> JavaKeywords.THROW;
					case "goto" -> JavaKeywords.GOTO;
					case "return" -> JavaKeywords.RETURN;
					case "switch" -> JavaKeywords.SWITCH;
					case "case" -> JavaKeywords.CASE;
					case "break" -> JavaKeywords.BREAK;
					case "default" -> JavaKeywords.DEFAULT;
					case "continue" -> JavaKeywords.CONTINUE;
					case "assert" -> JavaKeywords.ASSERT;
					case "new" -> JavaKeywords.NEW;
					case "instanceof" -> JavaKeywords.INSTANCEOF;
					case "this" -> JavaKeywords.THIS;
					case "super" -> JavaKeywords.SUPER;
					case "const" -> JavaKeywords.CONST;
					default -> new JavaID(str);
				};
			}
		});
	}

	private static JavaToken parseIntegerLiteral(final CharacterIterator it) {
		BigInteger x = BigInteger.ZERO;

		if (it.hasNext(2) && it.current() == '0' && it.current(1) == 'x') {
			// hexadecimal literal
			it.move(2);
			while (it.hasNext()
					&& (Character.isDigit(it.current())
							|| it.current() == 'a'
							|| it.current() == 'b'
							|| it.current() == 'c'
							|| it.current() == 'd'
							|| it.current() == 'e'
							|| it.current() == 'f')) {
				x = x.shiftLeft(4).add(new BigInteger(String.valueOf(it.current()), 16));
				it.move();
			}
		} else if (it.hasNext(2) && it.current() == '0' && it.next() == 'b') {
			// binary literal
			it.move(2);
			while (it.hasNext() && (it.current() == '0' || it.current() == '1')) {
				x = x.shiftLeft(1).add((it.current() == '1') ? BigInteger.ONE : BigInteger.ZERO);
				it.move();
			}
		} else {
			// base-10 literal
			while (it.hasNext() && (Character.isDigit(it.current()) || it.current() == '_')) {
				if (Character.isDigit(it.current())) {
					x = x.multiply(BigInteger.TEN).add(new BigInteger(String.valueOf(it.current())));
				}
				it.move();
			}
		}

		if (it.hasNext() && it.current() == 'L') {
			it.move();
			return new IntegerLiteral(x, true);
		}

		return new IntegerLiteral(x);
	}

	private static void skipBlanksAndComments(final CharacterIterator it) {
		boolean again;
		do {
			again = false;
			if (isBlank(it)) {
				skipBlanks(it);
				again = true;
			}
			if (isLineComment(it)) {
				skipLineComment(it);
				again = true;
			}
			if (isBlockComment(it)) {
				skipBlockComment(it);
				again = true;
			}
		} while (again);
	}

	private static boolean isBlank(final CharacterIterator it) {
		return it.hasNext() && Character.isWhitespace(it.current());
	}

	private static void skipBlanks(final CharacterIterator it) {
		while (it.hasNext() && Character.isWhitespace(it.current())) {
			it.move();
		}
	}

	private static boolean isLineComment(final CharacterIterator it) {
		return it.hasNext(2) && it.current() == '/' && it.next() == '/';
	}

	private static void skipLineComment(final CharacterIterator it) {
		it.move();
		do {
			it.move();
		} while (it.current() != '\n');
		it.move();
	}

	private static boolean isBlockComment(final CharacterIterator it) {
		return it.hasNext(2) && it.current() == '/' && it.next() == '*';
	}

	private static void skipBlockComment(final CharacterIterator it) {
		it.move();
		do {
			it.move();
		} while (it.current() != '*' || it.next() != '/');
		it.move();
		it.move();
	}
}
