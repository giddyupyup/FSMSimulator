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
                    return altNode;
                } else {
                    if(altNode.getNodeType().isSeq()) {
                        if(!altNode.getNodeList().isEmpty()) {
                            nodeList.add(altNode);
                        } else {
                            return new InvalidNode("Invalid Regex: Missing expression inside parenthesis");
                        }
                    }
                    break;    
                }
                
            } else {
                if(altNode.getNodeType().isInvalid()) {
                    return altNode;
                }

                if(altNode.getNodeType().isSeq()) {
                    if(!altNode.getNodeList().isEmpty()) {
                        nodeList.add(altNode);
                    } else {
                        return new InvalidNode("Invalid Regex: Missing expression inside parenthesis");
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
                        final String invalidString = (leftParen > rightParen) ? "right" : "left";
                        return new InvalidNode("Invalid Regex: Missing " + invalidString + " parenthesis");
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
                    return seqNode;
                }

                nodeList.add(seqNode);
            }
        }

        if(nodeList.isEmpty()) {
            String invalidString;
            if(lexer.getCurrentIdx() == lexer.getParseRegex().size()) invalidString = "Invalid Regex: Missing expression in UNION";
            else if(lexer.peek().getSpecials().isRightParen()) invalidString = "Invalid Regex: Missing expression inside parenthesis";
            else invalidString = "Invalid Regex: Missing expression in UNION";
            return new InvalidNode(invalidString);
        }

        return new SeqNode(nodeList);
    }


    private Node parseKStar(final Lexer lexer) {
        Node kStarNode = this.parseLexer(lexer);

        if(kStarNode != null) {
            if(kStarNode.getNodeType().isInvalid()) {
                return kStarNode;
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
                    if(lexer.getCurrentIdx() == lexer.getParseRegex().size()) {
                        lexerNode = new InvalidNode("Invalid Regex: Missing right parenthesis");
                    } else {
                        final Node innerNode = this.parseAlt(lexer);
                        if(innerNode.getNodeType().isInvalid()) {
                            lexerNode = innerNode;
                        } else {
                            if(lexer.getCurrentIdx() == lexer.getParseRegex().size()) {
                                lexerNode = new InvalidNode("Invalid Regex: Missing right parenthesis");
                            } else if(!lexer.peek().getSpecials().isRightParen()) {
                                lexerNode = new InvalidNode("Invalid Regex: Missing right parenthesis");
                            } else {
                                lexer.advance();
                                lexerNode = innerNode;
                            }
                        }
                    }
                } else if(lexer.peek().getSpecials().isEmptyString()) {
                    lexer.advance();
                    lexerNode = new EpsNode();
                } else if(lexer.peek().getSpecials().isRightParen()) {
                    boolean isLeftParenPresent = false;
                    for(int i = lexer.getCurrentIdx(); i > -1; --i) {
                        if(lexer.getParseRegex().get(i).getSpecials() != null &&
                           lexer.getParseRegex().get(i).getSpecials().isLeftParen()) isLeftParenPresent = true;
                    }

                    if(!isLeftParenPresent) {
                        lexerNode = new InvalidNode("Invalid Regex: Missing left parenthesis");
                    } else {
                        lexerNode = null;
                    }
                } else if(lexer.peek().getSpecials().isUnion()) {
                    lexerNode = null;
                } else {
                    lexerNode = new InvalidNode("Invalid Regex: Missing expression before Kleene star");
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