package fsmsim.gui.fsm;

import fsmsim.engine.fsm.State;
import fsmsim.engine.fsm.State.StateSpecial;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.layout.Pane;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

public final class FSMGenerator extends Pane {
        private double stateX;
        private double stateY;
        private final List<Double> firstKStarXList;
        private final List<Double> secondKStarXList;
        private final List<Double> topUnionXList;
        private final List<Double> bottomUnionXList;
        private int multiplier;
        private final List<FSMState> fsmStates;
        
        private static final int STATE_MULTIPLIER = 50;

        public FSMGenerator(final List<State> states,
        					final String regexString) {
            this.setPadding(new Insets(20));
            this.stateX = 75.0;
            this.firstKStarXList = new ArrayList<>();
            this.secondKStarXList = new ArrayList<>();
            this.topUnionXList = new ArrayList<>();
            this.bottomUnionXList = new ArrayList<>();
            this.multiplier = this.checkMultiplier(states, 0);

            this.stateY = 100.0 + (this.multiplier * STATE_MULTIPLIER);

            this.fsmStates = this.generateStates(states);
            final Label regexLabel = new Label(regexString);
        	regexLabel.setFont(Font.font(null, FontWeight.BOLD, 30));
        	regexLabel.setPadding(new Insets(10, 20, 10, 20));
        	regexLabel.setTextFill(Color.valueOf("#AF1313"));
            this.getChildren().add(regexLabel);
            this.getChildren().addAll(this.fsmStates);
        }

        private List<FSMState> generateStates(final List<State> states) {
            final List<FSMState> fsmStates = new ArrayList<>();
            final List<Double> unionXList = new ArrayList<>();
            final List<Double> unionYList = new ArrayList<>();
            final List<Double> lastUnionXList = new ArrayList<>();
            final List<Integer> multipliers = new ArrayList<>();

            states.stream().map((state) -> {
                final FSMState fsmState;
                if(state.getSpecial().contains(StateSpecial.UNION)) {
                    multipliers.add(this.checkMultiplier(states, state.getStateNumber()));
                    this.multiplier = multipliers.get(multipliers.size() - 1);
                    unionXList.add(this.stateX);
                    unionYList.add(this.stateY);
                }
                if(multipliers.size() > 1) {
                    if(state.getSpecial().contains(StateSpecial.LAST_UNION)) {
                        this.multiplier = multipliers.remove(multipliers.size() - 1);
                    }
                } else {
                    this.multiplier = (!multipliers.isEmpty()) ? multipliers.get(multipliers.size() - 1) : 0;
                }
                if(state.getSpecial().contains(StateSpecial.KSTAR)) this.firstKStarXList.add(this.stateX);
                if(state.getSpecial().contains(StateSpecial.SECOND_KSTAR)) this.secondKStarXList.add(this.stateX);
                if(state.isInitialState()) {
                    fsmState = this.addInitialState(state);
                } else if(state.isLastState()) {
                    fsmState = this.addLastState(state);
                } else {
                    fsmState = this.addCommonState(state);
                }

                if(state.getSpecial().contains(StateSpecial.UNION)) {
                    this.stateY -= (47 + (STATE_MULTIPLIER * this.multiplier / 2));
                }

                if(state.getSpecial().contains(StateSpecial.TOP_UNION)) {
                    lastUnionXList.add(this.stateX);
                    this.topUnionXList.add(this.stateX);
                    this.stateX = unionXList.get(unionXList.size() - 1);
                    this.multiplier = multipliers.get(multipliers.size() - 1);
                    this.stateY = unionYList.get(unionYList.size() - 1) + (47 + (STATE_MULTIPLIER * this.multiplier / 2));
                    unionXList.remove(unionXList.size() -1);
                }
                if(state.getSpecial().contains(StateSpecial.BOTTOM_UNION)) {
                    this.bottomUnionXList.add(this.stateX);
                    this.stateX = (lastUnionXList.get(lastUnionXList.size() - 1) > this.stateX) ? lastUnionXList.get(lastUnionXList.size() - 1) : this.stateX;
                    this.stateY = unionYList.get(unionYList.size() - 1);
                    unionYList.remove(unionYList.size() - 1);
                    lastUnionXList.remove(lastUnionXList.size() - 1);
                }
                this.stateX += 100;
            return fsmState;
        }).forEachOrdered((fsmState) -> {
            fsmStates.add(fsmState);
        });

            return fsmStates;
        }

