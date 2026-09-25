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
package com.ledmington.jformat;

import com.ledmington.jformat.ast.ClassDeclaration;
import com.ledmington.jformat.ast.Node;
import com.ledmington.jformat.ast.SourceFile;
import com.ledmington.jformat.gen.JavaParser.source_file;

public final class Converter {

	private Converter() {}

	public static Node convert(final com.ledmington.jformat.gen.JavaParser.Node raw) {
		if (raw instanceof final source_file sf) {
			return convertSourceFile(sf);
		}
		throw new IllegalArgumentException(String.format("Expected a prog node but was '%s'.", raw));
	}

	private static SourceFile convertSourceFile(final com.ledmington.jformat.gen.JavaParser.source_file sf) {
		return new SourceFile(new ClassDeclaration(sf.inner().n1().literal()));
	}
}
