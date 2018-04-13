package fsmsim.engine.fsm;

public class State {
	private boolean initialState;
	private boolean lastState;
	private List<Integer> toStates;
	private int fromState;
	private int stateNumber;
	private String symbol;

	public State(final boolean initialState,
				 final boolean lastState,
				 final List<Integer> toStates,
				 final int fromState,
				 final int stateNumber,
				 final String symbol) {
		this.initialState = initialState;
		this.lastState = lastState;
		this.toStates = toStates;
		this.fromState = fromState;
		this.stateNumber = stateNumber;
		this.symbol = symbol;
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

	public int getFromState() {
		return this.fromState;
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
}