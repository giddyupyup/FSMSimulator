package fsmsim.engine.regex;

import java.util.List;

public final class Lexer {

	private final List<Characters> parseRegex;
	private int currentChar;

	public Lexer(final List<Characters> parseRegex) {
		this.parseRegex = parseRegex;
		this.currentChar = 0;
	}

	public Characters peek() {
		return this.parseRegex.get(this.currentChar);
	}

	public void advance() {
		this.currentChar++;
	}
}