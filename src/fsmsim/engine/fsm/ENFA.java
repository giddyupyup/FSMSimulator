package fsmsim.engine.fsm;

import fsmsim.engine.Specials;
import fsmsim.engine.regex.Tree;
import fsmsim.engine.fsm.State.StateType;

import java.util.ArrayList;
import java.util.List;

public class ENFA implements FSM {

	private Tree regexTree;
	private int initialState;
	private int lastState;
	private int currentState;
	private List<State> states;


	public ENFA(final Tree regexTree) {
		this.regexTree = regexTree;
		this.currentState = 0;
		this.initialState = 0;
		this.lastState = 0;
		this.states = new ArrayList<>();
	}

	@Override
	public void create() {
		final List<Integer> toStates = new ArrayList<>();
		toStates.add(this.getCurrentState() + 1);
		this.getStates().add(this.addState(StateType.INITIAL,
										   this.getCurrentState()
										   null,
										   new ArrayList<>(),
										   Specials.EMPTY_STRING.toString()));
		this.advanceCurrentState();
		for(final Node node : this.regexTree.getNode().getNodeList()) { //creates the upper union transition
			final State initState = this.getState(0);
			initState.add(this.getCurrentState());
			this.createStates(initState.getStateNumber(), node);
		}
	}

	public List<State> getStates() {
		return this.states;
	}

	private State getState(final int state) {
		return this.states.get(state);
	}

	private State removeState(final int state) {
		return this.states.remove(state);
	}
	private int getCurrentState() {
		return this.currentState;
	}

	private int getLastState() {
		return this.lastState;
	}

	private void advanceCurrentState() {
		++this.currentState;
	}

	private void setLastState(final int lastState) {
		this.lastState = lastState;
	}

	private Tree getTree() {
		return this.regexTree;
	}

	private void createStates(final List<Integer> fromStates,
							  final Node node) {
		if(node.getNodeType().isSeq()) {
			for(final Node innerNode : node.getNodeList()) {
				this.createStates(fromStates, innerNode);
			}
		}

		if(node.getNodeType().isUnion()) { //creates the inner union transition states
			final List<Integer> fromStates = new ArrayList<>();
			fromStates.add(this.getCurrentState());
			for(final Node innerNode : node.getNodeList()) {
				this.createStates(fromStates, innerNode);
			}

		}

		if(node.getNodeType().isKStar()) {
			this.kStarTransitionStates(fromStates, node);
		}

		if(node.getNodeType().isSymbol()) {
			this.createSymbolStates(fromStates, node);
		}
	}

	private void kStarTransitionStates(final Node node) {
		
	}

	private void createSymbolStates(final Node node) {

	}

	private State addState(final StateType stateType,
						   final int currentState,
						   final List<State> fromStates,
						   final List<State> toStates,
						   final String symbol) {
		final State createState;

		switch (stateType) {
			case INITIAL: 
				createState = new State(currentState, toStates, symbol);
				break;
			case LAST: 
				createState = new State(currentState, fromStates);
				break;

			default: 
				createState = new State(currentState, fromStates, toStates, symbol);
				break;
		}

		return createState;
	}

	private State promoteState(final State state,
							   final int toStates,
							   final String symbol) {
		return new State(state.getStateNumber(), state.getFromStates(), toStates, symbol);
	}

}