        private FSMState addInitialState(final State state) {
            if(state.getSpecial().contains(StateSpecial.UNION)) {
                return new FSMState(this.stateX, this.stateY, state, this.multiplier);
            }
            return new FSMState(this.stateX, this.stateY, state);
        }

        private FSMState addLastState(final State state) {
            if(state.getSpecial().contains(StateSpecial.LAST_UNION)) {
                final double topUnionX = this.getTopUnionX();
                final double bottomUnionX = this.getBottomUnionX();
                return new FSMState(this.stateX, this.stateY, topUnionX, bottomUnionX, state, this.multiplier);
            } else {
                return new FSMState(this.stateX, this.stateY, state);
            }
        }

        private FSMState addCommonState(final State state) {
            if(state.getSpecial().contains(StateSpecial.UNION)) {
                return new FSMState(this.stateX, this.stateY, state, this.multiplier);
            }
            if(state.getSpecial().contains(StateSpecial.LAST_UNION) &&
               state.getSpecial().contains(StateSpecial.THIRD_KSTAR)) {
                final double topUnionX = this.getTopUnionX();
                final double bottomUnionX = this.getBottomUnionX();
                final double secondKStarX = this.getSecondKStarX();
                return new FSMState(this.stateX, this.stateY, secondKStarX, topUnionX, bottomUnionX, state, this.multiplier);
            }

            if(state.getSpecial().contains(StateSpecial.LAST_UNION)) {
                final double topUnionX = this.getTopUnionX();
                final double bottomUnionX = this.getBottomUnionX();
                return new FSMState(this.stateX, this.stateY, topUnionX, bottomUnionX, state, this.multiplier);
            }

            if(state.getSpecial().contains(StateSpecial.THIRD_KSTAR)) {
                final double secondKStarX = this.getSecondKStarX();
                return new FSMState(this.stateX, this.stateY, secondKStarX, state);
            }

            if(state.getSpecial().contains(StateSpecial.LAST_KSTAR)) {
                final double firstKStarX = this.getFirstKStarX();
                return new FSMState(this.stateX, this.stateY, firstKStarX, state);
            }

            return new FSMState(this.stateX, this.stateY, state);
        }

        private double getTopUnionX() {
            final double topUnionX = this.topUnionXList.get(this.topUnionXList.size() - 1);
            this.topUnionXList.remove(this.topUnionXList.size() - 1);

            return topUnionX;
        }

        private double getBottomUnionX() {
            final double bottomUnionX = this.bottomUnionXList.get(this.bottomUnionXList.size() - 1);
            this.bottomUnionXList.remove(this.bottomUnionXList.size() - 1);

            return bottomUnionX;
        }

        private double getFirstKStarX() {
            final double firstKStarX = this.firstKStarXList.get(this.firstKStarXList.size() - 1);
            this.firstKStarXList.remove(this.firstKStarXList.size() - 1);

            return firstKStarX;
        }

        private double getSecondKStarX() {
            final double secondKStarX = this.secondKStarXList.get(this.secondKStarXList.size() - 1);
            this.secondKStarXList.remove(this.secondKStarXList.size() - 1);

            return secondKStarX;	
        }

        private int checkMultiplier(final List<State> states, final int startState) {
            int multiplier = 0;
            final List<Integer> unionList = new ArrayList<>();
            final List<Integer> kStarList = new ArrayList<>();

            for(final State state : states) {
                if(state.getStateNumber() >= startState) {
                    if(state.getSpecial().contains(StateSpecial.UNION)) {
                        ++multiplier;
                        unionList.add(state.getStateNumber());
                    }

                    if(state.getSpecial().contains(StateSpecial.LAST_UNION)) {
                        unionList.remove(unionList.size() - 1);
                        if(unionList.isEmpty()) break;
                    }

                    if(state.getSpecial().contains(StateSpecial.KSTAR)) {
                        kStarList.add(state.getStateNumber());
                    }

                    if(state.getSpecial().contains(StateSpecial.LAST_KSTAR)) {
                        multiplier += (int) (state.getStateNumber() - kStarList.get(kStarList.size() - 1)) / 3;
                        kStarList.remove(kStarList.size() - 1);
                    }
                }
            }

            return multiplier;
        }
}