package fsmsim.gui.fsm.utils;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FSMCircle extends Group {
	private final static double STATE_RADIUS = 25.0;

	public FSMCircle(final double centerX,
					 final double centerY,
					 final int state) {
		final Circle fsmCircle = new Circle(centerX, centerY, STATE_RADIUS);
		fsmCircle.setFill(null);
		fsmCircle.setStroke(Color.BLACK);
		fsmCircle.setStrokeWidth(3);

		final Label stateLabel = new Label(Integer.toString(state));
		stateLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
		stateLabel.relocate(centerX - 6.3, centerY - 15);

		this.getChildren().addAll(fsmCircle, stateLabel);
	}
}