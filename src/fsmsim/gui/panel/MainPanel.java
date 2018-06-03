package fsmsim.gui.panel;

import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;

public final class MainPanel extends Scene {

    private final static MainPanel INSTANCE = new MainPanel();

    private MainPanel() {
        super(new MainContainer());
    }

    public static MainPanel get() {
        return INSTANCE;
    }
}