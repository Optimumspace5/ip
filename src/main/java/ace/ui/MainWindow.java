package ace.ui;

import ace.Ace;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Ace ace;

    private final Image userImage = new Image(getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image aceImage  = new Image(getClass().getResourceAsStream("/images/DaAce.png"));

    @FXML
    public void initialize() {
        // Auto-scroll
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Ace instance */
    public void setAce(Ace ace) {
        this.ace = ace;

        // optional initial message (safe to do after injection)
        dialogContainer.getChildren().add(
                DialogBox.getBotDialog("Hello! Type something and press Send.", aceImage)
        );
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = ace.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBotDialog(response, aceImage)
        );

        userInput.clear();
    }
}