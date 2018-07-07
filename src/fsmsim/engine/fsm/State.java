package fsmsim.engine.fsm;

import java.util.ArrayList;
import java.util.List;

public class State {
    final private boolean initialState;
    final private boolean lastState;
    final private int stateNumber;
    final private String symbol;
    private final List<StateSpecial> stateSpecial;
    private final List<Integer> toStates;
    private final List<Integer> fromStates;

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
        this.stateSpecial = new ArrayList<>();
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
        this.stateSpecial = new ArrayList<>();
    }

    public State(final int stateNumber,
                 final List<Integer> fromStates) {
        this.initialState = false;
        this.lastState = true;
        this.toStates = null;
        this.fromStates = fromStates;
        this.stateNumber = stateNumber;
        this.symbol = null;
        this.stateSpecial = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.stateNumber;
        result = prime * result + this.toStates.hashCode();
        result = prime * result + this.fromStates.hashCode();
        result = prime * result + this.symbol.hashCode();
        result = prime * result + this.stateSpecial.hashCode();
        
        return result;
    }

    @Override
    public boolean equals(final Object other) {
        if(this == other) {
            return true;
        }

        if(!(other instanceof State)) {
            return false;
        }

        final State otherState = (State) other;
        return this.getStateNumber() == otherState.getStateNumber() &&
               this.getSymbol() == otherState.getSymbol() &&
               this.getSpecial().equals(otherState.getSpecial()) &&
               this.isInitialState() == otherState.isInitialState() &&
               this.isLastState() == otherState.isLastState() &&
               this.getToStates().equals(otherState.getToStates()) &&
               this.getFromStates().equals(otherState.getFromStates());

    }

    public void updateSpecial(final StateSpecial stateSpecial) {
        this.stateSpecial.add(stateSpecial);
    }

    public List<StateSpecial> getSpecial() {
        return this.stateSpecial;
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

    @Override
    public String toString() {
        return "STATE - " + this.stateNumber + " Special: " + this.stateSpecial;
    }

    public enum StateType {
        INITIAL,
        COMMON,
        LAST
    }

    public enum StateSpecial {
        UNION,
        TOP_UNION,
        BOTTOM_UNION,
        LAST_UNION,
        KSTAR,
        SECOND_KSTAR,
        THIRD_KSTAR,
        LAST_KSTAR,
        NONE
    }
}