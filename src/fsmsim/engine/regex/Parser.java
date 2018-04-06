package fsmsim.model.regex;

import java.util.List;

public interface Parser {
	List<Characters> parse(String parseString);
	Tree createTree(List<Characters> parseRegex);
}