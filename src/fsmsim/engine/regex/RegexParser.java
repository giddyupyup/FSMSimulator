package fsmsim.regex;

import fsmsim.engine.Specials;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class RegexParser implements Parser {

	@Override
	public List<Characters> parse(final String parseString) {

		final List<Characters> literals = new ArrayList<>();

		for(int i = 0; i < parseString.length(); i++) {
			Characters literal;
			switch(parseString.charAt(i)) {
				case "$" : literal = new SpecialLiterals(Specials.EMPTY_STRING,
														 parseString.charAt(i),
														 i); break;
				case "+" : literal = new SpecialLiterals(Specials.UNION,
														 parseString.charAt(i),
														 i); break;
				case "*" : literal = new SpecialLiterals(Specials.KLEENE_STAR,
														 parseString.charAt(i),
														 i); break;
				case "(" : literal = new SpecialLiterals(Specials.LEFT_PAREN,
														 parseString.charAt(i),
														 i); break;
				case ")" : literal = new SpecialLiterals(Specials.RIGHT_PAREN,
														 parseString.charAt(i),
														 i); break;
				default : literal = new Literals(parseString.charAt(i), i); break;
			}
			literals.add(literal);
		}

		return ImmutableList.copyOf(literals);
	}

}