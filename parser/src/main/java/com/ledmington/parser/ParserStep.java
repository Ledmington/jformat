package com.ledmington.parser;

import java.util.Optional;
import java.util.function.Supplier;

public interface ParserStep extends Supplier<Optional<String>> {
	String toRegularExpression();
}