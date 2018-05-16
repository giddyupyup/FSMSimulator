package fsmsim.engine.fsm;

import java.util.ArrayList;
import java.util.List;

public class State {
	final private boolean initialState;
	final private boolean lastState;
	final private int stateNumber;
	final private String symbol;
	private List<Integer> toStates;
	private List<Integer> fromStates;

	public State(final int stateNumber,
				 final List<Integer> fromStates,
				 final List<Integer> toStates,
				 final String symbol) {
		this.initialState = false;
		this.lastState = false;
		this.toStates = toStates;
		this.fromStates = fromStates;
		this.stateNumber = stateNumber;
		this.symbol = symbol;
	}

	public State(final int stateNumber,
				 final List<Integer> toStates,
				 final String symbol) {
		this.initialState = true;
		this.lastState = false;
		this.toStates = toStates;
		this.fromStates = null;
		this.stateNumber = stateNumber;
		this.symbol = symbol;
	}

	public State(final int stateNumber,
				 final List<Integer> fromStates) {
		this.initialState = false;
		this.lastState = true;
		this.toStates = null;
		this.fromStates = fromStates;
		this.stateNumber = stateNumber;
		this.symbol = null;
	}

	public boolean isInitialState() {
		return this.initialState;
	}

	public boolean isLastState() {
		return this.lastState;
	}

	public List<Integer> getToStates() {
		return this.toStates;
	}

	public List<Integer> getFromStates() {
		return this.fromStates;
	}

	public List<Integer> toStates() {
		return this.toStates;
	}

	public int getStateNumber() {
		return this.stateNumber;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void addToStates(final int state) {
		this.toStates.add(state);
	}

	public void addFromStates(final int state) {
		this.fromStates.add(state);
	}

	public enum StateType {
		INITIAL,
		COMMON,
		LAST
	}
}