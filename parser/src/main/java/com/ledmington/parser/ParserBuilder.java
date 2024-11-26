package com.ledmington.parser;

import java.util.Optional;

public final class ParserBuilder {

	private String input;
	private boolean firstTime = true;

	// prev is initialized with the starter: a stateful supplier which provides the entire input string only the first time.
	private ParserStep prev = new ParserStep() {
		@Override
		public Optional<String> get() {
			if (firstTime) {
				firstTime = false;
				return Optional.of(input);
			} else {
				return Optional.empty();
			}
		}

		@Override
		public String toRegularExpression() {
			return "";
		}
	};

	ParserBuilder() {}

	public ParserBuilder one(final char ch) {
		prev = new ParserStep() {

			private final ParserStep previous = prev;

			@Override
			public Optional<String> get() {
					final Optional<String> opt = previous.get();
					if (opt.isPresent()) {
						final String s = opt.orElseThrow();
						if (!s.isEmpty() && s.charAt(0) == ch) {
							return Optional.of(s.substring(1));
						}
					}
					return Optional.empty();
			}

			@Override
			public String toRegularExpression() {
				return previous.toRegularExpression() + ch;
			}
		};
		return this;
	}

	public ParserBuilder one(final String sequence) {
		prev = new ParserStep() {

			private final ParserStep previous = prev;

			@Override
			public Optional<String> get() {
				final Optional<String> opt = previous.get();
				if (opt.isPresent()) {
					final String s = opt.orElseThrow();
					if (s.length() >= sequence.length() && s.startsWith(sequence)) {
						return Optional.of(s.substring(sequence.length()));
					}
				}
				return Optional.empty();
			}

			@Override
			public String toRegularExpression() {
				return previous.toRegularExpression() + sequence;
			}
		};
		return this;
	}

	public ParserBuilder zeroOrOne(final char ch) {
		prev = new ParserStep() {

			private final ParserStep previous = prev;

			@Override
			public Optional<String> get() {
				final Optional<String> opt = previous.get();
				if (opt.isPresent()) {
					final String s = opt.orElseThrow();
					if (!s.isEmpty() && s.charAt(0) == ch) {
						return Optional.of(s.substring(1));
					} else {
						return Optional.of(s);
					}
				}
				return Optional.empty();
			}

			@Override
			public String toRegularExpression() {
				return previous.toRegularExpression() + ch + '?';
			}
		};
		return this;
	}

	public ParserBuilder zeroOrOne(final String sequence) {
		prev = new ParserStep() {

			private final ParserStep previous = prev;

			@Override
			public Optional<String> get() {
				final Optional<String> opt = previous.get();
				if (opt.isPresent()) {
					final String s = opt.orElseThrow();
					if(s.length() >= sequence.length() && s.startsWith(sequence)) {
						return Optional.of(s.substring(sequence.length()));
					} else {
						return Optional.of(s);
					}
				}
				return Optional.empty();
			}

			@Override
			public String toRegularExpression() {
				return previous.toRegularExpression() + '(' + sequence + ")?";
			}
		};
		return this;
	}

	public ParserBuilder zeroOrMore(final char ch) {
		prev = new ParserStep() {

			private final ParserStep previous = prev;
			private String current = null;

			@Override
			public Optional<String> get() {
				if(current == null) {
					final Optional<String> opt = previous.get();
					if(opt.isEmpty()) {
						return Optional.empty();
					}
					current = opt.orElseThrow();
 				}

				Optional<String> result;
				if (!current.isEmpty() && current.charAt(0) == ch) {
					current = current.substring(1);
					result = Optional.of(current);
				} else {
					result = Optional.of(current);
					current = null;
				}
				return result;
			}

			@Override
			public String toRegularExpression() {
				return previous.toRegularExpression() + ch + '*';
			}
		};
		return this;
	}

	public Parser build() {
		return new Parser(prev, s -> {
			firstTime = true;
			input = s;
			while(true) {
				final Optional<String> opt = prev.get();
				if(opt.isEmpty()) {
					return false;
				}
				if(opt.orElseThrow().isEmpty()) {
					return true;
				}
			}
		});
	}
}
