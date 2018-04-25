package fsmsim.engine.fsm;

import fsmsim.engine.Specials;
import fsmsim.engine.regex.Tree;
import fsmsim.engine.fsm.State.StateType;

import java.util.ArrayList;
import java.util.Collections;
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
		final List<Integer> fromStates = new ArrayList<>();
		this.getStates().add(this.addState(StateType.INITIAL,
										   this.getCurrentState(),
										   null,
										   toStates,
										   Specials.EMPTY_STRING.toString()));
		this.advanceCurrentState();
		final State initState = this.getState(0);
		for(final Node node : this.regexTree.getNode().getNodeList()) { //creates the upper union transition
			initState.addToStates(this.getCurrentState());
			this.createStates(initState.getStateNumber(), node);
			fromStates.add(this.getCurrentState() - 1);
		}

		this.getStates().add(this.addState(StateType.LAST,
										   this.getCurrentState(),
										   fromStates,
										   null,
										   null));
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
		if(node.getNodeType().isSymbol() || node.getNodeType().isEmptyString()) {
			this.createSymbolStates(fromStates, node);
			return;
		}

		if(node.getNodeType().isKStar()) {
			this.kStarTransitionStates(fromStates, node);
			return;
		}

		if(node.getNodeType().isSeq()) {
			for(final Node innerNode : node.getNodeList()) {
				this.createStates(fromStates, innerNode);
			}
		}

		if(node.getNodeType().isUnion()) { //creates the inner union transition states
			final int currentState = this.getCurrentState();
			final List<Integer> toStates = new ArrayList<>();
			final List<Integer> _fromStates = new ArrayList<>();
			this.getStates().add(this.addState(StateType.COMMON,
											   currentState,
											   fromStates,
											   toStates,
											   Specials.EMPTY_STRING.toString()));
			this.advanceCurrentState();
			final State innerState = this.getState(currentState);
			for(final Node innerNode : node.getNodeList()) {
				innerState.addToStates(this.getCurrentState());
				this.createStates(innerState.getStateNumber(), innerNode);
				_fromStates.add(this.getCurrentState() - 1);
			}

			toStates.clear();
			toStates.add(this.getCurrentState() + 1);
			this.getStates().add(this.addState(StateType.COMMON,
											   this.getCurrentState(),
											   _fromStates,
											   toStates,
											   Specials.EMPTY_STRING.toString()));
			this.advanceCurrentState();
		}
	}

	private void kStarTransitionStates(final List<Integer> fromStates,
									   final Node node) {
		final List<Integer> toStates = new ArrayList<>();
		final List<Integer> fStates = new ArrayList<>();
		final State firstState;
		final State secondState;

		//create the first state
		toStates.add(this.getCurrentState() + 1);
		toStates.add(this.getCurrentState() + 3);
		fStates.addAll(fromStates);
		this.getStates().add(this.addState(StateType.COMMON,
										   this.getCurrentState(),
										   fStates,
										   toStates,
										   Specials.EMPTY_STRING.toString()));
		firstState = this.getState(this.getCurrentState());
		this.advanceCurrentState();
		toStates.clear();
		fStates.clear();

		//create the second state
		if(node.getNodeType().isSymbol() || node.getNodeType().isEmptyString()) {
			toStates.add(this.getCurrentState() + 1);
			fStates.add(this.getCurrentState() - 1);
			this.getStates().add(this.addState(StateType.COMMON,
											   this.getCurrentState(),
											   fStates,
											   toStates,
											   node.getLiteral()));
			secondState = this.getState(this.getCurrentState());
			this.advanceCurrentState();
		} else {
			toStates.add(this.getCurrentState() + 1);
			fStates.add(this.getCurrentState() - 1);
			this.getStates().add(this.addState(StateType.COMMON,
											   this.getCurrentState(),
											   fStates,
											   toStates,
											   Specials.EMPTY_STRING.toString()));
			secondState = this.getState(this.getCurrentState());
			this.advanceCurrentState();
			final List<Integer> _fStates = new ArrayList<>();
			_fStates.add(this.getCurrentState() - 1);
			this.createStates(_fStates, node.getNode());
		}

		secondState.addFromStates(this.getCurrentState());
		toStates.clear();
		fStates.clear();

		//create the third state
		toStates.add(this.getCurrentState() + 1);
		toStates.add(secondState.getStateNumber());
		fStates.add(this.getCurrentState() - 1);
		this.getStates().add(this.addState(StateType.COMMON,
										   this.getCurrentState(),
										   fStates,
										   toStates,
										   Specials.EMPTY_STRING.toString()));
		this.advanceCurrentState();
		toStates.clear();
		fStates.clear();

		//create the last state
		toStates.add(this.getCurrentState() + 1);
		fStates.add(this.getCurrentState() - 1);
		fStates.add(firstState.getStateNumber());
		this.getStates().add(this.addState(StateType.COMMON,
										   this.getCurrentState(),
										   fStates,
										   toStates,
										   Specials.EMPTY_STRING.toString()));
		this.advanceCurrentState();
	}

	private void createSymbolStates(final List<Integer> fromStates,
									final Node node) {
		final List<Integer> toStates = new ArrayList<>();
		toStates.add(this.getCurrentState() + 1);
		this.getStates().add(this.addState(StateType.COMMON,
										   this.getCurrentState(),
										   fromStates,
										   toStates,
										   node.getLiteral()));
		this.advanceCurrentState();
		final List<Integer> fStates = new ArrayList<>();
		fStates.add(this.getCurrentState() - 1);
		toStates.clear();
		toStates.add(this.getCurrentState() + 1);
		this.getStates().add(this.addState(StateType.COMMON,
										   this.getCurrentState(),
										   fStates,
										   toStates,
										   Specials.EMPTY_STRING.toString()));
		this.advanceCurrentState();
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