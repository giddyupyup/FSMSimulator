package fsmsim.engine.regex;

import fsmsim.engine.Specials;

public abstract class Characters {

	private final String regexChar;
	private final int regexId;

	private Characters(final String regexChar,
					   final int regexId) {
		this.regexChar = regexChar;
		this.regexId = regexId;
	}
	
	public String getChar() {
		return this.regexChar;
	}

	public int getId() {
		return this.regexId;
	}

	public Specials getSpecials() {
		return null;
	}

	public static final class Literals extends Characters {
		public Literals(final String regexChar,
						final int regexId) {
			super(regexChar, regexId);
		}
	}

	public static final class SpecialLiterals extends Characters {
		final Specials specialChar;

		public SpecialLiterals(final Specials specialChar,
						       final String regexChar,
							   final int regexId) {
			super(regexChar, regexId);
			this.specialChar = specialChar;
		}

		@Override
		public Specials getSpecials() {
			return this.specialChar;
		}
	}
}