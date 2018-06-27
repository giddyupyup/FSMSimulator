package fsmsim.gui.fsm.utils;

import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Arrow extends Group{
    private static final double ARROW_HEAD_SIZE = 10.0;
    
    public Arrow(final double startX,
                 final double startY,
                 final double endX,
                 final double endY,
                 final String symbol){
        super();
        final Path line = new Path();

        line.setStrokeWidth(3);
        line.strokeProperty().bind(line.fillProperty());
        line.setFill(Color.BLACK);
        
        //Line
        line.getElements().add(new MoveTo(startX, startY));
        line.getElements().add(new LineTo(endX, endY));

        final Path arrowHead = new Path();
        arrowHead.setStrokeWidth(3);
        arrowHead.strokeProperty().bind(arrowHead.fillProperty());
        arrowHead.setFill(Color.BLACK);
        
        //ArrowHead
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;

        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SIZE + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SIZE + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * ARROW_HEAD_SIZE + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * ARROW_HEAD_SIZE + endY;

        arrowHead.getElements().add(new MoveTo(endX , endY));
        arrowHead.getElements().add(new LineTo(x1, y1));
        arrowHead.getElements().add(new LineTo(x2, y2));
        arrowHead.getElements().add(new LineTo(endX, endY));

        final Label label = new Label(symbol);
        label.setFont(Font.font(null, FontWeight.BOLD, 20));

        final double angle2 = Math.atan2((endY - startY), (endX - startX));
        final double distance = Math.sqrt(Math.pow((endX - startX), 2) + Math.pow((endY - startY), 2)) / 2;

        final double labelX = startX + (distance * Math.cos(angle2)) - 7;
        final double labelY = startY + (distance * Math.sin(angle2)) - (endY - startY > 0 ? 37 : 33);

        label.relocate(labelX, labelY);

        this.getChildren().addAll(line, arrowHead, label);        
    }
}