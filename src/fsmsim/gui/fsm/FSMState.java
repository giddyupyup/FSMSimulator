package fsmsim.gui.fsm;

import fsmsim.engine.fsm.State;
import fsmsim.engine.fsm.State.StateSpecial;
import fsmsim.gui.fsm.utils.Arrow;
import fsmsim.gui.fsm.utils.CurveArrow;
import fsmsim.gui.fsm.utils.FSMCircle;

import java.util.Map;
import java.util.HashMap;

import javafx.scene.Group;

public class FSMState extends Group {

	private final double centerX;
	private final double centerY;
	private final double kStarX;
	private final double topUnionX;
	private final double bottomUnionX;
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
		this.init(this.state);
	}

	public FSMState(final double centerX,
					final double centerY,
					final double topUnionX,
					final double bottomUnionX,
					final State state) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.kStarX = 0.0;
		this.topUnionX = topUnionX;
		this.bottomUnionX = bottomUnionX;
		this.state = state;
		this.init(this.state);
	}

	public double getCenterX() {
		return this.centerX;
	}

	public double getCenterY() {
		return this.centerX;
	}

	private final void init(final State state) {
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
			final double lastTopUnionX = this.topUnionX + (this.centerX - (this.topUnionX + 100));
			final double lastBottomUnionX = this.bottomUnionX + (this.centerX - (this.bottomUnionX + 100));
			this.getChildren().add(new Arrow(this.topUnionX + 27, this.centerY - 47, lastTopUnionX + 75, this.centerY - 13, state.getSymbol()));
			this.getChildren().add(new Arrow(this.bottomUnionX + 27, this.centerY + 47, lastBottomUnionX + 75, this.centerY + 13, state.getSymbol()));
		}
	}

	private void createCommonState(final State state) {
		this.getChildren().add(new FSMCircle(this.centerX, this.centerY, state.getStateNumber()));
		this.addArrows(state);
	}

	private void addArrows(final State state) {
		if(state.getSpecial().contains(StateSpecial.UNION)) {
			this.getChildren().add(new Arrow(this.centerX + 25, this.centerY - 10, this.centerX + 73, this.centerY - 47, state.getSymbol()));
			this.getChildren().add(new Arrow(this.centerX + 25, this.centerY + 10, this.centerX + 73, this.centerY + 47, state.getSymbol()));
		}

		if(state.getSpecial().contains(StateSpecial.LAST_UNION)) {
			System.out.println("check for adding arrows in common state");
			final double lastTopUnionX = this.topUnionX + (this.centerX - (this.topUnionX + 100));
			final double lastBottomUnionX = this.bottomUnionX + (this.centerX - (this.bottomUnionX + 100));
			System.out.println("lastTopUnionX: " + lastTopUnionX);
			System.out.println("lastBottomUnionX: " + lastBottomUnionX);
			this.getChildren().add(new Arrow(this.topUnionX + 27, this.centerY - 47, lastTopUnionX + 75, this.centerY - 13, state.getSymbol()));
			this.getChildren().add(new Arrow(this.bottomUnionX + 27, this.centerY + 47, lastBottomUnionX + 75, this.centerY + 13, state.getSymbol()));
		}

		if(state.getSpecial().contains(StateSpecial.THIRD_KSTAR)) {
			if(!isStateInUnion(state)) this.getChildren().add(new Arrow(this.centerX + 27, this.centerY, this.centerX + 73, this.centerY, state.getSymbol()));
			this.getChildren().add(new CurveArrow(true, this.centerX, this.centerY - 27, this.kStarX, this.centerY - 27, state.getSymbol()));
		} else if(state.getSpecial().contains(StateSpecial.LAST_KSTAR)) {
			if(!isStateInUnion(state)) this.getChildren().add(new Arrow(this.centerX + 27, this.centerY, this.centerX + 73, this.centerY, state.getSymbol()));
			this.getChildren().add(new CurveArrow(false, this.kStarX, this.centerY + 27, this.centerX, this.centerY + 27, state.getSymbol()));
		} else {
			if(!isStateInUnion(state)) this.getChildren().add(new Arrow(this.centerX + 27, this.centerY, this.centerX + 73, this.centerY, state.getSymbol()));
		}
	}

	private boolean isStateInUnion(final State state) {
		return state.getSpecial().contains(StateSpecial.UNION) ||
			   state.getSpecial().contains(StateSpecial.TOP_UNION) ||
		  	   state.getSpecial().contains(StateSpecial.BOTTOM_UNION);
	}

}	