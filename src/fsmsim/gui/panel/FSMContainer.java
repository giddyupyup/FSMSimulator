package fsmsim.gui.panel;

import fsmsim.engine.regex.Tree;
import fsmsim.engine.regex.RegexParser;
import fsmsim.engine.fsm.ENFA;
import fsmsim.gui.fsm.FSMView;

import javafx.event.ActionEvent;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
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

public class FSMContainer extends VBox {
    private final Label enterLabel = new Label("Enter Regular Expression:");
    private final Button generateBtn = new Button("Generate eNFA");
    private final TextField userInput = new TextField();
    private final Label invalidRegex;

    private final RegexParser regex;
    private static Tree regexTree;

    FSMContainer() {
        this.invalidRegex = new Label();
        this.regex = new RegexParser();

        final HBox inputPane = new HBox();
        inputPane.setSpacing(40);
        inputPane.setPadding(new Insets(25, 20, 10, 20));

        final Pane labelPane = new Pane();
        this.enterLabel.setAlignment(Pos.BOTTOM_LEFT);
        this.enterLabel.setFont(Font.font(null, FontWeight.BOLD, 20));
        labelPane.getChildren().add(this.enterLabel);
        this.generateBtn.setAlignment(Pos.BOTTOM_RIGHT);
        this.generateBtn.setPadding(new Insets(5, 10, 5, 10));
        this.generateBtn.setCursor(Cursor.HAND);
        this.generateBtn.setFont(Font.font(null, FontWeight.BOLD, 20));

        this.userInput.setPromptText("(a+b)*a");
        this.userInput.setFont(Font.font(null, FontWeight.NORMAL, 20));
        this.userInput.setMaxSize(400, 20);
        inputPane.getChildren().addAll(labelPane, this.userInput, this.generateBtn);

        final Pane invalidPane = new Pane();
        this.invalidRegex.setTextFill(Color.RED);
        this.invalidRegex.setFont(Font.font(null, FontWeight.BOLD, 18));
        this.invalidRegex.setPadding(new Insets(0, 0, 0, 295));
        invalidPane.getChildren().add(this.invalidRegex);

        this.activateUserInputValidation(this.userInput, this.invalidRegex, this.regex, this.generateBtn);

        
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(0, 10, 25, 10));
        this.setSpacing(10);
        this.getChildren().addAll(inputPane, invalidPane);
    }

    private void activateUserInputValidation(final TextField userInput,
                                             final Label invalidRegex,
                                             final RegexParser regex,
                                             final Button generateBtn) {
    	userInput.setOnKeyReleased(event -> {
            if(event.getCode() != KeyCode.SHIFT) {
                if(userInput.getText() == null || userInput.getText().trim().isEmpty()) {
                    userInput.setStyle("-fx-border-color: red; -fx-focus-color: red;");
                    invalidRegex.setText("Empty Regular Expression");
                } else {
                    FSMContainer.regexTree = new Tree(regex.parse(userInput.getText().replaceAll("\\s","")));

                    if(!regexTree.validate()) {
                        userInput.setStyle("-fx-border-color: red; -fx-focus-color: red;");
                        invalidRegex.setText(regexTree.getParseNode().getInvalidString());
                    } else {
                        userInput.setStyle("-fx-border-color: green; -fx-focus-color: green;");
                        invalidRegex.setText("");
                    }
                }
                FSMContainer.generateFSM(FSMContainer.regexTree, generateBtn, userInput);
            }
    	});
    }

    private static void generateFSM(final Tree regexTree,
                                    final Button generateBtn,
                                    final TextField userInput) {
        generateBtn.setOnAction((ActionEvent event) -> {
            if(!(userInput.getText() == null || userInput.getText().trim().isEmpty()) &&
                regexTree.validate()) {
                final ENFA enfa = new ENFA();
                enfa.create(regexTree);
                new FSMView(enfa.getStates(), userInput.getText());
            }
        });
    }
}