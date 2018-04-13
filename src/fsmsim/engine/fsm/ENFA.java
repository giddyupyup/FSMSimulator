package fsmsim.engine.fsm;

import fsmsim.engine.regex.Tree;

public class ENFA implements FSM {

	private Tree regexTree;
	private int initialState;
	private int lastState;
	private int currentState;


	public ENFA(final Tree regexTree) {
		this.regexTree = regexTree;
	}

	@Override
	public void create() {
		for(final Node node : this.regexTree.getNode().getNodeList()) {
			if(node.getNodeType().isSeq())	
				//create a function that will evaluate the seq pattern, use for recursive
		}
	}

	private List<State> kStarTransitionState() {
		
	}

	private List<State> unionTransitionState() {

	}
}