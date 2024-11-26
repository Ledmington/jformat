package com.ledmington.javaparser.parser;

import java.util.List;
import java.util.Objects;

import com.ledmington.javaparser.lexer.JavaToken;

public final class TokenIterator {

    private final List<JavaToken> tokens;
    private int index = 0;

    public TokenIterator(final List<JavaToken> tokens) {
        this.tokens = Objects.requireNonNull(tokens);
    }

    public boolean hasNext(final int step) {
        if (step < 1) {
            throw new AssertionError();
        }
        return index < (tokens.size() - (step - 1));
    }

    public boolean hasNext() {
        return hasNext(1);
    }

    public JavaToken current(final int step) {
        return tokens.get(index + step);
    }

    public JavaToken current() {
        return current(0);
    }

    public JavaToken next() {
        return current(1);
    }

    public void move() {
        index++;
    }
}
