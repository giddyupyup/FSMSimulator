package fsmsim.gui.fsm;

import fsmsim.engine.fsm.State;
import fsmsim.gui.fsm.utils.Arrow;
import fsmsim.gui.fsm.utils.CurveArrow;

import java.util.List;

import javafx.scene.layout.Pane;

public class FSMGenerator extends Pane {
	public FSMGenerator(final List<State> states) {
		if(!states.isEmpty()) System.out.println("Check states");
		this.getChildren().add(new CurveArrow(0, 150, 100, 150, "A"));

		this.getChildren().add(new Arrow(0, 10, 100, 10, "B"));

		this.getChildren().add(new Arrow(0, 30, 100, 100, "C"));

		this.getChildren().add(new Arrow(0, 100, 100, 30, "Double"));
	}
}