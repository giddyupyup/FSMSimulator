package fsmsim.gui.fsm;

import fsmsim.engine.fsm.State;
import fsmsim.gui.fsm.utils.Arrow;
import fsmsim.gui.fsm.utils.CurveArrow;
import fsmsim.gui.fsm.utils.FSMCircle;

import java.util.Map;
import java.util.HashMap;

import javafx.scene.Group;

public class FSMState extends Group {

	private final Map<Integer, Integer> toEndPoints;


	public FSMState(final double centerX,
					final double centerY,
					final State state) {
		this.toEndPoints = new HashMap<>();
	}

}