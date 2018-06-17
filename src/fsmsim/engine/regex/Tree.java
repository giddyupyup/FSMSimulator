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
        try {
        while(true) {
            System.out.println("Alt Lexer current index: " + lexer.getCurrentIdx());
            node = this.parseSeq(lexer);
            System.out.println("Check after the return node");
            System.out.println("Alt Accepted Node: " + node);
            System.out.println("Alt Node: " + node.getNodeType());
            if(node.getNodeType() == NodeType.INVALID) {
                this.validateTree = false;
                return new InvalidNode();
            }
            System.out.println("Alt lexer.getCurrentIdx() : " + lexer.getCurrentIdx());
            System.out.println("Alt lexer.getParseRegex().size() : " + lexer.getParseRegex().size());
            if(lexer.getCurrentIdx() == lexer.getParseRegex().size()) {
                System.out.println("Out of bounds");
            }

            nodeList.add(node);


            if(lexer.peek().getSpecials() != null &&
               lexer.peek().getSpecials().isUnion()) {
                System.out.println("Error in this lexer.peek analysis parseAlt");
                lexer.advance();
            } else {
                break;
            }
        }  
        } catch(Exception e) {
            System.out.println("Error in parseAlt");
            e.getMessage();
        }

        return new AltNode(nodeList);
    }

    private Node parseSeq(final Lexer lexer) {
        final List<Node> nodeList = new ArrayList<>();
        Node node;
        
        while(true) {
            System.out.println("Seq Lexer current index: " + lexer.getCurrentIdx());
            node = this.parseKStar(lexer);
            System.out.println("Seq accepted Node: " + node);
            if(node == null) break;

            System.out.println("Seq Node: " + node.getNodeType());
            if(node.getNodeType() == NodeType.INVALID) {
                this.validateTree = false;
                return new InvalidNode();
            }
            System.out.println("Check after the needed INVALID");
        }
        System.out.println("Return the SeqNode");
        return new SeqNode(nodeList);
    }


    private Node parseKStar(final Lexer lexer) {
        Node node = this.parseLexer(lexer);

        if(node == null) return node;
        System.out.println("KStar Node: " + node.getNodeType());

        if(node.getNodeType() == NodeType.INVALID) {
            this.validateTree = false;
            return new InvalidNode();
        }

        System.out.println("KStar lexer.getCurrentIdx() : " + lexer.getCurrentIdx());
        System.out.println("KStar lexer.getParseRegex().size() : " + lexer.getParseRegex().size());

        if(lexer.getCurrentIdx() != lexer.getParseRegex().size()) {
            if(lexer.peek().getSpecials() != null &&
               lexer.peek().getSpecials().isKleeneStar()) {
                lexer.advance();
                node = new KStarNode(node);
            }
        }
        System.out.println("Return the KStar");
        System.out.println("KStar return Node: " + node);
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