package fsmsim.gui.panel;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

public class AboutMenu extends Stage {
    public AboutMenu() {
        this.initStyle(StageStyle.TRANSPARENT);
        this.initOwner(MainPanel.get().getWindow());
        this.initModality(Modality.WINDOW_MODAL);
        this.setScene(new Scene(new About(this)));
        this.sizeToScene();
        this.show();
    }

    public static class About extends BorderPane {
        private final Group closeButton = new Group();
        private final VBox about = new VBox();

        public About(final Stage stage) {
            this.setStyle("-fx-background-color: #ECEAEA; -fx-border-color: #000; -fx-border-width: 1;");
            this.createCloseButton();
            this.closeButton.setOnMouseClicked(e -> stage.close());
            this.setTop(this.closeButton);
            BorderPane.setAlignment(this.closeButton, Pos.CENTER_RIGHT);
            BorderPane.setMargin(this.closeButton, new Insets(10));
            this.createAbout();
            this.setCenter(this.about);
            BorderPane.setAlignment(this.about, Pos.CENTER);
            BorderPane.setMargin(this.about, new Insets(10, 20, 20, 20));
        }

        private void createCloseButton() {
            final Circle circleBtn = new Circle();
            circleBtn.setRadius(12);
            circleBtn.setFill(Color.TRANSPARENT);
            final Line line1 = new Line(-5.0, 5.0, 5.0, -5.0);
            final Line line2 = new Line(-5.0, -5.0, 5.0, 5.0);
            line1.setStrokeWidth(2);
            line2.setStrokeWidth(2);
            line1.setStroke(Color.BLACK);
            line2.setStroke(Color.BLACK);

            this.closeButton.setOnMouseEntered(e -> {
                circleBtn.setFill(Color.RED);
                line1.setStroke(Color.WHITE);
                line2.setStroke(Color.WHITE);
            });
            this.closeButton.setOnMouseExited(e -> {
                circleBtn.setFill(Color.TRANSPARENT);
                line1.setStroke(Color.BLACK);
                line2.setStroke(Color.BLACK);
            });

            this.closeButton.setCursor(Cursor.HAND);
            this.closeButton.getChildren().addAll(circleBtn, line1, line2);
        }

        private static final String ABOUTTITLE = 
            "Finite State Machine Simulator\n" + 
            "(Regex String to eNFA conversion)\n\n";

        private static final String ABOUTAUTHOR = 
            "Created by: Gilbert M. Arces\n" +
            "SY: 2017 -2018\n" +
            "Visayas State University";

        private void createAbout() {
            final Label aboutTitle = new Label(ABOUTTITLE);
            aboutTitle.setTextFill(Color.BLACK);
            aboutTitle.setTextAlignment(TextAlignment.CENTER);
            aboutTitle.setAlignment(Pos.CENTER);
            aboutTitle.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-font-family: Trebuchet MS, Helvetica, sans-serif;");

            final Label aboutAuthor = new Label(ABOUTAUTHOR);
            aboutAuthor.setTextFill(Color.BLACK);
            aboutAuthor.setTextAlignment(TextAlignment.CENTER);
            aboutAuthor.setAlignment(Pos.CENTER);
            aboutAuthor.setStyle("-fx-font-size: 20; -fx-font-family: Trebuchet MS, Helvetica, sans-serif;");
            this.about.setSpacing(20);
            this.about.setAlignment(Pos.CENTER);
            this.about.getChildren().addAll(aboutTitle, aboutAuthor);
        }
    }
}