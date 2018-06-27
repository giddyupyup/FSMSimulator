package fsmsim.gui.fsm;

import fsmsim.engine.fsm.State;
import fsmsim.gui.fsm.utils.Arrow;
import fsmsim.gui.fsm.utils.CurveArrow;
import fsmsim.gui.fsm.utils.FSMCircle;

import java.util.Map;
import java.util.HashMap;

import javafx.scene.Group;

public class FSMState extends Group {

	private final double centerX;
	private final double centerY;
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
		this.toEndPoints = new HashMap<>();
		this.init();
	}

	private void init() {
		if(this.state.isInitialState()) {
			this.createInitialState();
		} else if(this.state.isLastState()) {
			this.createLastState();
		} else {
			this.createNormalState();
		}
	}

	private void createInitialState() {

	}

	private void createLastState() {

	}

	private void createNormalState() {
		
	}

}