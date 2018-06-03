package fsmsim.gui.panel;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class MainContainer extends BorderPane {

	MainContainer() {
		final AboutContainer aboutContainer = new AboutContainer();
		this.setTop(aboutContainer);
		BorderPane.setAlignment(aboutContainer, Pos.CENTER_RIGHT);
		// BorderPane.setMargin(aboutContainer, new Insets(0, 20, 0, 20));
		this.setCenter(new FSMContainer());
	}
}