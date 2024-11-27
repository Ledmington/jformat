package com.ledmington.parser;

import java.util.Objects;
import java.util.Optional;

public final class ParserBuilder {

	// prev is initialized with the starter: a stateful supplier which provides the
	// entire input string only the first time.
	private ParserStep prev = new ParserStep() {

		private String input;
		private boolean firstTime = true;

		@Override
		public Optional<String> get() {
			if (firstTime) {
				Objects.requireNonNull(input);
				firstTime = false;
				return Optional.of(input);
			} else {
				return Optional.empty();
			}
		}

		@Override
		public void setInput(final String input) {
			this.input = Objects.requireNonNull(input);
			this.firstTime = true;
		}

		@Override
		public String toRegularExpression() {
			return "";
		}
	};

	ParserBuilder() {
	}

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
			public void setInput(final String input) {
				previous.setInput(input);
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
			public void setInput(final String input) {
				previous.setInput(input);
			}

			@Override
			public String toRegularExpression() {
				return previous.toRegularExpression() + sequence;
			}
		};
		return this;
	}

	public ParserBuilder one(final Parser par) {
		prev = new ParserStep() {

			private final ParserStep previous = prev;

			@Override
			public Optional<String> get() {
				final Optional<String> opt = previous.get();
				if (opt.isPresent()) {
					final String s = opt.orElseThrow();
					par.step.setInput(s);
					return par.step.get();
				}
				return Optional.empty();
			}

			@Override
			public void setInput(final String input) {
				previous.setInput(input);
			}

			@Override
			public String toRegularExpression() {
				return previous.toRegularExpression() + '(' + par.toString() + ')';
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
			public void setInput(final String input) {
				previous.setInput(input);
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
					if (s.length() >= sequence.length() && s.startsWith(sequence)) {
						return Optional.of(s.substring(sequence.length()));
					} else {
						return Optional.of(s);
					}
				}
				return Optional.empty();
			}

			@Override
			public void setInput(final String input) {
				previous.setInput(input);
			}

			@Override
			public String toRegularExpression() {
				return previous.toRegularExpression() + '(' + sequence + ")?";
			}
		};
		return this;
	}

	public ParserBuilder zeroOrOne(final Parser par) {
		prev = new ParserStep() {

			private final ParserStep previous = prev;

			@Override
			public Optional<String> get() {
				final Optional<String> opt = previous.get();
				if (opt.isPresent()) {
					final String s = opt.orElseThrow();
					par.step.setInput(s);
					final Optional<String> result = par.step.get();
					if(result.isPresent()) {
						return result;
					} else {
						return Optional.of(s);
					}
				}
				return Optional.empty();
			}

			@Override
			public void setInput(final String input) {
				previous.setInput(input);
			}

			@Override
			public String toRegularExpression() {
				return previous.toRegularExpression() + '(' + par.toString() + ")?";
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
				if (current == null) {
					final Optional<String> opt = previous.get();
					if (opt.isEmpty()) {
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
			public void setInput(final String input) {
				previous.setInput(input);
			}

			@Override
			public String toRegularExpression() {
				return previous.toRegularExpression() + ch + '*';
			}
		};
		return this;
	}

	public ParserBuilder zeroOrMore(final String sequence) {
		prev = new ParserStep() {

			private final ParserStep previous = prev;
			private String current = null;

			@Override
			public Optional<String> get() {
				if (current == null) {
					final Optional<String> opt = previous.get();
					if (opt.isEmpty()) {
						return Optional.empty();
					}
					current = opt.orElseThrow();
				}

				Optional<String> result;
				if (current.length() >= sequence.length() && current.startsWith(sequence)) {
					current = current.substring(sequence.length());
					result = Optional.of(current);
				} else {
					result = Optional.of(current);
					current = null;
				}
				return result;
			}

			@Override
			public void setInput(final String input) {
				previous.setInput(input);
			}

			@Override
			public String toRegularExpression() {
				return previous.toRegularExpression() + '(' + sequence + ")*";
			}
		};
		return this;
	}

	public ParserBuilder zeroOrMore(final Parser par) {
		prev = new ParserStep() {

			private final ParserStep previous = prev;
			private String current = null;

			@Override
			public Optional<String> get() {
				if (current == null) {
					final Optional<String> opt = previous.get();
					if (opt.isEmpty()) {
						return Optional.empty();
					}
					current = opt.orElseThrow();
				}

				par.step.setInput(current);
				final Optional<String> result = par.step.get();
				if(result.isEmpty()) {
					final Optional<String> tmp = Optional.of(current);
					current = null;
					return tmp;
				}
				final String s = result.orElseThrow();
				current = current.substring(s.length());
				return Optional.of(current);
			}

			@Override
			public void setInput(final String input) {
				previous.setInput(input);
			}

			@Override
			public String toRegularExpression() {
				return previous.toRegularExpression() + '(' + par.toString() + ")*";
			}
		};
		return this;
	}

	public Parser build() {
		return new Parser(prev, s -> {
			prev.setInput(s);
			while (true) {
				final Optional<String> opt = prev.get();
				if (opt.isEmpty()) {
					return false;
				}
				if (opt.orElseThrow().isEmpty()) {
					return true;
				}
			}
		});
	}
}
