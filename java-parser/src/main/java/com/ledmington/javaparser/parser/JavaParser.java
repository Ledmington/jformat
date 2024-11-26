package com.ledmington.javaparser.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ledmington.javaparser.lexer.IntegerLiteral;
import com.ledmington.javaparser.lexer.JavaID;
import com.ledmington.javaparser.lexer.JavaKeywords;
import com.ledmington.javaparser.lexer.JavaLexer;
import com.ledmington.javaparser.lexer.JavaSymbols;
import com.ledmington.javaparser.lexer.JavaToken;
import com.ledmington.javaparser.parser.expr.BracketNode;
import com.ledmington.javaparser.parser.expr.ConstantNode;
import com.ledmington.javaparser.parser.expr.DivideNode;
import com.ledmington.javaparser.parser.expr.ExpressionNode;
import com.ledmington.javaparser.parser.expr.MinusNode;
import com.ledmington.javaparser.parser.expr.MultiplyNode;
import com.ledmington.javaparser.parser.expr.PlusNode;
import com.ledmington.javaparser.parser.expr.VariableReferenceExpression;

public final class JavaParser {

	private static final JavaID DEFAULT_PACKAGE = new JavaID("<package_name>");
	private static final JavaID DEFAULT_CLASS = new JavaID("<class_name>");

	private JavaParser() {
	}

	public static JavaNode parse(final String code) {
		return parse(new TokenIterator(JavaLexer.tokenize(Objects.requireNonNull(code))));
	}

	private static void expect(final TokenIterator it, final JavaToken expected) {
		if (!it.hasNext()) {
			throw new TooFewTokensException(expected);
		}
		if (!it.current().equals(expected)) {
			throw new UnexpectedTokenException(expected, it.current());
		}
		it.move();
	}

	private static JavaNode parse(final TokenIterator it) {
		if (!it.hasNext()) {
			throw new TooFewTokensException(JavaKeywords.PACKAGE, JavaKeywords.IMPORT, JavaKeywords.CLASS);
		}

		final PackageDeclarationNode packageDeclaration;
		if (it.current() == JavaKeywords.PACKAGE) {
			it.move();
			packageDeclaration = parsePackageDeclaration(it);
		} else {
			packageDeclaration = PackageDeclarationNode.DEFAULT_PACKAGE;
		}

		final List<ImportDeclarationNode> imports = new ArrayList<>();
		while (it.hasNext() && it.current() == JavaKeywords.IMPORT) {
			it.move();
			final ImportDeclarationNode imp = parseImportDeclaration(it);
			imports.add(imp);
		}

		if (!it.hasNext()) {
			throw new TooFewTokensException(JavaKeywords.CLASS);
		}

		final List<AccessModifier> modifiers = parseAccessModifiers(it);

		if (it.hasNext()) {
			expect(it, JavaKeywords.CLASS);

			return parseClassDeclaration(it, packageDeclaration, imports, modifiers);
		}

		throw new TooFewTokensException(JavaKeywords.CLASS);
	}

	private static PackageDeclarationNode parsePackageDeclaration(final TokenIterator it) {
		final List<String> packageNames = new ArrayList<>();

		if (it.current() instanceof JavaID(String id)) {
			packageNames.add(id);
			it.move();
		} else {
			throw new UnexpectedTokenException(DEFAULT_PACKAGE, it.current());
		}

		while (it.hasNext(2) && it.current() == JavaSymbols.DOT && it.next() instanceof JavaID(String id2)) {
			packageNames.add(id2);
			it.move();
			it.move();
		}

		expect(it, JavaSymbols.SEMICOLON);

		return new PackageDeclarationNode(packageNames);
	}

	private static ImportDeclarationNode parseImportDeclaration(final TokenIterator it) {
		final List<String> packageNames = new ArrayList<>();

		if (it.current() instanceof JavaID(String id)) {
			packageNames.add(id);
			it.move();
		} else {
			throw new UnexpectedTokenException(DEFAULT_PACKAGE, it.current());
		}

		while (it.hasNext(2) && it.current() == JavaSymbols.DOT && it.next() instanceof JavaID(String id2)) {
			packageNames.add(id2);
			it.move();
			it.move();
		}

		expect(it, JavaSymbols.SEMICOLON);

		return new ImportDeclarationNode(packageNames);
	}

