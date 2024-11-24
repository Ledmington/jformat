package com.ledmington.javaparser.lexer;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

public final class JavaLexer {

	private final char[] v;
	private int i = 0;

	public JavaLexer(final String code) {
		this.v = Objects.requireNonNull(code).toCharArray();
	}

	/**
	 * Finds the next token in the input.
	 *
	 * @return An empty {@link Optional} if no other token could be found, a "full" optional otherwise.
	 */
	public Optional<JavaToken> next() {
		if (i >= v.length) {
			return Optional.empty();
		}

		skipBlanksAndComments();

		if (i >= v.length) {
			return Optional.empty();
		}

		if (v[i] == '{') {
			i++;
			return Optional.of(JavaSymbols.LEFT_CURLY_BRACKET);
		} else if (v[i] == '}') {
			i++;
			return Optional.of(JavaSymbols.RIGHT_CURLY_BRACKET);
		} else if (v[i] == '.') {
			i++;
			return Optional.of(JavaSymbols.DOT);
		} else if (v[i] == ',') {
			i++;
			return Optional.of(JavaSymbols.COMMA);
		} else if (v[i] == ';') {
			i++;
			return Optional.of(JavaSymbols.SEMICOLON);
		} else if (v[i] == ':') {
			i++;
			return Optional.of(JavaSymbols.COLON);
		} else if (v[i] == '[') {
			i++;
			return Optional.of(JavaSymbols.LEFT_SQUARE_BRACKET);
		} else if (v[i] == ']') {
			i++;
			return Optional.of(JavaSymbols.RIGHT_SQUARE_BRACKET);
		} else if (v[i] == '(') {
			i++;
			return Optional.of(JavaSymbols.LEFT_BRACKET);
		} else if (v[i] == ')') {
			i++;
			return Optional.of(JavaSymbols.RIGHT_BRACKET);
		} else if (v[i] == '<') {
			i++;
			if (i < v.length && v[i] == '=') {
				i++;
				return Optional.of(JavaSymbols.LESS_OR_EQUAL);
			}
			return Optional.of(JavaSymbols.LEFT_ANGLE_BRACKET);
		} else if (v[i] == '>') {
			i++;
			if (i < v.length && v[i] == '=') {
				i++;
				return Optional.of(JavaSymbols.GREATER_OR_EQUAL);
			}
			return Optional.of(JavaSymbols.RIGHT_ANGLE_BRACKET);
		} else if (v[i] == '+') {
			i++;
			if (i < v.length && v[i] == '+') {
				i++;
				return Optional.of(JavaSymbols.PLUS_PLUS);
			}
			if (i < v.length && v[i] == '=') {
				i++;
				return Optional.of(JavaSymbols.PLUS_EQUAL);
			}
			return Optional.of(JavaSymbols.PLUS);
		} else if (v[i] == '-') {
			i++;
			if (i < v.length && v[i] == '-') {
				i++;
				return Optional.of(JavaSymbols.MINUS_MINUS);
			}
			if (i < v.length && v[i] == '=') {
				i++;
				return Optional.of(JavaSymbols.MINUS_EQUAL);
			}
			if (i < v.length && v[i] == '>') {
				i++;
				return Optional.of(JavaSymbols.ARROW);
			}
			return Optional.of(JavaSymbols.MINUS);
		} else if (v[i] == '*') {
			i++;
			if (i < v.length && v[i] == '=') {
				i++;
				return Optional.of(JavaSymbols.ASTERISK_EQUAL);
			}
			return Optional.of(JavaSymbols.ASTERISK);
		} else if (v[i] == '/') {
			i++;
			if (i < v.length && v[i] == '=') {
				i++;
				return Optional.of(JavaSymbols.FORWARD_SLASH_EQUAL);
			}
			return Optional.of(JavaSymbols.FORWARD_SLASH);
		} else if (v[i] == '%') {
			i++;
			if (i < v.length && v[i] == '=') {
				i++;
				return Optional.of(JavaSymbols.PERCENT_EQUAL);
			}
			return Optional.of(JavaSymbols.PERCENT);
		} else if (v[i] == '^') {
			i++;
			if (i < v.length && v[i] == '=') {
				i++;
				return Optional.of(JavaSymbols.HAT_EQUAL);
			}
			return Optional.of(JavaSymbols.HAT);
		} else if (v[i] == '|') {
			i++;
			if (i < v.length && v[i] == '|') {
				i++;
				return Optional.of(JavaSymbols.DOUBLE_PIPE);
			}
			if (i < v.length && v[i] == '=') {
				i++;
				return Optional.of(JavaSymbols.PIPE_EQUAL);
			}
			return Optional.of(JavaSymbols.PIPE);
		} else if (v[i] == '&') {
			i++;
			if (i < v.length && v[i] == '&') {
				i++;
				return Optional.of(JavaSymbols.DOUBLE_AMPERSAND);
			}
			if (i < v.length && v[i] == '=') {
				i++;
				return Optional.of(JavaSymbols.AMPERSAND_EQUAL);
			}
			return Optional.of(JavaSymbols.AMPERSAND);
		} else if (v[i] == '~') {
			i++;
			return Optional.of(JavaSymbols.TILDE);
		} else if (v[i] == '!') {
			i++;
			if (i < v.length && v[i] == '=') {
				i++;
				return Optional.of(JavaSymbols.NOT_EQUAL);
			}
			return Optional.of(JavaSymbols.EXCLAMATION_MARK);
		} else if (v[i] == '?') {
			i++;
			return Optional.of(JavaSymbols.QUESTION_MARK);
		} else if (v[i] == '@') {
			i++;
			return Optional.of(JavaSymbols.AT_SIGN);
		} else if (v[i] == '\'') {
			i++;
			final StringBuilder sb = new StringBuilder();
			while(i < v.length && v[i] != '\'') {
				sb.append(v[i]);
				i++;
			}
			if(i >= v.length && v[v.length-1] != '\'') {
				throw new InvalidLiteralException('\'' + sb.toString());
			}
			i++;
			return Optional.of(new CharLiteral(sb.toString()));
		} else if (v[i] == '\"') {
			i++;
			final StringBuilder sb = new StringBuilder();
			while(i < v.length && v[i] != '\"') {
				sb.append(v[i]);
				i++;
			}
			if(i >= v.length && v[v.length-1] != '\"') {
				throw new InvalidLiteralException('\"' + sb.toString());
			}
			i++;
			return Optional.of(new StringLiteral(sb.toString()));
		} else if (v[i] == '=') {
			i++;
			if (i < v.length && v[i] == '=') {
				i++;
				return Optional.of(JavaSymbols.DOUBLE_EQUAL);
			}
			return Optional.of(JavaSymbols.EQUAL);
		}

		if (Character.isDigit(v[i])) {
			return parseIntegerLiteral();
		}

		if (!Character.isAlphabetic(v[i]) && v[i] != '_') {
			throw new UnknownTokenException(v[i]);
		}

		final StringBuilder sb = new StringBuilder();
		do {
			sb.append(v[i]);
			i++;
		} while (i < v.length && (Character.isAlphabetic(v[i]) || Character.isDigit(v[i]) || v[i] == '_'));

		final String s = sb.toString();
		return Optional.of(
				switch (s) {
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
					case "byte" -> JavaKeywords.BYTE;
					case "char" -> JavaKeywords.CHAR;
					case "short" -> JavaKeywords.SHORT;
					case "int" -> JavaKeywords.INT;
					case "float" -> JavaKeywords.FLOAT;
					case "long" -> JavaKeywords.LONG;
					case "double" -> JavaKeywords.DOUBLE;
					case "void" -> JavaKeywords.VOID;
					case "null" -> JavaKeywords.NULL;
					case "throws" -> JavaKeywords.THROWS;
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
					case "boolean" -> JavaKeywords.BOOLEAN;
					case "true" -> JavaKeywords.TRUE;
					case "false" -> JavaKeywords.FALSE;
					default -> new JavaID(s);
				});
	}

