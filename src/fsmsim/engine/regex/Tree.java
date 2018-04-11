package fsmsim.engine.regex;

import fsmsim.engine.Specials;

import java.util.List;
import java.util.ArrayList;

public class Tree {
	private Node node;
	private Lexer lexer;

	public Tree(final Lexer lexer) {
		this.lexer = lexer;
		this.node = null;
		this.parseAlt(this.lexer);
	}

	private Node parseAlt(final Lexer lexer) {
		final List<Node> nodeList = new ArrayList<>();

		while(true) {
			nodeList.add(this.parseSeq(lexer));
			if(lexer.peek().getSpecials().isAlt()) {
				lexer.advance();
			} else {
				break;
			}
		}

		return new AltNode(NodeType.ALT, nodeList);
	}

	private Node parseSeq(final Lexer lexer) {
		final List<Node> nodeList = new ArrayList<>();
		Node node;
		while(true) {
			node = this.parseKStar(lexer);
			if(node == null) break;
		}

		return new SeqNode(NodeType.SEQ, nodeList);
	}


	private Node parseKStar(final Lexer lexer) {
		Node node = this.parseLexer(final Lexer lexer);

		if(lexer.peek().getSpecials().isKleenStar()) {
			lexer.advance();
			node = new KStarNode(NodeType.KSTAR, node);
		}
		return node;
	}

	private Node parseLexer(final Lexer lexer) {
		if(lexer.peek().getSpecials().isLeftParen()) {
			lexer.advance();
			Node node = this.parseAlt(lexer);
			if(!lexer.peek().getSpecials().isRightParen()) {

			}
		}
	}
}