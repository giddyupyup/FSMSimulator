package fsmsim.gui.panel;

import javafx.scene.layout.BorderPane;
import javafx.geometry.Pos;

public class MainContainer extends BorderPane {

	MainContainer() {
            final AboutContainer aboutContainer = new AboutContainer();
            this.setTop(aboutContainer);
            BorderPane.setAlignment(aboutContainer, Pos.CENTER_RIGHT);
            this.setCenter(new FSMContainer());
	}
}