	private static List<AccessModifier> parseAccessModifiers(final TokenIterator it) {
		final List<AccessModifier> m = new ArrayList<>();

		while (it.hasNext()) {
			switch (it.current()) {
				case JavaKeywords.PUBLIC -> m.add(AccessModifier.PUBLIC);
				case JavaKeywords.PRIVATE -> m.add(AccessModifier.PRIVATE);
				case JavaKeywords.PROTECTED -> m.add(AccessModifier.PROTECTED);
				case JavaKeywords.ABSTRACT -> m.add(AccessModifier.ABSTRACT);
				case JavaKeywords.STATIC -> m.add(AccessModifier.STATIC);
				case JavaKeywords.FINAL -> m.add(AccessModifier.FINAL);
				case JavaKeywords.VOLATILE -> m.add(AccessModifier.VOLATILE);
				case JavaKeywords.SYNCHRONIZED -> m.add(AccessModifier.SYNCHRONIZED);
				case JavaKeywords.STRICTFP -> m.add(AccessModifier.STRICTFP);
				case JavaKeywords.TRANSIENT -> m.add(AccessModifier.TRANSIENT);
				case JavaKeywords.NATIVE -> m.add(AccessModifier.NATIVE);
				default -> {
					return m;
				}
			}
			it.move();
		}

		return m;
	}

	private static JavaNode parseClassDeclaration(
			final TokenIterator it, final PackageDeclarationNode packageDeclaration,
			final List<ImportDeclarationNode> imports,
			final List<AccessModifier> modifiers) {
		if (!it.hasNext()) {
			throw new TooFewTokensException(DEFAULT_CLASS);
		}

		final String className;
		if (it.current() instanceof JavaID(String thisClassName)) {
			className = thisClassName;
		} else {
			throw new UnexpectedTokenException(DEFAULT_CLASS, it.current());
		}

		it.move();

		final String superClassName;
		if (it.hasNext(2) && it.current() == JavaKeywords.EXTENDS && it.next() instanceof JavaID(String superClass)) {
			superClassName = superClass;
			it.move();
			it.move();
		} else {
			superClassName = null;
		}

		final List<String> interfaces = new ArrayList<>();
		if (it.hasNext() && it.current() == JavaKeywords.IMPLEMENTS) {
			do {
				it.move();
				interfaces.add(((JavaID) it.current()).id());
				it.move();
			} while (it.hasNext() && it.current() == JavaSymbols.COMMA);
		}

		expect(it, JavaSymbols.LEFT_CURLY_BRACKET);

		final List<FieldDeclarationNode> fields = new ArrayList<>();
		final List<MethodDeclarationNode> methods = new ArrayList<>();
		while (it.hasNext() && it.current() != JavaSymbols.RIGHT_CURLY_BRACKET) {
			final List<AccessModifier> mods = parseAccessModifiers(it);
			final TypeNode type = parseType(it);

			final String name;
			if (it.current() instanceof JavaID(String id)) {
				name = id;
			} else {
				throw new UnexpectedTokenException(new JavaID("<field_name>"), it.current());
			}
			it.move();

			if (it.current() == JavaSymbols.LEFT_BRACKET) {
				// it's a method
				methods.add(parseMethodDeclaration(it, mods, type, name));
			} else {
				// it's a field
				fields.add(parseFieldDeclaration(it, mods, type, name));
			}
		}

		it.move();

		return new JavaClassDeclaration(
				packageDeclaration, imports, modifiers, className, superClassName, interfaces, fields, methods);
	}

	private static FieldDeclarationNode parseFieldDeclaration(final TokenIterator it,
			final List<AccessModifier> mods, final TypeNode type, final String name) {
		if (!it.hasNext()) {
			throw new TooFewTokensException(JavaSymbols.EQUAL, JavaSymbols.SEMICOLON);
		}

		if (it.current() == JavaSymbols.SEMICOLON) {
			it.move();
			// fields declaration without initializer
			return new FieldDeclarationNode(mods, type, name);
		}

		expect(it, JavaSymbols.EQUAL);

		final ExpressionNode expression = parseExpression(it);

		expect(it, JavaSymbols.SEMICOLON);

		return new FieldDeclarationNode(mods, type, name, expression);
	}

