package fsmsim.gui.fsm;

import fsmsim.engine.fsm.State;
import fsmsim.gui.panel.MainPanel;

import java.util.List;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;

public class FSMView extends Stage {
    public FSMView(final List<State> states) {
        this.setTitle("Finite State Machine Generator");
        this.initStyle(StageStyle.DECORATED);
        this.initOwner(MainPanel.get().getWindow());
        this.initModality(Modality.WINDOW_MODAL);
        this.setScene(new Scene(this.wrapperPane(new FSMGenerator(states))));
        this.setResizable(false);
        this.show();
    }
    
    private ScrollPane wrapperPane(final FSMGenerator fsmGenerator) {
        final ScrollPane scrollPane = new ScrollPane();
        
        scrollPane.setContent(fsmGenerator);
        scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        
        return scrollPane;
    }
    
    
}