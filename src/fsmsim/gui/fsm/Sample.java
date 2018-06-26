package fsmsim.gui.fsm;

//sample
import javafx.scene.shape.Path;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.ArcTo;

public class Sample extends Path {
	public Sample() {
		MoveTo moveTo = new MoveTo();
		moveTo.setX(0.0f);
		moveTo.setY(0.0f);

		HLineTo hLineTo = new HLineTo();
		hLineTo.setX(70.0f);

		QuadCurveTo quadCurveTo = new QuadCurveTo();
		quadCurveTo.setX(120.0f);
		quadCurveTo.setY(60.0f);
		quadCurveTo.setControlX(100.0f);
		quadCurveTo.setControlY(0.0f);

		LineTo lineTo = new LineTo();
		lineTo.setX(175.0f);
		lineTo.setY(55.0f);

		ArcTo arcTo = new ArcTo();
		arcTo.setX(50.0f);
		arcTo.setY(50.0f);
		arcTo.setRadiusX(50.0f);
		arcTo.setRadiusY(50.0f);

		this.getElements().add(moveTo);
		this.getElements().add(hLineTo);
		this.getElements().add(quadCurveTo);
		this.getElements().add(lineTo);
		this.getElements().add(arcTo);
	}
}