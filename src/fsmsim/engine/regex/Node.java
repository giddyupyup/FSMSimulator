package fsmsim.engine.regex;

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

	public static class AltNode extends Node {
		private Node node;

		public AltNode(final NodeType nodeType,
					   final Node node) {
			super(nodeType);
			this.node = node;
		}

		@Override
		public Node getNode() {
			return this.node;
		}
	}

	public static class SeqNode extends Node {
		private Node node;

		public SeqNode(final NodeType nodeType,
					   final Node node) {
			super(nodeType);
			this.node = node;
		}

		@Override
		public Node getNode() {
			return this.node;
		}
	}

	public static class KStarNode extends Node {
		private Node node;

		public KStarNode(final NodeType nodeType,
						 final Node node) {
			super(nodeType);
			this.node = node;
		}

		@Override
		public Node getNode() {
			return this.node;
		}
	}

	public static class LitNode extends Node {
		private String alphabet;

		public LitNode(final NodeType nodeType,
					   final String alphabet) {
			super(nodeType);
			this.alphabet = alphabet;
		}
	}

	public static class EpsNode extends Node {
		private String emptyString;

		public LitNode(final NodeType nodeType) {
			super(nodeType);
			this.emptyString = "$";
		}
	}

	public enum NodeType {
		LIT,
		ALT,
		KSTAR,
		SEQ,
		EPS
	}
}