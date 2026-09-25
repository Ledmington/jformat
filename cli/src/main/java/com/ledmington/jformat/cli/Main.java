/*
 * jformat - Java source code formatter
 * Copyright (C) 2026-2026 Filippo Barbari <filippo.barbari@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ledmington.jformat.cli;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.ledmington.jformat.Formatter;

public final class Main {

	private static String readFile(final String filename) {
		try {
			return Files.readString(Path.of(filename));
		} catch (final IOException e) {
			System.err.printf("ERROR: could not read file '%s' because of '%s'.%n", filename, e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private static void writeFile(final String filename, final String content) {
		try {
			Files.writeString(Path.of(filename), content, StandardCharsets.UTF_8, StandardOpenOption.WRITE);
		} catch (final IOException e) {
			System.err.printf("ERROR: could not write to file '%s' because of '%s'.%n", filename, e.getMessage());
			throw new RuntimeException(e);
		}
	}

	// FIXME: remove this warning suppression
	@SuppressWarnings("PMD.AvoidLiteralsInIfCondition")
	public static void main(final String[] args) {
		if (args.length < 1) {
			System.err.println("Name of the input java source file needed but not provided.");
			System.exit(-1);
			return;
		}

		if (args.length < 2) {
			System.err.println("Name of the output java source file needed but not provided.");
			System.exit(-1);
			return;
		}

		final String inputFilename = args[0];
		final String outputFilename = args[1];

		final String original = readFile(inputFilename);
		final String formatted = Formatter.format(original);

		writeFile(outputFilename, formatted);
	}
}
