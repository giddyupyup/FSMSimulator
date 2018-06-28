package fsmsim.gui.fsm;

import fsmsim.engine.fsm.State;
import fsmsim.gui.panel.MainPanel;

import java.util.List;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Scene;

public class FSMView extends Stage {

	public FSMView(final List<State> states) {
		this.initStyle(StageStyle.DECORATED);
		this.initOwner(MainPanel.get().getWindow());
        this.initModality(Modality.WINDOW_MODAL);
        this.setScene(new Scene(new FSMGenerator(states)));
        // this.setResizable(false);
        this.sizeToScene();
        this.show();
	}
}