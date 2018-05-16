package fsmsim.gui;

import fsmsim.gui.panel.MainPanel;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class FSMApp extends Application {
	@Override
	public void start(final Stage stage) throws Exception {
		stage.setTitle("FSMSimulator");
		stage.setResizable(false);
		stage.sizeToScene();
		stage.setScene(MainPanel.get());
		stage.show();
	}
}