package fsmsim.engine.regex;

import java.util.List;

public final class Lexer {

	private final List<Characters> parseRegex;
	private int currentIdx;

	public Lexer(final List<Characters> parseRegex) {
		this.parseRegex = parseRegex;
		this.currentIdx = 0;
	}

	public Characters peek() {
		return this.parseRegex.get(this.currentIdx);
	}

	public void advance() {
		this.currentIdx++;
	}

	public int getCurrentIdx() {
		return this.currentIdx;
	}

	public List<Characters> getParseRegex() {
		return this.parseRegex;
	}
}