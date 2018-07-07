package fsmsim.engine.regex;

import fsmsim.engine.regex.Node.AltNode;
import fsmsim.engine.regex.Node.KStarNode;
import fsmsim.engine.regex.Node.SeqNode;
import fsmsim.engine.regex.Node.LitNode;
import fsmsim.engine.regex.Node.EpsNode;
import fsmsim.engine.regex.Node.InvalidNode;

import java.util.List;
import java.util.ArrayList;

public class Tree {
    private final Node node;
    private final Lexer lexer;
    private final boolean validateTree;

    public Tree(final Lexer lexer) {
        this.lexer = lexer;
        this.node = this.parseAlt(this.lexer);
        this.validateTree = !this.node.getNodeType().isInvalid();
    }

    public Node getParseNode() {
        return this.node;
    }

    public boolean validate() {
        return this.validateTree;
    }

    private Node parseAlt(final Lexer lexer) {
        final List<Node> nodeList = new ArrayList<>();
        Node altNode;
        while(true) {
            altNode = this.parseSeq(lexer);
            if(lexer.getCurrentIdx() == lexer.getParseRegex().size()) {
                if(altNode.getNodeType().isInvalid()) {
                    return new InvalidNode();
                } else {
                    if(altNode.getNodeType().isSeq()) {
                        if(!altNode.getNodeList().isEmpty()) {
                            nodeList.add(altNode);
                        } else {
                            return new InvalidNode();
                        }
                    }
                    break;    
                }
                
            } else {
                if(altNode.getNodeType().isInvalid()) {
                    return new InvalidNode();
                }

                if(altNode.getNodeType().isSeq()) {
                    if(!altNode.getNodeList().isEmpty()) {
                        nodeList.add(altNode);
                    } else {
                        return new InvalidNode();
                    }
                }

                if(lexer.peek().getSpecials() != null &&
                  lexer.peek().getSpecials().isRightParen()) {
                    int leftParen = 0;
                    int rightParen = 0;
                    for(final Characters character : lexer.getParseRegex()) {
                        if(character.getSpecials() != null &&
                           character.getSpecials().isRightParen()) {
                            ++rightParen;
                        }
                        
                        if(character.getSpecials() != null &&
                           character.getSpecials().isLeftParen()) {
                            ++leftParen;
                        }
                    }
                    
                    if(leftParen != rightParen) {
                        return new InvalidNode();
                    }

                    break;
                }

                if(lexer.peek().getSpecials() != null &&
                   lexer.peek().getSpecials().isUnion()) {
                    lexer.advance();
                }
            }
        }

        return new AltNode(nodeList);
    }

    private Node parseSeq(final Lexer lexer) {
        final List<Node> nodeList = new ArrayList<>();
        Node seqNode;
        
        while(true) {
            seqNode = this.parseKStar(lexer);
            if(seqNode == null) {
                break;
            } else {
                if(seqNode.getNodeType().isInvalid()) {
                    return new InvalidNode();
                }

                nodeList.add(seqNode);
            }
        }

        if(nodeList.isEmpty()) {
            return new InvalidNode();
        }

        return new SeqNode(nodeList);
    }


    private Node parseKStar(final Lexer lexer) {
        Node kStarNode = this.parseLexer(lexer);

        if(kStarNode != null) {
            if(kStarNode.getNodeType().isInvalid()) {
                kStarNode = new InvalidNode();
            } else {
                if(lexer.getCurrentIdx() != lexer.getParseRegex().size()) {
                    if(lexer.peek().getSpecials() != null &&
                       lexer.peek().getSpecials().isKleeneStar()) {
                        lexer.advance();
                        kStarNode = new KStarNode(kStarNode);
                    }                    
                }
            }
        }

        return kStarNode;
    }

    private Node parseLexer(final Lexer lexer) {
        Node lexerNode;

        if(lexer.getCurrentIdx() != lexer.getParseRegex().size()) {
            if(lexer.peek().getSpecials() != null) {
                if(lexer.peek().getSpecials().isLeftParen()) {
                    lexer.advance();
                    final Node innerNode = this.parseAlt(lexer);
                    if(innerNode.getNodeType().isInvalid()) {
                        lexerNode = new InvalidNode();
                    } else {
                        if(lexer.getCurrentIdx() == lexer.getParseRegex().size()) {
                            lexerNode = new InvalidNode();
                        } else if(!lexer.peek().getSpecials().isRightParen()) {
                            lexerNode = new InvalidNode();
                        } else {
                            lexer.advance();
                            lexerNode = innerNode;
                        }
                    }
                } else if(lexer.peek().getSpecials().isEmptyString()) {
                    lexer.advance();
                    lexerNode = new EpsNode();
                } else if(lexer.peek().getSpecials().isUnion() ||
                          lexer.peek().getSpecials().isRightParen()) {
                    lexerNode = null;
                } else {
                    lexerNode = new InvalidNode();
                }
            } else {
                lexerNode = new LitNode(lexer.peek().getChar());
                lexer.advance();
            }
        } else {
            lexerNode = null;
        }

        return lexerNode; 
    }
}