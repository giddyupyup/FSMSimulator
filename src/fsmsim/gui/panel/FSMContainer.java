package fsmsim.gui.panel;

import fsmsim.engine.regex.Tree;
import fsmsim.engine.regex.RegexParser;

import java.util.List;

import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;

public class FSMContainer extends HBox {
	private final Label enterLabel = new Label("Enter Regular Expression");
    private final Button generateBtn = new Button("Generate eNFA");
    private final TextField userInput = new TextField();

    FSMContainer() {
        this.enterLabel.setAlignment(Pos.BOTTOM_CENTER);

        this.generateBtn.setAlignment(Pos.CENTER);
        this.generateBtn.setPadding(new Insets(5, 10, 5, 10));
        this.generateBtn.setCursor(Cursor.HAND);

        this.userInput.setPromptText("(a+b)*a");
        this.userInput.setMaxSize(400, 10);
        this.activateUserInputValidation(this.userInput);
        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.getChildren().addAll(enterLabel, userInput, generateBtn);
    }

    private void activateUserInputValidation(final TextField userInput) {
    	userInput.setOnKeyReleased(event -> {
            if(event.getCode() != KeyCode.SHIFT) {
                System.out.println(userInput.getText());
                final RegexParser regex = new RegexParser();
                final Tree regexTree = new Tree(regex.parse(userInput.getText()));
                
                System.out.println(regexTree.getParseNode().getNodeType());
                System.out.println(regexTree.validate());
                System.out.println(regexTree.validate());
                System.out.println(regexTree.validate());
            }
    		

    	});
    }
}