	private Optional<JavaToken> parseIntegerLiteral() {
		BigInteger x = BigInteger.ZERO;

		if (i < v.length - 1 && v[i] == '0' && v[i + 1] == 'x') {
			// hexadecimal literal
			i += 2;
			while (i < v.length
					&& (Character.isDigit(v[i])
							|| v[i] == 'a'
							|| v[i] == 'b'
							|| v[i] == 'c'
							|| v[i] == 'd'
							|| v[i] == 'e'
							|| v[i] == 'f')) {
				x = x.shiftLeft(4).add(new BigInteger("" + v[i], 16));
				i++;
			}
		} else if (i < v.length - 1 && v[i] == '0' && v[i + 1] == 'b') {
			// binary literal
			i += 2;
			while (i < v.length && (v[i] == '0' || v[i] == '1')) {
				x = x.shiftLeft(1).add((v[i] == '1') ? BigInteger.ONE : BigInteger.ZERO);
				i++;
			}
		} else {
			// base-10 literal
			while (i < v.length && (Character.isDigit(v[i]) || v[i] == '_')) {
				if (Character.isDigit(v[i])) {
					x = x.multiply(BigInteger.TEN).add(new BigInteger("" + v[i]));
				}
				i++;
			}
		}

		if (i < v.length && v[i] == 'L') {
			i++;
			return Optional.of(new IntegerLiteral(x, true));
		}

		return Optional.of(new IntegerLiteral(x));
	}

	private void skipBlanksAndComments() {
		boolean again;
		do {
			again = false;
			if (isBlank()) {
				skipBlanks();
				again = true;
			}
			if (isLineComment()) {
				skipLineComment();
				again = true;
			}
			if (isBlockComment()) {
				skipBlockComment();
				again = true;
			}
		} while (again);
	}

	private boolean isBlank() {
		return i < v.length && Character.isWhitespace(v[i]);
	}

	private void skipBlanks() {
		while (i < v.length && Character.isWhitespace(v[i])) {
			i++;
		}
	}

	private boolean isLineComment() {
		return i < v.length - 1 && v[i] == '/' && v[i + 1] == '/';
	}

	private void skipLineComment() {
		i += 2;
		while (v[i] != '\n') {
			i++;
		}
		i++;
	}

	private boolean isBlockComment() {
		return i < v.length - 1 && v[i] == '/' && v[i + 1] == '*';
	}

	private void skipBlockComment() {
		i += 2;
		while (v[i] != '*' || v[i + 1] != '/') {
			i++;
		}
		i += 2;
	}
}
