package fsmsim.engine;

public enum Specials {
	private final String specialChar;

	Specials(final String specialChar) {
		this.specialChar = specialChar;
	}

	UNION("+") {
		@Override
		public String toString() {
			return this.specialChar;
		}

		@Override
		public boolean isUnion() {
			return true;
		}

		@Override
		public boolean isKleeneStar() {
			return false;
		}

		@Override
		public boolean isLeftParen() {
			return false;
		}

		@Override
		public boolean isRightParen() {
			return false;
		}

		@Override
		public boolean isEmptyString() {
			return false;
		}
	},

	KLEENE_STAR("*") {
		@Override
		public String toString() {
			return this.specialChar;
		}

		@Override
		public boolean isUnion() {
			return false;
		}

		@Override
		public boolean isKleeneStar() {
			return true;
		}

		@Override
		public boolean isLeftParen() {
			return false;
		}

		@Override
		public boolean isRightParen() {
			return false;
		}

		@Override
		public boolean isEmptyString() {
			return false;
		}
	},

	LEFT_PAREN("(") {
		@Override
		public String toString() {
			return this.specialChar;
		}

		@Override
		public boolean isUnion() {
			return false;
		}

		@Override
		public boolean isKleeneStar() {
			return false;
		}

		@Override
		public boolean isLeftParen() {
			return true;
		}

		@Override
		public boolean isRightParen() {
			return false;
		}

		@Override
		public boolean isEmptyString() {
			return false;
		}
	},

	RIGHT_PAREN(")") {
		@Override
		public String toString() {
			return this.specialChar;
		}

		@Override
		public boolean isUnion() {
			return false;
		}

		@Override
		public boolean isKleeneStar() {
			return false;
		}

		@Override
		public boolean isLeftParen() {
			return false;
		}

		@Override
		public boolean isRightParen() {
			return true;
		}

		@Override
		public boolean isEmptyString() {
			return false;
		}
	},

	EMPTY_STRING("$") {
		@Override
		public String toString() {
			return this.specialChar;
		}

		@Override
		public boolean isUnion() {
			return false;
		}

		@Override
		public boolean isKleeneStar() {
			return false;
		}

		@Override
		public boolean isLeftParen() {
			return false;
		}

		@Override
		public boolean isRightParen() {
			return false;
		}

		@Override
		public boolean isEmptyString() {
			return true;
		}
	};

	public abstract boolean isUnion();
	public abstract boolean isKleeneStar();
	public abstract boolean isLeftParen();
	public abstract boolean isRightParen();
	public abstract boolean isEmptyString();
}