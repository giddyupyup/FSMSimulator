package fsmsim.gui;

import fsmsim.gui.panel.MainPanel;
import javafx.geometry.Rectangle2D;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Screen;

public class FSMApp extends Application {
	@Override
	public void start(final Stage stage) throws Exception {
		stage.setTitle("FSMSimulator");
		stage.setResizable(true);
		stage.setScene(MainPanel.get());
		stage.setMaximized(true);
		stage.show();
	}
}