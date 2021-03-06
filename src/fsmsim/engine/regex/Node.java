package fsmsim.engine.regex;

import java.util.List;

public abstract class Node {
    private final NodeType nodeType;

    private Node(final NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public Node getNode() {
        return null;
    }

    public List<Node> getNodeList() {
        return null;
    }

    public String getLiteral() {
        return null;
    }

    public String getInvalidString() {
        return null;
    }

    @Override
    public String toString() {
        return this.getNodeType().toString();
    }

    public static class AltNode extends Node {
        private List<Node> node;

        public AltNode(final List<Node> node) {
            super(NodeType.UNION);
            this.node = node;
        }

        @Override
        public List<Node> getNodeList() {
            return this.node;
        }
    }

    public static class SeqNode extends Node {
        private List<Node> node;

        public SeqNode(final List<Node> node) {
            super(NodeType.SEQ);
            this.node = node;
        }

        @Override
        public List<Node> getNodeList() {
            return this.node;
        }
    }

    public static class KStarNode extends Node {
        private Node node;

        public KStarNode(final Node node) {
            super(NodeType.KSTAR);
            this.node = node;
        }

        @Override
        public Node getNode() {
            return this.node;
        }
    }

    public static class LitNode extends Node {
        private String symbol;

        public LitNode(final String symbol) {
            super(NodeType.SYMBOL);
            this.symbol = symbol;
        }

        @Override
        public String getLiteral() {
            return this.symbol;
        }
    }

    public static class EpsNode extends Node {
        private String symbol;

        public EpsNode() {
            super(NodeType.EPS);
            this.symbol = "$";
        }

        @Override
        public String getLiteral() {
            return this.symbol;
        }
    }

    public static class InvalidNode extends Node {
        private final String invalidString;

        public InvalidNode(final String invalidString) {
            super(NodeType.INVALID);
            this.invalidString = invalidString;
        }

        @Override
        public String getInvalidString() {
            return this.invalidString;
        }
    }

    public enum NodeType {
        SYMBOL("SYMBOL") {
            @Override
            public boolean isUnion() {
                return false;
            }

            @Override
            public boolean isKStar() {
                return false;
            }

            @Override
            public boolean isSeq() {
                return false;
            }

            @Override
            public boolean isSymbol() {
                return true;
            }

            @Override
            public boolean isEmptyString() {
                return false;
            }

            @Override
            public boolean isInvalid() {
                return false;
            }
        },

        UNION("UNION (+)") {
            @Override
            public boolean isUnion() {
                return true;
            }

            @Override
            public boolean isKStar() {
                return false;
            }

            @Override
            public boolean isSeq() {
                return false;
            }

            @Override
            public boolean isSymbol() {
                return false;
            }

            @Override
            public boolean isEmptyString() {
                return false;
            }

            @Override
            public boolean isInvalid() {
                return false;
            }

        },

        KSTAR("KLEENE STAR (*)") {
            @Override
            public boolean isUnion() {
                return false;
            }

            @Override
            public boolean isKStar() {
                return true;
            }

            @Override
            public boolean isSeq() {
                return false;
            }

            @Override
            public boolean isSymbol() {
                return false;
            }

            @Override
            public boolean isEmptyString() {
                return false;
            }

            @Override
            public boolean isInvalid() {
                return false;
            }
        },

        SEQ("SEQUENCE '()'") {
            @Override
            public boolean isUnion() {
                return false;
            }

            @Override
            public boolean isKStar() {
                return false;
            }

            @Override
            public boolean isSeq() {
                return true;
            }

            @Override
            public boolean isSymbol() {
                return false;
            }

            @Override
            public boolean isEmptyString() {
                return false;
            }

            @Override
            public boolean isInvalid() {
                return false;
            }
        },

        EPS("EMPTY STRING") {
            @Override
            public boolean isUnion() {
                return false;
            }

            @Override
            public boolean isKStar() {
                return false;
            }

            @Override
            public boolean isSeq() {
                return false;
            }

            @Override
            public boolean isSymbol() {
                return false;
            }

            @Override
            public boolean isEmptyString() {
                return true;
            }

            @Override
            public boolean isInvalid() {
                return false;
            }
        },

        INVALID("INVALID") {
            @Override
            public boolean isUnion() {
                return false;
            }

            @Override
            public boolean isKStar() {
                return false;
            }

            @Override
            public boolean isSeq() {
                return false;
            }

            @Override
            public boolean isSymbol() {
                return false;
            }

            @Override
            public boolean isEmptyString() {
                return false;
            }

            @Override
            public boolean isInvalid() {
                return true;
            }
        };

        private final String nodeName;

        NodeType(final String nodeName) {
            this.nodeName = nodeName;
        }

        public abstract boolean isUnion();
        public abstract boolean isKStar();
        public abstract boolean isSeq();
        public abstract boolean isSymbol();
        public abstract boolean isEmptyString();
        public abstract boolean isInvalid();
    }
}