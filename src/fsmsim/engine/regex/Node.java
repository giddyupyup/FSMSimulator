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

	public static class AltNode extends Node {
		private List<Node> node;

		public AltNode(final List<Node> node) {
			super(NodeType.UNION);
			this.node = node;
		}

		@Override
		public List<Node> getNode() {
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
		public List<Node> getNode() {
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

		public String getLiteral() {
			return this.symbol;
		}
	}

	public static class InvalidNode extends Node {
		public InvalidNode() {
			super(NodeType.INVALID);
		}
	}

	public enum NodeType {
		SYMBOL,
		UNION,
		KSTAR,
		SEQ,
		EPS,
		INVALID
	}
}