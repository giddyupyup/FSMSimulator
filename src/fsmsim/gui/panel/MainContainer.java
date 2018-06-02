package fsmsim.gui.panel;

import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;

public class MainContainer extends BorderPane {

	MainContainer() {
		this.setTop(new MenuContainer());
		this.setCenter(new FSMContainer());
	}
}