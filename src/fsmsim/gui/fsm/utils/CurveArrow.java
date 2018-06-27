package fsmsim.gui.fsm.utils;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CurveArrow extends Group{
    private static final double ARROW_HEAD_SIZE = 10.0;
    
    public CurveArrow(final boolean isUp,
                      final double startX,
                      final double startY,
                      final double endX,
                      final double endY,
                      final String symbol){
        final Path curve = new Path();
        curve.setStrokeWidth(3);

        final double controlX1 = Math.abs(endX - startX) / 4 + ((endX - startX >= 0) ? startX : endX);
        final double controlY1 = endY - (isUp ? 50 : -50);

        final double controlX2 = (Math.abs(endX - startX) / 4) * 3 + ((endX - startX >= 0) ? startX : endX);
        final double controlY2 = endY - (isUp ? 50 : -50);

        curve.getElements().add(new MoveTo(startX, startY));
        if(endX - startX >= 0) curve.getElements().add(new CubicCurveTo(controlX1, controlY1, controlX2, controlY2, endX, endY));
        else curve.getElements().add(new CubicCurveTo(controlX2, controlY2, controlX1, controlY1, endX, endY));

        final Path arrowHead = new Path();
        arrowHead.setStrokeWidth(3);
        arrowHead.strokeProperty().bind(arrowHead.fillProperty());
        arrowHead.setFill(Color.BLACK);

        //ArrowHead
        final double angle = (endX - startX >= 0) ? (Math.atan2((endY - controlY2), (endX - controlX2)) - Math.PI / 2.0) : (Math.atan2((endY - controlY1), (endX - controlX1)) - Math.PI / 2.0);
        final double sin = Math.sin(angle);
        final double cos = Math.cos(angle);
        //point1
        final double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SIZE + endX;
        final double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SIZE + endY;
        //point2
        final double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SIZE + endX;
        final double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SIZE + endY;
        
        arrowHead.getElements().add(new MoveTo(endX, endY));
        arrowHead.getElements().add(new LineTo(x1, y1));
        arrowHead.getElements().add(new LineTo(x2, y2));
        arrowHead.getElements().add(new LineTo(endX, endY));

        final Label label = new Label(symbol);
        label.setFont(Font.font(null, FontWeight.BOLD, 20));

        final double angle2 = Math.atan2((endY - startY), (endX - startX));
        final double distance = Math.sqrt(Math.pow((endX - startX), 2) + Math.pow((endY - startY), 2)) / 2;

        final double labelX = startX + (distance * Math.cos(angle2)) - 7;
        final double labelY = startY + (distance * Math.sin(angle2)) - (isUp ? 65 : -5);
        label.relocate(labelX, labelY);

        this.getChildren().addAll(curve, arrowHead, label);

    }
}