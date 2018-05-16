package fsmsim.gui.panel;

import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;

public class MainPanel extends Scene {

	private final static MainPanel INSTANCE = new MainPanel();

	private MainPanel() {
		super(new BorderPane());
	}

	public static MainPanel get() {
		return INSTANCE;
	}
}