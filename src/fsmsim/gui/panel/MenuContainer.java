package fsmsim.gui.panel;

import javafx.application.Platform;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;

public class MenuContainer extends MenuBar {
	public MenuContainer() {
		this.setStyle("-fx-font-size: 12; -fx-font-family: Trebuchet MS, Helvetica, sans-serif;");
		this.getMenus().addAll(this.createFileMenu(), this.createHelpMenu());
	}

	private Menu createFileMenu() {
		final Menu fileMenu = new Menu("File");

		final MenuItem newFSM = new MenuItem("New");
		final MenuItem exitFSM = new MenuItem("Exit");

		exitFSM.setOnAction(e -> {
			Platform.exit();
		});

		fileMenu.getItems().addAll(newFSM, new SeparatorMenuItem(), exitFSM);

		return fileMenu;
	}

	private Menu createHelpMenu() {
		final Menu helpMenu = new Menu("Help");

		final MenuItem aboutMenu = new MenuItem("About");

		helpMenu.getItems().add(aboutMenu);

		return helpMenu;
	}
}