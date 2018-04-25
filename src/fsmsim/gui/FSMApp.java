package fsmsim.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class FSMApp extends Application {
	@Override
	public void start(final Stage stage) throws Exception {
		stage.setTitle("FSMSimulator");
		stage.setResizable(false);
		stage.sizeToScene();
		stage.setScene(/* class for the whole app*/);
		stage.show();
	}
}