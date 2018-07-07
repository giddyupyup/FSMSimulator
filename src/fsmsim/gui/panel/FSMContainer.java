package fsmsim.gui.panel;

import fsmsim.engine.regex.Tree;
import fsmsim.engine.regex.RegexParser;
import fsmsim.engine.fsm.ENFA;
import fsmsim.gui.fsm.FSMView;

import javafx.event.ActionEvent;

import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class FSMContainer extends HBox {
    private final Label enterLabel = new Label("Enter Regular Expression");
    private final Button generateBtn = new Button("Generate eNFA");
    private final TextField userInput = new TextField();
    private final Label invalidRegex;

    private final RegexParser regex;
    private static Tree regexTree;

    FSMContainer() {
        this.invalidRegex = new Label();
        this.regex = new RegexParser();
        this.enterLabel.setAlignment(Pos.BOTTOM_CENTER);
        this.generateBtn.setAlignment(Pos.CENTER);
        this.generateBtn.setPadding(new Insets(5, 10, 5, 10));
        this.generateBtn.setCursor(Cursor.HAND);

        this.userInput.setPromptText("(a+b)*a");
        this.userInput.setMaxSize(400, 12);
        this.invalidRegex.setTextFill(Color.RED);
        this.invalidRegex.setFont(Font.font(null, FontWeight.BOLD, 14));
        final VBox userInputContainer = new VBox();
        userInputContainer.setSpacing(10);
        userInputContainer.getChildren().addAll(this.userInput, this.invalidRegex);
        this.activateUserInputValidation(this.userInput, this.invalidRegex, this.regex, this.generateBtn);
        this.setSpacing(30);
        this.setPadding(new Insets(25, 20, 0, 20));
        this.getChildren().addAll(this.enterLabel, userInputContainer, this.generateBtn);
    }

    private void activateUserInputValidation(final TextField userInput,
                                             final Label invalidRegex,
                                             final RegexParser regex,
                                             final Button generateBtn) {
    	userInput.setOnKeyReleased(event -> {
            if(event.getCode() != KeyCode.SHIFT) {
                if(userInput.getText() == null || userInput.getText().trim().isEmpty()) {
                    userInput.setStyle(null);
                    invalidRegex.setText("");
                } else {
                    FSMContainer.regexTree = new Tree(regex.parse(userInput.getText().replaceAll("\\s","")));

                    if(!regexTree.validate()) {
                        userInput.setStyle("-fx-border-color: red; -fx-focus-color: red;");
                        invalidRegex.setText("Invalid RegEx");
                    } else {
                        userInput.setStyle("-fx-border-color: green; -fx-focus-color: green;");
                        invalidRegex.setText("");
                    }
                    FSMContainer.generateFSM(FSMContainer.regexTree, generateBtn);
                }
            }
    	});
    }

    private static void generateFSM(final Tree regexTree, final Button generateBtn) {
        generateBtn.setOnAction((ActionEvent event) -> {
            if(regexTree.validate()) {
                final ENFA enfa = new ENFA();
                enfa.create(regexTree);
                new FSMView(enfa.getStates());
            }
        });
    }
}