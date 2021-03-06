package fsmsim.engine.fsm;

import fsmsim.engine.Specials;
import fsmsim.engine.regex.Tree;
import fsmsim.engine.regex.Node;
import fsmsim.engine.fsm.State.StateType;
import fsmsim.engine.fsm.State.StateSpecial;

import java.util.ArrayList;
import java.util.List;

public class ENFA implements FSM {

    private final int initialState;
    private int lastState;
    private int currentState;
    private final List<State> states;


    public ENFA() {
        this.currentState = 0;
        this.initialState = 0;
        this.lastState = 0;
        this.states = new ArrayList<>();
    }

    @Override
    public void create(final Tree regexTree) {
        this.states.addAll(this.createAltFSM(null, regexTree.getParseNode(), true));
    }

    public List<State> getStates() {
        return this.states;
    }

    private State getState(final List<State> states, final int stateNumber) {
        State returnState = new State(stateNumber, null, null, null);
        for(final State state : states) {
            if(stateNumber == state.getStateNumber()) {
                returnState = state;
                break;
            }
        }
        return returnState;
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

    private List<State> createAltFSM(final List<Integer> fromStates,
                                     final Node node,
                                     final boolean initial) {
        final List<State> altStates = new ArrayList<>();
        final List<Integer> firstAltToStates = new ArrayList<>();
        final List<Integer> lastAltFromStates = new ArrayList<>();
        final List<State> getLastStates = new ArrayList<>();

        final State firstState;
        final State lastState;

        if(initial) {
            firstState = this.createState(StateType.INITIAL,
                                    this.getCurrentState(),
                                    null,
                                    firstAltToStates,
                                    Specials.EMPTY_STRING.toString());
            altStates.add(firstState);
            this.advanceCurrentState();
        } else {
            firstState = this.createState(StateType.COMMON,
                                    this.getCurrentState(),
                                    fromStates,
                                    firstAltToStates,
                                    Specials.EMPTY_STRING.toString());
            altStates.add(firstState);
            this.advanceCurrentState();
        }

        node.getNodeList().stream().map((innerNode) -> {
            firstState.addToStates(this.getCurrentState());
            final List<Integer> innerFromStates = new ArrayList<>();
            innerFromStates.add(firstState.getStateNumber());
            altStates.addAll(this.createSeqFSM(innerFromStates, innerNode));
            return innerNode;
        }).map((_item) -> {
            getLastStates.add(this.getState(altStates, this.getCurrentState() - 1));
            return _item;
        }).forEachOrdered((_item) -> {
            lastAltFromStates.add(this.getCurrentState() - 1);
        });

        if(!getLastStates.isEmpty()) {
            if(getLastStates.size() > 1) {
                boolean top = true;

                for(final State getState : getLastStates) {
                    getState.addToStates(this.getCurrentState());
                    if(top) {
                        getState.updateSpecial(StateSpecial.TOP_UNION);
                        top = false;
                    } else {
                        getState.updateSpecial(StateSpecial.BOTTOM_UNION);
                        top = true;
                    }
                }
            } else {
                getLastStates.forEach((getState) -> {
                    getState.addToStates(this.getCurrentState());
                });
            }
        }

        if(initial) {
            lastState = this.createState(StateType.LAST,
                                         this.getCurrentState(),
                                         lastAltFromStates,
                                         null,
                                         null);
            altStates.add(lastState);
        } else {
            final List<Integer> lastAltToStates = new ArrayList<>();
            lastState = this.createState(StateType.COMMON,
                                         this.getCurrentState(),
                                         lastAltFromStates,
                                         lastAltToStates,
                                         Specials.EMPTY_STRING.toString());
            altStates.add(lastState);
            this.advanceCurrentState();
        }

        if(!firstState.getToStates().isEmpty() &&
            firstState.getToStates().size() > 1) {
            firstState.updateSpecial(StateSpecial.UNION);
            lastState.updateSpecial(StateSpecial.LAST_UNION);
        }

        return altStates;
    }

    private List<State> createSeqFSM(final List<Integer> fromStates,
                                     final Node node) {
        final List<State> seqStates = new ArrayList<>();

        node.getNodeList().forEach((innerNode) -> {
            seqStates.addAll(this.createStates(fromStates, innerNode));
        });

        return seqStates;
    }

    private List<State> createStates(final List<Integer> fromStates,
                                     final Node node) {
        final List<State> states = new ArrayList<>();

        if(node.getNodeType().isSymbol() || node.getNodeType().isEmptyString()) {
            states.addAll(this.createSymbolFSM(fromStates, node));
        }

        if(node.getNodeType().isKStar()) {
            states.addAll(this.createKStarFSM(fromStates, node.getNode()));
        }

        if(node.getNodeType().isUnion()) { //creates the inner union transition states
            states.addAll(this.createAltFSM(fromStates, node, false));
        }

        return states;
    }

    private List<State> createKStarFSM(final List<Integer> fromStates,
                                       final Node node) {
        final List<State> kStarStates = new ArrayList<>();
        final State firstState;
        final State secondState;
        final State thirdState;
        final State lastState;
        
        final List<Integer> firstToStates = new ArrayList<>();

        //create the first state
        firstToStates.add(this.getCurrentState() + 1);
        firstState = this.createState(StateType.COMMON,
                                      this.getCurrentState(),
                                      fromStates,
                                      firstToStates,
                                      Specials.EMPTY_STRING.toString());
        firstState.updateSpecial(StateSpecial.KSTAR);
        kStarStates.add(firstState);        
        this.advanceCurrentState();

        //get the number for the next state
        final int secondStateNumber = this.getCurrentState();

        //create the second and third state
        final List<Integer> secondAndThirdFromStates = new ArrayList<>();
        secondAndThirdFromStates.add(this.getCurrentState() - 1);
        if(node.getNodeType().isSymbol() || node.getNodeType().isEmptyString()) {
            kStarStates.addAll(this.createSymbolFSM(secondAndThirdFromStates, node));
        } else {
            if(node.getNodeType().isKStar()) {
                kStarStates.addAll(this.createKStarFSM(secondAndThirdFromStates, node));
            }

            if(node.getNodeType().isUnion()) {
                kStarStates.addAll(this.createAltFSM(secondAndThirdFromStates, node, false));
            }
        }

        secondState = this.getState(kStarStates, secondStateNumber);
        secondState.updateSpecial(StateSpecial.SECOND_KSTAR);
        thirdState = this.getState(kStarStates, this.getCurrentState() - 1);
        thirdState.updateSpecial(StateSpecial.THIRD_KSTAR);

        firstState.addToStates(this.getCurrentState());
        secondState.addFromStates(this.getCurrentState() - 1);
        thirdState.addToStates(secondStateNumber);
        thirdState.addToStates(this.getCurrentState());

        final List<Integer> lastFromStates = new ArrayList<>();
        final List<Integer> lastToStates = new ArrayList<>();

        //create the last state
        lastFromStates.add(thirdState.getStateNumber());
        lastFromStates.add(firstState.getStateNumber());

        lastState = this.createState(StateType.COMMON,
                                           this.getCurrentState(),
                                           lastFromStates,
                                           lastToStates,
                                           Specials.EMPTY_STRING.toString());
        lastState.updateSpecial(StateSpecial.LAST_KSTAR);
        kStarStates.add(lastState);
        this.advanceCurrentState();

        return kStarStates;
    }

    private List<State> createSymbolFSM(final List<Integer> fromStates,
                                        final Node node) {
        final List<State> symbolStates = new ArrayList<>();

        final List<Integer> symbolToState = new ArrayList<>();
        symbolToState.add(this.getCurrentState() + 1);
        symbolStates.add(this.createState(StateType.COMMON,
                                           this.getCurrentState(),
                                           fromStates,
                                           symbolToState,
                                           node.getLiteral()));
        this.advanceCurrentState();

        final List<Integer> epsFromStates = new ArrayList<>();
        final List<Integer> epsToStates = new ArrayList<>();

        epsFromStates.add(this.getCurrentState() - 1);
        symbolStates.add(this.createState(StateType.COMMON,
                                           this.getCurrentState(),
                                           epsFromStates,
                                           epsToStates,
                                           Specials.EMPTY_STRING.toString()));
        this.advanceCurrentState();

        return symbolStates;
    }

    private State createState(final StateType stateType,
                           final int currentState,
                           final List<Integer> fromStates,
                           final List<Integer> toStates,
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
                               final List<Integer> toStates,
                               final String symbol) {
        return new State(state.getStateNumber(), state.getFromStates(), toStates, symbol);
    }

}