	private static MethodDeclarationNode parseMethodDeclaration(final TokenIterator it,
			final List<AccessModifier> mods, final TypeNode type, final String name) {

		expect(it, JavaSymbols.LEFT_BRACKET);

		final List<MethodParameterDeclaration> parameters = new ArrayList<>();
		if (it.current() != JavaSymbols.RIGHT_BRACKET) {
			parameters.add(parseMethodParameter(it));
			while (it.hasNext() && it.current() == JavaSymbols.COMMA) {
				it.move();
				parameters.add(parseMethodParameter(it));
				// it.move();
			}
		}

		expect(it, JavaSymbols.RIGHT_BRACKET);

		if (it.current() == JavaSymbols.SEMICOLON) {
			it.move();
			return new MethodDeclarationNode(mods, type, name, parameters, null);
		}

		expect(it, JavaSymbols.LEFT_CURLY_BRACKET);

		final List<StatementNode> body = new ArrayList<>();
		while (it.hasNext() && it.current() != JavaSymbols.RIGHT_CURLY_BRACKET) {
			final StatementNode s = parseStatement(it);
			if (s == null) {
				throw new IllegalArgumentException("Parsed statement was null");
			}
			body.add(s);
		}

		expect(it, JavaSymbols.RIGHT_CURLY_BRACKET);

		return new MethodDeclarationNode(mods, type, name, parameters, body);
	}

	private static MethodParameterDeclaration parseMethodParameter(final TokenIterator it) {
		boolean isFinal = false;
		if (it.current() == JavaKeywords.FINAL) {
			isFinal = true;
			it.move();
			;
		}

		final TypeNode type = parseType(it);

		final String name = ((JavaID) it.current()).id();
		it.move();

		return new MethodParameterDeclaration(isFinal, type, name);
	}

	private static StatementNode parseStatement(final TokenIterator it) {
		if (it.current() == JavaKeywords.FINAL) {
			it.move();
			final TypeNode type = parseType(it);
			final String name = ((JavaID) it.current()).id();
			it.move();
			if (it.current() == JavaSymbols.EQUAL) {
				it.move();
				final ExpressionNode expr = parseExpression(it);
				expect(it, JavaSymbols.SEMICOLON);
				return new VariableDeclaration(true, type, name, expr);
			}
			expect(it, JavaSymbols.SEMICOLON);
			return new VariableDeclaration(true, type, name, null);
		} else if (it.current() == JavaKeywords.RETURN) {
			it.move();
			final ExpressionNode expr = parseExpression(it);
			expect(it, JavaSymbols.SEMICOLON);
			return new ReturnStatement(expr);
		} else if (it.current() == JavaSymbols.SEMICOLON) {
			it.move();
			return new EmptyStatement();
		} else if (it.current() instanceof JavaID(String id)) {
			// method invocation
			it.move();
			ExpressionNode expr = new VariableReferenceExpression(id);
			while (it.current() == JavaSymbols.DOT && it.next() instanceof JavaID(String id1)) {
				it.move();
				it.move();
				if (it.current() == JavaSymbols.LEFT_BRACKET) {
					// method invocation
					it.move();
					final List<ExpressionNode> callParameters = new ArrayList<>();
					callParameters.add(parseExpression(it));
					while (it.current() == JavaSymbols.COMMA) {
						it.move();
						callParameters.add(parseExpression(it));
					}
					expect(it, JavaSymbols.RIGHT_BRACKET);
					expr = new MethodInvocationExpression(expr, id1, callParameters);
				} else {
					// field access
					expr = new FieldAccessExpression(expr, id1);
				}
			}

			if (expr instanceof FieldAccessExpression fae) {
				expect(it, JavaSymbols.EQUAL);
				final ExpressionNode value = parseExpression(it);
				expect(it, JavaSymbols.SEMICOLON);
				return new FieldAssignment(fae, value);
			} else if (expr instanceof MethodInvocationExpression mie) {
				expect(it, JavaSymbols.SEMICOLON);
				return new MethodInvocationStatement(mie);
			}
		}

		return null;
	}

