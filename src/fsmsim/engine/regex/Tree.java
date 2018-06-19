package fsmsim.engine.regex;

import fsmsim.engine.Specials;
import fsmsim.engine.regex.Node.NodeType;
import fsmsim.engine.regex.Node.AltNode;
import fsmsim.engine.regex.Node.KStarNode;
import fsmsim.engine.regex.Node.SeqNode;
import fsmsim.engine.regex.Node.LitNode;
import fsmsim.engine.regex.Node.EpsNode;
import fsmsim.engine.regex.Node.InvalidNode;

import java.util.List;
import java.util.ArrayList;

public class Tree {
    private Node node;
    private Lexer lexer;
    private boolean validateTree;

    public Tree(final Lexer lexer) {
        this.lexer = lexer;
        this.node = this.parseAlt(this.lexer);
        this.validateTree = true;
    }

    public Node getParseNode() {
        return this.node;
    }

    public boolean validate() {
        return this.validateTree;
    }

    private Node parseAlt(final Lexer lexer) {
        final List<Node> nodeList = new ArrayList<>();
        Node node;
        while(true) {
            node = this.parseSeq(lexer);

            if(lexer.getCurrentIdx() == lexer.getParseRegex().size()) {
                break;
            } else {
                if(node.getNodeType().isInvalid()) {
                    this.validateTree = false;
                    return new InvalidNode();
                }

                if(lexer.peek().getSpecials() != null &&
                   lexer.peek().getSpecials().isUnion()) {
                    lexer.advance();
                }

                if(node.getNodeType().isSeq()) {
                    if(!node.getNodeList().isEmpty()) {
                        nodeList.add(node);
                    } else {
                        return new InvalidNode();
                    }
                }
            }
        }

        return new AltNode(nodeList);
    }

    private Node parseSeq(final Lexer lexer) {
        final List<Node> nodeList = new ArrayList<>();
        Node node;
        
        while(true) {
            node = this.parseKStar(lexer);

            if(lexer.getCurrentIdx() == lexer.getParseRegex().size() || node == null) {
                break;
            } else {
                if(node.getNodeType().isInvalid()) {
                    this.validateTree = false;
                    return new InvalidNode();
                }

                nodeList.add(node);
            }
        }
        System.out.println("Return the SeqNode");
        return new SeqNode(nodeList);
    }


    private Node parseKStar(final Lexer lexer) {
        Node node = this.parseLexer(lexer);

        if(lexer.getCurrentIdx() == lexer.getParseRegex().size() || node == null) {
            return node;
        } else {
            if(node.getNodeType().isInvalid()) {
                this.validateTree = false;
                return new InvalidNode();
            }

            if(lexer.peek().getSpecials() != null &&
               lexer.peek().getSpecials().isKleeneStar()) {
                lexer.advance();
                node = new KStarNode(node);
            }
        }

        return node;
    }

    private Node parseLexer(final Lexer lexer) {
        if(lexer.getCurrentIdx() != lexer.getParseRegex().size()) {
           if(lexer.peek().getSpecials() != null) {
                if(lexer.peek().getSpecials().isLeftParen()) {
                    lexer.advance();
                    final Node node = this.parseAlt(lexer);

                    
                    if(!lexer.peek().getSpecials().isRightParen()) {
                        this.validateTree = false;
                        return new InvalidNode();
                    }
                    System.out.println("After Right paren");
                    lexer.advance();
                    return node;
                } else if(lexer.peek().getSpecials().isEmptyString()) {
                    lexer.advance();
                    return new EpsNode();
                } else if(lexer.peek().getSpecials().isUnion() ||
                          lexer.peek().getSpecials().isRightParen()) {
                    return null;
                } else {
                    System.out.println("Check for first start");
                    this.validateTree = false;
                    return new InvalidNode();
                }
            } else {
                final Node node = new LitNode(lexer.peek().getChar());
                lexer.advance();
                return node;
            } 
        } else {
            return null;
        }
        
    }
}