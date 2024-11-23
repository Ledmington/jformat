package com.ledmington.jformat;

import com.ledmington.javaparser.parser.JavaNode;
import com.ledmington.javaparser.parser.JavaParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public final class Main {
	public static void main(final String[] args) {
		final List<Path> sources;
		try(final Stream<Path> s = Files.find(Path.of(args[0]).normalize().toAbsolutePath(),999, (p, bfa) -> bfa.isRegularFile() && p.normalize().toAbsolutePath().toString().endsWith(".java"))) {
			sources = s.distinct().sorted().toList();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		System.out.printf("Collected %,d files\n", sources.size());

		for(final Path p : sources) {
			System.out.printf("Scanning '%s'\n", p);
			final JavaParser parser = new JavaParser(p.toString());
			for(Optional<JavaNode> res = parser.next(); res.isPresent(); res = parser.next()) {
				System.out.println(res.orElseThrow());
			}
		}
	}
}