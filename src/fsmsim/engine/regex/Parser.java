package fsmsim.engine.regex;

import java.util.List;

public interface Parser {
    Lexer parse(String parseString);
}