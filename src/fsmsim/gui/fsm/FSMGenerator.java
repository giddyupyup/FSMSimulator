package fsmsim.gui.fsm;

import fsmsim.engine.fsm.State;
import fsmsim.gui.fsm.utils.Arrow;
import fsmsim.gui.fsm.utils.CurveArrow;
import fsmsim.gui.fsm.utils.FSMCircle;

import java.util.List;

import javafx.scene.layout.Pane;
import javafx.geometry.Insets;

public class FSMGenerator extends Pane {
	private double stateX;
	private double stateY;

	public FSMGenerator(final List<State> states) {
		this.setPadding(new Insets(20));



		this.stateX = 100;
		this.stateY = 75.0 * ((int)(states.size() / 5) + 1);

		System.out.println("stateX: " + stateX);
		System.out.println("stateY: " + stateY);

		for(final State state : states) {

		}

		//REGULAR FSM

		this.getChildren().add(new Arrow(50, 75, 73, 75, ""));

		this.getChildren().add(new FSMCircle(100, 75, 0)); // +50 for x , y should be the endY of previous state

		this.getChildren().add(new Arrow(127, 75, 173, 75, "$")); // +27 -> startX, +73 for endX, the same for y

		this.getChildren().add(new FSMCircle(200, 75, 1)); //+100 for x, y should be the endY of previous state

		this.getChildren().add(new Arrow(227, 75, 273, 75, "a ")); // +27 -> startX, +73 for endX, the same for y

		this.getChildren().add(new FSMCircle(300, 75, 2)); //+100 for x, y should be the endY of previous state

		this.getChildren().add(new Arrow(327, 75, 373, 75, "$ ")); // +27 -> startX, +73 for endX, the same for y

		this.getChildren().add(new FSMCircle(405, 75, 3, true)); //+100 for x, y should be the endY of previous state


		//UNION FSM

		this.getChildren().add(new Arrow(50, 300, 73, 300, ""));

		this.getChildren().add(new FSMCircle(100, 300, 0)); // +50 for x , y should be the endY of previous state

		this.getChildren().add(new Arrow(125, 290, 173, 253, "$")); //up +25 for startX, +73 for endX; -10 for startY, -47 for endY

		this.getChildren().add(new Arrow(125, 310, 173, 347, "$")); //down +25 for startX, +73 for endX; +10 for startY, +47 for endY

		//up union

		this.getChildren().add(new FSMCircle(200, 253, 1)); //+100 for x, y should be the endY of previous state

		this.getChildren().add(new Arrow(227, 253, 273, 253, "a")); // +27 -> startX, +73 for endX, the same for y

		this.getChildren().add(new FSMCircle(300, 253, 2)); //+100 for x, y should be the endY of previous state

		this.getChildren().add(new Arrow(327, 253, 375, 290, "$")); //down +27 for startX, +75 for endX; +0 for startY, +37 for endY

		//down union

		this.getChildren().add(new FSMCircle(200, 347, 3));  //+100 for x, y should be the endY of previous state

		this.getChildren().add(new Arrow(227, 347, 273, 347, "b")); // +27 -> startX, +73 for endX, the same for y

		this.getChildren().add(new FSMCircle(300, 347, 4)); //+100 for x, y should be the endY of previous state

		this.getChildren().add(new Arrow(327, 347, 375, 310, "$")); //up +27 for startX, +75 for endX; +0 for startY, -37 for endY

		this.getChildren().add(new FSMCircle(405, 300, 5, true)); //+100 for x, b is -100 from x


		//KSTAR FSM
		this.getChildren().add(new Arrow(50, 500, 73, 500, ""));

		this.getChildren().add(new FSMCircle(100, 500, 0)); // +50 for x , y should be the endY of previous state

		this.getChildren().add(new Arrow(127, 500, 173, 500, "$")); // +27 -> startX, +73 for endX, the same for y

		this.getChildren().add(new CurveArrow(false, 100, 527, 400, 527, "$")); //startX from stateX, endX +300; startY, endY = +27

		this.getChildren().add(new FSMCircle(200, 500, 1)); //+100 for x, y should be the endY of previous state

		this.getChildren().add(new Arrow(227, 500, 273, 500, "a ")); // +27 -> startX, +73 for endX, the same for y

		this.getChildren().add(new FSMCircle(300, 500, 2)); //+100 for x, y should be the endY of previous state

		this.getChildren().add(new Arrow(327, 500, 373, 500, "$ ")); // +27 -> startX, +73 for endX, the same for y

		this.getChildren().add(new CurveArrow(true, 300, 473, 200, 473, "$")); //startX from stateX, endX -> startX - 100; startY, endY = -27

		this.getChildren().add(new FSMCircle(400, 500, 3)); //+105 for x, y should be the endY of previous state

	}
}