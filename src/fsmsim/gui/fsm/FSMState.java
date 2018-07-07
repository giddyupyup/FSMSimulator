package fsmsim.gui.fsm;

import fsmsim.engine.fsm.State;
import fsmsim.engine.fsm.State.StateSpecial;
import fsmsim.gui.fsm.utils.Arrow;
import fsmsim.gui.fsm.utils.CurveArrow;
import fsmsim.gui.fsm.utils.FSMCircle;

import javafx.scene.Group;

public class FSMState extends Group {

	private final double centerX;
	private final double centerY;
	private final double kStarX;
	private final double topUnionX;
	private final double bottomUnionX;
        private final int multiplier;
	private final State state;

	private static final int ARROW_MULTIPLIER = 50;


	public FSMState(final double centerX,
                        final double centerY,
                        final State state,
                        final int multiplier) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.state = state;
            this.kStarX = 0.0;
            this.topUnionX = 0.0;
            this.bottomUnionX = 0.0;
            this.multiplier = multiplier;
            this.init(this.state);
	}

	public FSMState(final double centerX,
                        final double centerY,
                        final State state) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.kStarX = 0.0;
            this.topUnionX = 0.0;
            this.bottomUnionX = 0.0;
            this.state = state;
            this.multiplier = 0;
            this.init(this.state);
	}

	public FSMState(final double centerX,
                        final double centerY,
                        final double kStarX,
                        final State state) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.kStarX = kStarX;
            this.topUnionX = 0.0;
            this.bottomUnionX = 0.0;
            this.state = state;
            this.multiplier = 0;
            this.init(this.state);
	}

	public FSMState(final double centerX,
                        final double centerY,
                        final double topUnionX,
                        final double bottomUnionX,
                        final State state,
                        final int multiplier) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.kStarX = 0.0;
            this.topUnionX = topUnionX;
            this.bottomUnionX = bottomUnionX;
            this.state = state;
            this.multiplier = multiplier;
            this.init(this.state);
	}

	public FSMState(final double centerX,
                        final double centerY,
                        final double kStarX,
                        final double topUnionX,
                        final double bottomUnionX,
                        final State state,
                        final int multiplier) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.kStarX = kStarX;
            this.topUnionX = topUnionX;
            this.bottomUnionX = bottomUnionX;
            this.state = state;
            this.multiplier = multiplier;
            this.init(this.state);
	}

	public double getCenterX() {
		return this.centerX;
	}

	public double getCenterY() {
		return this.centerX;
	}

	private void init(final State state) {
            if(state.isInitialState()) {
                this.createInitialState(state);
            } else if(state.isLastState()) {
                this.createLastState(state);
            } else {
                this.createCommonState(state);
            }
	}

	private void createInitialState(final State state) {
            this.getChildren().add(new Arrow(this.centerX - 50, this.centerY, this.centerX - 28, this.centerY, ""));
            this.getChildren().add(new FSMCircle(this.centerX, this.centerY, state.getStateNumber()));
            this.addArrows(state);
	}

	private void createLastState(final State state) {
            this.getChildren().add(new FSMCircle(this.centerX + 5, this.centerY, state.getStateNumber(), true));
            if(state.getSpecial().contains(StateSpecial.LAST_UNION)) {
                this.addLastUnionArrows(state);
            }
	}

	private void createCommonState(final State state) {
            this.getChildren().add(new FSMCircle(this.centerX, this.centerY, state.getStateNumber()));
            this.addArrows(state);
	}

	private void addArrows(final State state) {
            if(state.getSpecial().contains(StateSpecial.LAST_UNION) && 
               state.getSpecial().contains(StateSpecial.THIRD_KSTAR)) {
                this.addLastUnionAndThirdKStarArrows(state);
            } else if(state.getSpecial().contains(StateSpecial.UNION)) {
                this.addUnionArrows(state);
            } else if(state.getSpecial().contains(StateSpecial.LAST_UNION)) {
                this.addLastUnionArrows(state);
            } else if(state.getSpecial().contains(StateSpecial.THIRD_KSTAR)) {
                this.addThirdKStarArrows(state);
            } else if(state.getSpecial().contains(StateSpecial.LAST_KSTAR)) {
                this.addLastKStarArrows(state);
            } else {
                this.addCommonArrows(state);
            }
	}

	private void addCommonArrows(final State state) {
            if(!this.isStateInUnion(state)) this.getChildren().add(new Arrow(this.centerX + 27, this.centerY, this.centerX + 73, this.centerY, state.getSymbol()));
	}

	private void addUnionArrows(final State state) {
            this.getChildren().add(new Arrow(this.centerX + 25, this.centerY - 10, this.centerX + 73, this.centerY - (47 + (ARROW_MULTIPLIER * this.multiplier / 2)), state.getSymbol()));
            this.getChildren().add(new Arrow(this.centerX + 25, this.centerY + 10, this.centerX + 73, this.centerY + (47 + (ARROW_MULTIPLIER * this.multiplier / 2)), state.getSymbol()));
	}

	private void addLastUnionArrows(final State state) {
            final double lastTopUnionX = this.topUnionX + (this.centerX - (this.topUnionX + 100));
            final double lastBottomUnionX = this.bottomUnionX + (this.centerX - (this.bottomUnionX + 100));
            this.getChildren().add(new Arrow(this.topUnionX + 27, this.centerY - (47 + (ARROW_MULTIPLIER * this.multiplier / 2)), lastTopUnionX + 75, this.centerY - 13, "$"));
            this.getChildren().add(new Arrow(this.bottomUnionX + 27, this.centerY + (47 + (ARROW_MULTIPLIER * this.multiplier / 2)), lastBottomUnionX + 75, this.centerY + 13, "$"));
	}

	private void addThirdKStarArrows(final State state) {
            this.addCommonArrows(state);
            this.getChildren().add(new CurveArrow(true, this.centerX, this.centerY - 27, this.kStarX, this.centerY - 27, state.getSymbol()));
	}

	private void addLastKStarArrows(final State state) {
            this.addCommonArrows(state);
            this.getChildren().add(new CurveArrow(false, this.kStarX, this.centerY + 27, this.centerX, this.centerY + 27, state.getSymbol()));
	}

	private void addLastUnionAndThirdKStarArrows(final State state) {
            this.addLastUnionArrows(state);
            this.addThirdKStarArrows(state);
	}

	private boolean isStateInUnion(final State state) {
            return state.getSpecial().contains(StateSpecial.UNION) ||
                   state.getSpecial().contains(StateSpecial.TOP_UNION) ||
                   state.getSpecial().contains(StateSpecial.BOTTOM_UNION);
	}

}	