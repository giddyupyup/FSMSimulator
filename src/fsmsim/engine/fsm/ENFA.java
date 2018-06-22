package fsmsim.engine.fsm;

import fsmsim.engine.Specials;
import fsmsim.engine.regex.Tree;
import fsmsim.engine.regex.Node;
import fsmsim.engine.fsm.State.StateType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ENFA implements FSM {

    private int initialState;
    private int lastState;
    private int currentState;
    private List<State> states;


    public ENFA() {
        this.currentState = 0;
        this.initialState = 0;
        this.lastState = 0;
        this.states = new ArrayList<>();
    }

    @Override
    public void create(final Tree regexTree) {
        this.states.addAll(this.createAltFSM(null, regexTree.getParseNode(), true));
        // System.out.println("Get the parseNode: " + regexTree.getParseNode());
        // System.out.println("Get the parseNodeList: " + regexTree.getParseNode().getNodeList().isEmpty());
        // final List<Integer> toStates = new ArrayList<>();
        // final List<Integer> fromStates = new ArrayList<>();
        // this.getStates().add(this.createState(StateType.INITIAL,
        //                                    this.getCurrentState(),
        //                                    null,
        //                                    toStates,
        //                                    Specials.EMPTY_STRING.toString()));
        // this.advanceCurrentState();
        // final State initState = this.getState(0);
        // for(final Node node : regexTree.getParseNode().getNodeList()) { //creates the upper union transition
        //     initState.addToStates(this.getCurrentState());
        //     final List<Integer> _initFromStates = new ArrayList<>();
        //     _initFromStates.add(initState.getStateNumber());
        //     this.createStates(_initFromStates, node);
        //     fromStates.add(this.getCurrentState() - 1);
        // }

        // this.getStates().add(this.createState(StateType.LAST,
        //                                    this.getCurrentState(),
        //                                    fromStates,
        //                                    null,
        //                                    null));
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

    private List<State> createAltFSM(final List<Integer> fromStates,
                                     final Node node,
                                     final boolean initial) {
        final List<State> altStates = new ArrayList<>();
        final List<Integer> toStates = new ArrayList<>();
        final List<Integer> lastFromStates = new ArrayList<>();
        State firstState;

        if(initial) {
            firstState = this.createState(StateType.INITIAL,
                                    this.getCurrentState(),
                                    null,
                                    toStates,
                                    Specials.EMPTY_STRING.toString())
            altStates.add(firstState);
            this.advanceCurrentState();
        } else {
            firstState = this.createState(StateType.COMMON,
                                    this.getCurrentState(),
                                    fromStates,
                                    toStates,
                                    Specials.EMPTY_STRING.toString())
            altStates.add(firstState);
            this.advanceCurrentState();
        }

        for(final Node node : node.getNodeList()) {
            firstState.addToStates(this.getCurrentState());
            List<Integer> innerFromStates = new ArrayList<>();
            innerFromStates.add(this.getCurrentState() -1);
            altStates.addAll(this.createSeqFSM(innerFromStates, node));
            lastFromStates.add(this.getCurrentState() - 1);
        }

        altStates.add(this.createState(StateType.LAST,
                                       this.getCurrentState(),
                                       lastFromStates,
                                       null,
                                       null));



        return altStates;
    }

    private List<State> createSeqFSM(final List<Integer> fromStates,
                                     final Node node) {
        final List<State> seqStates = new ArrayList<>();

        for(final Node node : node.getNodeList()) {
            seqStates.addAll(this.createStates(fromStates, node));
        }

        return seqStates;
    }

    private List<State> createStates(final List<Integer> fromStates,
                                     final Node node) {
        final List<State> states = new ArrayList<>();

        if(node.getNodeType().isSymbol() || node.getNodeType().isEmptyString()) {
            states.addAll(this.createSymbolFSM(fromStates, node));
        }

        if(node.getNodeType().isKStar()) {
            states.addAll(this.createKStarFSM(fromStates, node));
        }

        if(node.getNodeType().isUnion()) { //creates the inner union transition states
            states.addAll(this.createAltFSM(fromStates, node, false));
        }

        return states;
    }

    private List<State> createKStarFSM(final List<Integer> fromStates,
                                       final Node node) {
        final List<State> kStarStates = new ArrayList<>();
        
        final List<Integer> toStates = new ArrayList<>();
        final List<Integer> fStates = new ArrayList<>();
        final State firstState;
        final State secondState;

        //create the first state
        toStates.add(this.getCurrentState() + 1);
        toStates.add(this.getCurrentState() + 3);
        fStates.addAll(fromStates);
        this.getStates().add(this.createState(StateType.COMMON,
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
            this.getStates().add(this.createState(StateType.COMMON,
                                               this.getCurrentState(),
                                               fStates,
                                               toStates,
                                               node.getLiteral()));
            secondState = this.getState(this.getCurrentState());
            this.advanceCurrentState();
        } else {
            toStates.add(this.getCurrentState() + 1);
            fStates.add(this.getCurrentState() - 1);
            this.getStates().add(this.createState(StateType.COMMON,
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
        this.getStates().add(this.createState(StateType.COMMON,
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
        this.getStates().add(this.createState(StateType.COMMON,
                                           this.getCurrentState(),
                                           fStates,
                                           toStates,
                                           Specials.EMPTY_STRING.toString()));
        this.advanceCurrentState();
    }

    private LIst<State> createSymbolFSM(final List<Integer> fromStates,
                                    final Node node) {
        final List<Integer> toStates = new ArrayList<>();
        toStates.add(this.getCurrentState() + 1);
        this.getStates().add(this.createState(StateType.COMMON,
                                           this.getCurrentState(),
                                           fromStates,
                                           toStates,
                                           node.getLiteral()));
        this.advanceCurrentState();
        final List<Integer> fStates = new ArrayList<>();
        fStates.add(this.getCurrentState() - 1);
        toStates.clear();
        toStates.add(this.getCurrentState() + 1);
        this.getStates().add(this.createState(StateType.COMMON,
                                           this.getCurrentState(),
                                           fStates,
                                           toStates,
                                           Specials.EMPTY_STRING.toString()));
        this.advanceCurrentState();
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