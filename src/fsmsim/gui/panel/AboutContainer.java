package fsmsim.gui.panel;

import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.Cursor;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.TextAlignment;

public class AboutContainer extends StackPane {
    public AboutContainer() {
        this.setStyle("-fx-background-color: #FFF; " + 
                      "-fx-border-color: #EAEAEA; " + 
                      "-fx-font-size: 14; " + 
                      "-fx-font-weight: bold; " + 
                      "-fx-font-family: Trebuchet MS, Helvetica, sans-serif;");

        final Label about = new Label("?");
        about.setPrefWidth(20.0);
        about.setPrefHeight(20.0);
        about.setAlignment(Pos.CENTER);
        about.setTextAlignment(TextAlignment.CENTER);
        about.setOnMouseClicked(e -> {
            new AboutMenu();
        });

        about.setOnMouseEntered(e -> {
            about.setStyle("-fx-background-color: #0C93C9");
            about.setTextFill(Color.WHITE);
        });

        about.setOnMouseExited(e -> {
            about.setStyle("-fx-background-color: #FFF");
            about.setTextFill(Color.BLACK);
        });
        setMargin(about, new Insets(0, 15, 0, 15));
        about.setCursor(Cursor.HAND);
        this.setAlignment(Pos.CENTER_RIGHT);
        this.getChildren().add(about);
    }
}