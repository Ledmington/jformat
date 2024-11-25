package com.ledmington.javaparser.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

	private final JavaToken[] tokens;
	private int i = 0;

	public JavaParser(final String code) {
		this.tokens = JavaLexer.tokenize(Objects.requireNonNull(code)).toArray(new JavaToken[0]);
	}

	public Optional<JavaNode> next() {
		if (i >= tokens.length) {
			return Optional.empty();
		}

		final PackageDeclarationNode packageDeclaration;
		if (tokens[i] == JavaKeywords.PACKAGE) {
			i++;
			packageDeclaration = parsePackageDeclaration();
		} else {
			packageDeclaration = PackageDeclarationNode.DEFAULT_PACKAGE;
		}

		final List<ImportDeclarationNode> imports = new ArrayList<>();
		while (i < tokens.length && tokens[i] == JavaKeywords.IMPORT) {
			i++;
			final ImportDeclarationNode imp = parseImportDeclaration();
			imports.add(imp);
		}

		if (i >= tokens.length) {
			throw new TooFewTokensException(
					List.of(tokens[tokens.length - 3], tokens[tokens.length - 2], tokens[tokens.length - 1]));
		}

		final List<AccessModifier> modifiers = parseAccessModifiers();

		if (i < tokens.length) {
			if (tokens[i] != JavaKeywords.CLASS) {
				throw new UnexpectedTokenException(tokens[i]);
			}
			i++;

			return Optional.of(parseClassDeclaration(packageDeclaration, imports, modifiers));
		}

		throw new TooFewTokensException(
				List.of(tokens[tokens.length - 3], tokens[tokens.length - 2], tokens[tokens.length - 1]));
	}

	private void expect(final JavaToken expected) {
		if (!tokens[i].equals(expected)) {
			throw new UnexpectedTokenException(expected, tokens[i]);
		}
		i++;
	}

	private PackageDeclarationNode parsePackageDeclaration() {
		final List<String> packageNames = new ArrayList<>();

		if(tokens[i] instanceof JavaID(String id)) {
			packageNames.add(id);
			i++;
		}else {
			throw new UnexpectedTokenException(tokens[i], new JavaID("<package_name>"));
		}

		while (i < tokens.length - 1 && tokens[i] == JavaSymbols.DOT && tokens[i + 1] instanceof JavaID(String id2)) {
			packageNames.add(id2);
			i += 2;
		}

		expect(JavaSymbols.SEMICOLON);

		return new PackageDeclarationNode(packageNames);
	}

	private ImportDeclarationNode parseImportDeclaration() {
		final List<String> packageNames = new ArrayList<>();

		if (!(tokens[i] instanceof JavaID)) {
			throw new UnexpectedTokenException(new JavaID("<package_name>"), tokens[i]);
		}

		packageNames.add(((JavaID) tokens[i]).id());
		i++;

		while (i < tokens.length - 1 && tokens[i] == JavaSymbols.DOT && tokens[i + 1] instanceof JavaID(String id)) {
			packageNames.add(id);
			i += 2;
		}

		expect(JavaSymbols.SEMICOLON);

		return new ImportDeclarationNode(packageNames);
	}

	private List<AccessModifier> parseAccessModifiers() {
		final List<AccessModifier> m = new ArrayList<>();

		while (i < tokens.length
				&& (tokens[i] == JavaKeywords.PUBLIC
						|| tokens[i] == JavaKeywords.PRIVATE
						|| tokens[i] == JavaKeywords.PROTECTED
						|| tokens[i] == JavaKeywords.ABSTRACT
						|| tokens[i] == JavaKeywords.STATIC
						|| tokens[i] == JavaKeywords.FINAL
						|| tokens[i] == JavaKeywords.VOLATILE
						|| tokens[i] == JavaKeywords.SYNCHRONIZED
						|| tokens[i] == JavaKeywords.STRICTFP
						|| tokens[i] == JavaKeywords.TRANSIENT
						|| tokens[i] == JavaKeywords.NATIVE)) {
			m.add(
					switch (tokens[i]) {
						case JavaKeywords.PUBLIC -> AccessModifier.PUBLIC;
						case JavaKeywords.PRIVATE -> AccessModifier.PRIVATE;
						case JavaKeywords.PROTECTED -> AccessModifier.PROTECTED;
						case JavaKeywords.ABSTRACT -> AccessModifier.ABSTRACT;
						case JavaKeywords.STATIC -> AccessModifier.STATIC;
						case JavaKeywords.FINAL -> AccessModifier.FINAL;
						case JavaKeywords.VOLATILE -> AccessModifier.VOLATILE;
						case JavaKeywords.SYNCHRONIZED -> AccessModifier.SYNCHRONIZED;
						case JavaKeywords.STRICTFP -> AccessModifier.STRICTFP;
						case JavaKeywords.TRANSIENT -> AccessModifier.TRANSIENT;
						case JavaKeywords.NATIVE -> AccessModifier.NATIVE;
						default -> throw new UnexpectedTokenException(tokens[i]);
					});
			i++;
		}

		return m;
	}

	private JavaNode parseClassDeclaration(
			final PackageDeclarationNode packageDeclaration,
			final List<ImportDeclarationNode> imports,
			final List<AccessModifier> modifiers) {
		if (!(tokens[i] instanceof JavaID)) {
			throw new UnexpectedTokenException(new JavaID("<class_name>"), tokens[i]);
		}

		final String className = ((JavaID) tokens[i]).id();
		i++;

		final String superClassName;
		if (tokens[i] == JavaKeywords.EXTENDS) {
			i++;
			superClassName = ((JavaID) tokens[i]).id();
			i++;
		} else {
			superClassName = null;
		}

		final List<String> interfaces = new ArrayList<>();
		if (tokens[i] == JavaKeywords.IMPLEMENTS) {
			do {
				i++;
				interfaces.add(((JavaID) tokens[i]).id());
				i++;
			} while (i < tokens.length && tokens[i] == JavaSymbols.COMMA);
		}

		expect(JavaSymbols.LEFT_CURLY_BRACKET);

		final List<FieldDeclarationNode> fields = new ArrayList<>();
		final List<MethodDeclarationNode> methods = new ArrayList<>();
		while (i < tokens.length && tokens[i] != JavaSymbols.RIGHT_CURLY_BRACKET) {
			final List<AccessModifier> mods = parseAccessModifiers();
			final TypeNode type = parseType();

			if (!(tokens[i] instanceof JavaID)) {
				throw new UnexpectedTokenException(new JavaID("<field_name>"), tokens[i]);
			}
			final String name = ((JavaID) tokens[i]).id();
			i++;

			if (tokens[i] == JavaSymbols.LEFT_BRACKET) {
				// it's a method
				methods.add(parseMethodDeclaration(mods, type, name));
			} else {
				// it's a field
				fields.add(parseFieldDeclaration(mods, type, name));
			}
		}

		i++;

		return new JavaClassDeclaration(
				packageDeclaration, imports, modifiers, className, superClassName, interfaces, fields, methods);
	}

	private FieldDeclarationNode parseFieldDeclaration(
			final List<AccessModifier> mods, final TypeNode type, final String name) {
		if (i >= tokens.length) {
			throw new TooFewTokensException(
					List.of(tokens[tokens.length - 3], tokens[tokens.length - 2], tokens[tokens.length - 1]));
		}

		if (tokens[i] == JavaSymbols.SEMICOLON) {
			i++;
			// fields declaration without initializer
			return new FieldDeclarationNode(mods, type, name);
		}

		expect(JavaSymbols.EQUAL);

		final ExpressionNode expression = parseExpression();

		expect(JavaSymbols.SEMICOLON);

		return new FieldDeclarationNode(mods, type, name, expression);
	}

	private MethodDeclarationNode parseMethodDeclaration(
			final List<AccessModifier> mods, final TypeNode type, final String name) {

		expect(JavaSymbols.LEFT_BRACKET);

		final List<MethodParameterDeclaration> parameters = new ArrayList<>();
		if (tokens[i] != JavaSymbols.RIGHT_BRACKET) {
			parameters.add(parseMethodParameter());
			while (i < tokens.length && tokens[i] == JavaSymbols.COMMA) {
				i++;
				parameters.add(parseMethodParameter());
				//  i++;
			}
		}

		expect(JavaSymbols.RIGHT_BRACKET);

		if (tokens[i] == JavaSymbols.SEMICOLON) {
			i++;
			return new MethodDeclarationNode(mods, type, name, parameters, null);
		}

		expect(JavaSymbols.LEFT_CURLY_BRACKET);

		final List<StatementNode> body = new ArrayList<>();
		while (i < tokens.length && tokens[i] != JavaSymbols.RIGHT_CURLY_BRACKET) {
			final StatementNode s = parseStatement();
			if (s == null) {
				throw new IllegalArgumentException("Parsed statement was null");
			}
			body.add(s);
		}

		expect(JavaSymbols.RIGHT_CURLY_BRACKET);

		return new MethodDeclarationNode(mods, type, name, parameters, body);
	}

	private MethodParameterDeclaration parseMethodParameter() {
		boolean isFinal = false;
		if (tokens[i] == JavaKeywords.FINAL) {
			isFinal = true;
			i++;
		}

		final TypeNode type = parseType();

		final String name = ((JavaID) tokens[i]).id();
		i++;

		return new MethodParameterDeclaration(isFinal, type, name);
	}

	private StatementNode parseStatement() {
		if (tokens[i] == JavaKeywords.FINAL) {
			i++;
			final TypeNode type = parseType();
			final String name = ((JavaID) tokens[i]).id();
			i++;
			if (tokens[i] == JavaSymbols.EQUAL) {
				i++;
				final ExpressionNode expr = parseExpression();
				expect(JavaSymbols.SEMICOLON);
				return new VariableDeclaration(true, type, name, expr);
			}
			expect(JavaSymbols.SEMICOLON);
			return new VariableDeclaration(true, type, name, null);
		} else if (tokens[i] == JavaKeywords.RETURN) {
			i++;
			final ExpressionNode expr = parseExpression();
			expect(JavaSymbols.SEMICOLON);
			return new ReturnStatement(expr);
		} else if (tokens[i] == JavaSymbols.SEMICOLON) {
			i++;
			return new EmptyStatement();
		} else if (tokens[i] instanceof JavaID(String id)) {
			// method invocation
			i++;
			ExpressionNode expr = new VariableReferenceExpression(id);
			while (tokens[i] == JavaSymbols.DOT && tokens[i + 1] instanceof JavaID(String id1)) {
				i += 2;
				if (tokens[i] == JavaSymbols.LEFT_BRACKET) {
					// method invocation
					i++;
					final List<ExpressionNode> callParameters = new ArrayList<>();
					callParameters.add(parseExpression());
					while (tokens[i] == JavaSymbols.COMMA) {
						i++;
						callParameters.add(parseExpression());
					}
					expect(JavaSymbols.RIGHT_BRACKET);
					expr = new MethodInvocationExpression(expr, id1, callParameters);
				} else {
					// field access
					expr = new FieldAccessExpression(expr, id1);
				}
			}

			if (expr instanceof FieldAccessExpression fae) {
				expect(JavaSymbols.EQUAL);
				final ExpressionNode value = parseExpression();
				expect(JavaSymbols.SEMICOLON);
				return new FieldAssignment(fae, value);
			} else if (expr instanceof MethodInvocationExpression mie) {
				expect(JavaSymbols.SEMICOLON);
				return new MethodInvocationStatement(mie);
			}
		}

		return null;
	}

	private TypeNode parseType() {
		if (i >= tokens.length) {
			throw new TooFewTokensException(
					List.of(tokens[tokens.length - 3], tokens[tokens.length - 2], tokens[tokens.length - 1]));
		}

		TypeNode type =
				switch (tokens[i]) {
					case JavaKeywords.VOID -> {
						i++;
						yield PrimitiveTypes.VOID;
					}
					case JavaKeywords.BYTE -> {
						i++;
						yield PrimitiveTypes.BYTE;
					}
					case JavaKeywords.CHAR -> {
						i++;
						yield PrimitiveTypes.CHAR;
					}
					case JavaKeywords.SHORT -> {
						i++;
						yield PrimitiveTypes.SHORT;
					}
					case JavaKeywords.INT -> {
						i++;
						yield PrimitiveTypes.INT;
					}
					case JavaKeywords.FLOAT -> {
						i++;
						yield PrimitiveTypes.FLOAT;
					}
					case JavaKeywords.LONG -> {
						i++;
						yield PrimitiveTypes.LONG;
					}
					case JavaKeywords.DOUBLE -> {
						i++;
						yield PrimitiveTypes.DOUBLE;
					}
					default -> parseReferenceType();
				};

		while (tokens[i] == JavaSymbols.LEFT_SQUARE_BRACKET) {
			i++;
			expect(JavaSymbols.RIGHT_SQUARE_BRACKET);
			type = new ArrayType(type);
		}

		return type;
	}

	private ReferenceTypeNode parseReferenceType() {
		if (!(tokens[i] instanceof JavaID)) {
			throw new UnexpectedTokenException(new JavaID("<type_name>"), tokens[i]);
		}

		final String className = ((JavaID) tokens[i]).id();
		i++;

		if (i < tokens.length && tokens[i] == JavaSymbols.LEFT_ANGLE_BRACKET) {
			i++;

			final List<ReferenceTypeNode> innerTypes = new ArrayList<>();
			innerTypes.add(parseReferenceType());
			while (i < tokens.length && tokens[i] == JavaSymbols.COMMA) {
				i++;
				innerTypes.add(parseReferenceType());
			}

			expect(JavaSymbols.RIGHT_ANGLE_BRACKET);

			return new GenericReferenceTypeNode(className, innerTypes);
		}

		return new ReferenceTypeNode(className);
	}

	private ExpressionNode parseExpression() {
		if (i >= tokens.length) {
			throw new TooFewTokensException(
					List.of(tokens[tokens.length - 3], tokens[tokens.length - 2], tokens[tokens.length - 1]));
		}

		ExpressionNode expr = parseMinusExpression();

		while (tokens[i] == JavaSymbols.PLUS) {
			i++;
			final ExpressionNode rhs = parseExpression();
			expr = new PlusNode(expr, rhs);
		}

		return expr;
	}

	private ExpressionNode parseMinusExpression() {
		ExpressionNode expr = parseMultiplyExpression();

		while (tokens[i] == JavaSymbols.MINUS) {
			i++;
			final ExpressionNode rhs = parseExpression();
			expr = new MinusNode(expr, rhs);
		}

		return expr;
	}

	private ExpressionNode parseMultiplyExpression() {
		ExpressionNode expr = parseDivideExpression();

		while (tokens[i] == JavaSymbols.ASTERISK) {
			i++;
			final ExpressionNode rhs = parseExpression();
			expr = new MultiplyNode(expr, rhs);
		}

		return expr;
	}

	private ExpressionNode parseDivideExpression() {
		ExpressionNode expr = parseSimpleExpression();

		while (tokens[i] == JavaSymbols.FORWARD_SLASH) {
			i++;
			final ExpressionNode rhs = parseExpression();
			expr = new DivideNode(expr, rhs);
		}

		return expr;
	}

	private ExpressionNode parseSimpleExpression() {
		if (tokens[i] instanceof IntegerLiteral il) {
			i++;
			return new ConstantNode(il.value(), il.declaredAsLong());
		} else if (tokens[i] instanceof JavaID(String id)) {
			i++;
			return new VariableReferenceExpression(id);
		} else if (tokens[i] == JavaSymbols.LEFT_BRACKET) {
			i++;
			final ExpressionNode expr = parseExpression();
			expect(JavaSymbols.RIGHT_BRACKET);
			return new BracketNode(expr);
		}
		return parseExpression();
	}
}