	private static TypeNode parseType(final TokenIterator it) {
		if (!it.hasNext()) {
			throw new TooFewTokensException(JavaKeywords.VOID, JavaKeywords.BYTE, JavaKeywords.CHAR, JavaKeywords.SHORT,
					JavaKeywords.INT, JavaKeywords.FLOAT, JavaKeywords.LONG, JavaKeywords.DOUBLE);
		}

		TypeNode type = switch (it.current()) {
			case JavaKeywords.VOID -> {
				it.move();
				yield PrimitiveTypes.VOID;
			}
			case JavaKeywords.BYTE -> {
				it.move();
				yield PrimitiveTypes.BYTE;
			}
			case JavaKeywords.CHAR -> {
				it.move();
				yield PrimitiveTypes.CHAR;
			}
			case JavaKeywords.SHORT -> {
				it.move();
				yield PrimitiveTypes.SHORT;
			}
			case JavaKeywords.INT -> {
				it.move();
				yield PrimitiveTypes.INT;
			}
			case JavaKeywords.FLOAT -> {
				it.move();
				yield PrimitiveTypes.FLOAT;
			}
			case JavaKeywords.LONG -> {
				it.move();
				yield PrimitiveTypes.LONG;
			}
			case JavaKeywords.DOUBLE -> {
				it.move();
				yield PrimitiveTypes.DOUBLE;
			}
			default -> parseReferenceType(it);
		};

		while (it.current() == JavaSymbols.LEFT_SQUARE_BRACKET) {
			it.move();
			expect(it, JavaSymbols.RIGHT_SQUARE_BRACKET);
			type = new ArrayType(type);
		}

		return type;
	}

	private static ReferenceTypeNode parseReferenceType(final TokenIterator it) {
		final String className;
		if (it.current() instanceof JavaID(String referenceType)) {
			className = referenceType;
		} else {
			throw new UnexpectedTokenException(new JavaID("<type_name>"), it.current());
		}
		it.move();

		if (it.hasNext() && it.current() == JavaSymbols.LEFT_ANGLE_BRACKET) {
			it.move();

			final List<ReferenceTypeNode> innerTypes = new ArrayList<>();
			innerTypes.add(parseReferenceType(it));
			while (it.hasNext() && it.current() == JavaSymbols.COMMA) {
				it.move();
				innerTypes.add(parseReferenceType(it));
			}

			expect(it, JavaSymbols.RIGHT_ANGLE_BRACKET);

			return new GenericReferenceTypeNode(className, innerTypes);
		}

		return new ReferenceTypeNode(className);
	}

	private static ExpressionNode parseExpression(final TokenIterator it) {
		if (!it.hasNext()) {
			throw new TooFewTokensException(JavaSymbols.PLUS, JavaSymbols.MINUS, JavaSymbols.ASTERISK,
					JavaSymbols.FORWARD_SLASH);
		}

		ExpressionNode expr = parseMinusExpression(it);

		while (it.current() == JavaSymbols.PLUS) {
			it.move();
			final ExpressionNode rhs = parseExpression(it);
			expr = new PlusNode(expr, rhs);
		}

		return expr;
	}

	private static ExpressionNode parseMinusExpression(final TokenIterator it) {
		ExpressionNode expr = parseMultiplyExpression(it);

		while (it.current() == JavaSymbols.MINUS) {
			it.move();
			final ExpressionNode rhs = parseExpression(it);
			expr = new MinusNode(expr, rhs);
		}

		return expr;
	}

	private static ExpressionNode parseMultiplyExpression(final TokenIterator it) {
		ExpressionNode expr = parseDivideExpression(it);

		while (it.current() == JavaSymbols.ASTERISK) {
			it.move();
			final ExpressionNode rhs = parseExpression(it);
			expr = new MultiplyNode(expr, rhs);
		}

		return expr;
	}

	private static ExpressionNode parseDivideExpression(final TokenIterator it) {
		ExpressionNode expr = parseSimpleExpression(it);

		while (it.current() == JavaSymbols.FORWARD_SLASH) {
			it.move();
			final ExpressionNode rhs = parseExpression(it);
			expr = new DivideNode(expr, rhs);
		}

		return expr;
	}

	private static ExpressionNode parseSimpleExpression(final TokenIterator it) {
		if (it.current() instanceof IntegerLiteral il) {
			it.move();
			return new ConstantNode(il.value(), il.declaredAsLong());
		} else if (it.current() instanceof JavaID(String id)) {
			it.move();
			return new VariableReferenceExpression(id);
		} else if (it.current() == JavaSymbols.LEFT_BRACKET) {
			it.move();
			final ExpressionNode expr = parseExpression(it);
			expect(it, JavaSymbols.RIGHT_BRACKET);
			return new BracketNode(expr);
		}
		return parseExpression(it);
	}
}
