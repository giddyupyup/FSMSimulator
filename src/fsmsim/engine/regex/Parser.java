package fsmsim.model.regex;

import java.util.Collection;

public interface Parser {
	Collection<Literals> parse(String parseString);
}