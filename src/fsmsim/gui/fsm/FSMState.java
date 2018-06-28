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
	private final State state;
	private final Map<Integer, Integer> toEndPoints;

	private static final int ARROW_MULTIPLIER = 50;


	public FSMState(final double centerX,
					final double centerY,
					final State state,
					final int multiplier) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.state = state;
		this.kStarX = 0.0;
		this.toEndPoints = new HashMap<>();
		this.init(this.state);
	}

	public FSMState(final double centerX,
					final double centerY,
					final State state) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.kStarX = 0.0;
		this.state = state;
		this.toEndPoints = new HashMap<>();
		this.init(this.state);
	}

	public FSMState(final double centerX,
					final double centerY,
					final double kStarX,
					final State state) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.kStarX = kStarX;
		this.state = state;
		this.toEndPoints = new HashMap<>();
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
	}

	private void createCommonState(final State state) {
		this.getChildren().add(new FSMCircle(this.centerX, this.centerY, state.getStateNumber()));
		this.addArrows(state);
	}

	private void addArrows(final State state) {
		switch(state.getSpecial()) {
			case UNION: {
				this.getChildren().add(new Arrow(this.centerX + 25, this.centerY - 10, this.centerX + 73, this.centerY - 47, state.getSymbol()));
				this.getChildren().add(new Arrow(this.centerX + 25, this.centerY + 10, this.centerX + 73, this.centerY + 47, state.getSymbol()));
				break;
			}
			case TOP_UNION: {
				this.getChildren().add(new Arrow(this.centerX + 27, this.centerY, this.centerX + 75, this.centerY + 37, state.getSymbol()));
				break;
			}
			case BOTTOM_UNION: {
				this.getChildren().add(new Arrow(this.centerX + 27, this.centerY, this.centerX + 73, this.centerY - 37, state.getSymbol()));
				break;
			}
			case THIRD_KSTAR: {
				this.getChildren().add(new Arrow(this.centerX + 27, this.centerY, this.centerX + 73, this.centerY, state.getSymbol()));
				this.getChildren().add(new CurveArrow(true, this.centerX, this.centerY - 27, this.kStarX, this.centerY - 27, state.getSymbol()));
				break;
			}
			case LAST_KSTAR: {
				this.getChildren().add(new Arrow(this.centerX + 27, this.centerY, this.centerX + 73, this.centerY, state.getSymbol()));
				this.getChildren().add(new CurveArrow(false, this.kStarX, this.centerY + 27, this.centerX, this.centerY + 27, state.getSymbol()));
				break;
			}
			default: {
				this.getChildren().add(new Arrow(this.centerX + 27, this.centerY, this.centerX + 73, this.centerY, state.getSymbol()));
				break;
			}
		}
	}

}