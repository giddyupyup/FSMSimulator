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
            
            System.out.println("Check the current index of the lexer: " + lexer.getCurrentIdx());
            System.out.println("Check the current size of the regex: " + lexer.getParseRegex().size());

            if(lexer.getCurrentIdx() == lexer.getParseRegex().size()) {

                break;
            } else {
                if(lexer.peek().getSpecials() != null &&
                  lexer.peek().getSpecials().isRightParen()) break;
                System.out.println("Alt return node type: " + node.getNodeType());

                if(node.getNodeType().isInvalid()) {
                    this.validateTree = false;
                    return new InvalidNode();
                }

                if(lexer.peek().getSpecials() != null &&
                   lexer.peek().getSpecials().isUnion()) {
                    lexer.advance();
                }

                if(node.getNodeType().isSeq()) {
                    System.out.println("Seq Node nodeList: " + node.getNodeList().isEmpty());
                    if(!node.getNodeList().isEmpty()) {
                        nodeList.add(node);
                    } else {
                        return new InvalidNode();
                    }
                }
            }
        }
        System.out.println("Return Alt Node");
        return new AltNode(nodeList);
    }

    private Node parseSeq(final Lexer lexer) {
        final List<Node> nodeList = new ArrayList<>();
        Node node;
        
        while(true) {
            node = this.parseKStar(lexer);

            if(node == null) {
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

        if(node != null) {
            if(node.getNodeType().isInvalid()) {
                this.validateTree = false;
                node = new InvalidNode();
            } else {
                if(lexer.peek().getSpecials() != null &&
                   lexer.peek().getSpecials().isKleeneStar()) {
                    lexer.advance();
                    node = new KStarNode(node);
                }
            }
        }

        // if(node == null) {
        //     if(lexer.getCurrentIdx() == lexer.getParseRegex().size()) {
        //         break;
        //     } else if()
        // } else {
        //     if(node.getNodeType().isInvalid()) {
        //         this.validateTree = false;
        //         return new InvalidNode();
        //     }

        //     if(lexer.peek().getSpecials() != null &&
        //        lexer.peek().getSpecials().isKleeneStar()) {
        //         lexer.advance();
        //         node = new KStarNode(node);
        //     }
        // }

        return node;
    }

    private Node parseLexer(final Lexer lexer) {
        Node node;

        if(lexer.getCurrentIdx() != lexer.getParseRegex().size()) {
            if(lexer.peek().getSpecials() != null) {
                if(lexer.peek().getSpecials().isLeftParen()) {
                    lexer.advance();
                    final Node innerNode = this.parseAlt(lexer);

                    if(innerNode.getNodeType().isInvalid()) {
                        this.validateTree = false;
                        node = new InvalidNode();
                    } else {
                        if(lexer.getCurrentIdx() == lexer.getParseRegex().size()) {
                            this.validateTree = false;
                            node = new InvalidNode();
                        } else if(!lexer.peek().getSpecials().isRightParen()) {
                            this.validateTree = false;
                            node = new InvalidNode();
                        } else {
                            lexer.advance();
                            node = innerNode;
                        }
                    }
                } else if(lexer.peek().getSpecials().isEmptyString()) {
                    lexer.advance();
                    node = new EpsNode();
                } else if(lexer.peek().getSpecials().isUnion() ||
                          lexer.peek().getSpecials().isRightParen()) {
                    this.validateTree = false;
                    node = new InvalidNode();
                } else {
                    this.validateTree = false;
                    node = new InvalidNode();
                }
            } else {
                node = new LitNode(lexer.peek().getChar());
                lexer.advance();
            }
        } else {
            node = null;
        }

        // if(lexer.getCurrentIdx() != lexer.getParseRegex().size()) {
        //    if(lexer.peek().getSpecials() != null) {
        //         if(lexer.peek().getSpecials().isLeftParen()) {
        //             lexer.advance();
        //             final Node node = this.parseAlt(lexer);

        //             System.out.println("Return inner node: " + node.getNodeType());

        //             if(lexer.getCurrentIdx() == lexer.getParseRegex().size()) {
        //                 this.validateTree = false;
        //                 return new InvalidNode();
        //             }
                    
        //             if(!lexer.peek().getSpecials().isRightParen()) {
        //                 this.validateTree = false;
        //                 return new InvalidNode();
        //             }
        //             System.out.println("After Right paren");
        //             lexer.advance();
        //             return node;
        //         } else if(lexer.peek().getSpecials().isEmptyString()) {
        //             lexer.advance();
        //             return new EpsNode();
        //         } else if(lexer.peek().getSpecials().isUnion() ||
        //                   lexer.peek().getSpecials().isRightParen()) {
        //             return null;
        //         } else {
        //             System.out.println("Check for first start");
        //             this.validateTree = false;
        //             return new InvalidNode();
        //         }
        //     } else {
        //         final Node node = new LitNode(lexer.peek().getChar());
        //         lexer.advance();
        //         return node;
        //     } 
        // } else {
        //     return null;
        // }

        return node;
        
    }
}