package com.ledmington.jformat;

import com.ledmington.javaparser.parser.JavaParser;
import com.ledmington.parser.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public final class Main {

	private static final Parser idDeclaration = Parser.builder().build();
	private static final Parser importDeclaration = Parser.builder().one("import ").one(idDeclaration).zeroOrMore(Parser.builder().one('.').one(idDeclaration).build()).one(";").build();

	public static void main(final String[] args) {
		final List<Path> sources;
		try (final Stream<Path> s = Files.find(Path.of(args[0]).normalize().toAbsolutePath(), 999,
				(p, bfa) -> bfa.isRegularFile() && p.normalize().toAbsolutePath().toString().endsWith(".java"))) {
			sources = s.distinct().sorted().toList();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		System.out.printf("Collected %,d files\n", sources.size());

		for (final Path p : sources) {
			System.out.printf("Scanning '%s'\n", p);
			try {
				System.out.println(JavaParser.parse(Files.readString(p)));
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}