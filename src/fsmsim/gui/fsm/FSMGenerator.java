package fsmsim.gui.fsm;

import fsmsim.engine.fsm.State;
import fsmsim.engine.fsm.State.StateSpecial;
import fsmsim.gui.fsm.utils.Arrow;
import fsmsim.gui.fsm.utils.CurveArrow;
import fsmsim.gui.fsm.utils.FSMCircle;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.layout.Pane;
import javafx.geometry.Insets;

public final class FSMGenerator extends Pane {
	private double stateX;
	private double stateY;
	private double topUnionX;
	private double bottomUnionX;
	private double firstKStarX;
	private double secondKStarX;
	private int multiplier;
	private List<FSMState> fsmStates;

	public FSMGenerator(final List<State> states) {
		this.setPadding(new Insets(20));
		this.stateX = 75.0;
		this.firstKStarX = 0.0;
		this.secondKStarX = 0.0;
		this.topUnionX = 0.0;
		this.bottomUnionX = 0.0;
		this.multiplier = 0;

		// for(final State state : states) {
		// 	if(state.getSpecial().contains(StateSpecial.UNION) || state.getSpecial().contains(StateSpecial.KSTAR)) ++this.multiplier;
		// }

		this.stateY = 200.0 * (multiplier + 1);

		System.out.println("multiplier: " + multiplier);

		System.out.println("stateX: " + stateX);
		System.out.println("stateY: " + stateY);

		this.fsmStates = this.init(states);

		this.getChildren().addAll(this.fsmStates);
	}

	private final List<FSMState> init(final List<State> states) {
		final List<FSMState> fsmStates = new ArrayList<>();
		double unionX = 0.0;
		double unionY = 0.0;
		double lastUnionX = 0.0;

		for(final State state : states) {
			final FSMState fsmState;
			if(state.getSpecial().contains(StateSpecial.UNION)) {
				unionX = this.stateX;
				unionY = this.stateY;
			}

			if(state.getSpecial().contains(StateSpecial.KSTAR)) this.firstKStarX = this.stateX;
			if(state.getSpecial().contains(StateSpecial.SECOND_KSTAR)) this.secondKStarX = this.stateX;

			if(state.isInitialState()) {
				fsmState = this.addInitialState(state);
			} else if(state.isLastState()) {
				fsmState = this.addLastState(state);
			} else {
				fsmState = this.addCommonState(state);
			}
			if(state.getSpecial().contains(StateSpecial.UNION)) this.stateY -= 47;
			if(state.getSpecial().contains(StateSpecial.TOP_UNION)) {
				lastUnionX = this.topUnionX = this.stateX;
				System.out.println("topUnionX: " + this.topUnionX);
				System.out.println("lastUnionX: " + lastUnionX);
				this.stateX = unionX;
				this.stateY = unionY + 47;	
			}

			if(state.getSpecial().contains(StateSpecial.BOTTOM_UNION)) {
				this.bottomUnionX = this.stateX;
				this.stateX = (lastUnionX > this.stateX) ? lastUnionX : this.stateX;
				System.out.println("bottomUnionX: " + this.bottomUnionX);
				System.out.println("stateX: " + this.stateX);
				this.stateY = unionY;
			}

			this.stateX += 100;


			fsmStates.add(fsmState);
		}

		return fsmStates;
	}

	private FSMState addInitialState(final State state) {
		return new FSMState(this.stateX, this.stateY, state);
	}

	private FSMState addLastState(final State state) {
		if(state.getSpecial().contains(StateSpecial.LAST_UNION)) {
			return new FSMState(this.stateX, this.stateY, this.topUnionX, this.bottomUnionX, state);
		} else {
			return new FSMState(this.stateX, this.stateY, state);
		}
	}

	private FSMState addCommonState(final State state) {
		if(state.getSpecial().contains(StateSpecial.LAST_UNION)) {
			return new FSMState(this.stateX, this.stateY, this.topUnionX, this.bottomUnionX, state);
		}
		if(state.getSpecial().contains(StateSpecial.THIRD_KSTAR)) {
			return new FSMState(this.stateX, this.stateY, this.secondKStarX, state);
		} else if(state.getSpecial().contains(StateSpecial.LAST_KSTAR)) {
			return new FSMState(this.stateX, this.stateY, this.firstKStarX, state);
		} else {
			return new FSMState(this.stateX, this.stateY, state);
		}
	}
}