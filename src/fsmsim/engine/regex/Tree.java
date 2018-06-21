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
        this.validateTree = !this.node.getNodeType().isInvalid() ? true : false;
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
                if(node.getNodeType().isInvalid()) {
                    System.out.println("Check for invalid node and out of range");
                    return new InvalidNode();
                } else {
                    System.out.println("Check for out of range");
                    System.out.println(node.getNodeType());
                    if(node.getNodeType().isSeq()) {
                        System.out.println(node.getNodeType());
                        if(!node.getNodeList().isEmpty()) {
                            System.out.println("Check for adding node");
                            nodeList.add(node);
                        } else {
                            System.out.println("Check for empty Seq Node");
                            return new InvalidNode();
                        }
                    }
                    break;    
                }
                
            } else {
                if(node.getNodeType().isInvalid()) {
                    System.out.println("Check for invalid node");
                    return new InvalidNode();
                }

                if(lexer.peek().getSpecials() != null &&
                  lexer.peek().getSpecials().isRightParen()) {
                    System.out.println("Check for right paren node");
                    boolean isLeftParenPresent = false; 
                    for(int i = lexer.getCurrentIdx()-1; i > -1; i--) {
                        if(lexer.getParseRegex().get(i).getSpecials() != null &&
                           lexer.getParseRegex().get(i).getSpecials().isRightParen()) {
                            return new InvalidNode();
                        }

                        if(lexer.getParseRegex().get(i).getSpecials() != null &&
                           lexer.getParseRegex().get(i).getSpecials().isLeftParen()) {
                            isLeftParenPresent = true;
                            break;
                        }
                    }

                    if(!isLeftParenPresent) {
                        return new InvalidNode();
                    }

                    break;
                }

                if(lexer.peek().getSpecials() != null &&
                   lexer.peek().getSpecials().isUnion()) {
                    lexer.advance();
                }
                System.out.println(node.getNodeType());
                if(node.getNodeType().isSeq()) {
                    System.out.println(node.getNodeType());
                    if(!node.getNodeList().isEmpty()) {
                        System.out.println("Check for adding node");
                        nodeList.add(node);
                    } else {
                        System.out.println("Check for empty Seq Node");
                        return new InvalidNode();
                    }
                }
            }
        }
        System.out.println("CHeck for Alt nodelist: " + nodeList.isEmpty());
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
                    return new InvalidNode();
                }

                nodeList.add(node);
            }
        }

        if(nodeList.isEmpty()) {
            return new InvalidNode();
        }
        System.out.println("CHeck for Seq nodelist: " + nodeList.isEmpty());
        return new SeqNode(nodeList);
    }


    private Node parseKStar(final Lexer lexer) {
        Node node = this.parseLexer(lexer);

        if(node != null) {
            if(node.getNodeType().isInvalid()) {
                node = new InvalidNode();
            } else {
                if(lexer.getCurrentIdx() != lexer.getParseRegex().size()) {
                    if(lexer.peek().getSpecials() != null &&
                       lexer.peek().getSpecials().isKleeneStar()) {
                        lexer.advance();
                        node = new KStarNode(node);
                    }                    
                }
            }
        }

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
                        node = new InvalidNode();
                    } else {
                        if(lexer.getCurrentIdx() == lexer.getParseRegex().size()) {
                            node = new InvalidNode();
                        } else if(!lexer.peek().getSpecials().isRightParen()) {
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
                    node = null;
                } else {
                    node = new InvalidNode();
                }
            } else {
                node = new LitNode(lexer.peek().getChar());
                lexer.advance();
            }
        } else {
            node = null;
        }

        return node; 
    }
}