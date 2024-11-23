package com.ledmington.javaparser.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.ledmington.javaparser.parser.expr.ConstantNode;
import com.ledmington.javaparser.parser.expr.DivideNode;
import com.ledmington.javaparser.parser.expr.MinusNode;
import com.ledmington.javaparser.parser.expr.MultiplyNode;
import com.ledmington.javaparser.parser.expr.PlusNode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class TestJavaParser {

	@Test
	void minimal() {
		final String input = "class A{}";
		final JavaParser parser = new JavaParser(input);
		Optional<JavaNode> opt;
		opt = parser.next();
		assertTrue(opt.isPresent());
		assertEquals(
				new JavaClassDeclaration(
						PackageDeclarationNode.DEFAULT_PACKAGE,
						List.of(),
						List.of(),
						"A",
						null,
						List.of(),
						List.of(),
						List.of()),
				opt.orElseThrow());
		opt = parser.next();
		assertTrue(opt.isEmpty());
	}

	@Test
	void complexClass() {
		final String input = String.join(
				"\n",
				"package org.testing;",
				"import java.util.List;",
				"import java.util.ArrayList;",
				"private abstract class A extends Object implements Interface1,Interface2{",
				"int x=0;",
				"private float y;",
				"private void banana(){return 1+x+2;}",
				"public static final List<Map<String,List<Integer>>> myList=(3*5)+(7/(9-11));",
				"static abstract int mymethod();",
				"void m(int x, final char[] y){a();long z=\"ciao\";if('a'!=1.5f){}else{a.b[5].c(17,p);}}");
		final JavaParser parser = new JavaParser(input);
		Optional<JavaNode> opt;
		opt = parser.next();
		assertTrue(opt.isPresent());
		assertEquals(
				new JavaClassDeclaration(
						new PackageDeclarationNode("org.testing"),
						List.of(
								new ImportDeclarationNode("java.util.List"),
								new ImportDeclarationNode("java.util.ArrayList")),
						List.of(AccessModifier.PRIVATE, AccessModifier.ABSTRACT),
						"A",
						"Object",
						List.of("Interface1", "Interface2"),
						List.of(
								new FieldDeclarationNode(
										List.of(), PrimitiveTypes.INT, "x", new ConstantNode(BigInteger.ZERO, false)),
								new FieldDeclarationNode(List.of(AccessModifier.PRIVATE), PrimitiveTypes.FLOAT, "y"),
								new FieldDeclarationNode(
										List.of(AccessModifier.PUBLIC, AccessModifier.STATIC, AccessModifier.FINAL),
										new GenericReferenceTypeNode(
												"List",
												new GenericReferenceTypeNode(
														"Map",
														new ReferenceTypeNode("String"),
														new GenericReferenceTypeNode(
																"List", new ReferenceTypeNode("Integer")))),
										"myList",
										new PlusNode(
												new MultiplyNode(
														new ConstantNode(BigInteger.valueOf(3), false),
														new ConstantNode(BigInteger.valueOf(5), false)),
												new DivideNode(
														new ConstantNode(BigInteger.valueOf(7), false),
														new MinusNode(
																new ConstantNode(BigInteger.valueOf(9), false),
																new ConstantNode(BigInteger.valueOf(11), false)))))),
						List.of()),
				opt.orElseThrow());
		opt = parser.next();
		assertTrue(opt.isEmpty());
	}

	private static Stream<Arguments> wrongJavaSourceCode() {
		return Stream.of(
						// wrong package declarations
						"pack",
						"package;",
						"package 1;",
						// wrong import declarations
						"impo",
						"import;",
						"import 1;",
						// wrong class declarations
						"clazz",
						"class A}{")
				.map(Arguments::of);
	}

	@ParameterizedTest
	@MethodSource("wrongJavaSourceCode")
	void invalidParsing(final String sourceCode) {
		final JavaParser it = new JavaParser(sourceCode);

		try {
			while (it.next().isPresent()) {
				// nothing to do
			}
			// if we reach the end without exceptions we fail
			Assertions.fail();
		} catch (final UnexpectedTokenException e) {
			// ignored because it is what we expect
		}
	}
}
