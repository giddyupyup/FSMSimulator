package fsmsim.engine.regex;

import fsmsim.engine.Specials;
import fsmsim.engine.regex.Characters.*;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class RegexParser implements Parser {

	@Override
	public List<Characters> parse(final String parseString) {

		final List<Characters> literals = new ArrayList<>();

		for(int i = 0; i < parseString.length(); i++) {
			Characters literal;
			switch(Character.toString(parseString.charAt(i))) {
				case "$" : literal = new SpecialLiterals(Specials.EMPTY_STRING,
														 Character.toString(parseString.charAt(i)),
														 i); break;
				case "+" : literal = new SpecialLiterals(Specials.UNION,
														 Character.toString(parseString.charAt(i)),
														 i); break;
				case "*" : literal = new SpecialLiterals(Specials.KLEENE_STAR,
														 Character.toString(parseString.charAt(i)),
														 i); break;
				case "(" : literal = new SpecialLiterals(Specials.LEFT_PAREN,
														 Character.toString(parseString.charAt(i)),
														 i); break;
				case ")" : literal = new SpecialLiterals(Specials.RIGHT_PAREN,
														 Character.toString(parseString.charAt(i)),
														 i); break;
				default : literal = new Literals(Character.toString(parseString.charAt(i)), i); break;
			}
			literals.add(literal);
		}

		return ImmutableList.copyOf(literals);
	}

}