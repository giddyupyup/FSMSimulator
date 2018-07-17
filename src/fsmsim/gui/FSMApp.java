package fsmsim.gui;

import fsmsim.gui.panel.MainPanel;

import javafx.application.Application;
import javafx.stage.Stage;

public class FSMApp extends Application {
    @Override
    public void start(final Stage stage) throws Exception {
        stage.setTitle("Finite State Machine Simulator");
        stage.setResizable(false);
        stage.setScene(MainPanel.get());
        stage.show();
    }
}