package fsmsim.gui.fsm;

import fsmsim.engine.fsm.State;
import fsmsim.gui.fsm.utils.Arrow;
import fsmsim.gui.fsm.utils.CurveArrow;
import fsmsim.gui.fsm.utils.FSMCircle;

import java.util.List;

import javafx.scene.layout.Pane;

public class FSMGenerator extends Pane {
	public FSMGenerator(final List<State> states) {
		if(!states.isEmpty()) System.out.println("Check states");
		this.getChildren().add(new CurveArrow(true, 0, 300, 300, 300, "A"));

		this.getChildren().add(new CurveArrow(false, 0, 350, 100, 350, "E"));

		this.getChildren().add(new Arrow(0, 20, 500, 20, "B"));

		this.getChildren().add(new Arrow(0, 40, 100, 110, "C"));

		this.getChildren().add(new Arrow(0, 220, 100, 120, "D"));

		this.getChildren().add(new FSMCircle(200, 200, 0));
	}
}